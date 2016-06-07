package com.joey.expresscall.account;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.protocol.BackgroundHandler;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.expresscall.protocol.TaskBuilder;
import com.joey.expresscall.protocol.TaskBuilder.OnTaskListener;
import com.joey.expresscall.protocol.comm.ECCallInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ECCallManager {

    private static ECCallManager callManager;
    private ECCallInterface callInterface;

    private ECCallManager() {
    }

    public static ECCallManager getInstance() {
        if (callManager != null)
            return callManager;
        synchronized (ECAccountManager.class) {
            if (callManager == null)
                callManager = new ECCallManager();
            return callManager;
        }
    }

    public void init(String token) {
        callInterface = new ECCallInterface(token);
    }

    public <T> void getCallListDetail(final String callListId,
                                      ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("callListDetail", listener,
                new OnTaskListener() {
                    @Override
                    public String execute() {
                        return callInterface.getCallListDetail(callListId);
                    }
                });

        BackgroundHandler.execute(task);
    }

    public <T> void getCallDetail(final String callId,
                                  ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("callDetail", listener,
                new OnTaskListener() {
                    @Override
                    public String execute() {
                        return callInterface.getBillDetail(callId);
                    }
                });

        BackgroundHandler.execute(task);
    }

    public <T> void getCalls(final int pageNum, final int pageSize,
                             ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("callList", listener,
                new OnTaskListener() {
                    @Override
                    public String execute() {
                        return callInterface.getBillList();
                    }
                });

        BackgroundHandler.execute(task);
    }

    public <T> void getGroupCallList(final int pageNum, final int pageSize,
                                     ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("groupCallList", listener,
                new OnTaskListener() {
                    @Override
                    public String execute() {
                        return callInterface.getCallList(pageSize, pageNum);
                    }
                });

        BackgroundHandler.execute(task);
    }

    public <T> void call(final String fileId, final String fileType,
                         String extra, final List<String> phones,
                         ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("call", listener,
                new OnTaskListener() {
                    @Override
                    public String execute() {
                        return callInterface
                                .callArray(fileId, fileType, phones);
                    }
                });
        BackgroundHandler.execute(task);
    }

    public <T> void getFileList(ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("files", listener,
                new OnTaskListener() {
                    @Override
                    public String execute() {
                        return callInterface.getFiles();
                    }
                });
        BackgroundHandler.execute(task);
    }

    public <T> void deleteFile(final String fileIds, ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("deleteFiles", listener,
                new OnTaskListener() {
                    @Override
                    public String execute() {
                        return callInterface.deleteFile(fileIds);
                    }
                });
        BackgroundHandler.execute(task);
    }

    public <T> void updateFile(final String fileId, String extraName,
                               ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("updateFiles", listener,
                new OnTaskListener() {
                    @Override
                    public String execute() {
                        return callInterface.updateFile(fileId, extraName);

                    }
                });
        BackgroundHandler.execute(task);
    }

    public <T> void upLoadCallFile(String path, String phone, String type,
                                   String extraName, long duration, long fileSize, ResponseListener<T> listener) {
        listener.onStart();
        new Thread() {
            @Override
            public void run() {
                super.run();
                String result = callInterface.upLoadFile(path, phone, type, extraName, duration, fileSize);
                listener.onFinish();
                if (result != null) {
                    JSONObject object = JSON.parseObject(result);
                    int code = object.getInteger("code");
                    if(code == 0)
                    {
                        listener.onSuccess((T)object.get("data"));
                        return;
                    }
                    listener.onError(new RequestError(code));
                }
                listener.onError(new RequestError(-1));
            }
        }.start();
//        TaskBuilder task = new TaskBuilder("upLoadFiles", listener,
//                new OnTaskListener() {
//                    @Override
//                    public String execute() {
//                        return callInterface.upLoadFile(path, phone, type, extraName, duration, fileSize);
//                    }
//                });
//        BackgroundHandler.execute(task);
    }

    public <T> void downloadFile(String fileId, String type, String path, ResponseListener<T> listener) {
        listener.onStart();
        new Thread() {
            @Override
            public void run() {
                super.run();
                boolean result = callInterface.downloadFile(fileId, type, path);
                listener.onFinish();
                if (result) {
                    listener.onSuccess((T) (result + ""));
                    return;
                }
                listener.onError(new RequestError(-1));
            }
        }.start();
    }

}
