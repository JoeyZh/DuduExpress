
package com.joey.expresscall;

import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.joey.expresscall.contacts.ECContactsFragment;
import com.joey.expresscall.file.ECFileListFragment;
import com.joey.expresscall.main.ECMainFragment;
import com.joey.expresscall.setting.ECSettingFragment;
import com.joey.general.BaseActivity;
import com.joey.general.tab.TabHost;

public class MainActivity extends BaseActivity {

    private TabHost tabHost;
    private  FrameLayout contentRoot;
    private LinearLayout mTabLayout;
    private ECMainFragment mainFragment;
    private ECSettingFragment settingFragment;
    private ECContactsFragment contactsFragment;
    private ECFileListFragment fileListFragment;

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
//        settingFragment = new ECSettingFragment();
        contactsFragment = new ECContactsFragment();
        fileListFragment = new ECFileListFragment();
        
        tabHost.addTab("主页",R.drawable.tabbar_main_selector,R.drawable.tabbar_home_selected, mainFragment);
        tabHost.addTab("联系人",R.drawable.tabbar_info_selector ,R.drawable.tabbar_profile_selected, contactsFragment);
        tabHost.addTab("文件",R.drawable.tabbar_files_selector ,R.drawable.tabbar_message_center_selected, fileListFragment);
        
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
