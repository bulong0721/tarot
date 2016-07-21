package com.myee.tarot.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.myee.djinn.dto.*;
import com.myee.djinn.endpoint.OrchidService;
//import com.myee.djinn.rpc.bootstrap.ServerBootstrap;
import com.myee.tarot.catering.dao.TableTypeDao;
import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.merchant.dao.MerchantStoreDao;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.weixin.dao.WFeedBackDao;
import com.myee.tarot.weixin.dao.WxWaitTokenDao;
import com.myee.tarot.weixin.domain.WxWaitToken;
import com.myee.tarot.weixin.domain.WFeedBack;
import com.myee.tarot.weixin.domain.WxWaitToken;
import com.myee.tarot.weixin.service.WeixinService;
import com.myee.tarot.weixin.util.TimeUtil;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Martin on 2016/1/19.
 */
@Service
public class WeixinManager extends RedisOperation implements WeixinService {
    private static final Logger logger = LoggerFactory.getLogger(WeixinManager.class);

    @Autowired
    private TableTypeDao tableTypeDao;
    @Autowired
    private MerchantStoreDao merchantStoreDao;
    @Autowired
    private WxWaitTokenDao waitTokenDao;
    @Autowired
    private WFeedBackDao wFeedBackDao;
//    @Autowired
//    private ServerBootstrap serverBootstrap;

    static Integer iCount = 0;

    @Autowired
    public WeixinManager(RedisTemplate redisTemplate) {
        super(redisTemplate);
    }

    /**
     * 查看品牌下的店铺
     * @param clientId
     * @param orgId
     * @return
     */
    @Override
    public Collection<ShopDetail> allStoreOfClient(Long clientId, Long orgId) {
        String redisKey = RedisKeys.shopOfClient(clientId);
        Map<String, ShopDetail> storeMap = hgetall(redisKey, ShopDetail.class);
        if (null == storeMap || storeMap.isEmpty()) {
            List<MerchantStore> storeList = merchantStoreDao.findByClientId(clientId);
            storeMap = Maps.newHashMap();
            for (MerchantStore store : storeList) {
                List<TableType> typeList = tableTypeDao.findTypeByOrgId(store.getMerchant().getId());
                ShopDetail shopDetail = toDto(store);
                shopDetail.setWaitList(toWaitList(typeList));
                storeMap.put(String.valueOf(store.getId()), shopDetail);
            }
            hsetall(redisKey, storeMap);
        }
        return storeMap.values();
    }

    private List<TableWait> toWaitList(List<TableType> typeList) {
        List<TableWait> waitList = Lists.transform(typeList, new Function<TableType, TableWait>() {
            @Override
            public TableWait apply(TableType tableType) {
                TableWait wait = new TableWait();
                wait.setMaxDiner(tableType.getCapacity());
                wait.setMinDiner(tableType.getMinimum());
                wait.setTableType(tableType.getName());
                wait.setTableTypeId(tableType.getId());
                return wait;
            }
        });
        return waitList;
    }

    @Override
    public ShopDetail storeOfClient(long clientId, long storeId) {
        String redisKey = RedisKeys.shopOfClient(clientId);
        String fieldKey = String.valueOf(storeId);
        ShopDetail shopDetail = hget(redisKey, fieldKey, ShopDetail.class);
        if (null == shopDetail) {
            MerchantStore store = merchantStoreDao.findOne(storeId);
            if (null != store) {
                shopDetail = toDto(store);
                List<TableType> typeList = tableTypeDao.findTypeByOrgId(store.getMerchant().getId());
                shopDetail.setWaitList(toWaitList(typeList));
                hset(redisKey, fieldKey, shopDetail, null);
            }
        }
        return shopDetail;
    }

    /**
     * 根据经纬度地理位置搜附近的餐厅
     * @return
     */
    public Collection<ShopDetail> allOrgByGeo(Map map) {
        //通过map里的经纬度查询附近的餐厅
        return null;
    }

    /**
     * 根据选择的地点搜餐厅
     * @param map
     * @return
     */
    public Collection<ShopDetail> allOrgByLocation(Map map) {
        if(map.get("city") != null && map.get("district") != null) {

        } else {
            //让其选择具体的城市
            return null;
        }
        return null;
    }

    @Override
    public WaitToken waitOfTableType(long tableTypeId, String token,Long shopId) {
        String redisKey = RedisKeys.waitOfTableType(shopId, tableTypeId);
        return hget(redisKey, token, WaitToken.class);
    }

