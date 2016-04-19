package com.joey.expresscall.account;

import android.os.Handler;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.protocol.ResponseListener;

public class ECAccountManager {
    
    static ECAccountManager accountManager;
    
    private ECAccountManager(){
       
    }

    public static ECAccountManager getInstance(){
        if(accountManager != null)
            return accountManager;
        synchronized(ECAccountManager.class){
            if(accountManager == null)
                accountManager = new ECAccountManager();
            return accountManager;
        }
        
    }

    /**
     *
     * @param username
     * @param password
     * @param responseListener
     */
    public void login(String username,String password,ResponseListener<JSONObject> listener){
        test(listener);
    }
    
    public void register(String username,String password,String veryCode,ResponseListener<JSONObject> listener){
        test(listener);
    }

    public void validateCode(final String username,final ResponseListener<JSONObject> listener){
        test(listener);
    }

    public void logOut(ResponseListener<JSONObject> listener){
        test(listener);
    }
    
    public void forgetPassword(String username,String resetPass,String veryCode,ResponseListener<JSONObject> listener){
        test(listener);
    }

    public void isAccountExist(String username,ResponseListener<JSONObject> listener){
        test(listener);
    }
    
    public void test(final ResponseListener<JSONObject> listener){
    	
    	Handler handler = new Handler();
    	handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				listener.onSuccess(new JSONObject());

			}
		},3000);
    }
}
