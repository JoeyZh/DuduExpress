
package com.joey.expresscall;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.joey.general.BaseActivity;

public class MainActivity extends BaseActivity {

    private TabHost tabHost;
    private  FrameLayout contentRoot;
    private LinearLayout mTabLayout;

    @Override
    protected void initSettings() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void initUi() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_main);
        contentRoot = (FrameLayout)findViewById(R.id.tab_content);
        mTabLayout = (LinearLayout)findViewById(R.id.tab_layout);
        tabHost = new TabHost(this,mTabLayout,contentRoot);
//        tabHost.addTab("1",R.drawable.ic_launcher,);
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
