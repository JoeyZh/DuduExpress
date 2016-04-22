
package com.joey.expresscall;

import android.transition.ChangeBounds;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.joey.expresscall.main.ECMainFragment;
import com.joey.expresscall.setting.ECSettingFragment;
import com.joey.general.BaseActivity;
import com.joey.general.BaseFragment;

public class MainActivity extends BaseActivity {

    private TabHost tabHost;
    private  FrameLayout contentRoot;
    private LinearLayout mTabLayout;
    private ECMainFragment mainFragment;

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
        ECSettingFragment fragment = new ECSettingFragment(); 
        tabHost.addTab("主页",R.drawable.icon_back , mainFragment);
        tabHost.addTab("消息",R.drawable.icon_back , fragment);
//        tabHost.addTab("设置",R.drawable.icon_back , mainFragment);
        
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
