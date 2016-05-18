package com.joey.expresscall.welcome;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.joey.expresscall.R;

public class GuideContoler {
    /**
     * 当指示器选择矩形时，默认的宽
     **/
    private static final int INDICATOR_WIDTH_FOR_RECT = 40;
    /**
     * 当指示器选择矩形时，默认的高
     **/
    private static final int INDICATOR_HEIGHT_FOR_RECT = 5;
    /**
     * 当指示器选择圆形时，默认的宽
     **/
    private static final int INDICATOR_WIDTH_FOR_OVAL = 25;
    /**
     * 当指示器选择圆形时，默认的高
     **/
    private static final int INDICATOR_HEIGHT_FOR_OVAL = 25;
    private Context mContext;
    private ViewPager mViewPager;


    /**
     * ViewPager要显示的视图集合
     **/
    private List<View> mViews;
    private GuideViewPagerAdapter mPagerAdapter;
    private TextView guidTitleTV;
    private TextView guidTipTV;
    private int[] titleArray = {R.string.guid_title1, R.string.guid_title2, R.string.guid_title3, R.string.guid_title4};
    private int[] tipArray = {R.string.guid_tip1, R.string.guid_tip2, R.string.guid_tip3, R.string.guid_tip4};
    /**
     * 装指示器的LinearLayout
     **/
    private LinearLayout mIndicatorGroup;
    /**
     * 指示器的集合
     **/
    private View[] indicators;
    /**
     * 指示器的宽
     **/
    private int mIndicatorWidth;
    /**
     * 指示器的高
     **/
    private int mIndicatorHeight;

    private int mIndicatorBgResForSelected;
    private int mIndicatorBgResForUnselected;
    /**
     * 指示器默认为圆形
     **/
    private ShapeType mShapeType;
    /**
     * 监听ViewPager的页面变化
     **/
    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < indicators.length; i++) {
                if (i == arg0) {
                    indicators[i].setBackgroundResource(mIndicatorBgResForSelected);
                } else {
                    indicators[i].setBackgroundResource(mIndicatorBgResForUnselected);
                }
            }
            if (arg0 == (indicators.length - 1)) {
                mIndicatorGroup.setVisibility(View.GONE);
            } else {
                mIndicatorGroup.setVisibility(View.VISIBLE);
            }

            guidTitleTV.setText(titleArray[arg0]);
            guidTipTV.setText(tipArray[arg0]);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * @param context
     */
    public GuideContoler(Context context) {
        super();
        this.mContext = context;

    }

    /***
     * 设置数据,适用于前面页面是图片，最后一个页面是一个layout布局
     *
     * @param imgIds 图片的id数组
     * @param view
     */
    public void init(int[] imgIds, View view) {
        mViews = new ArrayList<View>();
        for (int i = 0; i < imgIds.length; i++) {
            ImageView iv = new ImageView(mContext);
            iv.setImageResource(imgIds[i]);
            iv.setScaleType(ScaleType.FIT_XY);
            mViews.add(iv);
        }
        mViews.add(view);
        set();
    }

    /**
     * 传入数据,当页面视图全是由layout生产的时候适用
     **/
    public void init(List<View> views) {
        mViews = views;
        set();
    }

    /**
     * 设置ViewPager和指示器
     **/
    private void set() {
        setViewPager();
        setIndicators();
    }

    /**
     * 设置ViewPager
     **/
    private void setViewPager() {
        mViewPager = (ViewPager) ((Activity) mContext).findViewById(R.id.viewPager_lib);
        mPagerAdapter = new GuideViewPagerAdapter(mViews);
        guidTitleTV = (TextView) ((Activity) mContext).findViewById(R.id.guid_title_textview);
        guidTipTV = (TextView) ((Activity) mContext).findViewById(R.id.guid_tip_textview);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnPageChangeListener(pageChangeListener);
    }

    /**
     * 设置指示器
     **/
    private void setIndicators() {
        setConfigure(mShapeType);
        mIndicatorGroup = (LinearLayout) ((Activity) mContext)
                .findViewById(R.id.indicatorGroup_lib);
        indicators = new View[mViews.size()];
        LayoutParams params = new LayoutParams(mIndicatorWidth, mIndicatorHeight);
        params.setMargins(0, 0, 15, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new View(mContext);
            if (i == 0) {
                indicators[i].setBackgroundResource(mIndicatorBgResForSelected);
            } else {
                indicators[i].setBackgroundResource(mIndicatorBgResForUnselected);
            }
            indicators[i].setLayoutParams(params);
            mIndicatorGroup.addView(indicators[i]);
        }
    }

    /**
     * 根据枚举参数，设置指示器的背景和宽高
     **/
    private void setConfigure(ShapeType shapeType) {
        if (shapeType != null) {
            if (shapeType == ShapeType.OVAL) {
                mIndicatorWidth = mIndicatorWidth == 0 ? INDICATOR_WIDTH_FOR_OVAL : mIndicatorWidth;
                mIndicatorHeight = mIndicatorHeight == 0 ? INDICATOR_HEIGHT_FOR_OVAL
                        : mIndicatorHeight;
                mIndicatorBgResForSelected = R.drawable.icon_dot_selected;
                mIndicatorBgResForUnselected = R.drawable.icon_dot_default;
            } else if (shapeType == ShapeType.RECT) {
                mIndicatorWidth = mIndicatorWidth == 0 ? INDICATOR_WIDTH_FOR_RECT : mIndicatorWidth;
                mIndicatorHeight = mIndicatorHeight == 0 ? INDICATOR_HEIGHT_FOR_RECT
                        : mIndicatorHeight;
                mIndicatorBgResForSelected = R.drawable.shape_indicator_selected_rect;
                mIndicatorBgResForUnselected = R.drawable.shape_indicator_unselected_rect;
            }
        } else {
            mIndicatorWidth = mIndicatorWidth == 0 ? INDICATOR_WIDTH_FOR_OVAL : mIndicatorWidth;
            mIndicatorHeight = mIndicatorHeight == 0 ? INDICATOR_HEIGHT_FOR_OVAL : mIndicatorHeight;
            mIndicatorBgResForSelected = R.drawable.icon_dot_selected;
            mIndicatorBgResForUnselected = R.drawable.icon_dot_default;
        }
    }

    public void setmIndicatorWidth(int mIndicatorWidth) {
        this.mIndicatorWidth = mIndicatorWidth;
    }

    public void setmIndicatorHeight(int mIndicatorHeight) {
        this.mIndicatorHeight = mIndicatorHeight;
    }

    public void setmShapeType(ShapeType mShapeType) {
        this.mShapeType = mShapeType;
    }

    /***
     * 定义一个枚举(用来指定指示器的形状)
     *
     * @author zhy
     */
    public enum ShapeType {
        RECT, OVAL
    }

}
