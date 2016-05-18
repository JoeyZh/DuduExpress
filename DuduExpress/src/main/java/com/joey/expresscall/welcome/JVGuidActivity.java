package com.joey.expresscall.welcome;

import com.joey.expresscall.R;
import com.joey.expresscall.login.JVLoginActivity;
import com.joey.general.BaseActivity;
import com.joey.general.utils.MySharedPreference;
import com.joey.general.utils.MySharedPreferencesConsts;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;


public class JVGuidActivity extends BaseActivity {


    @Override
	public void initSettings() {

    }

    @Override
	public void initUi() {
        setContentView(R.layout.guid_layout);
        setTopBarVisiable(-1);
        initViewPager();
    }

    /**
     * 使用写好的库初始化引导页面
     **/
    private void initViewPager() {
        GuideContoler contoler = new GuideContoler(this);
        // contoler.setmShapeType(ShapeType.RECT);//设置指示器的形状为矩形，默认是圆形
        int[] imgIds = {
                R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3
        };
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.guid_last_page, null);
        contoler.init(imgIds, view);
        view.findViewById(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySharedPreference.getInstance().putBoolean(MySharedPreferencesConsts.FIRST_OPEN_APP, false);
                Intent startIntent = new Intent(JVGuidActivity.this, JVLoginActivity.class);
                JVGuidActivity.this.startActivity(startIntent);
                JVGuidActivity.this.finish();
            }
        });

    }

    @Override
	public void saveSettings() {

    }

    @Override
	public void freeMe() {

    }

}
