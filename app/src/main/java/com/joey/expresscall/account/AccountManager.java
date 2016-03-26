package com.joey.expresscall.account;

public class AccountManager {
    
    static AccountManager accountManager;
    
    private AccountManager(){
       
    }

    public AccountManager getInstance(){
        if(accountManager != null)
            return accountManager;
        synchronized(AccountManager.class){
            if(accountManager == null)
                accountManager = new AccountManager();
            return accountManager;
        }
        
    }
    public void login(String username,String password){
        
    }
    
    public void regist(String username,String password,String veryCode){
        
    }
    
    public void logOut(){
        
    }
    
    public void forgetPassword(String username,String resetPass,String veryCode){
        
    }
}
