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
    public void login(String username,String password,ResponseListener<T> responseListener){
        
    }
    
    public void register(String username,String password,String veryCode,ResponseListener<T> responseListener){
        
    }

    public void validateCode(final String username,final ResponseListener<T> listener){

    }

    public void logOut(ResponseListener<T> responseListener){
        
    }
    
    public void forgetPassword(String username,String resetPass,String veryCode,ResponseListener<T> responseListener){
        
    }

    public void isAccountExist(String username,ResponseListener<T> responseListener){

    }
}
