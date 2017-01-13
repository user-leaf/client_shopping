package com.bjaiyouyou.thismall.task;

/**
 * 异步任务通用的数据结果对象：任何异步任务都可以使用
 * @author kanbin
 * @date 2016/6/1
 */
public class TaskResult {

    /**
     * 异步任务执行的状态，无网络、成功等
     * 0: 成功
     * 1：无数据
     * 2：数据错误
     */
    public int mCode;

    /**
     * 异步任务执行的结果数据
     */
    public Object mData;


}