    @Override
    public Collection<WaitToken> allWaitOfTableType(long tableTypeId, Long shopId) {
        String redisKey = RedisKeys.waitOfTableType(shopId, tableTypeId);
        Map<String, WaitToken> waitTokenMap = hgetall(redisKey, WaitToken.class);
        return waitTokenMap.values();
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

    @Override
    public WxWaitToken enqueue(long shopId, int dinerCount, String openId) {

        //测试代码  OrchidService eptService = serverBootstrap.getClient(OrchidService.class, toClientUUID(shopId));
        OrchidService eptService = null;
        Diner diner = new Diner();
        try {
            ResponseData responseData = eptService.take(dinerCount, diner);
            WaitToken waitToken = JSON.parseObject(responseData.getData().toString(), WaitToken.class);
            waitToken.setOpenId(openId);
            waitToken.setWaitStatus(WaitTokenState.WAITING.getValue());
            Date date = new Date();
            waitToken.setTimeTook(date.getTime()/1000);
            //保存某品牌餐厅下某分店的对应餐桌类型ID的排号
            String redisKey = RedisKeys.waitOfTableType(shopId, waitToken.getTableTypeId());
            String redisKeyToOpenIdRef = RedisKeys.openIdToTableType(shopId, openId);
            WxWaitToken rwToken = convertTo(waitToken, date.getTime() / 1000);
            waitTokenDao.update(rwToken);
            Map<String,Date> map = TimeUtil.getAfterDate(date);
            hsetSimple(redisKeyToOpenIdRef, redisKey, map.get("eTime"));
            //将identityCode和查询餐桌类型的排号放入Redis
            hsetSimple(waitToken.getIdentityCode(), redisKey, map.get("eTime"));
            hset(redisKey.trim(), waitToken.getToken(), waitToken, map.get("eTime"));
            //将主键ID和查询餐桌类型的排号放入Redis
            hsetSimple(rwToken.getId().toString(), redisKey, map.get("eTime"));
            hset(redisKey.trim(), rwToken.getId().toString(), waitToken, map.get("eTime"));
            //去查询进展
            Map<String,Object> msgMap = selectLatestDevelopmentsByIc(waitToken.getIdentityCode());
            int i = waitTokenDao.modifyWaitingInfo(Long.parseLong(msgMap.get("waitedTableCount").toString()), waitToken.getIdentityCode(), Long.valueOf(msgMap.get("timeTook").toString()), Long.parseLong(msgMap.get("predictWaitingTime").toString()));
            if(i !=1) {
                logger.error("修改等位状态失败!");
            }
            return rwToken;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    static WxWaitToken convertTo(WaitToken waitToken, Long date) {
        WxWaitToken entity = new WxWaitToken();
        entity.setTableId(waitToken.getTableId());
        entity.setTableTypeId(waitToken.getTableTypeId());
        entity.setComment(waitToken.getComment());
        entity.setChannelType("weixin");
        entity.setDinerId(100L);
        entity.setIdentityCode(waitToken.getIdentityCode());
        entity.setOpenId(waitToken.getOpenId());
        entity.setToken(waitToken.getToken());
        entity.setClientID(waitToken.getClientId());
        entity.setOrgID(waitToken.getShopId());
        entity.setDinnerCount(waitToken.getDinnerCount());
        Date dt = new Date(date * 1000);
        entity.setTimeTook(dt);
        entity.setState(waitToken.getWaitStatus());
        return entity;
    }

    @Override
    public void dequeue(long tableTypeId, String token, Long clientId, Long shopId) {
        String redisKey = RedisKeys.waitOfTableType(shopId, tableTypeId);
        hdelete(redisKey, token);
    }

    @Override
    public DrawToken drawOfStore(long storeId, String token) {
        String redisKey = RedisKeys.drawOfStore(storeId);
        return hget(redisKey, token, DrawToken.class);
    }

    /**
     * 根据openId和clientID和orgId查询状态
     *
     * @param openId
     * @param orgID
     * @return
     */
    @Override
    public Map<String,Object> selectTokensByInfo(String openId, Long orgID, Long tableTypeId) {
        String redisKeyOpenIdRef = RedisKeys.openIdToTableType(orgID, openId);
        String redisKey2Find = (String) getSimple(redisKeyOpenIdRef);
        Map<String, WaitToken> waitTokenMap = hgetall(redisKey2Find, WaitToken.class);
        Set<Integer> waitNumSet = new HashSet<Integer>();
        Map<String, Object> latestDevInfo = new HashMap<String, Object>();
        String infoStr = "";
        //顾客取号的号数
        Integer userNum = null;
        //之前等待桌数
        Integer beforeCount = 0;
        //等待时长
        Integer waitMinutes = 0;
        String tokenNum = null;
        String shopName = null;
        String queueStatus = null;
        for (WaitToken waitToken : waitTokenMap.values()) {
            waitNumSet.add(Integer.parseInt(waitToken.getToken().substring(1, 3)));
            if (openId.equals(waitToken.getOpenId())) {
                tokenNum = waitToken.getToken();
                String shopKey = RedisKeys.shopOfClient(waitToken.getClientId());
//                shopName = getSimple(shopKey).toString();
                userNum = Integer.parseInt(waitToken.getToken().substring(1, 3));
                waitMinutes = waitToken.getWaitMinutes();
                if(waitToken.getWaitStatus() == 1) {
                    queueStatus = "排队中";
                }
                if(waitToken.getWaitStatus() == 2) {
                    queueStatus = "就餐中";
                }
                if(waitToken.getWaitStatus() == 3) {
                    queueStatus = "已过号";
                }
                if(waitToken.getWaitStatus() == 4) {
                    queueStatus = "已取消";
                }
                if(waitToken.getWaitStatus() == 6) {
                    queueStatus = "已发送";
                }
                if(waitToken.getWaitStatus() == 7) {
                    queueStatus = "未发送";
                }
            }
        }
        for (Integer i : waitNumSet) {
            if (i < userNum) {
                beforeCount++;
            }
        }
        latestDevInfo.put("shopName",shopName);
        latestDevInfo.put("tokenNum",tokenNum);
        latestDevInfo.put("waitedTable",beforeCount.toString() + "桌");
        latestDevInfo.put("predictTime",String.valueOf(beforeCount * 30) + "分钟");
        latestDevInfo.put("queueStatus",queueStatus);
        latestDevInfo.put("queryTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return latestDevInfo;
    }

    /**
     * 拿identityCode去数据库里查找
     *
     * @param identityCode
     * @return
     */
    @Override
    public Map<String,Object> selectLatestDevelopmentsByIc(String identityCode) {
        //先到Redis里去找，通过identityCode获取当时保存token的redisKey
        String redisKey = getSimple(identityCode).toString();
        //店名
        String shopName = null;
        //排号
        String tokenNum = null;
        Map<String,Object> infoStr = new HashMap<String,Object>();
        //前面还有多少桌
        Integer index = 0;
        String queueStatus = null;
        Long timeTook = null;
        if (redisKey != null) {
            Map<String, WaitToken> myMap = hgetall(redisKey,WaitToken.class);
            if(myMap != null) {
                List<WaitToken> sortedTokens = orderingByTook.sortedCopy(myMap.values());
                for (WaitToken wt : sortedTokens) {
                    if (!wt.getIdentityCode().equals(identityCode)) {
                        index++;
                    } else {
                        String shopKey = RedisKeys.shopOfClient(wt.getClientId());
//                        shopName = getSimple(shopKey).toString();
                        tokenNum = wt.getToken();
                        timeTook = wt.getTimeTook();
                        if(wt.getWaitStatus() == 1) {
                            queueStatus = "排队中";
                        }
                        if(wt.getWaitStatus() == 2) {
                            queueStatus = "就餐中";
                        }
                        if(wt.getWaitStatus() == 3) {
                            queueStatus = "已过号";
                        }
                        if(wt.getWaitStatus() == 4) {
                            queueStatus = "已取消";
                        }
                        if(wt.getWaitStatus() == 6) {
                            queueStatus = "已发送";
                        }
                        if(wt.getWaitStatus() == 7) {
                            queueStatus = "未发送";
                        }
                        break;
                    }
                }
            }
        } else {
            Map<String,Date> map = TimeUtil.getAfterDate(new Date());
            Long bTimeLong = map.get("bTime").getTime()/1000;
            Long eTimeLong = map.get("eTime").getTime()/1000;
            //再到MYSQL去找,用identityCode找到对应的clientId,orgId,和token，
            WxWaitToken wtoken = waitTokenDao.selectTokenByIc(identityCode,bTimeLong,eTimeLong);
            if (wtoken != null) {
                //根据clientId和orgId和tableId，找到该餐馆的某餐桌类型等待的token
                List<WxWaitToken> tokenList = waitTokenDao.selectAllTokenByInfo(wtoken.getClientID(), wtoken.getOrgID(), wtoken.getTableId(), WaitTokenState.WAITING.getValue());
                List<WxWaitToken> sortedTokens = orderingByTook2.sortedCopy(tokenList);
                for (WxWaitToken wt : sortedTokens) {
                    if (!wt.getIdentityCode().equals(identityCode)) {
                        index++;
                    } else {
                        if(wt.getState() == 1) {
                            queueStatus = "排队中";
                        }
                        if(wt.getState() == 2) {
                            queueStatus = "就餐中";
                        }
                        if(wt.getState() == 3) {
                            queueStatus = "已过号";
                        }
                        if(wt.getState() == 4) {
                            queueStatus = "已取消";
                        }
                        if(wt.getState() == 6) {
                            queueStatus = "已发送";
                        }
                        if(wt.getState() == 7) {
                            queueStatus = "未发送";
                        }
                        timeTook = wt.getTimeTook().getTime();
                        break;
                    }
                }
            } else {
                infoStr.put("shopName",shopName);
                infoStr.put("tokenNum",tokenNum);
                infoStr.put("waitedTable",index.toString()+ "桌");
                infoStr.put("waitedTableCount",index.longValue());
                infoStr.put("predictTime",String.valueOf(index * 10) + "分钟");
                infoStr.put("predictWaitingTime",Long.valueOf(index * 10));
                infoStr.put("queueStatus",queueStatus);
                infoStr.put("timeTook",timeTook);
                infoStr.put("queryTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                return infoStr;
            }
        }
        infoStr.put("shopName",shopName);
        infoStr.put("tokenNum",tokenNum);
        infoStr.put("waitedTable",index.toString()+ "桌");
        infoStr.put("waitedTableCount",index.longValue());
        infoStr.put("predictTime",String.valueOf(index * 10) + "分钟");
        infoStr.put("predictWaitingTime",Long.valueOf(index * 10));
        infoStr.put("queueStatus",queueStatus);
        infoStr.put("timeTook",timeTook);
        infoStr.put("queryTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return infoStr;
    }

    @Override
    public List<WxWaitToken> selectTokensByOpenIdState(String openId, Integer state) {
        List<WxWaitToken> waitTList = new ArrayList<WxWaitToken>();
        waitTList = waitTokenDao.selectAllTokenByOpenIdState(openId, state);
        return waitTList;
    }

    final static Charset charset = Charset.forName("UTF8");

    static String toClientUUID(long shopId) {
        String rawId = String.format("shopId:%08d", shopId);
        return Base64.encodeBase64String(rawId.getBytes(charset));
    }

    /**
     * java生成随机数字和字母组合
     *
     * @param length [生成随机数的长度]
     * @return
     */
    public static String getCharAndNumr(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 查询Redis里保存的某餐厅的排号List
     *
     * @param flag
     * @param iCode
     * @param idtCodeList
     * @return
     */
    public static boolean checkIfHave(boolean flag, String iCode, List<String> idtCodeList) {
        for (String code : idtCodeList) {
            if (code.equals(iCode)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 绑定二维码
     *
     * @param identityCode
     */
    @Override
    public boolean bondQrCodeByScan(String identityCode, String openId) {
        //先到Redis里去找，通过identityCode获取当时保存token的redisKey
        String redisKey = getSimple(identityCode).toString();
        if (redisKey != null) {
            Map<String, WaitToken> myMap = hgetall(redisKey, WaitToken.class);
            //按WaitToken来取
            for (String key : myMap.keySet()) {
                if (myMap.get(key).getIdentityCode().equals(identityCode)) {
                    WaitToken newWt = myMap.get(key);
                    hdelete(redisKey, myMap.get(key).getToken());
                    newWt.setOpenId(openId);
                    Map<String,Date> map1 = TimeUtil.getAfterDate(new Date(myMap.get(key).getTimeTook() * 1000));
                    hset(redisKey, newWt.getToken(), newWt, map1.get("eTime"));
                    Long timeTookLong = myMap.get(key).getTimeTook();
                    waitTokenDao.updateWaitTokenOpenId(openId, myMap.get(key).getIdentityCode(), timeTookLong);
                    String redisKeyOpenIdRef = RedisKeys.openIdToTableType(myMap.get(key).getShopId(), openId);
                    hsetSimple(redisKeyOpenIdRef, redisKey, map1.get("eTime"));
                    break;
                }
            }
            return true;
        } else { //如果Redis里找不到，就到MySql里通过当前时间的范围，按identityCode搜索
            Map<String,Date> map = TimeUtil.getAfterDate(new Date());
            Long bTimeLong = map.get("bTime").getTime()/1000;
            Long eTimeLong = map.get("eTime").getTime()/1000;
            WxWaitToken rw  = waitTokenDao.selectTokenByIc(identityCode, bTimeLong, eTimeLong);
            if(rw != null) {
                rw.setOpenId(openId);
                Long timeTookLong = rw.getTimeTook().getTime() / 1000;
                waitTokenDao.updateWaitTokenOpenId(rw.getOpenId(), rw.getIdentityCode(), timeTookLong);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public List<WxWaitToken> selectWaitList(String openId, Map<String,Date> map) {
        List<WxWaitToken> list = waitTokenDao.selectWait(openId, map.get("bTime").getTime() / 1000, map.get("eTime").getTime() / 1000);
        return list;
    }

    @Override
    public int modifyWaitingInfo(Long waitedCount, String identityCode, Long date, Long predictWaitingTime) {
        int i = waitTokenDao.modifyWaitingInfo(waitedCount, identityCode, date, predictWaitingTime);
        return i;
    }

    @Override
    public String hGetWaitTokenRedisKey(Long waitTokenId) {
        String key = getSimple(waitTokenId.toString()).toString();
        return key;
    }

    @Override
    public void hDelWaitTokenRedisRecord( Long waitTokenId) {
        hdeleteSimple(waitTokenId.toString());
    }

    @Override
    public int modifyWaitingStatus(int statusValue, long waitQueueId) {
        int i = waitTokenDao.modifyWaitingStatus(statusValue,waitQueueId);
        return i;
    }

    /**
     * 根据商店的经纬
     * @param storeLongitude
     * @param storeLatitude
     * @param longitude
     * @param latitude
     * @return
     */
    @Override
    public Double calcDistance(Double storeLongitude, Double storeLatitude, Double longitude, Double latitude) {
        Double distance = LantitudeLongitudeDist(storeLongitude, storeLatitude, longitude, latitude);
        return distance;
    }

    /**
     * 发送意见
     * @param context
     * @return
     */
    @Override
    public void sendFeedback(String context, String openId) {
        WFeedBack fb = new WFeedBack();
        fb.setFeedBackOpenId(openId);
        fb.setFeedBackContext(context);
        wFeedBackDao.update(fb);
    }

    private static final  double EARTH_RADIUS = 6378137;//赤道半径(单位m)

    /**
     * 转化为弧度(rad)
     * */
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /**
     * 基于余弦定理求两经纬度距离
     * @param lon1 第一点的精度
     * @param lat1 第一点的纬度
     * @param lon2 第二点的精度
     * @param lat2 第二点的纬度
     * @return 返回的距离，单位km
     * */
    public static double LantitudeLongitudeDist(double lon1, double lat1,double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);

        double radLon1 = rad(lon1);
        double radLon2 = rad(lon2);

        if (radLat1 < 0)
            radLat1 = Math.PI / 2 + Math.abs(radLat1);// south
        if (radLat1 > 0)
            radLat1 = Math.PI / 2 - Math.abs(radLat1);// north
        if (radLon1 < 0)
            radLon1 = Math.PI * 2 - Math.abs(radLon1);// west
        if (radLat2 < 0)
            radLat2 = Math.PI / 2 + Math.abs(radLat2);// south
        if (radLat2 > 0)
            radLat2 = Math.PI / 2 - Math.abs(radLat2);// north
        if (radLon2 < 0)
            radLon2 = Math.PI * 2 - Math.abs(radLon2);// west
        double x1 = EARTH_RADIUS * Math.cos(radLon1) * Math.sin(radLat1);
        double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1);
        double z1 = EARTH_RADIUS * Math.cos(radLat1);

        double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2);
        double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2);
        double z2 = EARTH_RADIUS * Math.cos(radLat2);

        double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)+ (z1 - z2) * (z1 - z2));
        //余弦定理求夹角
        double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS));
        double dist = theta * EARTH_RADIUS;
        return dist;
    }

    public ShopDetail toDto(MerchantStore merchantStore) {
        ShopDetail dto = new ShopDetail();
        dto.setClientId(merchantStore.getMerchant().getId());
        dto.setClientName(merchantStore.getMerchant().getName());
        dto.setShopId(merchantStore.getId());
        dto.setShopName(merchantStore.getName());
        dto.setScore(merchantStore.getScore());
        dto.setShopType(merchantStore.getStoreType());
        dto.setAddress(merchantStore.getAddress().getAddress());
//        dto.setLatitude(latitude);
//        dto.setLongitude(longitude);
        dto.setOpeningTime(toHourString(merchantStore.getTimeOpen()) + "~" + toHourString(merchantStore.getTimeClose()));
        return dto;
    }


    String toHourString(Date time) {
        return new DateTime(time.getTime()).toString("HH:mm");
    }
}
