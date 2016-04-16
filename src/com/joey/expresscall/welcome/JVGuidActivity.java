package com.jovision.xiaowei.welcome;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.jovision.AppConsts;
import com.jovision.view.GuideContoler;
import com.jovision.xiaowei.BaseActivity;
import com.jovision.xiaowei.R;
import com.jovision.xiaowei.login.JVLoginActivity;
import com.jovision.xiaowei.utils.MySharedPreference;

public class JVGuidActivity extends BaseActivity {

    @Override
    public void onHandler(int what, int arg1, int arg2, Object obj) {

    }

    @Override
    public void onNotify(int what, int arg1, int arg2, Object obj) {

    }

    @Override
    protected void initSettings() {

    }

    @Override
    protected void initUi() {
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
                MySharedPreference.putBoolean(AppConsts.FIRST_OPEN_APP, false);
                Intent startIntent = new Intent(JVGuidActivity.this, JVLoginActivity.class);
                JVGuidActivity.this.startActivity(startIntent);
                JVGuidActivity.this.finish();
            }
        });

    }

    @Override
    protected void saveSettings() {

    }

    @Override
    protected void freeMe() {

    }

}
