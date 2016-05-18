package com.joey.expresscall.storage;

import java.io.Serializable;

public class JVAccount implements Serializable {

    private static final long serialVersionUID = 1;

    private String username;
    private String password;
    private String logoPath;
    private boolean isAutoLogin;

    public JVAccount(String username, String password, String logoPath) {
        this.username = username;
        this.password = password;
        this.logoPath = logoPath;
        this.isAutoLogin = true;
    }

    public String getLogoPath() {
        return this.logoPath;
    }

    public void setLogoPath(String path) {
        this.logoPath = path;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getAutoLogin() {
        return isAutoLogin;
    }

    public void setAutoLogin(boolean flag) {
        this.isAutoLogin = flag;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return String.format("{username:%s,password:%s,isAutoLogin:%s,logoPath:%s}", this.username,
                this.password, "" + isAutoLogin, this.logoPath);
    }

}
