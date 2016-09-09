package com.myee.tarot.web.apiold.controller.v10;

import com.myee.tarot.apiold.constants.OrderConstant;
import com.myee.tarot.apiold.domain.OrderInfo;
import com.myee.tarot.apiold.service.OrderInfoService;
import com.myee.tarot.core.util.DateUtil;
import com.myee.tarot.web.apiold.controller.BaseController;
import com.myee.tarot.web.ClientAjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Info: clever
 * User: Gary.zhang@clever-m.com
 * Date: 2016-01-20
 * Time: 14:29
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
@RestController
//@Scope("prototype")
@RequestMapping("/api/v10/order")
public class OrderInfoController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(OrderInfoController.class);

    @Autowired
    private OrderInfoService orderInfoService;


    /**
     * 订单申请，看代码暂时没用到的接口
     * @return
     */
    @RequestMapping(value = "/create")
    public ClientAjaxResult createOrder(@ModelAttribute(value="orderInfo") OrderInfo orderInfo){
        logger.info("创建订单");
        try {


            String orderId = DateUtil.getOrderNum();
            orderInfo.setOrderId(orderId);


            //默认未支付
            orderInfo.setPayStatus(OrderConstant.NON_PAY_STATUS);

            orderInfoService.createOrder(orderInfo);
            return ClientAjaxResult.success(orderInfo.getOrderId());
        }catch ( Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }

    /**
     * 微信支付回调地址
     * @return
     */
    @RequestMapping(value = "/wechat")
    public ClientAjaxResult orderPaymentForWechat(@RequestParam(value = "orderId", required = true) String orderId) {
        logger.info("支付订单,orderId:"+orderId);

        //获取订单信息
        OrderInfo orderInfo = orderInfoService.getById(orderId);

        try {

            orderInfoService.update(orderInfo);

            return ClientAjaxResult.success("");

        }catch ( Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }

    /**
     * 支付宝支付回调
     *
     * @return
     */
    @RequestMapping(value = "/alipay")
    @ResponseBody
    public ClientAjaxResult orderPaymentForAlipay(@RequestParam(value = "orderId", required = true) String orderId) throws Exception {
        logger.info("支付订单,orderId:" + orderId);

        //获取订单信息
        OrderInfo orderInfo = orderInfoService.getById(orderId);

        try {


            return ClientAjaxResult.success("");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }

}
