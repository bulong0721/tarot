package com.myee.tarot.web.campaign.controller;

import com.google.common.collect.Lists;
import com.myee.tarot.apiold.eum.TemplateSMSType;
import com.myee.tarot.apiold.service.SendRecordService;
import com.myee.tarot.campaign.service.ClientPrizeGetInfoService;
import com.myee.tarot.campaign.service.ClientPrizeService;
import com.myee.tarot.campaign.service.ClientPrizeTicketService;
import com.myee.tarot.clientprize.domain.ClientPrize;
import com.myee.tarot.clientprize.domain.ClientPrizeGetInfo;
import com.myee.tarot.clientprize.domain.ClientPrizeTicket;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.*;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.web.ClientAjaxResult;
import com.myee.tarot.web.apiold.controller.BaseController;
import com.myee.tarot.web.apiold.util.AlidayuSmsClient;
import com.myee.tarot.web.apiold.util.CommonLoginParam;
import com.myee.tarot.web.apiold.util.IPUtils;
import jodd.io.FileNameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Administrator on 2016/8/29.
 */
@RestController
public class ClientPrizeController extends BaseController {

    @Autowired
    private ClientPrizeService clientPrizeService;
    @Autowired
    private ClientPrizeGetInfoService clientPrizeGetInfoService;
    @Autowired
    private ClientPrizeTicketService clientPrizeTicketService;
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
            String sessionName = CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION).toString();
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
            if (startDate.compareTo(startToday) < 0) {   //开始时间小于当天开始时间
                resp.setErrorString("有效期开始日期不得小于当天日期");
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                return resp;
            } else {
                if (endDate.compareTo(startDate) < 0) {  //有效期结束时间不能小于开始时间
                    resp.setErrorString("有效期结束时间不能小于开始时间");
                    resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                    return resp;
                }
            }
            //添加 修改
            MerchantStore merchantStore = (MerchantStore) request.getSession().getAttribute(sessionName);
            //设置剩余数量  防止重置超奖池
            Integer total = clientPrize.getTotal();
            if (clientPrize.getId() != null) {
                int remainInPool = clientPrizeGetInfoService.countUnGetByPrizeId(clientPrize.getId());
                total = total.intValue() - remainInPool;
            }
            if(clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_SCANCODE){
                clientPrize.setPhonePrizeType(null);
            } else if(clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_THANKYOU){
                clientPrize.setName("谢谢惠顾");
                clientPrize.setPhonePrizeType(null);
            }
            clientPrize.setLeftNum(total);
            clientPrize.setStore(merchantStore);
            ClientPrize updatePrize = clientPrizeService.update(clientPrize);
            resp.addEntry("updateResult", objectToEntry(updatePrize));
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
    }

    /**
     * 分页查询
     *
     * @param request
     * @param whereRequest
     * @return
     */
    @RequestMapping(value = {"admin/clientPrize/pagingList", "shop/clientPrize/pagingList"}, method = RequestMethod.GET)
    public AjaxPageableResponse pageListOfPrize(HttpServletRequest request, WhereRequest whereRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            String sessionName = CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION).toString();
            //从session中读取merchantStore信息，如果为空，则提示用户先切换门店
            if (request.getSession().getAttribute(sessionName) == null) {
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore store = (MerchantStore) request.getSession().getAttribute(sessionName);
            PageResult<ClientPrize> pageClientPrizes = clientPrizeService.pageList(whereRequest, store.getId());
            List<ClientPrize> clientPrizeList = pageClientPrizes.getList();
            for (ClientPrize clientPrize : clientPrizeList) {
                resp.addDataEntry(objectToEntry(clientPrize));
            }
            resp.setRecordsTotal(pageClientPrizes.getRecordsTotal());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
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
            String sessionName = CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION).toString();
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
            LOGGER.error(e.getMessage(),e);
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
            String sessionName = CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION).toString();
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
            LOGGER.error(e.getMessage(),e);
            resp.setErrorString("出错");
        }
        return resp;
    }

    /**
     * 检验验证码 兑奖
     *
     * @param checkCode
     * @param request
     * @return
     */
    @RequestMapping(value = {"admin/clientPrizeInfo/checkClientPriceInfo", "shop/clientPrizeInfo/checkClientPriceInfo"}, method = RequestMethod.POST)
    public AjaxResponse checkClientPriceInfo(@RequestParam("checkCode") String checkCode, HttpServletRequest request) {
        AjaxResponse resp = new AjaxResponse();
        try {
            String sessionName = CommonLoginParam.getRequestInfo(request).get(Constants.REQUEST_INFO_SESSION).toString();
            //从session中读取merchantStore信息，如果为空，则提示用户先切换门店
            if (request.getSession().getAttribute(sessionName) == null) {
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore store = (MerchantStore) request.getSession().getAttribute(sessionName);
            ClientPrizeGetInfo clientPrizeGetInfo = clientPrizeGetInfoService.checkClientPriceInfo(store.getId(), checkCode.toUpperCase());
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
            if (clientPrizeGetInfo != null) {
                int status = clientPrizeGetInfo.getStatus();
                if (status == Constants.CLIENT_PRIZEINFO_STATUS_EXPIRED) {
                    resp.setErrorString("该优惠券已过期");
                } else if (status == Constants.CLIENT_PRIZEINFO_STATUS_USED) {
                    resp.setErrorString("该优惠码已被使用");
                } else {
                    //再次判断是否过期或是否未在使用期内
                    Date startDate = clientPrizeGetInfo.getPrizeStartDate();
                    Date endDate = clientPrizeGetInfo.getPrizeEndDate();
                    Date nowDate = DateTimeUtils.startToday();
                    if (endDate.compareTo(nowDate) < 0) {
                        clientPrizeGetInfo.setStatus(Constants.CLIENT_PRIZEINFO_STATUS_EXPIRED);
                        resp.setErrorString("该优惠码已过期");
                    } else if (startDate.compareTo(nowDate) > 0) {
                        resp.setErrorString("该优惠码不在兑换时间内");
                    } else {
                        //验证通过后
                        resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
                        resp.addDataEntry(objectToEntryForInfo(clientPrizeGetInfo));
                        clientPrizeGetInfo.setStatus(Constants.CLIENT_PRIZEINFO_STATUS_USED);
                        clientPrizeGetInfo.setCheckDate(new Date());
                    }
                }
                clientPrizeGetInfoService.update(clientPrizeGetInfo);
                return resp;
            } else {
                resp.setErrorString("此优惠码该店无效");
                return resp;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
        return AjaxResponse.failed(-1);

    }

    /**
     * 奖券兑换码 txt文件上传分析
     * @param files
     * @param prizeId
     * @param request
     * @return
     */
    @RequestMapping(value = {"admin/clientPrize/checkCodeUpload", "shop/clientPrize/checkCodeUpload"}, method = RequestMethod.POST)
    public AjaxResponse checkCodeUpload(@RequestParam("file") CommonsMultipartFile[] files, @RequestParam("prizeId")Long prizeId ,HttpServletRequest request){
        LOGGER.info("----奖券兑换码上传----,对应奖券ID:" + prizeId);
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getOriginalFilename();
            LOGGER.info("fileName---------->" + fileName);
            if (!files[i].isEmpty()) {
                String type = files[i].getContentType();
                if(!type.equals("text/plain")){
                    return AjaxResponse.failed( -1,"请上传txt文档，其他格式不支持");
                }
                // name_start_end 按照上述格式上传 分解 获取有效的时限
                String newFileName = FileNameUtil.removeExtension(fileName);
                String[] nameInfo = newFileName.split("_");
                Date startDate = DateTimeUtils.getDateByString(nameInfo[1], DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT);
                Date endDate = DateTimeUtils.getDateByString(nameInfo[2], DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT);
                if(startDate==null || endDate == null){
                    return AjaxResponse.failed(-1,"上传的txt文档名不规范");
                }
                int startTime = (int) System.currentTimeMillis();
                try {
                    BufferedReader d = new BufferedReader(new InputStreamReader(files[i].getInputStream(),"utf-8"));
                    String valueString = null;
                    while ((valueString = d.readLine())!=null) {
                        //先过滤判定是否存在
                        boolean isExistTicket = clientPrizeTicketService.isExistPrizeTicket(prizeId,valueString,startDate,endDate);
                        if(!isExistTicket){
                            ClientPrizeTicket ticket = new ClientPrizeTicket();
                            ClientPrize prize = clientPrizeService.findById(prizeId);
                            ticket.setPrize(prize);
                            ticket.setTicketCode(valueString);
                            ticket.setStartDate(startDate);
                            ticket.setEndDate(endDate);
                            ticket.setStatus(Constants.CLIENT_PRIZE_TICKET_YES);
                            clientPrizeTicketService.update(ticket);
                        }
                    }
                    d.close();
                    int finalTime = (int) System.currentTimeMillis();
                    LOGGER.info( (finalTime - startTime) +"");

                } catch (Exception e) {
                    LOGGER.error("上传出错:"+e.getMessage(),e);
                    return AjaxResponse.failed(-1, "糟糕,服务器出错了");
                }
            }
        }

        return AjaxResponse.success("上传成功");

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
        entry.put("deskId", clientPrizeGetInfo.getDeskId());
        entry.put("checkCode", clientPrizeGetInfo.getCheckCode());
        entry.put("checkDate", clientPrizeGetInfo.getCheckDate());
        entry.put("prizeName", clientPrizeGetInfo.getPrizeName());
        entry.put("prizeDescription", clientPrizeGetInfo.getPrizeDescription());
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
            if (activeClientPrize != null && activeClientPrize.size() != 0) {
                int size = activeClientPrize.size();
                //若没满8个，需用谢谢惠顾填满
                ClientPrize thankYouPrize = clientPrizeService.getThankYouPrize(orgId);
                if (thankYouPrize == null) {
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
            List<ClientPrize> activeClientPrize = clientPrizeService.listActiveAndAboveZero(shopId);
            if (activeClientPrize != null && activeClientPrize.size() != 0) {
                int totalLeft = 0;
                //先获取剩余手机奖券的
                for (ClientPrize clientPrize : activeClientPrize) {
                    int leftNum = clientPrize.getLeftNum();
                    totalLeft += leftNum;
                }
                if(totalLeft == 0) {
                    return ClientAjaxResult.failed("该店奖券已被抽完");
                }
                Random random = new Random();
                //从总量随机抽取一个
                int randomPrizeNum = random.nextInt(totalLeft) + 1;
                int compareNum = 0;
                for (ClientPrize clientPrize : activeClientPrize) {
                    compareNum += clientPrize.getLeftNum();
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
                        } else {
                            clientPrize.setLeftNum(clientPrize.getLeftNum() - 1);
                            clientPrizeService.update(clientPrize);
                            clientPrize.setPriceGetId(-1L);
                        }
                        return ClientAjaxResult.success(objectToClientEntry(clientPrize));
                    }
                }
            } else {
                return ClientAjaxResult.failed("该店奖券已被抽完");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
        return ClientAjaxResult.failed("已无奖券");
    }

    /**
     * 接口三：确认领奖，同时判断手机是否超次
     *
     * @param phoneNum   手机号码
     * @param deskId     桌号
     * @param prizeGetId 获奖信息奖券id
     * @return
     */
    @RequestMapping(value = "services/public/confirmPriceInfo", method = RequestMethod.POST)
    public ClientAjaxResult confirmPriceInfo(@RequestParam("phoneNum") Long phoneNum,
                                             @RequestParam("deskId") String deskId,
                                             @RequestParam("prizeGetId") Long prizeGetId) {
        try {
            //手机号当天不超过3次判定
            boolean isOverThreeTimes = clientPrizeGetInfoService.isOverThreeTimes(phoneNum);
            if (isOverThreeTimes) {
                return ClientAjaxResult.failed("本号码今天已超过3次领奖记录");
            }
            ClientPrizeGetInfo priceGetInfo = clientPrizeGetInfoService.findByIdAndDeskId(prizeGetId, deskId);
            if (priceGetInfo != null) {
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
                String content = null;
                if (priceGetInfo.getPrice().getPhonePrizeType() == Constants.CLIENT_PRICE_PHONE_OBJECT) {
                    content = "恭喜您获得" + priceGetInfo.getPrizeName() + "一份,凭此短信到餐厅服务台领取,兑奖码：" + priceGetInfo.getCheckCode()+ ",当天领取有效,转发无效";
                } else if (priceGetInfo.getPrice().getPhonePrizeType() == Constants.CLIENT_PRICE_PHONE_CINEMA) {
                    //查取2张电影券票
                    int limit = 2;
                    List<ClientPrizeTicket> tickets = clientPrizeTicketService.listLimitTickets(limit,priceGetInfo.getPrice().getId());
                    if(tickets!=null && tickets.size() == 2){
                        //修改电影票的状态
                        for (ClientPrizeTicket ticket : tickets) {
                            ticket.setStatus(Constants.CLIENT_PRIZE_TICKET_NO);
                            clientPrizeTicketService.update(ticket);
                        }
                        content = "恭喜您获得格瓦拉" + priceGetInfo.getPrizeName() + "2张,兑换码为" + tickets.get(0).getTicketCode()+","+ tickets.get(1).getTicketCode()+ ",有效期至"+ DateTimeUtils.getDateString(tickets.get(1).getEndDate(),DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT) + ",购票流程：Step1：登陆格瓦拉生活网（www.gewara.com），手机登陆APP；Step2：选择需要预定的影片:非常父子档、影院及场次，点击“订票”按钮；Step3：进入选座页面，根据影厅实际座位图自由选座，输入观影人手机号码，点击“立即购买”；Step4：进入兑换/支付页面，输入兑换券的密码，点击“使用”，将会自动抵用单张影票的价格。多张兑换券可同时使用；Step5：兑换成功，收到取票短信，凭取票短信中的取票密码至影院格瓦拉取票机自助取票；Step6：观影";
                    } else {
                        return ClientAjaxResult.failed("你手慢了，电影票已被领取完");
                    }
                } else if (priceGetInfo.getPrice().getPhonePrizeType() == Constants.CLIENT_PRICE_PHONE_CMB_CREDIT_CARD){
                    //String oldUrl = "https://ccclub.cmbchina.com/mca/MPreContract.aspx?cardsel=8745&swbrief=y&WT.mc_id=N37CJYX20614307500MY";
                    content = "尊敬的顾客，恭喜您获得招商银行信用卡新户专享礼品，办卡成功并满足条件，即可获得新秀丽电脑双肩包或奔腾全新4D剃须刀，快通过下方链接办卡领取吧！http://t.cn/RVkCluX";
                }
                Runnable task = AlidayuSmsClient.sendSMS(
                        IPUtils.getIpAddr(getRequest()),
                        sendRecordManageService,
                        phones,
                        content,//短信内容
                        TemplateSMSType.PARTNERGAME.getName(),
                        getCommConfig().getAlidayuSignNameMuyePartner(),//签名,为null默认是美味点点笔
                        getCommConfig(),//从commonApi.properties获取默认配置
                        System.currentTimeMillis()
                );//发送短信
                taskExecutor.submit(task);
                return ClientAjaxResult.success("领奖成功");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
        return ClientAjaxResult.failed("领奖失败");

    }

    /**
     * 接口四：主动撤销回奖池
     *
     * @param deskId
     * @param prizeGetId
     * @return
     */
    @RequestMapping(value = "services/public/backToPrizePool", method = RequestMethod.POST)
    public ClientAjaxResult backToPrizePool(@RequestParam("deskId") String deskId,
                                            @RequestParam("prizeGetId") Long prizeGetId) {
        try {
            ClientPrizeGetInfo clientPrizeGetInfo = clientPrizeGetInfoService.findByIdAndDeskId(prizeGetId, deskId);
            if (clientPrizeGetInfo != null) {
                ClientPrize prize = clientPrizeGetInfo.getPrice();
                prize.setLeftNum(prize.getLeftNum() + 1);
                clientPrizeGetInfoService.delete(clientPrizeGetInfo);
                return ClientAjaxResult.success("撤回奖池成功");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
        return ClientAjaxResult.failed("撤回奖池失败");
    }

    private Map objectToClientEntry(ClientPrize clientPrize) {
        Map entry = new HashMap();
        entry.put("id", clientPrize.getId());
        entry.put("name", clientPrize.getName() == null ? "" : clientPrize.getName());
        entry.put("type", clientPrize.getType());
        entry.put("description", clientPrize.getDescription() == null ? "" : clientPrize.getDescription());
        entry.put("bigPic", clientPrize.getBigPic() == null ? "" : clientPrize.getBigPic());
        entry.put("smallPic", clientPrize.getSmallPic() == null ? "" : clientPrize.getSmallPic());
        entry.put("prizeGetId", clientPrize.getPriceGetId());
        return entry;
    }

}
