package com.dmio.org.tut.activity.demo.moxie;

/**
 * 身份认证类型
 * Mars.Wong on 2017/3/28 9:16
 * noneorone@yeah.net
 */
public enum IDVerify {

    /**
     * 银行卡四要素
     */
    BANK("bank", "银行卡四要素"),

    /**
     * 身份证认证
     */
    ID_CARD("idcard", "身份证认证"),

    /**
     * 运营商三要素
     */
    CARRIER("carrier", "运营商三要素");

    private String type;
    private String title;

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    IDVerify(String type, String title) {
        this.type = type;
        this.title = title;
    }
}