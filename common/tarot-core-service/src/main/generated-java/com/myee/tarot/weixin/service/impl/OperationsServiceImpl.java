package com.myee.tarot.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import com.myee.tarot.uitl.CacheUtil;
import com.myee.tarot.weixin.dao.WxWaitTokenDao;
import com.myee.tarot.weixin.domain.WxWaitToken;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.WxMpTemplateMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteSet;
import org.apache.ignite.configuration.CollectionConfiguration;
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
import javax.cache.Cache;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.io.File;
import java.util.*;
import static org.apache.ignite.cache.CacheAtomicityMode.TRANSACTIONAL;
import static org.apache.ignite.cache.CacheMode.PARTITIONED;

/**
 * Created by Martin on 2016/1/27.
 */
@Service
public class OperationsServiceImpl implements OperationsService {
    private static Logger logger = LoggerFactory.getLogger(OperationsServiceImpl.class);

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    @Value("${weixin.imageArticleMsg.templateId}")
    private String IMAGE_ARTICLE_MSG_TEMPLEID;

    @Autowired
    @Lazy
    private WxWaitTokenDao waitTokenDao;

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

    private static final int WEIXIN_EXPIRE_TIME = 2592000; //微信过期时效

    private static final int DIVIDE_MILLSECOND = 1000; //new Date().getTime()要除以1000

    private static final int HOUR = 2;

    private static final int MINUTE = 30;

    private static final int SECOND = 0;

    private static final String STATE_WAITING = "排队中";

    private static final String STATE_DINNING = "就餐中";

    private static final String STATE_PASSED = "已过号";

    private static final String STATE_CANCELED = "已取消";

    private static final String STATE_SENT = "已发送";

    private static final String STATE_NOT_SENT = "未发送";

    private static final String SHOP_NAME = "keyword1";

    private static final String QUEUE_NUMBER = "keyword2";

    private static final String WAITING_AHEAD_COUNT = "keyword3";

    private static final String PREDICT_WAITING_TIME = "keyword4";

    private static final String QUEUE_STATUS = "keyword5";

    private static final String QUERY_TIME = "remark";

