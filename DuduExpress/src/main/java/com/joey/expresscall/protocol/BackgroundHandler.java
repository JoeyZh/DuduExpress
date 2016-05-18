/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.joey.expresscall.protocol;

import android.os.HandlerThread;
import android.os.Looper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BackgroundHandler {

    // HandlerThread对象
    static HandlerThread sLooperThread;
    // 线程池ExecutorService对象
    static ExecutorService mThreadPool;
    // -----------------扩展 start---------------------
    // 任务名称列表
    static List<String> mTaskNameList = new ArrayList<String>();
    // 任务句柄列表
    static List<Future<JVTask>> mTaskFutureList = new ArrayList<Future<JVTask>>();
    // 任务句柄列表(任务名称, 事件)
    static HashMap<String, Future<JVTask>> mTaskFutureMap = new HashMap<String, Future<JVTask>>();
    // -----------------扩展 end-----------------------

    static {
        sLooperThread = new HandlerThread("BackgroundHandler", HandlerThread.MIN_PRIORITY);
        sLooperThread.start();
        mThreadPool = Executors.newCachedThreadPool();
    }

    private BackgroundHandler() {
    }

    public static void execute(Runnable runnable) {
        mThreadPool.execute(runnable);
    }

    public static Looper getLooper() {
        return sLooperThread.getLooper();
    }

    // ---------------------------------------------------
    // ## 扩展
    // ---------------------------------------------------

    /**
     * web接口调用
     *
     * @param task
     */
    public static void execute(TaskBuilder task) {
        if (!mTaskNameList.contains(task.getTaskName())) {
            // 将任务追加到任务列表中
            mTaskNameList.add(task.getTaskName());
            // 使用ExecutorService执行Callable类型的任务, 并将结果保存在future变量中
            Future<JVTask> future = mThreadPool.submit(task);
            mTaskFutureList.add(future);
            mTaskFutureMap.put(task.getTaskName(), future);
        } else {
            // 相同的任务未执行完成, 不再响应任务.
            return;
        }
    }

    /**
     * 从任务列表中移除任务
     *
     * @return
     */
    public static void removeTask(String name) {
        mTaskNameList.remove(name);
        mTaskFutureMap.remove(name);
    }

    /**
     * 取消任务
     */
    public static void cancelTask(String name) {
        Future<JVTask> future = mTaskFutureMap.get(name);
        future.cancel(true);
    }

    /**
     * 获取任务列表
     */
    public static List<Future<JVTask>> getTaskFutureList() {
        return mTaskFutureList;
    }
}
