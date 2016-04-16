package com.joey.expresscall.account;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.protocol.ResponseListener;

public class AccountManager {
    
    static AccountManager accountManager;
    
    private AccountManager(){
       
    }

    public static AccountManager getInstance(){
        if(accountManager != null)
            return accountManager;
        synchronized(AccountManager.class){
            if(accountManager == null)
                accountManager = new AccountManager();
            return accountManager;
        }
        
    }

    /**
     *
     * @param username
     * @param password
     * @param responseListener
     */
    public void login(String username,String password,ResponseListener<JSONObject> responseListener){
        
    }
    
    public void register(String username,String password,String veryCode,ResponseListener<JSONObject> responseListener){
        
    }

    public void validateCode(final String username,final ResponseListener<JSONObject> listener){

    }

    public void logOut(ResponseListener<JSONObject> responseListener){
        
    }
    
    public void forgetPassword(String username,String resetPass,String veryCode,ResponseListener<JSONObject> responseListener){
        
    }

    public void isAccountExist(String username,ResponseListener<JSONObject> responseListener){

    }
}
