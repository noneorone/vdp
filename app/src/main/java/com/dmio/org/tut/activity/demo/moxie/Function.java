package com.dmio.org.tut.activity.demo.moxie;

import android.text.TextUtils;

import com.moxie.client.model.MxParam;

/**
 * 功能类型
 * wangmeng on 2017/3/28 9:16
 * wangmeng@pycredit.cn
 */
public enum Function {
    /**
     * 资产信息-网银
     */
    ONLINE_BANK(MxParam.PARAM_FUNCTION_ONLINEBANK),
    /**
     * 资产信息-邮箱账单
     */
    EMAIL(MxParam.PARAM_FUNCTION_EMAIL),
    /**
     * 资产信息-车险
     */
    INSURANCE(MxParam.PARAM_FUNCTION_INSURANCE),
    /**
     * 资产信息-公积金
     */
    FUND(MxParam.PARAM_FUNCTION_FUND),


    /**
     * 消费信息-支付宝
     */
    ALIPAY(MxParam.PARAM_FUNCTION_ALIPAY),
    /**
     * 消费信息-京东
     */
    JINGDONG(MxParam.PARAM_FUNCTION_JINGDONG),
    /**
     * 消费信息-淘宝
     */
    TAOBAO(MxParam.PARAM_FUNCTION_TAOBAO),


    /**
     * 社交信息验证-运营商
     */
    CARRIER(MxParam.PARAM_FUNCTION_CARRIER),
    /**
     * 社交信息验证-QQ
     */
    QQ(MxParam.PARAM_FUNCTION_QQ),
    /**
     * 社交信息验证-脉脉
     */
    MAIMAI(MxParam.PARAM_FUNCTION_MAIMAI),
    /**
     * 社交信息验证-领英
     */
    LINKEDIN(MxParam.PARAM_FUNCTION_LINKEDIN),


    /**
     * 身份信息验证-学信网
     */
    CHSI(MxParam.PARAM_FUNCTION_CHSI),
    /**
     * 身份信息验证-社保
     */
    SECURITY(MxParam.PARAM_FUNCTION_SECURITY),


    /**
     * 法院查询-法院失信人
     */
    SHIXINCOURT(MxParam.PARAM_FUNCTION_SHIXINCOURT),
    /**
     * 法院查询-法院执行人
     */
    ZHIXINGCOURT(MxParam.PARAM_FUNCTION_ZHIXINGCOURT),


    /**
     * 个人征信-征信报告
     */
    ZHENGXIN(MxParam.PARAM_FUNCTION_ZHENGXIN),
    /**
     * 个人征信-个人所得税
     */
    TAX(MxParam.PARAM_FUNCTION_TAX);


    private String value;

    public String getValue() {
        return value;
    }

    Function(String value) {
        this.value = value;
    }

    /**
     * 通过value获取对应功能类型
     *
     * @param value
     * @return
     */
    public static Function getByValue(String value) {
        if (!TextUtils.isEmpty(value)) {
            Function[] values = Function.values();
            for (Function v : values) {
                if (v.value.equals(value)) {
                    return v;
                }
            }
        }
        return null;
    }
}