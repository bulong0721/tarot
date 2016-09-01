package com.myee.tarot.web.campaign.controller;

import com.google.common.collect.Lists;
import com.myee.tarot.campaign.service.ClientPrizeGetInfoService;
import com.myee.tarot.campaign.service.ClientPrizeService;
import com.myee.tarot.clientprize.domain.ClientPrize;
import com.myee.tarot.clientprize.domain.ClientPrizeGetInfo;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.AutoNumUtil;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.weixin.domain.ClientAjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2016/8/29.
 */
@RestController
public class ClientPrizeController {

    @Autowired
    private ClientPrizeService clientPrizeService;
    @Autowired
    private ClientPrizeGetInfoService clientPrizeGetInfoService;

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
            String path = request.getServletPath();
            String sessionName = null;
            if (path.contains("/admin/")) {
                sessionName = Constants.ADMIN_STORE;
            } else if (path.contains("/shop/")) {
                sessionName = Constants.CUSTOMER_STORE;
            }
            //从session中读取merchantStore信息，如果为空，则提示用户先切换门店
            if (request.getSession().getAttribute(sessionName) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore = (MerchantStore) request.getSession().getAttribute(sessionName);
            clientPrize.setLeftNum(clientPrize.getTotal());
            clientPrize.setStore(merchantStore);
            if (clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_SCANCODE) {
                clientPrize.setLeftNum(null);
                clientPrize.setTotal(null);
            } else if (clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_THANKYOU) {
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
            String path = request.getServletPath();
            String sessionName = null;
            if (path.contains("/admin/")) {
                sessionName = Constants.ADMIN_STORE;
            } else if (path.contains("/shop/")) {
                sessionName = Constants.CUSTOMER_STORE;
            }
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
            String path = request.getServletPath();
            String sessionName = null;
            if (path.contains("/admin/")) {
                sessionName = Constants.ADMIN_STORE;
            } else if (path.contains("/shop/")) {
                sessionName = Constants.CUSTOMER_STORE;
            }
            //从session中读取merchantStore信息，如果为空，则提示用户先切换门店
            if (request.getSession().getAttribute(sessionName) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            ClientPrize existPrize = clientPrizeService.findById(clientPrize.getId());
            if (existPrize != null) {
                clientPrizeService.delete(existPrize);
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("出错");
        }
        return resp;
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
            int size = activeClientPrize.size();
            //若没满8个，需用谢谢惠顾填满
            if (size <= 8) {
                for (int i = 0; i < 8 - size; i++) {
                    ClientPrize addPrize = new ClientPrize();
                    addPrize.setName("谢谢惠顾");
                    activeClientPrize.add(addPrize);
                }
            } else {
                return ClientAjaxResult.failed("设置的奖品列表超过8个");
            }
            //去除简化字段
            List<Object> resultObject = Lists.newArrayList();
            for (ClientPrize clientPrize : activeClientPrize) {
                resultObject.add(objectToClientEntry(clientPrize));
            }
            return ClientAjaxResult.success(resultObject);
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
    public ClientAjaxResult getRandomClientPrizeInfo(@RequestParam("shopId") Long shopId,
                                                     @RequestParam("deskId") String deskId) {
        try {
            List<ClientPrize> activeClientPrize = clientPrizeService.listActive(shopId);
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
                        getInfo.setStatus(Constants.CLIENT_PRIZEINFO_STATUS_UNGET);
                        ClientPrizeGetInfo clientPrizeGetInfo = clientPrizeGetInfoService.update(getInfo);
                        return ClientAjaxResult.success(clientPrizeGetInfo);
                    } else if (clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_SCANCODE) {
                        //无需消奖
                    } else if (clientPrize.getType() == Constants.CLIENT_PRIZE_TYPE_THANKYOU) {
                        clientPrize.setLeftNum(clientPrize.getLeftNum() - 1);
                        clientPrizeService.update(clientPrize);
                    }
                    return ClientAjaxResult.success(clientPrize);
                }
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
        entry.put("name", clientPrize.getName());
        entry.put("type", clientPrize.getType());
        entry.put("description", clientPrize.getDescription());
        entry.put("bigPic", clientPrize.getBigPic());
        entry.put("smallPic", clientPrize.getSmallPic());
        return entry;
    }

}
