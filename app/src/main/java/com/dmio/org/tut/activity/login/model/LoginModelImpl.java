package com.dmio.org.tut.activity.login.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dmio.org.tut.core.ExApplication;
import com.dmio.org.tut.data.model.LoginInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noo.core.utils.AssertUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;

/**
 * 功能说明：数据操作
 * 作者：Mars.Wong on 2017/2/8 9:50
 * 邮箱：noneorone@yeah.net
 */

public class LoginModelImpl implements LoginModel {

    private static final String TAG = "LoginModelImpl";

    @Override
    public Result login(final String username, final String password) {
        boolean usernameError = true;
        boolean passwordError = true;

        String jsonContent = AssertUtils.getJsonData(ExApplication.getInstance(), "login.json");
        Gson gson = new Gson();
        Type type = new TypeToken<List<LoginInfo>>() {
        }.getType();
        List<LoginInfo> loginInfos = gson.fromJson(jsonContent, type);
        if (loginInfos != null && !loginInfos.isEmpty()) {
            for (LoginInfo info : loginInfos) {
                if (username.equals(info.getAccount())) {
                    usernameError = false;
                    if (password.equals(info.getPassword())) {
                        passwordError = false;
                        break;
                    }
                }
            }
        }

        if (usernameError) {
            return Result.USERNAME_ERROR;
        }

        if (passwordError) {
            return Result.PASSWORD_ERROR;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        LoginInfo loginInfo = realm.createObject(LoginInfo.class, UUID.randomUUID().toString());
                        loginInfo.setAccount(username);
                        loginInfo.setPassword(password);
                        loginInfo.setUpdateTime(System.currentTimeMillis());
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "save login info success.");
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.e(TAG, "save login info failed.");
                        error.printStackTrace();
                    }
                });
            }
        });

        return Result.OK;

    }
}
