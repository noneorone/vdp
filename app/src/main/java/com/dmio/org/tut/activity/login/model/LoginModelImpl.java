package com.dmio.org.tut.activity.login.model;

/**
 * 功能说明：数据操作
 * 作者：wangmeng on 2017/2/8 9:50
 * 邮箱：noneorone@yeah.net
 */

public class LoginModelImpl implements LoginModel {

    @Override
    public Result login(String username, String password) {
        if (!"admin".equals(username)) {
            return Result.USERNAME_ERROR;
        }

        if (!"111111".equals(password)) {
            return Result.PASSWORD_ERROR;
        }

        return Result.OK;

    }
}
