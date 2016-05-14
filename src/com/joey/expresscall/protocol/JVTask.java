package com.joey.expresscall.protocol;

/**
 * 每一个web接口的调用都是一项任务
 */
public class JVTask {
    // 任务名称
    private String mTaskName;
    // 任务开始时间
    private String mTaskStartTime;
    // 任务结束时间
    private String mTaskEndTime;
    // 任务耗时
    private String mTaskTime;
    // 任务执行结果
    private String mResult;

    /**
     * @return the taskName
     */
    public String getTaskName() {
        return mTaskName;
    }

    /**
     * @param taskName the taskName to set
     */
    public void setTaskName(String taskName) {
        mTaskName = taskName;
    }

    /**
     * @return the taskStartTime
     */
    public String getTaskStartTime() {
        return mTaskStartTime;
    }

    /**
     * @param taskStartTime the taskStartTime to set
     */
    public void setTaskStartTime(String taskStartTime) {
        mTaskStartTime = taskStartTime;
    }

    /**
     * @return the taskEndTime
     */
    public String getTaskEndTime() {
        return mTaskEndTime;
    }

    /**
     * @param taskEndTime the taskEndTime to set
     */
    public void setTaskEndTime(String taskEndTime) {
        mTaskEndTime = taskEndTime;
    }

    /**
     * @return the taskTime
     */
    public String getTaskTime() {
        return mTaskTime;
    }

    /**
     * @param taskTime the taskTime to set
     */
    public void setTaskTime(String taskTime) {
        mTaskTime = taskTime;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return mResult;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        mResult = result;
    }

}
