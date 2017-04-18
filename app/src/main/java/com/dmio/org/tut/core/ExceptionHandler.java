package com.dmio.org.tut.core;

import android.os.Looper;
import android.os.Process;
import android.widget.Toast;

import com.dmio.org.tut.application.ExApplication;
import com.dmio.org.tut.core.log.Logger;

/**
 * 功能说明：异常处理类<br/>
 * 作者：wangmeng on 2017/3/31 22:14<br/>
 * 邮箱：noneorone@yeah.net
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(final Thread t, final Throwable th) {
        // 记录异常日志
        Logger.e(th);

        // 弹出提示
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(ExApplication.getInstance().getBaseContext(), "Exception Occured!!!", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }) {
        }.start();

        // 延迟退出
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                Looper.loop();
            }
        }) {
        }.start();
    }

}
