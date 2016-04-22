package com.joey.general;

public interface OnCreateInterface {
	
	 /**
     * 初始化设置，不要在这里写费时的操作
     */
    void initSettings();

    /**
     * 初始化界面，不要在这里写费时的操作
     */
    void initUi();

    /**
     * 保存设置，不要在这里写费时的操作
     */
    void saveSettings();

    /**
     * 释放资源、解锁、删除不用的对象，不要在这里写费时的操作
     */
    void freeMe();

}
