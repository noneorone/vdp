package com.dmio.org.tut.data.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Login model
 *
 * @author wangmeng noneorone@yeah.net
 * @since 2017/2/23
 */
public class LoginInfo extends RealmObject implements Serializable {

    @PrimaryKey
    private String lid;

    @Required
    private String account;
    @Required
    private String password;

    @Ignore
    private long updateTime;

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
