package com.myee.tarot.apiold.eum;

public enum TemplateSMSType {
    ADDFOOD("SMS_4430130", 1),//加菜
    ADDWATER("SMS_4455128", 2), //加水
    NAPKIN("SMS_4420145", 3), //纸巾
    ACCOUNTS("SMS_4410106", 4), //结账
    SERVICE("SMS_4485156", 5), //服务
    BATTERIES("SMS_4415091", 6),//充电

    //pad上面使用
    ADDWATERP("SMS_5023640", 7), //加水
    ADDSTEAMEDRICE("SMS_5038160", 8), //加米饭

    PENPULLOUT("SMS_5430188", 15), //笔已拔除
    CASHPAY("SMS_5415571", 16), //现金支付
    BANKCARDPAY("SMS_5400525", 17), //银行卡支付
    WEIXINPAY("SMS_5385408", 18), //微信支付
    ALIPAYPAY("SMS_5380540", 19), //支付宝支付
    CLEANDESK("SMS_6160382", 20), //收拾桌面
    FONDUESOUP ("SMS_7890052", 21), //火锅加汤
    CHANGETABLEWARE("SMS_7835053", 22), //更换餐具
    OUT_OF_CHARGING("SMS_12891207", 23), //充电宝断开

    PARTNERGAME("SMS_7880052", 100000);  //速领取短信模板，短信签名是“木爷提示”

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private TemplateSMSType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (TemplateSMSType c : TemplateSMSType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