    @Autowired
    private Ignite ignite;

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
        return true;
    }

    /**
     * 取号接口的实现
     *
     * @param waitToken
     * @return
     */
    @Override
    public ResponseData take(WaitToken waitToken) {
        Date createDate = waitToken.getCreateDate();
        waitToken.setTimeTook(createDate.getTime() / DIVIDE_MILLSECOND);
        //获取第二天凌晨2点30分作为结束时间
        Date endDate = DateUtil.getNextDayOfDate(createDate, HOUR, MINUTE, SECOND);
        //排号Key，第一个参数是店铺id，第二个参数是餐桌类型ID，例如：waitOfTableType:100-103
        String sourceStr = CacheUtil.getIdentityCode(Long.valueOf(waitToken.getSceneId()));
        //sourceStr截取之前是sceneIdToIdentityCode:110510303，把1截掉，其中1是为了区分查询最新进展和扫码抽奖设置的标识位,10510303是传递的参数
        String keyQrCode = sourceStr.substring(0, 22) + sourceStr.substring(23);
        IgniteCache<String, WaitToken> waitTokenCache = CacheUtil.waitOfTableType(ignite, waitToken.getTableTypeId());
        String waitOfTableTypeKey = CacheUtil.getWaitOfTableType(waitToken.getTableTypeId());

        //然后二维码的数字码放Redis，跟identityCode唯一码绑定，第一个参数redisKeyOfUcdScId作为key，第二个参数waitToken.getIdentityCode()唯一码作为value，第三个参数null是失效时间
        IgniteCache<String, String> cacheQrCodeIdentityCode = CacheUtil.qrParametersIdentityCode(ignite);
        cacheQrCodeIdentityCode.put(keyQrCode, waitToken.getIdentityCode());
        cacheQrCodeIdentityCode.withExpiryPolicy(new AccessedExpiryPolicy(new Duration(createDate.getTime() / DIVIDE_MILLSECOND, endDate.getTime() / DIVIDE_MILLSECOND)));

        //然后identityCode唯一码放Redis，跟第二个参数排号key绑定，第一个参数是key,第二个参数是value，这里放排号key，第三个是失效时间
        IgniteCache<String, String> identityCodeWaitTokenTypeKeyCache = CacheUtil.identityCodeWaitTokenType(ignite);
        identityCodeWaitTokenTypeKeyCache.put(waitToken.getIdentityCode(), waitOfTableTypeKey);

        identityCodeWaitTokenTypeKeyCache.withExpiryPolicy(new AccessedExpiryPolicy(new Duration(createDate.getTime() / DIVIDE_MILLSECOND, endDate.getTime() / DIVIDE_MILLSECOND)));

        CollectionConfiguration setCfg = new CollectionConfiguration();
        setCfg.setAtomicityMode(TRANSACTIONAL);
        setCfg.setCacheMode(PARTITIONED);
        IgniteSet<String> setTokenCache = CacheUtil.waitOfTableTypeSet(ignite, waitOfTableTypeKey, setCfg);
        setTokenCache.add(waitToken.getToken());

        //第一个参数是排号key，作为key，第二个参数是排号(如：A01，B02)，第三个失效时间
        waitTokenCache.put(waitToken.getToken(), waitToken);
        waitTokenCache.withExpiryPolicy(new AccessedExpiryPolicy(new Duration(createDate.getTime() / DIVIDE_MILLSECOND, endDate.getTime() / DIVIDE_MILLSECOND)));
        //微信第三方封装的二维码ticket实体
        WxMpQrCodeTicket myticket = null;
        //微信排号实体
        WxWaitToken wxWaitToken = null;
        try {
            //创建临时二维码，第一个参数是二维码封装的参数如：110510303，第二个参数有效时间，这里设置1个月
            myticket = wxMpService.qrCodeCreateTmpTicket(Integer.parseInt(waitToken.getSceneId()), WEIXIN_EXPIRE_TIME);
            //入库
            wxWaitToken = waitTokenDao.update(WeixinServiceImpl.convertTo(waitToken, createDate.getTime() / DIVIDE_MILLSECOND));
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
        String waitOfTableTypeKey = CacheUtil.getWaitOfTableType(waitToken.getTableTypeId());
        IgniteCache<String, WaitToken> waitTokenCache = CacheUtil.waitOfTableType(ignite, waitToken.getTableTypeId());

        CollectionConfiguration setCfg = new CollectionConfiguration();
        setCfg.setAtomicityMode(TRANSACTIONAL);
        setCfg.setCacheMode(PARTITIONED);
        IgniteSet<String> setTokenCache = CacheUtil.waitOfTableTypeSet(ignite,waitOfTableTypeKey, setCfg);

        Map<String, WaitToken> waitTokenMap = waitTokenCache.getAll(setTokenCache);
        if (waitTokenMap != null) { //如果redis里有数据，直接从Redis里拿
            //按照取号的先后顺序排序
            List<WaitToken> sortedTokens = orderingByTook.sortedCopy(waitTokenMap.values());
            if (waitToken.getShopId().equals(sortedTokens.get(0).getShopId()) && waitToken.getToken().equals(sortedTokens.get(0).getToken())) { //判断如果当前修改的waitToken是第一个结果的shopId，排号也相同
                waitTokenCache.remove(waitToken.getToken());
                Date timeTook = new Date(sortedTokens.get(0).getTimeTook()); //获取取号时间
                backStatus = waitTokenDao.updateState(stateValue, waitToken.getClientId(), waitToken.getShopId(), waitToken.getToken(), timeTook, date); //改状态值,如果成功则返回1
            }
        } else { //如果redis里没有数据，从MySql里同步
            //根据店铺ID，店铺桌型ID和状态值查询都有哪些排号
            List<WxWaitToken> tokenList = waitTokenDao.listByConditions(waitToken.getShopId(), waitToken.getTableTypeId(), WaitTokenState.WAITING.getValue());
            List<WaitToken> sortedTokensW = Lists.newArrayList();
            for (int i = 0; i < tokenList.size(); i++) { //将这些排号放redis
                WaitToken w = new WaitToken();
                WxWaitToken wxToken = tokenList.get(i);
                w.setToken(wxToken.getToken());
                w.setTimeTook(wxToken.getTimeTook().getTime());
                w.setTableId(wxToken.getTableId());
                w.setOpenId(wxToken.getOpenId());
                w.setShopId(wxToken.getStore().getId());
                w.setIdentityCode(wxToken.getIdentityCode());
                sortedTokensW.add(w);
                //将数据库里的记录放入ignite，并设置失效时间
                waitTokenCache.put(waitToken.getToken(),waitToken);
            }
            //判断是否跟最近的排号匹配
            if (waitToken.getToken().equals(sortedTokensW.get(0).getToken()) && waitToken.getShopId().equals(sortedTokensW.get(0).getShopId())) {
                //改状态
                backStatus = waitTokenDao.updateState(stateValue, waitToken.getClientId(), waitToken.getShopId(), waitToken.getToken(), new Date(sortedTokensW.get(0).getTimeTook()), new Date());
            }
        }
        if (waitTokenMap != null) {
            for (WaitToken w : waitTokenMap.values()) {
                if (w.getOpenId() != null && w.getOpenId().trim().length() > 0) {
                    //获取每个排号的唯一码，并返回它们的进展信息
                    Map<String, Object> msgMap = listProgressByIdentityCode(w.getIdentityCode(), w.getTableTypeId());
                    sendImageArticleMsg(msgMap);
                }
            }
        } else {
            //查询正在排队等待的已经扫码查询过的人的排号List
            List<WxWaitToken> rwList = waitTokenDao.listByConditions(waitToken.getShopId(), waitToken.getTableTypeId(), WaitTokenState.WAITING.getValue());
            for (WxWaitToken rw : rwList) {
                if (rw.getOpenId() != null && rw.getOpenId().trim().length() > 0) {
					//比较token查询进展
                    Map<String, Object> msgMap = listProgressByIdentityCode(rw.getIdentityCode(), rw.getTableTypeId());
                    sendImageArticleMsg(msgMap);
                }
            }
        }
        return backStatus == 1;
    }

    /**
     * 根据map里的保存的最新进展信息，发送图文消息
     *
     * @param m
     */
    private void sendImageArticleMsg(Map m) {
        try {
            WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
            templateMessage.setToUser(m.get("openId").toString());
            templateMessage.setTemplateId(IMAGE_ARTICLE_MSG_TEMPLEID);
            templateMessage.getDatas().add(new WxMpTemplateData(SHOP_NAME, m.get("shopName") == null ? "" : m.get("shopName").toString(), "#c37160"));
            templateMessage.getDatas().add(new WxMpTemplateData(QUEUE_NUMBER, m.get("tokenNum").toString(), "#36b2cc"));
            templateMessage.getDatas().add(new WxMpTemplateData(WAITING_AHEAD_COUNT, m.get("waitedTable").toString(), "#ff9900"));
            templateMessage.getDatas().add(new WxMpTemplateData(PREDICT_WAITING_TIME, m.get("predictTime").toString(), "#053eea"));
            templateMessage.getDatas().add(new WxMpTemplateData(QUEUE_STATUS, m.get("queueStatus") == null ? "" : m.get("queueStatus").toString(), "#ea05b1"));
            templateMessage.getDatas().add(new WxMpTemplateData(QUERY_TIME, m.get("queryTime").toString(), "#4a5de8"));
            wxMpService.templateSend(templateMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据唯一码查询进展信息
     *
     * @param identityCode
     * @return
     */
    public Map<String, Object> listProgressByIdentityCode(String identityCode, long tableTypeId) {
        //先到Ignite里去找，通过identityCode获取当时保存token的redisKey
        Cache<String, String> identityCodeWaitTokenTypeKeyCache = CacheUtil.identityCodeWaitTokenType(ignite);
        String waitTokenTypeKey = identityCodeWaitTokenTypeKeyCache.get(identityCode);

        CollectionConfiguration setCfg = new CollectionConfiguration();
        setCfg.setAtomicityMode(TRANSACTIONAL);
        setCfg.setCacheMode(PARTITIONED);
        IgniteSet<String> setTokenCache = CacheUtil.waitOfTableTypeSet(ignite, waitTokenTypeKey, setCfg);

        IgniteCache<String, WaitToken> waitTokenCache = CacheUtil.waitOfTableType(ignite, tableTypeId);
        Map<String, WaitToken> waitTokenMap = waitTokenCache.getAll(setTokenCache);
        Set<Integer> waitNumSet = Sets.newHashSet();
        Map<String, Object> latestProgressInfo = Maps.newHashMap();
        String shopName = null;
        //顾客取号的号数
        int userNum = 0;
        //之前等待桌数
        int beforeCount = 0;
        String waitToken = "";
        String queueStatus = null;
        long timeTook = 0;
        String openId = "";
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
                    openId = wt.getOpenId();
                }
            }
            for (int i : waitNumSet) {
                if (i < userNum) {
                    beforeCount++;
                }
            }
            codeBlock(latestProgressInfo, shopName, waitToken, beforeCount, queueStatus, timeTook, date, openId);
            latestProgressInfo.put("waitedTableCount", beforeCount);
            latestProgressInfo.put("predictWaitingTime", Long.valueOf(beforeCount * 10));
        } else {
            //获取当天的0点0分0秒的时间
            Date startDate = DateUtil.getZeroClockOfDate(date);
            //获取第二天的2点30分的时间
            Date endDate = DateUtil.getNextDayOfDate(date, HOUR, MINUTE, SECOND);
            Long bTimeLong = startDate.getTime();
            Long eTimeLong = endDate.getTime();
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
                        openId = wt.getOpenId();
                    }
                }
                for (int i : waitNumSet) {
                    if (i < userNum) {
                        beforeCount++;
                    }
                }
                codeBlock(latestProgressInfo, shopName, waitToken, beforeCount, queueStatus, timeTook, date, openId);
            } else {
                latestProgressInfo.put("vaild", "您查询的编号不存在");
            }
        }
        return latestProgressInfo;
    }

    /**
     * 设置返回信息封装在map里
     *
     * @param latestProgressInfo
     * @param shopName
     * @param waitToken
     * @param beforeCount
     * @param queueStatus
     * @param timeTook
     * @param date
     */
    private void codeBlock(Map<String, Object> latestProgressInfo, String shopName, String waitToken, int beforeCount, String queueStatus, long timeTook, Date date, String openId) {
        latestProgressInfo.put("shopName", shopName);
        latestProgressInfo.put("tokenNum", waitToken);
        latestProgressInfo.put("waitedTable", String.valueOf(beforeCount) + "桌");
        latestProgressInfo.put("predictTime", String.valueOf(beforeCount * 10) + "分钟");
        latestProgressInfo.put("queueStatus", queueStatus);
        latestProgressInfo.put("timeTook", timeTook);
        latestProgressInfo.put("queryTime", DateUtil.formatDateTime(date));
        latestProgressInfo.put("openId", openId);
    }

    /**
     * 根据状态值赋值不同的状态
     *
     * @param status
     * @return
     */
    private String assignVal(int status) {
        String str = "";
        if (status == 1) {
            str = STATE_WAITING;
        } else if (status == 2) {
            str = STATE_DINNING;
        } else if (status == 3) {
            str = STATE_PASSED;
        } else if (status == 4) {
            str = STATE_CANCELED;
        } else if (status == 6) {
            str = STATE_SENT;
        } else
            str = STATE_NOT_SENT;
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
    public Map<String, String> getWaitTokenInfo(Long tableTypeId) {
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
    public Map<String, String> getShopInfo(String mainBoardCode) {
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

    public String getIdentityCode(String sceneIdToIdentityCode) {
        IgniteCache<String, String> scenIdIdentityCodeCache = CacheUtil.qrParametersIdentityCode(ignite);
        String identityCode = scenIdIdentityCodeCache.get(sceneIdToIdentityCode);
        return identityCode;
    }

}
