package com.joey.general;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joey.expresscall.R;
import com.joey.general.utils.MobileUtil;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.ToastUtil;
import com.joey.general.views.TopBarLayout;

import java.util.HashMap;


/**
 * 抽象的活动基类，所有活动都应该继承这个类，并实现其抽象方法和接口
 *
 * @author neo
 */
public abstract class BaseActivity extends Activity {

    public HashMap<String, String> statusHashMap;
    public int mScreenWidth;
    public int mScreenHeight;
    // 设备无SD卡提示
    public AlertDialog devNoSDCardDialog;
    public MediaPlayer mediaPlayer;
    public Handler handler = new Handler();
    // 日志标记
    protected String TAG = getClass().toString();
    protected Dialog proDialog;
    private long exitTime = 0;
    private LayoutInflater mLayoutInflater;
   
    // 报警提示方式:震动,声音
    private Vibrator mVibrator;
   
    // ----------------------------------------------------------
    // Title bar
    // 1.定义一个根布局,
    // 2.如果界面有标题栏,把标题栏添加到根布局中,
    // 3.把界面的布局添加到根布局中.
    // ----------------------------------------------------------
    private View mBaseLayoutView;
    /**
     * root view
     */
    private LinearLayout mContentLayoutView;
    /**
     * 标题
     */
    private View mTopBarView;

    /**
     * 初始化设置，不要在这里写费时的操作
     */
    protected abstract void initSettings();

    /**
     * 初始化界面，不要在这里写费时的操作
     */
    protected abstract void initUi();

    /**
     * 保存设置，不要在这里写费时的操作
     */
    protected abstract void saveSettings();

    /**
     * 释放资源、解锁、删除不用的对象，不要在这里写费时的操作
     */
    protected abstract void freeMe();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MyActivityManager.getActivityManager().pushActivity(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        mScreenHeight = dm.heightPixels;
        
        mLayoutInflater = LayoutInflater.from(this);
        statusHashMap = ((MainApplication) getApplicationContext())
                .getStatusHashMap();     
        mVibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        
        initSuperView();
        initSettings();
        initUi();
    } 

