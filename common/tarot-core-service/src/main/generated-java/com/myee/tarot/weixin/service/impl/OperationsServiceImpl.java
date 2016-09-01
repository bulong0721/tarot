package com.myee.tarot.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.myee.djinn.dto.*;
import com.myee.djinn.rpc.RemoteException;
import com.myee.djinn.server.operations.OperationsService;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.catering.service.TableTypeService;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.DateUtil;
import com.myee.tarot.datacenter.domain.SelfCheckLog;
import com.myee.tarot.datacenter.domain.SelfCheckLogVO;
import com.myee.tarot.datacenter.service.SelfCheckLogService;
import com.myee.tarot.catalog.service.DeviceUsedService;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantStoreService;
import com.myee.tarot.weixin.dao.WxWaitTokenDao;
import com.myee.tarot.weixin.domain.WxWaitToken;
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
import java.io.File;
import java.util.*;

/**
 * Created by Martin on 2016/1/27.
 */
@Service
public class OperationsServiceImpl extends RedisOperation implements OperationsService {
    private static Logger logger = LoggerFactory.getLogger(OperationsServiceImpl.class);

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    @Autowired
    @Lazy
    private WxWaitTokenDao waitTokenDao;

    @Autowired
    public OperationsServiceImpl(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Autowired
    private WxMpService wxMpService;

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
	public ResponseData execute(Map<String, Object> map) throws RemoteException {
		return null;
	}

	@Override
	public Boolean isConnection() throws RemoteException {
		return  true;
	}

	@Override
    public ResponseData take(WaitToken waitToken) {
        //排号Key
        String redisKeyOfShopTb = RedisKeys.waitOfTableType(waitToken.getShopId(), waitToken.getTableTypeId());
        //唯一码-SeceneId对应Key
        String sourceStr = RedisKeys.getIdentityCode(Long.valueOf(waitToken.getSceneId()));
        String redisKeyOfUcdScId = sourceStr.substring(0, 22) + sourceStr.substring(23);
        Date date = new Date();
        Date endDate = DateUtil.getNextDayOfDate(date, 2, 30, 0);
        waitToken.setTimeTook(date.getTime() / 1000);
        //然后二维码的数字码放Redis，跟identityCode唯一码绑定
        hsetSimple(redisKeyOfUcdScId, waitToken.getIdentityCode(), null);
        //然后identityCode放Redis，跟排号的key绑定
        hsetSimple(waitToken.getIdentityCode(), redisKeyOfShopTb, endDate);
        hset(redisKeyOfShopTb, waitToken.getToken(), waitToken, endDate);
        WxMpQrCodeTicket myticket = null;
        WxWaitToken wxWaitToken = null;
        try {
            myticket = wxMpService.qrCodeCreateTmpTicket(Integer.parseInt(waitToken.getSceneId()), 2592000);
            wxWaitToken = waitTokenDao.update(WeixinServiceImpl.convertTo(waitToken, date.getTime() / 1000));
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
    public Boolean skip(WaitToken waitToken) {
        return statusChange(WaitTokenState.SKIP.getValue(), waitToken);
    }

    private Ordering<WaitToken> orderingByTook = Ordering.from(new Comparator<WaitToken>() {
        @Override
        public int compare(WaitToken o1, WaitToken o2) {
            return o1.getTimeTook().compareTo(o2.getTimeTook());
        }
    });

    /*
     * 就餐
     *
     * @param waitToken
     * @param tableId
      */
    @Override
    public Boolean repast(WaitToken waitToken, long tableId) {
        return statusChange(WaitTokenState.REPASTING.getValue(), waitToken);
    }

    /*
     * 改变就餐状态的方法
     * @param stateValue
     * @param waitToken
     * @return
     * */

    private boolean statusChange(int stateValue, WaitToken waitToken) {
        int backStatus = 0;
        Date date = new Date();
        String redisKey = RedisKeys.waitOfTableType(waitToken.getShopId(), waitToken.getTableTypeId());
        Map<String, WaitToken> map = hgetall(redisKey, WaitToken.class);
        if (map != null) { //如果redis里有数据，直接从Redis里拿
            List<WaitToken> sortedTokens = orderingByTook.sortedCopy(map.values());
            if (waitToken.getShopId().equals(sortedTokens.get(0).getShopId()) && waitToken.getToken().equals(sortedTokens.get(0).getToken())) {
                hdelete(redisKey, waitToken.getToken());
                //改状态2
                backStatus = waitTokenDao.updateState(stateValue, waitToken.getClientId(), waitToken.getShopId(), waitToken.getToken(), new Date(sortedTokens.get(0).getTimeTook()), new Date());
            }
        } else { //如果redis里没有数据，从MySql里同步
            List<WxWaitToken> tokenList = waitTokenDao.listByConditions(waitToken.getShopId(), waitToken.getTableTypeId(), WaitTokenState.WAITING.getValue());
            List<WaitToken> sortedTokensW = Lists.newArrayList();
            for (int i = 0; i < tokenList.size(); i++) {
                WaitToken w = new WaitToken();
                WxWaitToken wwToken = tokenList.get(i);
                w.setToken(wwToken.getToken());
                w.setTimeTook(wwToken.getTimeTook().getTime());
                w.setTableId(wwToken.getTableId());
                w.setOpenId(wwToken.getOpenId());
                w.setShopId(wwToken.getStore().getId());
                w.setIdentityCode(wwToken.getIdentityCode());
                sortedTokensW.add(w);
                hset(redisKey, w.getToken(), w, DateUtil.getNextDayOfDate(date, 2, 30, 0));
            }
            if (waitToken.getToken().equals(sortedTokensW.get(0).getToken()) && waitToken.getShopId().equals(sortedTokensW.get(0).getShopId())) {
                //改状态2
                backStatus = waitTokenDao.updateState(stateValue, waitToken.getClientId(), waitToken.getShopId(), waitToken.getToken(), new Date(sortedTokensW.get(0).getTimeTook()), new Date());
            }
        }
        //移除就餐桌号后的
        Map<String, WaitToken> afterMap = hgetall(redisKey, WaitToken.class);
        List<Map<String, Object>> openIdList = Lists.newArrayList();
        if (afterMap != null) {
            for (WaitToken w : afterMap.values()) {
                if (w.getOpenId() != null && w.getOpenId().trim().length() > 0) {
                    //比较token查询进展
                    Map<String, Object> msgMap = listProgressByIdentityCode(w.getIdentityCode());
                    msgMap.put("openId", w.getOpenId());
                    openIdList.add(msgMap);
                }
            }
        } else {
            List<WxWaitToken> rwList = waitTokenDao.listByConditions(waitToken.getShopId(), waitToken.getTableTypeId(), WaitTokenState.WAITING.getValue());
            for (WxWaitToken rw : rwList) {
                if (rw.getOpenId() != null && rw.getOpenId().trim().length() > 0) {
                    //比较token查询进展
                    Map<String, Object> msgMap = listProgressByIdentityCode(rw.getIdentityCode());
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
                templateMessage.getDatas().add(new WxMpTemplateData("keyword1", m.get("shopName") == null ? "" : m.get("shopName").toString(), "#c37160"));
                templateMessage.getDatas().add(new WxMpTemplateData("keyword2", m.get("tokenNum").toString(), "#36b2cc"));
                templateMessage.getDatas().add(new WxMpTemplateData("keyword3", m.get("waitedTable").toString(), "#ff9900"));
                templateMessage.getDatas().add(new WxMpTemplateData("keyword4", m.get("predictTime").toString(), "#053eea"));
                templateMessage.getDatas().add(new WxMpTemplateData("keyword5", m.get("queueStatus") == null ? "" : m.get("queueStatus").toString(), "#ea05b1"));
                templateMessage.getDatas().add(new WxMpTemplateData("remark", m.get("queryTime").toString(), "#4a5de8"));
                wxMpService.templateSend(templateMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (backStatus == 1) {
            return true;
        } else {
            return false;
        }
    }

    public Map<String, Object> listProgressByIdentityCode(String identityCode) {
        //先到Redis里去找，通过identityCode获取当时保存token的redisKey
        String redisKey2Find = getSimple(identityCode).toString();
        Map<String, WaitToken> waitTokenMap = hgetall(redisKey2Find, WaitToken.class);
        Set<Integer> waitNumSet = Sets.newHashSet();
        Map<String, Object> latestDevInfo = new HashMap<String, Object>();
        String shopName = null;
        //顾客取号的号数
        int userNum = 0;
        //之前等待桌数
        int beforeCount = 0;
        //等待时长
        int waitMinutes = 0;
        String waitToken = "";
        String queueStatus = null;
        long timeTook = 0;
        Date date = new Date();
        if (waitTokenMap != null && !waitTokenMap.isEmpty()) {
            for (WaitToken wt : waitTokenMap.values()) {
                waitNumSet.add(Integer.parseInt(wt.getToken().substring(1, 3)));
                if (identityCode.equals(wt.getIdentityCode())) {
                    userNum = Integer.parseInt(wt.getToken().substring(1, 3));
                    waitToken = wt.getToken();
                    timeTook = wt.getTimeTook();
                    shopName = wt.getClientName();
                    queueStatus = assignVal(wt.getWaitStatus());
                }
            }
            for (int i : waitNumSet) {
                if (i < userNum) {
                    beforeCount++;
                }
            }
            codeBlock(latestDevInfo, shopName, waitToken, beforeCount, queueStatus, timeTook, date);
            latestDevInfo.put("waitedTableCount", beforeCount);
            latestDevInfo.put("predictWaitingTime", Long.valueOf(beforeCount * 10));
        } else {
            Date startDate = DateUtil.getZeroClockOfDate(date);
            Date endDate = DateUtil.getNextDayOfDate(date, 2, 30, 0);
            Long bTimeLong = startDate.getTime() / 1000;
            Long eTimeLong = endDate.getTime() / 1000;
            //再到MYSQL去找,用identityCode找到对应的clientId,orgId,和token，
            WxWaitToken wtoken = waitTokenDao.getByIdentityCode(identityCode, bTimeLong, eTimeLong);
            if (wtoken != null) {
                //根据clientId和orgId和tableId，找到该餐馆的某餐桌类型等待的token
                List<WxWaitToken> tokenList = waitTokenDao.listByConditions(wtoken.getStore().getId(), wtoken.getTableId(), WaitTokenState.WAITING.getValue());
                for (WxWaitToken wt : tokenList) {
                    waitNumSet.add(Integer.parseInt(wt.getToken().substring(1, 3)));
                    if (identityCode.equals(wt.getIdentityCode())) {
                        userNum = Integer.parseInt(wt.getToken().substring(1, 3));
                        timeTook = wt.getTimeTook().getTime();
                        shopName = merchantStoreService.findById(wt.getStore().getId()).getName();
                        queueStatus = assignVal(wt.getState());
                    }
                }
                for (int i : waitNumSet) {
                    if (i < userNum) {
                        beforeCount++;
                    }
                }
                codeBlock(latestDevInfo, shopName, waitToken, beforeCount, queueStatus, timeTook, date);
            } else {
                latestDevInfo.put("vaild", "您查询的编号不存在");
            }
        }
        return latestDevInfo;
    }

    private void codeBlock(Map<String, Object> latestDevInfo, String shopName, String waitToken, int beforeCount, String queueStatus, long timeTook, Date date) {
        latestDevInfo.put("shopName", shopName);
        latestDevInfo.put("tokenNum", waitToken);
        latestDevInfo.put("waitedTable", String.valueOf(beforeCount) + "桌");
        latestDevInfo.put("predictTime", String.valueOf(beforeCount * 10) + "分钟");
        latestDevInfo.put("queueStatus", queueStatus);
        latestDevInfo.put("timeTook", timeTook);
        latestDevInfo.put("queryTime", DateUtil.formatDateTime(date));
    }

    private String assignVal(int status) {
        String str = "";
        if (status == 1) {
            str = "排队中";
        } else if (status == 2){
            str = "就餐中";
        } else if(status == 3) {
            str = "已过号";
        } else if (status == 4) {
            str = "已取消";
        } else if (status == 6) {
            str = "已发送";
        } else
            str = "未发送";
        return str;
    }

    /*
     * 取消排号
     *
     * @param waitToken
     * @param reason
     * @return
     * */

    @Override
    public Boolean cancel(WaitToken waitToken, String reason) {
        return true;
    }

    @Override
    public Map<String,String> getWaitTokenInfo(Long tableTypeId) {
        return null;
    }
    /**
     * @param jsonArgs
     * @return
     */
//    @Override
    public String getResourceInfo(String jsonArgs) {
        System.out.println("jsonArgs: " + jsonArgs);
        System.out.println("DOWNLOAD_HOME: " + DOWNLOAD_HOME);
        String result = null;
        try {
            JSONObject object = JSON.parseObject(jsonArgs);
            String name = object.getString("name");
            String type = object.getString("type");
            String orgId = object.getString("orgId");
            StringBuilder sb = new StringBuilder();
            logger.info("================ request info  name:" + name + "  type:" + type + "   orgId:" + orgId);
            //TODO
            if ("app".equals(type) || "ipc".equals(type)) {
                sb.append(DOWNLOAD_HOME).append(File.separator).append(orgId).append(File.separator).append(type).append(File.separator).append(name).append(File.separator).append("VersionInfo.xml");
            } else {

            }
            logger.info("========File path :" + sb.toString());
            File file = new File(sb.toString());
            if (file.exists()) {
                result = readfile(file);
            }
//			else {
//                String filePath = sb.toString();
//                String cloudFilePath = filePath.replace(orgId, "100");
//                File cloudFile = new File(cloudFilePath);
//                if (cloudFile.exists()) {
//                    logger.info("========Cloud File path :"+cloudFilePath);
//                    result = ResponseData.successData(readfile(cloudFile));
//                }else{
//                result = ResponseData.errorData("resource not exist ");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Message: " + e.getMessage());
            return null;
        }
        return result;
    }

    /**
     * 根据主板编号返回店铺信息
     *
     * @param mainBoardCode
     * @return
     */
    @Override
    public Map<String,String> getShopInfo(String mainBoardCode) {
        DeviceUsed deviceUsed = deviceUsedService.getStoreInfoByMbCode(mainBoardCode);
        MerchantStore merchantStore = deviceUsed.getStore();
        List<TableType> tableTypeList = tableTypeService.listByStore(merchantStore.getId());
        for (TableType tableType : tableTypeList) {
            tableType.setStore(null);
        }
        Map<String, String> map = new HashMap<String, String>();
        if (merchantStore != null) {
            map.put("shopInfo", JSON.toJSONString(merchantStore));
            map.put("tableType", JSON.toJSONString(tableTypeList));
        }
        return map;
    }

    @Override
    public Boolean sendResourceToCloud(Long orgId, UploadFileType resourceType, String filePath) {
        return false;
    }

    @Override
    public Boolean uploadData(String type, String data) {
        if (type != null && "selfCheckLog".equals(type)) {
            SelfCheckLogVO selfCheckLogVO = JSON.parseObject(data, SelfCheckLogVO.class);
            try {
                SelfCheckLog scl = selfCheckLogService.update(new SelfCheckLog(selfCheckLogVO));
                if (scl != null) {
                    return true;
                }
            } catch (ServiceException e) {
                System.out.println("error: " + e.toString());
            }
        }
        return false;
    }

    @Override
    public ResponseData checkAgentUpdateInfo() {
        //TODO 云端不用实现
        return null;
    }

    private String readfile(File file) {
        SAXReader saxReader = new SAXReader();
        JSONObject object = new JSONObject();
        try {
            Document document = saxReader.read(file);
            Element root = document.getRootElement();
            // 迭代
            for (Iterator iter = root.elementIterator(); iter.hasNext(); ) {
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