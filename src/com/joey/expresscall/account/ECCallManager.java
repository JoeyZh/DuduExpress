package com.joey.expresscall.account;

import com.joey.expresscall.protocol.BackgroundHandler;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.expresscall.protocol.TaskBuilder;
import com.joey.expresscall.protocol.TaskBuilder.OnTaskListener;
import com.joey.expresscall.protocol.comm.ECCallInterface;

public class ECCallManager {

	private static ECCallManager callManager;
	private ECCallInterface callInterface;
	
	private ECCallManager(){
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
	
	public void init(String token)
	{
		callInterface = new ECCallInterface(token);
	}
	
	public <T> void getCallList(ResponseListener<T> listener){
		TaskBuilder task = new TaskBuilder("callList", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return callInterface.getBillList();
            }
        });
       
        BackgroundHandler.execute(task);
	}
	
	public <T> void getCallDetail(final String callId,ResponseListener<T> listener){
		TaskBuilder task = new TaskBuilder("callDetail", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return callInterface.getBillDetail(callId);
            }
        });
       
        BackgroundHandler.execute(task);
	}

//	public boolean UpLoadCallFile()

}
