package com.noo.core.utils;

import android.text.TextUtils;

import com.noo.core.log.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 字符串处理工具<br/>
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2013/7/2 20:09:54<br/>
 * @since 1.0
 */
public final class StringUtils {

    // default charset
    private static final String CHARSET_UTF8 = "UTF-8";
    // domain
    private static final String REGEX_DOMAIN = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$";
    // ip
    private static final String REGEX_IP = "^(2[0-5]{2}|2[0-4][0-9]|1?[0-9]{1,2}).(2[0-5]{2}|2[0-4][0-9]|1?[0-9]{1,2}).(2[0-5]{2}|2[0-4][0-9]|1?[0-9]{1,2}).(2[0-5]{2}|2[0-4][0-9]|1?[0-9]{1,2})$";
    // email
    private static final String REGEX_EMAIL = "\\b^['_a-z0-9-\\+]+(\\.['_a-z0-9-\\+]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)*\\.([a-z]{2}|aero|arpa|asia|biz|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|nato|net|org|pro|tel|travel|xxx)$\\b";
    // 身份证
    private static final String REGEX_ID = "\\d{15}(\\d\\d[0-9xX])?";
    //去除尖括号以及尖括号里面的内容
    private static final String REGEX_BRACKET = "[\\<][^\\<\\>]+[\\>]";

    /**
     * 邮编正则表达式  [0-9]\d{5}(?!\d) 国内6位邮编
     */
    public static final String POSTCODE = "[0-9]\\d{5}(?!\\d)";


    /**
     * Check that the given CharSequence is neither <code>null</code> nor of
     * length 0. Note: Will return <code>true</code> for a CharSequence that
     * purely consists of whitespace.
     * <p>
     * <p>
     * <pre>
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength("Hello") = true
     * </pre>
     *
     * @param str the CharSequence to check (may be <code>null</code>)
     * @return <code>true</code> if the CharSequence is not null and has length
     */
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Check that the given String is neither <code>null</code> nor of length 0.
     * Note: Will return <code>true</code> for a String that purely consists of
     * whitespace.
     *
     * @param str the String to check (may be <code>null</code>)
     * @return <code>true</code> if the String is not null and has length
     * @see #hasLength(CharSequence)
     */
    public static boolean hasLength(String str) {
        return hasLength((CharSequence) str);
    }

