package com.bjaiyouyou.thismall.task;

/**
 * 异步任务的回调接口
 * @author JackB
 * @date 2016/6/1
 * @see TaskResult
 */
public interface TaskCallback {

    /**
     * 当异步任务执行完成，结果会通过这个方法回调给对象
     * 该方法运行于主线程
     * @param result
     */
    void onTaskFinished(TaskResult result);
}
