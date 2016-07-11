package com.myee.tarot.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Ordering;
import com.myee.djinn.dto.DrawToken;
import com.myee.djinn.dto.ResponseData;
import com.myee.djinn.dto.WaitToken;
import com.myee.djinn.dto.WaitTokenState;
import com.myee.djinn.server.operations.OperationsService;
import com.myee.tarot.weixin.common.Constants;
import com.myee.tarot.weixin.dao.RWaitTokenDao;
import com.myee.tarot.weixin.domain.RWaitToken;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Martin on 2016/1/27.
 */
@Service
public class OperationsManager extends RedisOperation implements OperationsService {
    private static Logger logger = LoggerFactory.getLogger(OperationsManager.class);

    @Autowired
    @Lazy
    private RWaitTokenDao waitTokenDao;

    @Autowired
    public OperationsManager(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    @Autowired
    private WxMpService wxMpService;

    static int i = 0;

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
        String redisKeyOfUcdScId = RedisKeys.getIdentityCode(Long.valueOf(waitToken.getSceneId()));
        String identityCode = waitToken.getIdentityCode();
        waitToken.setIdentityCode(identityCode);
        Date date = new Date();
        waitToken.setTimeTook(date.getTime() / 1000);
        RWaitToken rWaitToken = new RWaitToken();
        rWaitToken = waitTokenDao.update(WeixinManager.convertTo(waitToken, date.getTime() / 1000));
        //然后二维码的数字码放Redis，跟identityCode唯一码绑定
        hsetSimple(redisKeyOfUcdScId, waitToken.getIdentityCode(), null);
        //然后identityCode放Redis，跟排号的key绑定
        Map<String, Date> map1 = new HashMap<String, Date>();
        map1 = TimeUtil.getAfterDate(date);
        append(waitToken.getIdentityCode(), redisKeyOfShopTb, map1.get("eTime"));
        hset(redisKeyOfShopTb, waitToken.getToken(), waitToken, map1.get("eTime"));
        WxMpQrCodeTicket myticket = null;
        try {
            myticket = wxMpService.qrCodeCreateTmpTicket(Integer.parseInt(waitToken.getSceneId()), 2592000);
            logger.info("QrCodeUrl-----Take-------->"+ myticket.getUrl());
            System.out.println("QrCodeUrl-----Take");
        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        if (rWaitToken.getId() != null) {
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
        logger.info("-----Skip-----");
        System.out.println("-----Skip-----");
        return statusChange(WaitTokenState.SKIP.getValue(),waitToken);
    }

    private Ordering<WaitToken> orderingByTook = Ordering.from(new Comparator<WaitToken>() {
        @Override
        public int compare(WaitToken o1, WaitToken o2) {
            return o1.getTimeTook().compareTo(o2.getTimeTook());
        }
    });

    private Ordering<RWaitToken> orderingByTook2 = Ordering.from(new Comparator<RWaitToken>() {
        @Override
        public int compare(RWaitToken o1, RWaitToken o2) {
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
        logger.info("-----repast-----");
        System.out.println("-----repast-----");
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
            List<RWaitToken> tokenList = waitTokenDao.selectAllTokenByInfo(waitToken.getClientId(), waitToken.getShopId(), waitToken.getTableTypeId(), WaitTokenState.WAITING.getValue());
            List<RWaitToken> sortedTokens = orderingByTook2.sortedCopy(tokenList);
            List<WaitToken> sortedTokensW = new ArrayList<WaitToken>();
            for (int i = 0; i < sortedTokens.size(); i++) {
                WaitToken w = new WaitToken();
                w.setToken(sortedTokens.get(i).getToken());
                w.setTimeTook(sortedTokens.get(i).getTimeTook().getTime());
                w.setTableTypeId(sortedTokens.get(i).getTableTypeId());
                w.setClientId(sortedTokens.get(i).getClientID());
                w.setOpenId(sortedTokens.get(i).getOpenId());
                w.setShopId(sortedTokens.get(i).getOrgID());
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
            List<RWaitToken> rwList = waitTokenDao.selectAllTokenOpenIdNotNull(waitToken.getClientId(), waitToken.getShopId(), waitToken.getTableTypeId(), WaitTokenState.WAITING.getValue());
            for (RWaitToken rw : rwList) {
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
                    String shopKey = RedisKeys.shopOfClient(wt.getShopId());
//                    shopName = getSimple(shopKey).toString();
                    userNum = Integer.parseInt(wt.getToken().substring(1, 3));
                    waitMinutes = wt.getWaitMinutes();
                    waitToken = wt.getToken();
                    timeTook = wt.getTimeTook();
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
            RWaitToken wtoken = waitTokenDao.selectTokenByIc(identityCode, bTimeLong, eTimeLong);
            if (wtoken != null) {
                //根据clientId和orgId和tableId，找到该餐馆的某餐桌类型等待的token
                List<RWaitToken> tokenList = waitTokenDao.selectAllTokenByInfo(wtoken.getClientID(), wtoken.getOrgID(), wtoken.getTableTypeId(), WaitTokenState.WAITING.getValue());
                for (RWaitToken wt : tokenList) {
                    waitNumSet.add(Integer.parseInt(wt.getToken().substring(1, 3)));
                    if (identityCode.equals(wt.getIdentityCode())) {
                        userNum = Integer.parseInt(wt.getToken().substring(1, 3));
                        timeTook = wt.getTimeTook().getTime();
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
        logger.info("----cancel----");
        System.out.println("----cancel----");
        return new ResponseData(true);
    }

    @Override
    public ResponseData draw(DrawToken drawToken) {
        logger.info("----draw----");
        System.out.println("----draw----");
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

//    @Override
    public ResponseData getResourceInfo(String jsonArgs) {
        ResponseData result = null;
        JSONObject object = JSON.parseObject(jsonArgs);
        String name = object.getString("name");
        String type = object.getString("type");
        String orgId = object.getString("orgId");
        StringBuilder sb = new StringBuilder();
        logger.info("================ request info  name:"+name+"  type:"+type+"   orgId:"+orgId);
        if ("app".equals(type)) {
            sb.append(Constants.DOWNLOAD_HOME).append(File.separator).append(orgId).append(File.separator).append(type).append(File.separator).append(name).append(File.separator).append("VersionInfo.xml");
        } else {

        }
        logger.info("========File path :"+sb.toString());
        File file = new File(sb.toString());
        if (file.exists()) {
            result = ResponseData.successData(readfile(file));
        } else {
            String filePath = sb.toString();
            String cloudFilePath = filePath.replace("104", "100");
            File cloudFile = new File(cloudFilePath);
            if (cloudFile.exists()) {
                logger.info("========Cloud File path :"+cloudFilePath);
                result = ResponseData.successData(readfile(cloudFile));
            }else{
                result = ResponseData.errorData("error");
            }
        }
        return result;
    }

    @Override
    public ResponseData sendResourceToCloud(String resourceType, String filePath) {
        return null;
    }

    @Override
    public ResponseData getShopInfo(String clientId) {
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