    /**
     * Trim leading and trailing whitespace from the given String.
     *
     * @param str the String to check
     * @return the trimmed String
     * @see java.lang.Character#isWhitespace
     */
    public static String trimWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
            sb.deleteCharAt(0);
        }
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * <p>
     * Reverses a String as per {@link StringBuilder#reverse()}.
     * </p>
     * <p>
     * <p>
     * A <code>null</code> String returns <code>null</code>.
     * </p>
     * <p>
     * <pre>
     * StringUtils.reverse(null)  = null
     * StringUtils.reverse("")    = ""
     * StringUtils.reverse("bat") = "tab"
     * </pre>
     *
     * @param str the String to reverse, may be null
     * @return the reversed String, <code>null</code> if null String input
     */
    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * ip validation
     *
     * @param ip
     * @return
     */
    public static boolean validateIP(String ip) {
        return Pattern.compile(REGEX_IP).matcher(ip).matches();
    }

    /**
     * domain validation
     *
     * @param domain
     * @return
     */
    public static boolean validateDomain(String domain) {
        return Pattern.compile(REGEX_DOMAIN).matcher(domain).matches();
    }

    /**
     * port validation
     *
     * @param port
     * @return
     */
    public static boolean validatePort(int port) {
        return !(port < 0 || port > 65535);
    }

    /**
     * email validation
     *
     * @param email
     * @return
     */
    public static boolean validateEmail(String email) {
        return Pattern.compile(REGEX_EMAIL).matcher(email).matches();

    }

    /**
     * ID validation
     *
     * @param ID
     * @return
     */
    public static boolean validateID(String ID) {
        return Pattern.compile(REGEX_ID).matcher(ID).matches();
    }

    /**
     * get boolean value
     *
     * @param value
     * @return
     */
    public static boolean booleanValue(String value) {
        return "true".equalsIgnoreCase(value) || "1".equals(value) || "Y".equals(value) || "on".equals(value);
    }


    /**
     * encode string
     *
     * @param s
     * @return
     */
    public static String encode(String s) {
        return encode(s, CHARSET_UTF8);
    }

    /**
     * encode string with specified charset
     *
     * @param s
     * @return
     */
    public static String encode(String s, String charset) {
        try {
            return URLEncoder.encode(s, charset);
        } catch (UnsupportedEncodingException e) {
            Logger.e(e);
        }
        return s;
    }

    /**
     * decode string
     *
     * @param s
     * @return
     */
    public static String decode(String s) {
        return decode(s, CHARSET_UTF8);
    }

    /**
     * decode string with specified charset
     *
     * @param s
     * @param encoding
     * @return
     */
    public static String decode(String s, String encoding) {
        try {
            return URLDecoder.decode(s, encoding);
        } catch (UnsupportedEncodingException e) {
            Logger.e(e);
        }
        return s;
    }

    /**
     * filt the special chars :\\\\/*?|<> \"'
     *
     * @param name
     * @return
     */
    public static String sanitizeFilename(String name) {
        return name.replaceAll("[:\\\\/*?|<> \"']", "_");
    }

    /**
     * 验证邮编
     *
     * @param postCode 邮编 6位数字
     * @return 成功返回true 失败返回false
     */
    public static boolean checkPostCode(String postCode) {
        try {
            return Pattern.matches(POSTCODE, postCode);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {
//    	String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";  
//        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w-]+\\.)+[\\w]+[\\w]$";  
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return Pattern.matches(regex, email);
    }

    public static boolean checkMobile(String mobile) {
        return checkMobile("", mobile);
    }

    /**
     * ^[1][3-8]\d{9}$|^([6|9])\d{7}$|^[0][9]\d{8}$|^[6]([8|6])\d{5}$
     * 规则说明：
     * 中国大陆：开头1 3-8号段，后边跟9位数字
     * 台湾：09开头后面跟8位数字
     * 香港：9或6开头后面跟7位数字
     * 澳门：66或68开头后面跟5位数字
     * 注意：以上表达式只验证港澳台及大陆手机号码，不包含座机小灵通及区号等验证
     *
     * @param areaCode 国家和地区代码
     * @param mobile   手机号码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobile(String areaCode, String mobile) {
        if (mobile.startsWith("852")) {
            areaCode = "+852";
            mobile = mobile.replace("852", "");
        }

        String regex = "";
        if ("+86".equals(areaCode)) {
            regex = "(\\+\\d+)?1[3-8]\\d{9}$";
        } else if ("+852".equals(areaCode)) {
            regex = "(\\+\\d+)?([5|6|9])\\d{7}$";
        } else {
            regex = "(\\+\\d+)?[1][3-8]\\d{9}$|(\\+\\d+)?([5|6|9])\\d{7}$|(\\+\\d+)?[0][9]\\d{8}$|(\\+\\d+)?[6]([8|6])\\d{5}$";
        }
        return Pattern.matches(regex, mobile);
    }

//    /** 
//     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港）） 
//     * @param mobile 移动、联通、电信运营商的号码段 
//     *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡） 
//     *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p> 
//     *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p> 
//     *<p>电信的号段：133、153、180（未启用）、189</p> 
//     * @return 验证成功返回true，验证失败返回false 
//     */  
//    public static boolean checkMobile(String mobile) {  
//        String regex = "(\\+\\d+)?1[3458]\\d{9}$";  
//        return Pattern.matches(regex,mobile);  
//    }  

    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *              <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *              数字之后是空格分隔的国家（地区）代码。</p>
     *              <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *              对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     *              <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    private static Pattern mobilePattern = Pattern.compile("^[1][3,4,5,8][0-9]{9}$");
    private static Pattern mobileOrPhonePattern = Pattern.compile("1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}");
    private static Pattern emailPattern = Pattern.compile("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w-]+\\.)+[\\w]+[\\w]$");
    static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 通过已知字符串按分隔符获取两个联接的字符串中另一个<br>
     * <i>格式规则为："A-B"。</i>如"10147-10146"，可通过10417获取10416
     *
     * @param sub     字符串子串
     * @param all     字符串全串
     * @param spliter 分割字符
     * @return
     */
    public static String getTheOtherFix(String sub, String all, String spliter) {
        StringBuilder builder = new StringBuilder();

        if (null != sub && null != all) {
            if (all.indexOf(sub) == -1 || all.indexOf(spliter) == -1) {
                return all;
            } else {
                builder.append(all);
                int start = builder.indexOf(sub);
                builder.delete(start, start + sub.length());
                builder.deleteCharAt(builder.indexOf(spliter));
            }
        }

        return builder.toString();
    }

    /**
     * 将以逗号分隔的字符串转为对应List
     *
     * @param ids
     * @return
     */
    public static List<String> idsToList(String ids) {
        if (TextUtils.isEmpty(ids)) {
            return null;
        }

        List<String> lists = new ArrayList<String>();
        if (ids.contains(",")) {
            String[] strs = ids.split(",");
            for (String s : strs) {
                if (!TextUtils.isEmpty(s)) {
                    lists.add(s);
                }
            }
        } else {
            lists.add(ids);
        }

        return lists;
    }

    /**
     * 将第一个字母转为大写
     *
     * @param string
     * @return
     */
    public static String firstLetterToUpper(String string) {
        byte[] items = string.getBytes();
        if (items[0] >= 'a' && items[0] <= 'z')
            items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    /**
     * 获取指定长度的随机数
     *
     * @param length 生成字符串的长度
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 去除尖括号以及尖括号里面的内容
     *
     * @param str
     * @return
     */
    public static String removeBracket(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return str.replaceAll(REGEX_BRACKET, "");

    }

    /**
     * 去掉字符串中包含的空格|水平制表|换行|回车符
     *
     * @param str
     * @return
     */
    public static String removeBlankSpaceChars(String str) {
        if (hasLength(str)) {
            return str.replace("\\s*|\t|\r|\n", "");
        }
        return str;
    }

}
