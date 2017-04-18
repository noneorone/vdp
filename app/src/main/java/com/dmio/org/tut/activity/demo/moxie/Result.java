package com.dmio.org.tut.activity.demo.moxie;

import android.text.TextUtils;

import org.json.JSONObject;

/**
 * 功能说明：结果<br/>
 * 作者：wangmeng on 2017/3/28 14:25<br/>
 * 邮箱：noneorone@yeah.net<br/>
 */

public class Result {

    private int code;
    private String taskType;
    private String searchId;
    private String taskId;
    private String message;
    private String account;

    public Result() {
    }

    public Result(int code, String taskType, String searchId, String taskId, String message, String account) {
        this.code = code;
        this.taskType = taskType;
        this.searchId = searchId;
        this.taskId = taskId;
        this.message = message;
        this.account = account;
    }

    public int getCode() {
        return code;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getSearchId() {
        return searchId;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getMessage() {
        return message;
    }

    public String getAccount() {
        return account;
    }

    /**
     * 返回结果JSON解析<br/>
     *
     * @param json
     * @return
     */
    public static Result getValue(String json) {
        Result result = null;

        try {
            if (!TextUtils.isEmpty(json)) {
                JSONObject obj = new JSONObject(json);
                int code = obj.getInt("code");
                String taskId = obj.getString("taskId");
                String message = obj.getString("message");
                String account = obj.getString("account");
                String taskType = obj.getString("taskType");
                String searchId = obj.getString("searchId");
                result = new Result(code, taskType, searchId, taskId, message, account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 结果类型
     */
    public enum Type {
        /**
         * 1.1.没有进行账单导入(后台没有通知)<br/>
         * {"code" : -1, "taskType" : "mail", "searchId" : "", "taskId" : "", "message" : "", "account" : ""}
         */
        C11(-1, "用户没有进行导入操作"),

        /**
         * 1.2.平台方服务问题(后台没有通知)<br/>
         * {"code" : -2, "taskType" : "mail", "searchId" : "", "taskId" : "", "message" : "", "account" : "xxx"}
         */
        C12(-2, "导入失败(平台方服务问题)"),

        /**
         * 1.3.魔蝎数据服务异常(后台没有通知)<br/>
         * {"code" : -3, "taskType" : "mail", "searchId" : "", "taskId" : "", "message" : "", "account" : "xxx"}
         */
        C13(-3, "导入失败(魔蝎数据服务异常)"),

        /**
         * 1.4.用户输入出错（密码、验证码等输错且未继续输入）<br/>
         * {"code" : -4, "taskType" : "mail", "searchId" : "", "taskId" : "", "message" : "密码错误", "account" : "xxx"}
         */
        C14(-4, "导入失败"),

        /**
         * 2.账单导入失败(后台有通知)<br/>
         * {"code" : 0, "taskType" : "mail", "searchId" : "3550622685459407187", "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112", "account" : "xxx"}
         */
        C2(0, "导入失败"),

        /**
         * 3.账单导入成功(后台有通知)<br/>
         * {"code" : 1, "taskType" : "mail", "searchId" : "3550622685459407187", "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112", "account" : "xxx"}
         */
        C3(1, "导入成功"),

        /**
         * 4.账单导入中(后台有通知)<br/>
         * {"code" : 2, "taskType" : "mail", "searchId" : "3550622685459407187", "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112", "account" : "xxx"}<br/>
         * 如果用户中途导入魔蝎SDK会出现这个情况，如需获取最终状态请轮询贵方后台接口<br/>
         * 魔蝎后台会向贵方后台推送Task通知和Bill通知<br/>
         * Task通知：登录成功/登录失败<br/>
         * Bill通知：账单通知<br/>
         */
        C4(2, "导入中");

        private int code;
        private String value;

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        Type(int code, String value) {
            this.code = code;
            this.value = value;
        }

        /**
         * 通过code获取对应类型
         *
         * @param code
         * @return
         */
        public static Type getByCode(int code) {
            Type[] values = Type.values();
            for (Type v : values) {
                if (v.code == code) {
                    return v;
                }
            }

            return null;
        }

    }

}
