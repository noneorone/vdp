package com.noo.app.activity.login.presenter;

import android.os.AsyncTask;

import com.noo.app.activity.login.model.LoginModel;
import com.noo.app.activity.login.model.LoginModelImpl;
import com.noo.app.activity.login.view.LoginView;

/**
 * 功能说明：业务处理
 * 作者：Mars.Wong on 2017/2/8 9:57
 * 邮箱：noneorone@yeah.net
 */

public class LoginPresenterImpl implements LoginPresenter {

    private LoginView mLoginView;
    private LoginModel mLoginModel;

    private UserLoginTask mAuthTask;

    public LoginPresenterImpl(LoginView loginView) {
        this.mLoginView = loginView;
        this.mLoginModel = new LoginModelImpl();
    }

    @Override
    public void validateLogin(String username, String password) {
        if (mLoginView != null) {
            mLoginView.showProgress(true);
        }

        if (mAuthTask == null) {
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, LoginModel.Result> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected LoginModel.Result doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return LoginModel.Result.EXCEPTION;
            }

            return mLoginModel.login(mUsername, mPassword);
        }

        @Override
        protected void onPostExecute(final LoginModel.Result result) {
            mAuthTask = null;
            if (mLoginView != null) {
                mLoginView.showProgress(false);
            }

            if (result != null) {
                switch (result) {
                    case USERNAME_ERROR:
                        if (mLoginView != null) {
                            mLoginView.usernameError();
                        }
                        break;
                    case PASSWORD_ERROR:
                        if (mLoginView != null) {
                            mLoginView.passwordError();
                        }
                        break;
                    case EXCEPTION:
                        break;
                    case OK:
                        if (mLoginView != null) {
                            mLoginView.loginSuccess();
                        }
                        break;
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;

            if (mLoginView != null) {
                mLoginView.showProgress(false);
            }
        }
    }
}
