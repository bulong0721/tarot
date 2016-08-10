package com.myee.tarot.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Ordering;
import com.myee.djinn.dto.DrawToken;
import com.myee.djinn.dto.ResponseData;
import com.myee.djinn.dto.WaitToken;
import com.myee.djinn.dto.WaitTokenState;
import com.myee.djinn.server.operations.OperationsService;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.catering.service.TableTypeService;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.datacenter.domain.SelfCheckLog;
import com.myee.tarot.datacenter.domain.SelfCheckLogVO;
import com.myee.tarot.datacenter.service.SelfCheckLogService;
import com.myee.tarot.device.service.DeviceUsedService;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantStoreService;
import com.myee.tarot.weixin.dao.WxWaitTokenDao;
import com.myee.tarot.weixin.domain.WxWaitToken;
import com.myee.tarot.weixin.util.TimeUtil;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.WxMpTemplateMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Martin on 2016/1/27.
 */
@Service
public class OperationsManager extends RedisOperation implements OperationsService {
    private static Logger logger = LoggerFactory.getLogger(OperationsManager.class);

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    @Autowired
    @Lazy
    private WxWaitTokenDao waitTokenDao;

    @Autowired
    public OperationsManager(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Autowired
    private WxMpService wxMpService;

    static int i = 0;

    @Autowired
    private DeviceUsedService deviceUsedService;

    @Autowired
    private TableTypeService tableTypeService;

    @Autowired
    private SelfCheckLogService selfCheckLogService;

    @Autowired
    private MerchantStoreService merchantStoreService;

    /*
     * 小女生取号
     *
     * @param waitToken
     **/

    @Override
    public ResponseData take(WaitToken waitToken) {
        //排号Key
        String redisKeyOfShopTb = RedisKeys.waitOfTableType(waitToken.getShopId(), waitToken.getTableTypeId());
        //唯一码-SeceneId对应Key
        String sourceStr = RedisKeys.getIdentityCode(Long.valueOf(waitToken.getSceneId()));
        String redisKeyOfUcdScId = sourceStr.substring(0, 22) + sourceStr.substring(23,sourceStr.length());
        Date date = new Date();
        waitToken.setTimeTook(date.getTime() / 1000);
        //然后二维码的数字码放Redis，跟identityCode唯一码绑定
        hsetSimple(redisKeyOfUcdScId, waitToken.getIdentityCode(), null);
        //然后identityCode放Redis，跟排号的key绑定
        Map<String, Date> map1 = new HashMap<String, Date>();
        map1 = TimeUtil.getAfterDate(date);
        hsetSimple(waitToken.getIdentityCode(), redisKeyOfShopTb, map1.get("eTime"));
        hset(redisKeyOfShopTb, waitToken.getToken(), waitToken, map1.get("eTime"));
        WxMpQrCodeTicket myticket = null;
        WxWaitToken wxWaitToken = null;
        try {
            myticket = wxMpService.qrCodeCreateTmpTicket(Integer.parseInt(waitToken.getSceneId()), 2592000);
            wxWaitToken = waitTokenDao.update(WeixinManager.convertTo(waitToken, date.getTime() / 1000));
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        if (wxWaitToken != null && wxWaitToken.getId() != null) {
            return ResponseData.successData(myticket.getUrl()); //返回二维码图片的短连接
        } else {
            return ResponseData.errorData("fail");
        }
    }

    /*
     * 过号
     * @param waitToken
     * */

    @Override
    public ResponseData skip(WaitToken waitToken) {
        return statusChange(WaitTokenState.SKIP.getValue(),waitToken);
    }

    private Ordering<WaitToken> orderingByTook = Ordering.from(new Comparator<WaitToken>() {
        @Override
        public int compare(WaitToken o1, WaitToken o2) {
            return o1.getTimeTook().compareTo(o2.getTimeTook());
        }
    });

    private Ordering<WxWaitToken> orderingByTook2 = Ordering.from(new Comparator<WxWaitToken>() {
        @Override
        public int compare(WxWaitToken o1, WxWaitToken o2) {
            Long o1TimeTook1 = new Long(o1.getTimeTook().getTime());
            Long o1TimeTook2 = new Long(o2.getTimeTook().getTime());
            return o1TimeTook1.compareTo(o1TimeTook2);
        }
    });

    /*
     * 就餐
     *
     * @param waitToken
     * @param tableId
      */
    @Override
    public ResponseData repast(WaitToken waitToken, long tableId) {
        return statusChange(WaitTokenState.REPASTING.getValue(),waitToken);
    }

    /*
     * 改变就餐状态的方法
     * @param stateValue
     * @param waitToken
     * @return
     * */

    private ResponseData statusChange(int stateValue,WaitToken waitToken) {
        Integer backStatus = 0;
        String redisKey = RedisKeys.waitOfTableType(waitToken.getShopId(), waitToken.getTableTypeId());
        Map<String, WaitToken> map = hgetall(redisKey, WaitToken.class);
        if (map != null) { //如果redis里有数据，直接从Redis里拿
            List<WaitToken> sortedTokens = orderingByTook.sortedCopy(map.values());
            if (waitToken.getShopId().equals(sortedTokens.get(0).getShopId()) && waitToken.getToken().equals(sortedTokens.get(0).getToken())) {
                hdelete(redisKey, waitToken.getToken());
                //改状态2
                backStatus = waitTokenDao.updateState(stateValue, waitToken.getClientId(), waitToken.getShopId(), waitToken.getToken(), sortedTokens.get(0).getTimeTook(), new Date().getTime() / 1000);
            }
        } else { //如果redis里没有数据，从MySql里同步
            List<WxWaitToken> tokenList = waitTokenDao.selectAllTokenByInfo(waitToken.getClientId(), waitToken.getShopId(), waitToken.getTableTypeId(), WaitTokenState.WAITING.getValue());
            List<WxWaitToken> sortedTokens = orderingByTook2.sortedCopy(tokenList);
            List<WaitToken> sortedTokensW = new ArrayList<WaitToken>();
            for (int i = 0; i < sortedTokens.size(); i++) {
                WaitToken w = new WaitToken();
                w.setToken(sortedTokens.get(i).getToken());
                w.setTimeTook(sortedTokens.get(i).getTimeTook().getTime());
                w.setTableId(sortedTokens.get(i).getTableId());
                w.setClientId(sortedTokens.get(i).getMerchantId());
                w.setOpenId(sortedTokens.get(i).getOpenId());
                w.setShopId(sortedTokens.get(i).getMerchantStoreId());
                w.setIdentityCode(sortedTokens.get(i).getIdentityCode());
                sortedTokensW.add(w);
            }
            if (waitToken.getToken().equals(sortedTokensW.get(0).getToken()) && waitToken.getShopId().equals(sortedTokensW.get(0).getShopId())) {
                //改状态2
                backStatus = waitTokenDao.updateState(stateValue, waitToken.getClientId(), waitToken.getShopId(), waitToken.getToken(), sortedTokensW.get(0).getTimeTook(), new Date().getTime() / 1000);
            }
            for (WaitToken tokenInfo : sortedTokensW) {
                Map<String, Date> map1 = TimeUtil.getAfterDate(new Date(tokenInfo.getTimeTook()));
                hset(redisKey, tokenInfo.getToken(), tokenInfo, map1.get("eTime"));
            }
        }
        //移除就餐桌号后的
        Map<String, WaitToken> afterMap = hgetall(redisKey, WaitToken.class);
        List<Map<String, Object>> openIdList = new ArrayList<Map<String, Object>>();
        if (afterMap != null) {
            for (WaitToken w : afterMap.values()) {
                if (w.getOpenId() != null && w.getOpenId().trim().length() > 0) {
                    //比较token查询进展
                    Map<String, Object> msgMap = selectLatestDevelopmentsByIc(w.getIdentityCode());
                    msgMap.put("openId", w.getOpenId());
                    openIdList.add(msgMap);
                }
            }
        } else {
            List<WxWaitToken> rwList = waitTokenDao.selectAllTokenOpenIdNotNull(waitToken.getClientId(), waitToken.getShopId(), waitToken.getTableTypeId(), WaitTokenState.WAITING.getValue());
            for (WxWaitToken rw : rwList) {
                if (rw.getOpenId() != null && rw.getOpenId().trim().length() > 0) {
                    //比较token查询进展
                    Map<String, Object> msgMap = selectLatestDevelopmentsByIc(rw.getIdentityCode());
                    msgMap.put("openId", rw.getOpenId());
                    openIdList.add(msgMap);
                }
            }
        }
        //向服务器发送请求
        for (Map m : openIdList) {
            try {
                WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
                templateMessage.setToUser(m.get("openId").toString());
                templateMessage.setTemplateId("p9ZGWqeFEYbLPkMKOdd5KUKc_QiqfXaJgu1ZNmezIUo");
                templateMessage.setUrl("null");
                templateMessage.getDatas().add(new WxMpTemplateData("keyword1", m.get("shopName") == null ? "" : m.get("shopName").toString(), "#c37160"));
                templateMessage.getDatas().add(new WxMpTemplateData("keyword2", m.get("tokenNum").toString(), "#36b2cc"));
                templateMessage.getDatas().add(new WxMpTemplateData("keyword3", m.get("waitedTable").toString(), "#ff9900"));
                templateMessage.getDatas().add(new WxMpTemplateData("keyword4", m.get("predictTime").toString(), "#053eea"));
                templateMessage.getDatas().add(new WxMpTemplateData("keyword5", m.get("queueStatus") == null ? "" : m.get("queueStatus").toString(), "#ea05b1"));
                templateMessage.getDatas().add(new WxMpTemplateData("remark", m.get("queryTime").toString(), "#4a5de8"));
                wxMpService.templateSend(templateMessage);
//                wxMpService.customMessageSend(WxMpCustomMessage.TEXT().toUser(m.get("openId").toString()).content(m.get("msg").toString()).build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (backStatus == 1) {
            return new ResponseData(true);
        } else {
            return ResponseData.errorData("fail");
        }
    }

    public Map<String, Object> selectLatestDevelopmentsByIc(String identityCode) {
        //先到Redis里去找，通过identityCode获取当时保存token的redisKey
        String redisKey2Find = getSimple(identityCode).toString();
        Map<String, WaitToken> waitTokenMap = hgetall(redisKey2Find, WaitToken.class);
        Set<Integer> waitNumSet = new HashSet<Integer>();
        Map<String, Object> latestDevInfo = new HashMap<String, Object>();
        String infoStr = null;
        String shopName = null;
        //顾客取号的号数
        Integer userNum = null;
        //之前等待桌数
        Integer beforeCount = 0;
        //等待时长
        Integer waitMinutes = 0;
        String waitToken = "";
        String queueStatus = null;
        Long timeTook = null;
        if (waitTokenMap != null && waitTokenMap.size() > 0) {
            for (WaitToken wt : waitTokenMap.values()) {
                waitNumSet.add(Integer.parseInt(wt.getToken().substring(1, 3)));
                if (identityCode.equals(wt.getIdentityCode())) {
//                    String shopKey = RedisKeys.shopOfClient(wt.getShopId());
//                    shopName = getSimple(shopKey).toString();
                    userNum = Integer.parseInt(wt.getToken().substring(1, 3));
                    waitMinutes = wt.getWaitMinutes();
                    waitToken = wt.getToken();
                    timeTook = wt.getTimeTook();
                    shopName = wt.getClientName();
                    if (wt.getWaitStatus() == 1) {
                        queueStatus = "排队中";
                    }
                    if (wt.getWaitStatus() == 2) {
                        queueStatus = "就餐中";
                    }
                    if (wt.getWaitStatus() == 3) {
                        queueStatus = "已过号";
                    }
                    if (wt.getWaitStatus() == 4) {
                        queueStatus = "已取消";
                    }
                    if (wt.getWaitStatus() == 6) {
                        queueStatus = "已发送";
                    }
                    if (wt.getWaitStatus() == 7) {
                        queueStatus = "未发送";
                    }
                }
            }
            for (Integer i : waitNumSet) {
                if (i < userNum) {
                    beforeCount++;
                }
            }
            latestDevInfo.put("shopName", shopName);
            latestDevInfo.put("tokenNum", waitToken);
            latestDevInfo.put("waitedTable", beforeCount.toString() + "桌");
            latestDevInfo.put("waitedTableCount", beforeCount.longValue());
            latestDevInfo.put("predictTime", String.valueOf(beforeCount * 10) + "分钟");
            latestDevInfo.put("predictWaitingTime", Long.valueOf(beforeCount * 10));
            latestDevInfo.put("queueStatus", queueStatus);
            latestDevInfo.put("timeTook", timeTook);
            latestDevInfo.put("queryTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } else {
            Map<String, Date> map = TimeUtil.getAfterDate(new Date());
            Long bTimeLong = map.get("bTime").getTime() / 1000;
            Long eTimeLong = map.get("eTime").getTime() / 1000;
            //再到MYSQL去找,用identityCode找到对应的clientId,orgId,和token，
            WxWaitToken wtoken = waitTokenDao.selectTokenByIc(identityCode, bTimeLong, eTimeLong);
            if (wtoken != null) {
                //根据clientId和orgId和tableId，找到该餐馆的某餐桌类型等待的token
                List<WxWaitToken> tokenList = waitTokenDao.selectAllTokenByInfo(wtoken.getMerchantId(), wtoken.getMerchantStoreId(), wtoken.getTableId(), WaitTokenState.WAITING.getValue());
                for (WxWaitToken wt : tokenList) {
                    waitNumSet.add(Integer.parseInt(wt.getToken().substring(1, 3)));
                    if (identityCode.equals(wt.getIdentityCode())) {
                        userNum = Integer.parseInt(wt.getToken().substring(1, 3));
                        timeTook = wt.getTimeTook().getTime();
                        shopName = merchantStoreService.findById(wt.getMerchantStoreId()).getName();
                        if (wt.getState() == 1) {
                            queueStatus = "排队中";
                        }
                        if (wt.getState() == 2) {
                            queueStatus = "就餐中";
                        }
                        if (wt.getState() == 3) {
                            queueStatus = "已过号";
                        }
                        if (wt.getState() == 4) {
                            queueStatus = "已取消";
                        }
                        if (wt.getState() == 6) {
                            queueStatus = "已发送";
                        }
                        if (wt.getState() == 7) {
                            queueStatus = "未发送";
                        }
                    }
                }
                for (Integer i : waitNumSet) {
                    if (i < userNum) {
                        beforeCount++;
                    }
                }
                latestDevInfo.put("shopName", shopName);
                latestDevInfo.put("tokenNum", waitToken);
                latestDevInfo.put("waitedTable", beforeCount.toString() + "桌");
                latestDevInfo.put("predictTime", String.valueOf(beforeCount * 30) + "分钟");
                latestDevInfo.put("queueStatus", queueStatus);
                latestDevInfo.put("timeTook", timeTook);
                latestDevInfo.put("queryTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            } else {
                latestDevInfo.put("vaild", "您查询的编号不存在");
            }
        }
        return latestDevInfo;
    }

    /*
     * 取消排号
     *
     * @param waitToken
     * @param reason
     * @return
     * */

    @Override
    public ResponseData cancel(WaitToken waitToken, String reason) {
        return new ResponseData(true);
    }

    @Override
    public ResponseData draw(DrawToken drawToken) {
        return new ResponseData(true);
    }

    @Override
    public ResponseData getWaitTokenInfo(Long tableTypeId) {
        return null;
    }

   /* @Override
    public ResponseData getWaitTokenInfo(int tableTypeId) {
        //TODO 不用云端实现
        return null;
    }*/

    /**
     *
     * @param jsonArgs
     * @return
     */
//    @Override
    public ResponseData getResourceInfo(String jsonArgs) {
        System.out.println("jsonArgs: " + jsonArgs);
        System.out.println("DOWNLOAD_HOME: " + DOWNLOAD_HOME);
        ResponseData result = null;
        try{
            JSONObject object = JSON.parseObject(jsonArgs);
            String name = object.getString("name");
            String type = object.getString("type");
            String orgId = object.getString("orgId");
            StringBuilder sb = new StringBuilder();
            logger.info("================ request info  name:"+name+"  type:"+type+"   orgId:"+orgId);
            //TODO
            if ("app".equals(type) || "ipc".equals(type)) {
                sb.append(DOWNLOAD_HOME).append(File.separator).append(orgId).append(File.separator).append(type).append(File.separator).append(name).append(File.separator).append("VersionInfo.xml");
            } else {

            }
            logger.info("========File path :"+sb.toString());
            File file = new File(sb.toString());
            if (file.exists()) {
                result = ResponseData.successData(readfile(file));
            } else {
//                String filePath = sb.toString();
//                String cloudFilePath = filePath.replace(orgId, "100");
//                File cloudFile = new File(cloudFilePath);
//                if (cloudFile.exists()) {
//                    logger.info("========Cloud File path :"+cloudFilePath);
//                    result = ResponseData.successData(readfile(cloudFile));
//                }else{
                result = ResponseData.errorData("resource not exist ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Message: "+ e.getMessage());
            return ResponseData.errorData("find file error ");
        }
        return result;
    }

    /**
     * 根据主板编号返回店铺信息
     * @param mainBoardCode
     * @return
     */
    @Override
    public ResponseData getShopInfo(String mainBoardCode) {
        DeviceUsed deviceUsed = deviceUsedService.getStoreInfoByMbCode(mainBoardCode);
        MerchantStore merchantStore = deviceUsed.getStore();
        List<TableType> tableTypeList = tableTypeService.listByStore(merchantStore.getId());
        for(TableType tableType:tableTypeList){
            tableType.setStore(null);
        }
        ResponseData result = null;
        Map<String, Object> map = new HashMap<String,Object>();
        if (merchantStore != null) {
            map.put("shopInfo",merchantStore);
            map.put("tableType",tableTypeList);
            result = ResponseData.successData(JSON.toJSONString(map));
        } else {
            result = ResponseData.errorData("error");
        }
        return result;
    }

    @Override
    public ResponseData sendResourceToCloud(boolean flag, Long orgId,String resourceType, String filePath) {
        return null;
    }

    @Override
    public ResponseData uploadData(String type, String data) {
        if (type != null && "selfCheckLog".equals(type)) {
            SelfCheckLogVO selfCheckLogVO = JSON.parseObject(data, SelfCheckLogVO.class);
            try {
                SelfCheckLog scl = selfCheckLogService.update(new SelfCheckLog(selfCheckLogVO));
                if(scl != null) {
                    return ResponseData.success();
                }
            } catch (ServiceException e) {
                System.out.println("error: " + e.toString());
            }
        }
        return ResponseData.error();
    }

    @Override
    public ResponseData getAgentVersionCode() {
        //TODO 云端不用实现
        return null;
    }

    private String readfile(File file){
        SAXReader saxReader = new SAXReader();
        JSONObject object = new JSONObject();
        try {
            Document document = saxReader.read(file);
            Element root = document.getRootElement();
            // 迭代
            for (Iterator iter = root.elementIterator(); iter.hasNext();) {
                Element e = (Element) iter.next();
                object.put(e.getName(), e.getData());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return object.toJSONString();
    }

    public String getIdentityCode(String redisKeyOfUcdScId) {
        String identityCode = getSimple(redisKeyOfUcdScId).toString();
        return identityCode;
    }

}
