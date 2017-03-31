package com.dmio.org.tut.core;

import com.dmio.org.tut.core.log.Logger;

/**
 * 功能说明：异常处理类<br/>
 * 作者：wangmeng on 2017/3/31 22:14<br/>
 * 邮箱：wangmeng@pycredit.cn
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logger.e(e);
    }

}
