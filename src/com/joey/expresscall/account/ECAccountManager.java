package com.joey.expresscall.account;

import android.os.Handler;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.protocol.BackgroundHandler;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.expresscall.protocol.TaskBuilder;
import com.joey.expresscall.protocol.TaskBuilder.OnTaskListener;
import com.joey.expresscall.protocol.comm.ECAccountInterface;

public class ECAccountManager {

    public static ECAccountManager accountManager;
    public ECAccountInterface mAccount;
    public boolean isLogin;

    private ECAccountManager() {
        mAccount = new ECAccountInterface();
    }

    public static ECAccountManager getInstance() {
        if (accountManager != null)
            return accountManager;
        synchronized (ECAccountManager.class) {
            if (accountManager == null)
                accountManager = new ECAccountManager();
            return accountManager;
        }

    }

    public void setToken(String token) {
        mAccount.setToken(token);
        ECCallManager.getInstance().init(token);
    }

    public boolean isLogin() {
        return isLogin;
    }

    /**
     * @param <T>
     * @param username
     * @param password
     * @param listener
     */
    public <T> void login(final String username, final String password, ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("login", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return mAccount.login(username, password);
            }
        });

        BackgroundHandler.execute(task);
    }

    public <T> void register(final String username, final String password, final String veryCode, ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("register", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return mAccount.register(username, password, veryCode);
            }
        });

        BackgroundHandler.execute(task);
    }

    public <T> void validateCode(final String username, final ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("validateCode", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return mAccount.validateCode(username);
            }
        });

        BackgroundHandler.execute(task);
    }

    public <T> void logOut(ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("logout", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return mAccount.logout();
            }
        });

        BackgroundHandler.execute(task);
    }

    public <T> void getUserInfo(ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("userInfo", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return mAccount.requireInfo();
            }
        });

        BackgroundHandler.execute(task);
    }

    public void forgetPassword(final String username, final String resetPass, final String veryCode, final ResponseListener<JSONObject> listener) {
        TaskBuilder task = new TaskBuilder("forgetPwd", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return mAccount.forgetPwd(username, resetPass, veryCode);
            }
        });

        BackgroundHandler.execute(task);
    }
    
    public <T> void modifyPwd(final String oldPwd, final String newPwd,final ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("modifyPwd", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return mAccount.modifyPwd(oldPwd, newPwd);
            }
        });

        BackgroundHandler.execute(task);
    }
    
    public <T> void modifyNickName( final String nickName,final String username,final ResponseListener<T> listener) {
        TaskBuilder task = new TaskBuilder("modifyNickName", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return mAccount.modifyInfo(nickName,username);
            }
        });

        BackgroundHandler.execute(task);
    }

    public void isAccountExist(final String username, ResponseListener<JSONObject> listener) {
        TaskBuilder task = new TaskBuilder("isExist", listener, new OnTaskListener() {
            @Override
            public String execute() {
                return mAccount.accountisExist(username);
            }
        });

        BackgroundHandler.execute(task);
    }

    public <T> void handleOperation(final ResponseListener<T> listener) {
        listener.onStart();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                listener.onSuccess((T) new JSONObject());

            }
        }, 3000);
    }
}
