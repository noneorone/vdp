package com.noo.app.activity.login.model;

/**
 * 功能说明：数据层
 * 作者：Mars.Wong on 2017/2/7 17:32
 * 邮箱：noneorone@yeah.net
 */

public interface LoginModel {

    enum Result {
        USERNAME_ERROR, PASSWORD_ERROR, EXCEPTION, OK
    }

    /**
     * 登录验证
     *
     * @param username
     * @param password
     */
    Result login(String username, String password);

}
