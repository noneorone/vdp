package com.dmio.org.tut.activity.demo.moxie;

/**
 * 配置项<br/>
 * 备注: apiKey和token跟应用的包名和应用名称没有关联关系<br/>
 * wangmeng on 2017/3/28 9:16
 * wangmeng@pycredit.cn
 */
public class Config {

    /**
     * 合作方系统中的客户ID（用来做用户标识，魔蝎不做强校验，只提供回传）
     */
    public static final String USER_ID = "1111";

    /**
     * 获取任务状态时使用
     */
    public static final String API_KEY = "e684f3ca689248048353d45c67bae426";

    /**
     * 魔蝎分配的token
     */
    public static final String TOKEN = "6540e3e241b04332885ebda8e4df2c3e";

    /**
     * 标题栏背景色
     */
    public static final String BANNER_BG_COLOR = "#000000";

    /**
     * 标题栏字体颜色
     */
    public static final String BANNER_TXT_COLOR = "#ffffff";

    /**
     * 页面主色调
     */
    public static final String THEME_COLOR = "#ff9500";

    /**
     * 协议URL
     */
    public static final String AGREEMENT_URL = "https://api.51datakey.com/h5/agreement.html";

    /**
     * 人行征信URL
     */
    public static final String CREDIT_URL = "https://api.51datakey.com/h5/credit/index.html";

    /**
     * 身份验证URL
     */
    public static final String VERTIFICATION_URL = "https://api.51datakey.com/h5/vertification/%s/index.html?token=%s";

}
