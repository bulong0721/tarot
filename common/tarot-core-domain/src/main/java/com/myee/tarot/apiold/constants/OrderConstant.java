package com.myee.tarot.apiold.constants;

/**
 * Info: 订单状态常量
 * User: Gary.zhang@clever-m.com
 * Date: 5/3/15
 * Time: 11:24
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
public class OrderConstant {

    /**
     * 未支付
     */
    public static final int NON_PAY_STATUS = 1;

    /**
     * 已支付
     */
    public static final int PAY_SUCCESS = 2;

    /**
     * 退款
     */
    public static final int REFUND = 3;

    /**
     * 支付宝支付
     */
    public static final int PAY_TYPE_ALIPAY = 1;

    /**
     * 微信支付
     */
    public static final int PAY_TYPE_TENPAY = 2;
}
