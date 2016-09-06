package com.dmio.org.tut.utils;

/**
 * 功能说明：字符串处理工具
 * 作者：wangmeng on 2016/8/29 15:12
 * 邮箱：wangmeng@pycredit.cn
 */
public class StringUtils {

    /**
     * 检测字符串是否为空
     * @param s
     * @return
     */
    public static final boolean isEmpty(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }

        return false;
    }

    /**
     * 检测字符是否为空
     * @param cs
     * @return
     */
    public static final boolean isEmpty(CharSequence cs) {
        return isEmpty(String.valueOf(cs));
    }

}
