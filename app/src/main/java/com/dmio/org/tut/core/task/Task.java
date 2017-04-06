package com.dmio.org.tut.core.task;

import com.dmio.org.tut.core.log.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 功能说明：基础任务处理<br/>
 * 作者：wangmeng on 2017/3/29 21:15<br/>
 * 邮箱：wangmeng@pycredit.cn
 */

public class Task {

    public interface CallBack<T> {
        /**
         * 预处理
         */
        void preExec();

        /**
         * 执行中
         */
        T inHandle();

        /**
         * 处理完成
         */
        void complete(T t);
    }

    private static Task instance;

    private ExecutorService executor;

    private CallBack cb;

    public static final Task getInstance() {
        if (instance == null) {
            synchronized (Task.class) {
                if (instance == null) {
                    instance = new Task();
                }
            }
        }
        return instance;
    }

    private Task() {
        executor = Executors.newFixedThreadPool(5);
    }

    /**
     * 5秒任务超时返回
     *
     * @param cb
     * @return
     */
    public Object exec(CallBack cb) {
        return exec(cb, 5000L);
    }

    /**
     * 执行任务
     *
     * @param cb      {@link CallBack}
     * @param timeout 超时时间
     * @return 结果对象
     */
    public Object exec(final CallBack cb, final long timeout) {
        try {
            if (cb != null) {
                cb.preExec();
            }

            Object obj = null;
            if (!executor.isTerminated() && !executor.isShutdown()) {
                Future<Object> future = executor.submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        if (cb != null) {
                            return cb.inHandle();
                        }
                        return null;
                    }
                });

                obj = future.get(timeout, TimeUnit.MILLISECONDS);
                if (cb != null) {
                    cb.complete(obj);
                }
                return obj;
            }

            if (cb != null) {
                cb.complete(obj);
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return null;
    }

}