    @Override
    protected void onDestroy() {
        // 报警(震动/铃声)
        mVibrator.cancel();
       

        // 关闭进度条Dialog
        dismissDialog();
      

        try {
            if (null != mediaPlayer) {
                mediaPlayer.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        freeMe();
        // 当前Activity出栈
        MyActivityManager.getActivityManager().popActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        saveSettings();
        super.onPause();
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        // 取消Toast显示
        ToastUtil.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 判断是否有sd卡
     *
     * @param minSize 最小容量
     * @param alert   是否弹提示
     * @return
     */
    public boolean hasSDCard(int minSize, boolean alert) {
        boolean canSave = true;
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            if (alert) {
                ToastUtil.show(this, R.string.sdcard_out_memery);
            }
            canSave = false;
        } else {
            if (MobileUtil.getSDFreeSize() < minSize) {
                if (alert) {
                    ToastUtil.show(this, R.string.sdcard_notenough);
                }
                canSave = false;
            }
        }
        return canSave;
    }

    /**
     * 弹dialog
     *
     * @param id
     */
    public void createDialog(final int id, final boolean cancel) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null != BaseActivity.this && !BaseActivity.this.isFinishing()) {
                        if (null == proDialog) {
                            // proDialog = new ProgressDialog(BaseActivity.this,
                            // ProgressDialog.THEME_HOLO_LIGHT);
                            proDialog = createLoadingDialog(BaseActivity.this,
                                    BaseActivity.this.getString(id));
                        }
                        if (null != proDialog) {
                            if (null != BaseActivity.this
                                    && !BaseActivity.this.isFinishing()) {
                                // proDialog.setMessage(BaseActivity.this.getString(id));
                                proDialog = createLoadingDialog(BaseActivity.this,
                                        BaseActivity.this.getString(id));
                                proDialog.setCancelable(cancel);
                                proDialog.show();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 弹dialog
     *
     * @param msg
     */
    public void createDialog(String msg, final boolean cancel) {

        if (null == msg || "".equalsIgnoreCase(msg)) {
            msg = getResources().getString(R.string.waiting);
        }

        if (null != proDialog && proDialog.isShowing()) {
            return;
        }

        final String dlgmsg = msg;
        handler.post(new Runnable() {
            @Override
            public void run() {

                try {
                    if (null != BaseActivity.this && !BaseActivity.this.isFinishing()) {
                        if (null == proDialog) {
                            // proDialog = new ProgressDialog(BaseActivity.this,
                            // ProgressDialog.THEME_TRADITIONAL);
                            proDialog = createLoadingDialog(BaseActivity.this, dlgmsg);
                        }
                        if (null != proDialog) {
                            if (null != BaseActivity.this
                                    && !BaseActivity.this.isFinishing()) {
                                proDialog = createLoadingDialog(BaseActivity.this, dlgmsg);
                                // proDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.xiaowei));
                                // proDialog.setMessage(dlgmsg);
                                proDialog.setCancelable(cancel);
                                if (!BaseActivity.this.isFinishing()) {
                                    proDialog.show();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param msg
     * @return
     */
    public Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        proDialog = new Dialog(context, R.style.my_loading_dialog);// 创建自定义样式dialog

        proDialog.setCancelable(false);// 不可以用“返回键”取消
        proDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return proDialog;

    }

    /**
     * 关闭dialog
     */
    public void dismissDialog() {
        /**
         * 用这个!this.isFinishing()做为条件,容易出现窗口泄露<br/>
         * 例:创建了Dialog,在Activity结束跳转的时候,Dialog无法关闭.
         */
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (null != proDialog && proDialog.isShowing()) {
                    proDialog.dismiss();
                    proDialog = null;
                }
            }
        });
    }
    
    /**
     * 点击两次返回退出程序
     */
    public void exitTip() {
        if ((System.currentTimeMillis() - exitTime) > 2 * 1000) {
            ToastUtil.show(this, R.string.pressback_exit);
            exitTime = System.currentTimeMillis();
        } else {
            // 注销
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    /**
     * 初始化操作
     */
    private void initSuperView() {
        mBaseLayoutView = mLayoutInflater.inflate(
                R.layout.activity_fragment_base_view, null);
        mContentLayoutView = (LinearLayout) mBaseLayoutView
                .findViewById(R.id.xiaowei_root_view);

        // 往根View中添加标题栏
        if (getTitleLayout() != -1) {
            mTopBarView = mLayoutInflater.inflate(getTitleLayout(), null);
            // 这里给根视图添加了一个共通的属性，默认方式是这样的，topBarLayout也可以自定义
            ((TopBarLayout) mTopBarView).setTopBar(R.drawable.icon_back, -1,
                    "", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            BaseActivity.this.finish();
                        }
                    });
            mContentLayoutView.addView(mTopBarView,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * 子类重载该方法自定义标题布局文件<br />
     * 子类重载该方法返回-1,不显示标题
     *
     * @return
     */
    protected int getTitleLayout() {
        return R.layout.common_titlebar_view;
    }

    /**
     * 将布局添加到根View中
     */
    @Override
    public void setContentView(int layoutResID) {
        View view = mLayoutInflater.inflate(layoutResID, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        for (int i = 1; i < mContentLayoutView.getChildCount(); i++) {
            mContentLayoutView.removeViewAt(i);
        }
        mContentLayoutView.addView(view);
        super.setContentView(mBaseLayoutView);
    }

    @Override
    public void setContentView(View view) {
        view.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        for (int i = 1; i < mContentLayoutView.getChildCount(); i++) {
            mContentLayoutView.removeViewAt(i);
        }
        mContentLayoutView.addView(view);
        super.setContentView(mBaseLayoutView);
    }

    /**
     * 获取TopBar
     *
     * @return
     */
    protected TopBarLayout getTopBarView() {
        if (mTopBarView instanceof TopBarLayout) {
            return (TopBarLayout) mTopBarView;
        }
        return null;
    }

    /**
     * @param visiable
     */
    protected void setTopBarVisiable(int visiable) {
        if (mTopBarView == null) {
            return;
        }
        if (visiable == View.VISIBLE) {
            showTitleView();
            return;
        }
        hideTitleView();
    }

    /**
     * 判断TopBar是否显示
     *
     * @return
     */
    protected final boolean isTitleShowing() {
        MyLog.d(TAG, "isTitleShowing hasTitle :"
                + (mTopBarView != null ? true : false));
        if (mTopBarView == null) {
            return false;
        }

        return mTopBarView.getVisibility() == View.VISIBLE;
    }

    /**
     * 隐藏TopBar
     */
    private final void hideTitleView() {
        MyLog.d(TAG, "hideTitleView hasTitle :"
                + (mTopBarView != null ? true : false));
        if (mTopBarView != null) {
            mTopBarView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示TopBar
     */
    private final void showTitleView() {
        MyLog.d(TAG, "showTitleView hasTitle :"
                + (mTopBarView != null ? true : false));
        if (mTopBarView != null) {
            mTopBarView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = activity.getResources()
                        .getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 全屏方法
     *
     * @param enable
     */
    public void fullScreen(boolean enable) {
        if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    // ----------------------------------------------------------
    // ## 登录回调
    // ----------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
