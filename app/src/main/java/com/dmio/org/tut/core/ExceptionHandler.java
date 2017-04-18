package com.dmio.org.tut.core;

import android.content.Context;
import android.os.Looper;
import android.os.Process;
import android.widget.Toast;

import com.noo.core.log.Logger;

/**
 * 异常处理类
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/3/31 22:14<br/>
 * @since 1.0
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    final private Context mContext;

    public ExceptionHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public void uncaughtException(final Thread t, final Throwable th) {
        // 记录异常日志
        Logger.e(th);

        // 弹出提示
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "Exception Occured!!!", Toast.LENGTH_LONG).show();
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
