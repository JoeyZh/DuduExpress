package com.joey.general.views;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joey.duduexpress.R;
/**
 * @author Joey 抽象出来的统一的topBar布局部分
 */
public class TopBarLayout extends RelativeLayout {

    /**
     * 点击标题栏后要执行的后续动作标记
     */
    // 打开网络设置
    public static final int SUB_TITLEBAR_OPEN_NET = 1;
    // 跳转到登录
    public static final int SUB_TITLEBAR_JUMP_LOGIN = 2;
    // 收到设备分享的消息
    public static final int SUB_TITLEBAR_GET_SHARE = 3;

    private Context mContext;
    private MarqueeTextView title;
    private ImageView mLeftImage;
    private ImageView mRightImage;
    private RelativeLayout alarmNetLayout;
    private FrameLayout mLeftFlyt, mRightFlyt;
    private RelativeLayout topBarContentLayout;
    private TextView alarmText;
    private ImageView mRightArrow;
    private TextView mRightText;
    private int mSubTitleBarClickEvent = -1;
    private AlarmTitleBarOnClickListener mAlarmTitleBarListener;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.alarmnet:
                    // 有自定义监听的场合,走自己定义的处理逻辑
                    if (mAlarmTitleBarListener != null) {
                        mAlarmTitleBarListener.alarmTitleBarOnClick(mSubTitleBarClickEvent, v);
                        break;
                    }
                    // 没有自定义监听的场合,走下面的处理逻辑
                    switch (mSubTitleBarClickEvent) {
                        case SUB_TITLEBAR_OPEN_NET:// 启动网络设置
                            Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
                            // 如果使用context.startActivity(),下面这句必须添加
                            wifiSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(wifiSettingsIntent);
                            break;
                        case SUB_TITLEBAR_JUMP_LOGIN:// 登录
                           
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public TopBarLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    public TopBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.topbar_layout, this);
        title = (MarqueeTextView) findViewById(R.id.center_title);
        mLeftFlyt = (FrameLayout) findViewById(R.id.left_btn);
        mRightFlyt = (FrameLayout) findViewById(R.id.right_btn);
        mLeftImage = (ImageView) findViewById(R.id.iv_left);
        mRightImage = (ImageView) findViewById(R.id.iv_right);
        alarmNetLayout = (RelativeLayout) findViewById(R.id.alarmnet);
        alarmText = (TextView) findViewById(R.id.warning_textview);
        topBarContentLayout = (RelativeLayout) findViewById(R.id.topbar_content_layout);
        mRightArrow = (ImageView) findViewById(R.id.iv_right_arrow);
        mRightText = (TextView) findViewById(R.id.tv_right);
    }

    /**
     * 设置TopBarView标题
     *
     * @param text
     */
    public void setTitle(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            title.setVisibility(View.GONE);
            return;
        }
        title.setText(text);
        title.setVisibility(View.VISIBLE);
    }

    /**
     * 设置TopBarView标题
     *
     * @param resid
     */
    public void setTitle(int resid) {
        if (resid < 0) {
            title.setVisibility(View.GONE);
            return;
        }
        title.setText(resid);
        title.setVisibility(View.VISIBLE);
    }

    /**
     * 设置TopBarView 左侧按钮的图标
     *
     * @param resId
     */
    public void setLeftButtonRes(int resId) {
        if (resId < 0) {
            mLeftFlyt.setVisibility(View.INVISIBLE);
            return;
        }
        mLeftImage.setImageResource(resId);
        mLeftFlyt.setVisibility(View.VISIBLE);
    }

    /**
     * 设置TopBarView 右边按钮的图标
     *
     * @param resId
     */
    public void setRightButtonRes(int resId) {
        if (resId <= 0) {
            mRightFlyt.setVisibility(View.INVISIBLE);
            return;
        }
        mRightFlyt.setVisibility(View.VISIBLE);
        mRightImage.setImageResource(resId);
        mRightImage.setVisibility(View.VISIBLE);
        mRightText.setVisibility(View.GONE);
    }

    /**
     * 设置右侧的文字
     *
     * @param resId
     */
    public void setRightTextRes(int resId) {
        if (resId < 0) {
            mRightFlyt.setVisibility(View.INVISIBLE);
            return;
        }
        mRightFlyt.setVisibility(View.VISIBLE);
        mRightText.setText(resId);
        mRightText.setVisibility(View.VISIBLE);
        mRightImage.setVisibility(View.GONE);
    }

    /**
     * 显示提示布局
     *
     * @param resId      显示内容的资源ID
     * @param clickEvent 点击的后续工作 (-1表示没有后续工作)
     * @param l          自定义的监听
     */
    public void showAlarmNet(int resId, int clickEvent, AlarmTitleBarOnClickListener l) {
        if (resId < 0) {
            alarmNetLayout.setVisibility(View.GONE);
            return;
        }
        mAlarmTitleBarListener = l;
        alarmText.setText(resId);
        if (alarmNetLayout.getVisibility() != View.VISIBLE) {
            alarmNetLayout.setVisibility(View.VISIBLE);
        }

        // 右侧箭头和点击事件
        setAlarmTitleBarClickEvent(clickEvent);
    }

    /**
     * 显示提示布局
     *
     * @param str        显示内容
     * @param clickEvent 点击的后续工作 (-1表示没有后续工作)
     * @param l          自定义监听
     */
    public void showAlarmNet(String str, int clickEvent, AlarmTitleBarOnClickListener l) {
        if (!TextUtils.isEmpty(str)) {
            alarmText.setText(str);
            if (alarmNetLayout.getVisibility() != View.VISIBLE) {
                alarmNetLayout.setVisibility(View.VISIBLE);
            }
        } else {
            alarmNetLayout.setVisibility(View.GONE);
        }
        mAlarmTitleBarListener = l;

        // 右侧箭头和点击事件
        setAlarmTitleBarClickEvent(clickEvent);
    }

    /**
     * 隐藏提示布局
     */
    public void dismissAlarmNet() {
        if (alarmNetLayout.getVisibility() == View.VISIBLE) {
            alarmNetLayout.setVisibility(GONE);
        }
    }

    /**
     * 设置纯图片的按钮TopBarView
     *
     * @param leftResid  左侧图标资源ID
     * @param rightResid 右侧图标的资源ID
     * @param titleResid 中间标题的资源ID
     * @param listener   监听
     */
    public void setTopBar(int leftResid, int rightResid,
                          int titleResid, View.OnClickListener listener) {
        // 左侧
        setLeftButtonRes(leftResid);
        mLeftFlyt.setOnClickListener(listener);
        // 右侧
        setRightButtonRes(rightResid);
        mRightFlyt.setOnClickListener(listener);
        // 标题
        setTitle(titleResid);
        title.setOnClickListener(listener);

        // 异常信息状态栏
        alarmNetLayout.setOnClickListener(mOnClickListener);
    }

    /**
     * 设置纯图片的按钮TopBarView
     *
     * @param leftResid  左侧图标资源ID
     * @param rightResid 右侧图标的资源ID
     * @param titleRes   中间标题的资源ID
     * @param listener   监听
     */
    public void setTopBar(int leftResid, int rightResid,
                          String titleRes, View.OnClickListener listener) {
        // 左侧
        setLeftButtonRes(leftResid);
        mLeftFlyt.setOnClickListener(listener);
        // 右侧
        setRightButtonRes(rightResid);
        mRightFlyt.setOnClickListener(listener);

        // 标题
        setTitle(titleRes);
        title.setOnClickListener(listener);

        // 异常信息状态栏
        alarmNetLayout.setOnClickListener(mOnClickListener);
    }

    public void setTopBarBackgroundResource(int resid) {
        topBarContentLayout.setBackgroundResource(resid);
    }

    // --------------------------------------------------------
    // # 异常信息状态栏
    // --------------------------------------------------------
    private void setAlarmTitleBarClickEvent(int clickEvent) {
        if (clickEvent > 0) {
            mSubTitleBarClickEvent = clickEvent;
            // 右侧箭头
            mRightArrow.setVisibility(View.VISIBLE);
        } else {
            mSubTitleBarClickEvent = -1;
            mRightArrow.setVisibility(View.GONE);
        }
    }

    public interface AlarmTitleBarOnClickListener {
        void alarmTitleBarOnClick(int clickEvent, View v);
    }
}
