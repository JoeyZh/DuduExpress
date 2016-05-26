package com.joey.expresscall.account;

import com.joey.expresscall.protocol.BackgroundHandler;
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

    public <T> void getCallListDetail(final String callListId,ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("callListDetail", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return callInterface.getCallListDetail(callListId);
            }
        });

        BackgroundHandler.execute(task);
    }

    public <T> void getCallDetail(final String callId, ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("callDetail", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return callInterface.getBillDetail(callId);
            }
        });

        BackgroundHandler.execute(task);
    }

    public <T> void getCalls(final int pageNum, final int pageSize, ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("callList", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return callInterface.getBillList();
            }
        });

        BackgroundHandler.execute(task);
    }

    public <T> void getGroupCallList(final int pageNum, final int pageSize, ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("groupCallList", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return callInterface.getCallList(pageSize, pageNum);
            }
        });

        BackgroundHandler.execute(task);
    }

    public <T> void call(final String fileId, final String fileType, String extra, final List<String> phones, ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("call", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return callInterface.callArray(fileId, fileType, phones);
            }
        });
        BackgroundHandler.execute(task);
    }

    public <T> void getFileList(ResponseListener<T> listener){
        TaskBuilder task = new TaskBuilder("files", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return callInterface.getFiles();
            }
        });
        BackgroundHandler.execute(task);
    }

    public <T> void deleteFile(final String fileId){

    }

    public <T> void upDateFile(final String fileId,String extraName,ResponseListener<T> listener){
        TaskBuilder task = new TaskBuilder("files", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return callInterface.getFiles();
            }
        });
        BackgroundHandler.execute(task);
    }
//	public boolean UpLoadCallFile()

}
