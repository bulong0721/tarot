package com.myee.tarot.web.campaign.controller;

import com.google.common.collect.Lists;
import com.myee.tarot.apiold.eum.TemplateSMSType;
import com.myee.tarot.apiold.service.SendRecordService;
import com.myee.tarot.campaign.service.ClientPrizeGetInfoService;
import com.myee.tarot.campaign.service.ClientPrizeService;
import com.myee.tarot.campaign.service.impl.redis.DateTimeUtils;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.clientprize.domain.ClientPrize;
import com.myee.tarot.clientprize.domain.ClientPrizeGetInfo;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.AutoNumUtil;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ValidatorUtil;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.web.apiold.controller.BaseController;
import com.myee.tarot.web.apiold.util.AlidayuSmsClient;
import com.myee.tarot.web.apiold.util.IPUtils;
import com.myee.tarot.weixin.domain.ClientAjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2016/8/29.
 */
@RestController
public class ClientPrizeController extends BaseController{

    @Autowired
    private ClientPrizeService clientPrizeService;
    @Autowired
    private ClientPrizeGetInfoService clientPrizeGetInfoService;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private SendRecordService sendRecordManageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientPrizeController.class);

    /**
     * 新增 修改
     *
     * @param clientPrize
     * @param request
     * @return
     */
    @RequestMapping(value = {"admin/saveClientPrize", "shop/saveClientPrize"}, method = RequestMethod.POST)
    public AjaxResponse saveClientPrize(@RequestBody ClientPrize clientPrize, HttpServletRequest request) {
        AjaxResponse resp = new AjaxResponse();
        try {
            String sessionName = ValidatorUtil.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION).toString();
            //从session中读取merchantStore信息，如果为空，则提示用户先切换门店
            if (request.getSession().getAttribute(sessionName) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            //在此判断 设置时间是否合理
            Date startDate = clientPrize.getStartDate();
            Date endDate = clientPrize.getEndDate();
            Date startToday = DateTimeUtils.startToday();
            if(startDate.compareTo(startToday) < 0){   //开始时间小于当天开始时间
                resp.setErrorString("有效期开始日期不得小于当天日期");
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                return resp;
            }else {
                if(endDate.compareTo(startDate) < 0){  //有效期结束时间不能小于开始时间
                    resp.setErrorString("有效期结束时间不能小于开始时间");
                    resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                    return resp;
                }
            }
            //添加 修改
            MerchantStore merchantStore = (MerchantStore) request.getSession().getAttribute(sessionName);
            int total = clientPrize.getTotal();
            //设置剩余数量  防止重置超奖池
            if(clientPrize.getId()!= null){
                int remainInPool = clientPrizeGetInfoService.countUnGetByPrizeId(clientPrize.getId());
                total = total - remainInPool;
            }
            clientPrize.setLeftNum(total);
            clientPrize.setStore(merchantStore);
            if (clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_SCANCODE) {
                clientPrize.setLeftNum(null);
                clientPrize.setTotal(null);
                clientPrize.setPhonePrizeType(null);
            } else if (clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_THANKYOU) {
                clientPrize.setPhonePrizeType(null);
                clientPrize.setName("谢谢惠顾");
            }
            ClientPrize updatePrize = clientPrizeService.update(clientPrize);
            resp.addEntry("updateResult", objectToEntry(updatePrize));
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
    }

    /**
     * 分页查询
     *
     * @param request
     * @param pageRequest
     * @return
     */
    @RequestMapping(value = {"admin/clientPrize/pagingList", "shop/clientPrize/pagingList"}, method = RequestMethod.GET)
    public AjaxPageableResponse pageListOfPrize(HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            String sessionName = ValidatorUtil.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION).toString();
            //从session中读取merchantStore信息，如果为空，则提示用户先切换门店
            if (request.getSession().getAttribute(sessionName) == null) {
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore store = (MerchantStore) request.getSession().getAttribute(sessionName);
            PageResult<ClientPrize> pageClientPrizes = clientPrizeService.pageList(pageRequest, store.getId());
            List<ClientPrize> clientPrizeList = pageClientPrizes.getList();
            for (ClientPrize clientPrize : clientPrizeList) {
                resp.addDataEntry(objectToEntry(clientPrize));
            }
            resp.setRecordsTotal(pageClientPrizes.getRecordsTotal());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    /**
     * 删除
     *
     * @param clientPrize
     * @param request
     * @return
     */
    @RequestMapping(value = {"admin/deleteClientPrize", "shop/deleteClientPrize"}, method = RequestMethod.POST)
    public AjaxResponse deleteClientPrize(@RequestBody ClientPrize clientPrize, HttpServletRequest request) {
        AjaxResponse resp = new AjaxResponse();
        try {
            String sessionName = ValidatorUtil.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION).toString();
            //从session中读取merchantStore信息，如果为空，则提示用户先切换门店
            if (request.getSession().getAttribute(sessionName) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            ClientPrize existPrize = clientPrizeService.findById(clientPrize.getId());
            if (existPrize != null) {
                existPrize.setDeleteStatus(Constants.CLIENT_PRIZE_DELETE_NO);
                clientPrizeService.update(existPrize);
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
    }

    /**
     * 获取小超人 兑奖页面的历史兑奖记录
     *
     * @param request
     * @param pageRequest
     * @return
     */
    @RequestMapping(value = {"admin/clientPrizeInfo/pagingListOfChecked", "shop/clientPrizeInfo/pagingListOfChecked"}, method = RequestMethod.GET)
    public AjaxPageableResponse pageListOfPrizeInfoChecked(HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            String sessionName = ValidatorUtil.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION).toString();
            //从session中读取merchantStore信息，如果为空，则提示用户先切换门店
            if (request.getSession().getAttribute(sessionName) == null) {
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore store = (MerchantStore) request.getSession().getAttribute(sessionName);
            PageResult<ClientPrizeGetInfo> pageClientGetPrizes = clientPrizeGetInfoService.pageListOfChecked(pageRequest, store.getId());
            List<ClientPrizeGetInfo> clientPrizeGetList = pageClientGetPrizes.getList();
            for (ClientPrizeGetInfo clientPrizeGetInfo : clientPrizeGetList) {
                resp.addDataEntry(objectToEntryForInfo(clientPrizeGetInfo));
            }
            resp.setRecordsTotal(pageClientGetPrizes.getRecordsTotal());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("出错");
        }
        return resp;
    }

    /**
     * 检验验证码 兑奖
     * @param checkCode
     * @param request
     * @return
     */
    @RequestMapping(value = {"admin/clientPrizeInfo/checkClientPriceInfo", "shop/clientPrizeInfo/checkClientPriceInfo"},method = RequestMethod.POST)
    public AjaxResponse checkClientPriceInfo(@RequestParam("checkCode")String checkCode,HttpServletRequest request){
        AjaxResponse resp = new AjaxResponse();
        try {
            String sessionName = ValidatorUtil.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION).toString();
            //从session中读取merchantStore信息，如果为空，则提示用户先切换门店
            if (request.getSession().getAttribute(sessionName) == null) {
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore store = (MerchantStore) request.getSession().getAttribute(sessionName);
            ClientPrizeGetInfo clientPrizeGetInfo = clientPrizeGetInfoService.checkClientPriceInfo(store.getId(), checkCode.toUpperCase());
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
            if(clientPrizeGetInfo!= null){
                int status = clientPrizeGetInfo.getStatus();
                if(status == Constants.CLIENT_PRIZEINFO_STATUS_EXPIRED){
                    resp.setErrorString("该优惠券已过期");
                }else if(status==Constants.CLIENT_PRIZEINFO_STATUS_USED){
                    resp.setErrorString("该优惠码已被使用");
                }else{
                    //再次判断是否过期或是否未在使用期内
                    Date startDate = clientPrizeGetInfo.getPrizeStartDate();
                    Date endDate = clientPrizeGetInfo.getPrizeEndDate();
                    Date nowDate = DateTimeUtils.startToday();
                    if(endDate.compareTo(nowDate) < 0){
                        clientPrizeGetInfo.setStatus(Constants.CLIENT_PRIZEINFO_STATUS_EXPIRED);
                        resp.setErrorString("该优惠码已过期");
                    }else if(startDate.compareTo(nowDate) > 0 ){
                        resp.setErrorString("该优惠码不在兑换时间内");
                    }else {
                        //验证通过后
                        resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
                        resp.addDataEntry(objectToEntryForInfo(clientPrizeGetInfo));
                        clientPrizeGetInfo.setStatus(Constants.CLIENT_PRIZEINFO_STATUS_USED);
                        clientPrizeGetInfo.setCheckDate(new Date());
                    }
                }
                clientPrizeGetInfoService.update(clientPrizeGetInfo);
                return resp;
            }else {
                resp.setErrorString("此优惠码该店无效");
                return resp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);

    }

    private Map objectToEntry(ClientPrize clientPrize) {
        Map entry = new HashMap();
        entry.put("id", clientPrize.getId());
        entry.put("name", clientPrize.getName());
        entry.put("type", clientPrize.getType());
        entry.put("total", clientPrize.getTotal());
        entry.put("leftNum", clientPrize.getLeftNum());
        entry.put("startDate", clientPrize.getStartDate());
        entry.put("endDate", clientPrize.getEndDate());
        entry.put("description", clientPrize.getDescription());
        entry.put("bigPic", clientPrize.getBigPic());
        entry.put("smallPic", clientPrize.getSmallPic());
        entry.put("activeStatus", clientPrize.getActiveStatus());
        entry.put("phonePrizeType", clientPrize.getPhonePrizeType());
        return entry;
    }

    private Map objectToEntryForInfo(ClientPrizeGetInfo clientPrizeGetInfo) {
        Map entry = new HashMap();
        entry.put("id", clientPrizeGetInfo.getId());
        entry.put("phoneNum", clientPrizeGetInfo.getPhoneNum());
        entry.put("deskId",clientPrizeGetInfo.getDeskId());
        entry.put("checkCode",clientPrizeGetInfo.getCheckCode());
        entry.put("checkDate",clientPrizeGetInfo.getCheckDate());
        entry.put("prizeName",clientPrizeGetInfo.getPrizeName());
        entry.put("prizeDescription",clientPrizeGetInfo.getPrizeDescription());
        return entry;
    }

    //为 移动端提供接口

    /**
     * 接口一：奖品列表
     *
     * @param orgId 商户ID
     * @return
     */
    @RequestMapping(value = "services/public/listOfClientPrize", method = RequestMethod.POST)
    public ClientAjaxResult listOfClientPrize(@RequestParam("orgId") Long orgId) {
        LOGGER.info("开始查询奖品列表");
        try {
            //获取激活的奖券
            List<ClientPrize> activeClientPrize = clientPrizeService.listActive(orgId);
            if(activeClientPrize != null && activeClientPrize.size() != 0){
                int size = activeClientPrize.size();
                //若没满8个，需用谢谢惠顾填满
                ClientPrize thankYouPrize = clientPrizeService.getThankYouPrize(orgId);
                if(thankYouPrize == null){
                    return ClientAjaxResult.failed("请至少设置一个谢谢惠顾类型的奖券");
                }
                if (size <= 8) {
                    for (int i = 0; i < 8 - size; i++) {
                        ClientPrize addPrize = new ClientPrize();
                        addPrize.setId(-1L);
                        addPrize.setType(thankYouPrize.getType());
                        addPrize.setBigPic(thankYouPrize.getBigPic());
                        addPrize.setSmallPic(thankYouPrize.getSmallPic());
                        addPrize.setDescription(thankYouPrize.getDescription());
                        addPrize.setName(thankYouPrize.getName());
                        activeClientPrize.add(addPrize);
                    }
                } else {
                    return ClientAjaxResult.failed("设置激活的奖券不能超过8个");
                }
                //去除简化字段
                List<Object> resultObject = Lists.newArrayList();
                for (ClientPrize clientPrize : activeClientPrize) {
                    resultObject.add(objectToClientEntry(clientPrize));
                }
                return ClientAjaxResult.success(resultObject);
            } else {
                return ClientAjaxResult.failed("该商户未设置有效奖券");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }

    }

    /**
     * 接口二 获取随机的奖券
     *
     * @param shopId
     * @param deskId
     * @return
     */
    @RequestMapping(value = "services/public/getRandomClientPrizeInfo", method = RequestMethod.POST)
    public ClientAjaxResult getRandomClientPrizeInfo(@RequestParam("orgId") Long shopId,
                                                     @RequestParam("deskId") String deskId) {
        try {
            List<ClientPrize> activeClientPrize = clientPrizeService.listActive(shopId);
            if(activeClientPrize != null && activeClientPrize.size() != 0 ){
                int totalLeft = 0;
                int leftPhoneTotal = 0;
                //先获取剩余手机奖券的
                for (ClientPrize clientPrize : activeClientPrize) {
                    if (clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_PHONE) {
                        int leftNum = clientPrize.getLeftNum().intValue();
                        leftPhoneTotal += leftNum;
                        totalLeft += leftNum;
                    } else if (clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_THANKYOU) {
                        int leftNum = clientPrize.getLeftNum().intValue();
                        totalLeft += leftNum;
                    }
                }
                //随机生成二维码计算数量
                Random random = new Random();
                for (ClientPrize clientPrize : activeClientPrize) {
                    if (clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_SCANCODE) {
                        int randomLeft = random.nextInt(leftPhoneTotal) + 1;
                        clientPrize.setLeftNumCache(randomLeft);
                        totalLeft += randomLeft;
                    }
                }
                //从总量随机抽取一个
                int randomPrizeNum = random.nextInt(totalLeft) + 1;
                int compareNum = 0;
                for (ClientPrize clientPrize : activeClientPrize) {
                    if(clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_SCANCODE){
                        compareNum += clientPrize.getLeftNumCache();
                    }else{
                        compareNum += clientPrize.getLeftNum();
                    }
                    if (randomPrizeNum <= compareNum) {
                        //获取到抽到的奖项
                        if (clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_PHONE) {
                            //添加中奖纪录 并去除一张奖券
                            clientPrize.setLeftNum(clientPrize.getLeftNum() - 1);
                            ClientPrizeGetInfo getInfo = new ClientPrizeGetInfo();
                            getInfo.setDeskId(deskId);
                            getInfo.setPrice(clientPrize);
                            getInfo.setGetDate(new Date());
                            getInfo.setStatus(Constants.CLIENT_PRIZEINFO_STATUS_UNGET);
                            ClientPrizeGetInfo clientPrizeGetInfo = clientPrizeGetInfoService.update(getInfo);
                            clientPrize.setPriceGetId(clientPrizeGetInfo.getId());
                        } else if (clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_SCANCODE) {
                            //无需消奖
                            clientPrize.setPriceGetId(-1L);
                        } else if (clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_THANKYOU) {
                            clientPrize.setLeftNum(clientPrize.getLeftNum() - 1);
                            clientPrizeService.update(clientPrize);
                            clientPrize.setPriceGetId(-1L);
                        }
                        return ClientAjaxResult.success(objectToClientEntry(clientPrize));
                    }
                }
            }else {
                return ClientAjaxResult.failed("该店奖券已停止");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
        return ClientAjaxResult.failed("已无奖券");
    }

    /**
     * 接口三：确认领奖，同时判断手机是否超次
     * @param phoneNum 手机号码
     * @param deskId  桌号
     * @param prizeGetId 获奖信息奖券id
     * @return
     */
    @RequestMapping(value = "services/public/confirmPriceInfo",method = RequestMethod.POST)
    public ClientAjaxResult confirmPriceInfo(@RequestParam("phoneNum")Long phoneNum,
                                             @RequestParam("deskId")String deskId,
                                             @RequestParam("prizeGetId")Long prizeGetId){
        try {
            //手机号当天不超过3次判定
            boolean isOverThreeTimes = clientPrizeGetInfoService.isOverThreeTimes(phoneNum);
            if(isOverThreeTimes){
                return ClientAjaxResult.failed("本号码今天已超过3次领奖记录");
            }
            ClientPrizeGetInfo priceGetInfo = clientPrizeGetInfoService.findByIdAndDeskId(prizeGetId, deskId);
            if(priceGetInfo!= null){
                ClientPrize prize = priceGetInfo.getPrice();
                priceGetInfo.setPhoneNum(phoneNum);
                priceGetInfo.setGetDate(new Date());
                priceGetInfo.setCheckCode(AutoNumUtil.getCode(6, 3).toUpperCase());
                //快照存储
                priceGetInfo.setPrizeName(prize.getName());
                priceGetInfo.setPrizeDescription(prize.getDescription());
                priceGetInfo.setPrizeStartDate(prize.getStartDate());
                priceGetInfo.setPrizeEndDate(prize.getEndDate());
                priceGetInfo.setPrizeType(prize.getType());
                //状态改为领取
                priceGetInfo.setStatus(Constants.CLIENT_PRIZEINFO_STATUS_GET);
                clientPrizeGetInfoService.update(priceGetInfo);
                //领取后 发送短信至用户
                String[] phones = {phoneNum.toString()};
                String content = "恭喜你获得"+ priceGetInfo.getPrizeName() + ",兑换码为" + priceGetInfo.getCheckCode() + ",兑换时间为" + DateTimeUtils.getDateString(priceGetInfo.getPrizeStartDate(),DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT) + "至" + DateTimeUtils.getDateString(priceGetInfo.getPrizeEndDate(), DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT);
                Table table = new Table();
                table.setId(Long.parseLong(deskId));
                Runnable task = AlidayuSmsClient.sendSMS(
                        IPUtils.getIpAddr(getRequest()),
                        sendRecordManageService,
                        phones,
                        content,//短信内容
                        TemplateSMSType.PARTNERGAME.getName(),
                        "木爷提示",//签名,为null默认是美味点点笔
                        getCommConfig(),//从commonApi.properties获取默认配置
                        System.currentTimeMillis()
                );//发送短信
                taskExecutor.submit(task);
                return ClientAjaxResult.success("领奖成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
        return ClientAjaxResult.failed("领奖失败");

    }

    /**
     * 接口四：主动撤销回奖池
     * @param deskId
     * @param prizeGetId
     * @return
     */
    @RequestMapping(value = "services/public/backToPrizePool",method = RequestMethod.POST)
    public ClientAjaxResult backToPrizePool(@RequestParam("deskId")String deskId,
                                            @RequestParam("prizeGetId")Long prizeGetId){
        try {
            ClientPrizeGetInfo clientPrizeGetInfo = clientPrizeGetInfoService.findByIdAndDeskId(prizeGetId, deskId);
            if(clientPrizeGetInfo!=null){
                ClientPrize prize = clientPrizeGetInfo.getPrice();
                prize.setLeftNum(prize.getLeftNum() + 1);
                clientPrizeGetInfoService.delete(clientPrizeGetInfo);
                return ClientAjaxResult.success("撤回奖池成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
        return ClientAjaxResult.failed("撤回奖池失败");
    }

    private Map objectToClientEntry(ClientPrize clientPrize) {
        Map entry = new HashMap();
        entry.put("id", clientPrize.getId());
        entry.put("name", clientPrize.getName() == null ? "" : clientPrize.getName());
        entry.put("type", clientPrize.getType());
        entry.put("description", clientPrize.getDescription() == null ? "" :clientPrize.getDescription());
        entry.put("bigPic", clientPrize.getBigPic() == null ? "": clientPrize.getBigPic());
        entry.put("smallPic", clientPrize.getSmallPic() == null ? "":clientPrize.getSmallPic());
        entry.put("prizeGetId", clientPrize.getPriceGetId());
        return entry;
    }

}
