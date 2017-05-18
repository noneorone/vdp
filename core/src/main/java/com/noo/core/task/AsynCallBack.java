package com.noo.core.task;

/**
 * 任务流程回调<br/>
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/17 19:57<br/>
 * @since 1.0
 */
public interface AsynCallBack {
    /**
     * 任务执行中
     *
     * @param tag    任务标签
     * @param params 参数对象
     * @return 结果对象
     */
    Object onBackgroundProcess(String tag, Object... params);

    /**
     * 处理成功
     *
     * @param tag 任务标签
     * @param obj 结果对象
     */
    void onResultSuccess(String tag, Object obj);

    /**
     * 处理失败
     *
     * @param tag 任务标签
     * @param obj 结果对象
     */
    void onResultError(String tag, Object obj);
}


