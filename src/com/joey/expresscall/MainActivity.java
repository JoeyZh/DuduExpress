
package com.joey.expresscall;

import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.joey.expresscall.contacts.ECContactsFragment;
import com.joey.expresscall.main.ECMainFragment;
import com.joey.expresscall.setting.ECSettingFragment;
import com.joey.general.BaseActivity;

public class MainActivity extends BaseActivity {

    private TabHost tabHost;
    private  FrameLayout contentRoot;
    private LinearLayout mTabLayout;
    private ECMainFragment mainFragment;
    private ECSettingFragment settingFragment;
    private ECContactsFragment contactsFragment;

    @Override
    public void initSettings() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void initUi() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_main);
        setTopBarVisiable(-1);
        contentRoot = (FrameLayout)findViewById(R.id.tab_content);
        mTabLayout = (LinearLayout)findViewById(R.id.tab_layout);
        tabHost = new TabHost(this,mTabLayout,contentRoot);
        
        mainFragment = new ECMainFragment();
        settingFragment = new ECSettingFragment();
        contactsFragment = new ECContactsFragment();
        
        tabHost.addTab("主页",R.drawable.tabbar_main_selector, mainFragment);
        tabHost.addTab("联系人",R.drawable.tabbar_info_selector , contactsFragment);
        tabHost.addTab("文件",R.drawable.tabbar_files_selector , settingFragment);
        
       tabHost.changeToIndex(0);
        
    }

    @Override
    public void saveSettings() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void freeMe() {
        // TODO Auto-generated method stub
        
    }


}
