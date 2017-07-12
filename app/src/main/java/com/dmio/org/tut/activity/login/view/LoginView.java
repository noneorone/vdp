package com.dmio.org.tut.activity.login.view;

/**
 * 功能说明：视图层
 * 作者：Mars.Wong on 2017/2/8 9:29
 * 邮箱：noneorone@yeah.net
 */

public interface LoginView {

    void showProgress(boolean show);

    void usernameError();

    void passwordError();

    void attemptLogin();

    void loginSuccess();

}
