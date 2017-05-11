package com.bjaiyouyou.thismall.model;

/**
 *
 * @author Alice
 *Creare 2016/6/7 17:19
 * 任务数据
 *
 */
public class MyTask {
    private  boolean isFinsh;
    private  String name;

    public MyTask(String name, boolean isFinsh) {
        this.name = name;
        this.isFinsh = isFinsh;
    }

    public boolean isFinsh() {
        return isFinsh;
    }

    public void setFinsh(boolean finsh) {
        isFinsh = finsh;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
