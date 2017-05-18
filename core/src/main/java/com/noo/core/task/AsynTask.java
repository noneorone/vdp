package com.noo.core.task;

import com.noo.core.log.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


/**
 * 基础任务处理<br/>
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/3/29 21:15<br/>
 * @since 1.0
 */
public class AsynTask {

    private static AsynTask instance;

    private ExecutorService executor;

    public static final AsynTask getInstance() {
        if (instance == null) {
            synchronized (AsynTask.class) {
                if (instance == null) {
                    instance = new AsynTask();
                }
            }
        }
        return instance;
    }

    private AsynTask() {
        executor = Executors.newCachedThreadPool();
    }

    /**
     * 默认30秒任务超时返回
     *
     * @param tag
     * @param cb
     * @return 任务处理完成的对象{@link Object}
     */
    public Object exec(String tag, AsynCallBack cb) {
        return exec(tag, cb, 30000L);
    }

    /**
     * 执行任务
     *
     * @param cb      {@link AsynCallBack}
     * @param tag     {@link String}
     * @param timeout 超时时间
     * @return 结果对象
     */
    public Object exec(final String tag, final AsynCallBack cb, final long timeout) {
        Object obj = null;
        try {
            if (!executor.isTerminated() && !executor.isShutdown()) {
                Future<?> future = executor.submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        if (cb != null) {
                            Logger.d("task(" + tag + ") background.");
                            return cb.onBackgroundProcess(tag);
                        }
                        return null;
                    }
                });

                obj = future.get(timeout, TimeUnit.MILLISECONDS);
                if (cb != null) {
                    Logger.d("task(" + tag + ") success.");
                    cb.onResultSuccess(tag, obj);
                }
                return obj;
            }

            if (cb != null) {
                Logger.d("task(" + tag + ") error.");
                cb.onResultError(tag, obj);
            }
        } catch (Exception e) {
            if (cb != null) {
                Logger.d("task(" + tag + ") error.");
                cb.onResultError(tag, obj);
            }
            Logger.e(e);
        }

        return null;
    }
}
