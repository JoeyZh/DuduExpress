
package com.joey.expresscall;

import android.transition.ChangeBounds;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.joey.expresscall.main.ECMainFragment;
import com.joey.general.BaseActivity;
import com.joey.general.BaseFragment;

public class MainActivity extends BaseActivity {

    private TabHost tabHost;
    private  FrameLayout contentRoot;
    private LinearLayout mTabLayout;
    private ECMainFragment mainFragment;

    @Override
    protected void initSettings() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void initUi() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_main);
        setTopBarVisiable(-1);
        contentRoot = (FrameLayout)findViewById(R.id.tab_content);
        mTabLayout = (LinearLayout)findViewById(R.id.tab_layout);
        tabHost = new TabHost(this,mTabLayout,contentRoot);
        
        mainFragment = new ECMainFragment();
        TabFragment fragment = new TabFragment(); 
        tabHost.addTab("主页",R.drawable.icon_back , mainFragment);
        tabHost.addTab("消息",R.drawable.icon_back , fragment);
//        tabHost.addTab("设置",R.drawable.icon_back , mainFragment);
        
       tabHost.changeToIndex(0);
        
    }

    @Override
    protected void saveSettings() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void freeMe() {
        // TODO Auto-generated method stub
        
    }


}
