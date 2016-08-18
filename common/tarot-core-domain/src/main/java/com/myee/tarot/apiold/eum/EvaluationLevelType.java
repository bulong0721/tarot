package com.myee.tarot.apiold.eum;

public enum EvaluationLevelType {
    VERYBAD("非常差", 20),//一颗星
    BAD("差", 40), //二颗星
    NORMAL("一般", 60), //三颗星
    GOOD("好", 80), //四颗星
    VERYGOOD("棒棒哒", 100);//五颗星
    // 成员变量
    private String name;
    private Integer index;

    // 构造方法
    private EvaluationLevelType(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(Integer index) {
        for (EvaluationLevelType c : EvaluationLevelType.values()) {
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
