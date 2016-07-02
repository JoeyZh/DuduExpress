package com.joey.expresscall.common.widget;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.joey.expresscall.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * Created by Joey on 16/7/2.
 */
public class BannerViewPager extends FrameLayout implements ViewPager.OnPageChangeListener{

    private ViewPager viewPager;
    private LinearLayout indicatorLayout;
    private List<View> bannerViews;
    private View indicators[] ;

    private BannerPagerAdapter adapter;
    private final int INDICATOR_WIDTH = 15;
    private final int INDICATOR_PADDING = 5;

    private Handler handler;

    private ImageCycleViewListener mImageCycleViewListener;

//    标记是否滚动
    boolean isScrolling;
    private int currentPosition;
    boolean isCycle = true;

    private long timeDelayed = 3000;

    private Runnable autoFlingRunnable = new Runnable() {
        @Override
        public void run() {
            if(!isCycle)
                stop();
            nextPager();
            handler.postDelayed(autoFlingRunnable,timeDelayed);
        }
    };

    public BannerViewPager(Context context) {
        super(context);
        init(context);

    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public BannerViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        View.inflate(context, R.layout.banner_layout,this);
        viewPager = (ViewPager)findViewById(R.id.banner_view_pager);
        indicatorLayout = (LinearLayout)findViewById(R.id.banner_indicator_root);
        handler = new Handler();
    }

    public void setBanner(int size,int width,int height){

        bannerViews = new ArrayList<>();
        indicators = new View[size];
        for(int i = 0;i<size;i++){
            FrameLayout view = new FrameLayout(getContext());
            view.setLayoutParams(new FrameLayout.LayoutParams(width,height));
            createBanner(view);
            indicators[i] = createIndicator();
        }

//        set pager adapter
      setViewPager();

    }

    public void setBanner(int size,int layoutRes){
        bannerViews = new ArrayList<>();
        indicators = new View[size];
        for(int i = 0;i<size;i++){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(layoutRes,null);
            createBanner(view);
            indicators[i] = createIndicator();
        }

//        set pager adapter
       setViewPager();

    }

    public void setBanner(int size,View view){
        bannerViews = new ArrayList<>();
        indicators = new View[size];
        for(int i = 0;i<size;i++){
            createBanner(view);
            indicators[i] = createIndicator();

        }

        setViewPager();

    }

    public void nextPager(){
        currentPosition ++;
        currentPosition %= bannerViews.size();
        viewPager.setCurrentItem(currentPosition,true);
        setIndicator(currentPosition);
    }


    public void start(long timeDelayed){
        this.timeDelayed = timeDelayed;
        isCycle = true;
        handler.postDelayed(autoFlingRunnable,timeDelayed);
    }

    public void stop(){
        isCycle = false;
        handler.removeCallbacks(autoFlingRunnable);
    }
    private void setViewPager(){
        adapter = new BannerPagerAdapter(bannerViews);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
        setSelected(0);
    }

    /**
     *
     * @param view
     */
    private void createBanner(View view){
        if(view.findViewById(R.id.banner_img) == null){
            ImageView child = new ImageView(getContext());
            child.setId(R.id.banner_img);
            ((ViewGroup)view).addView(child,-1,-1);
        }
        bannerViews.add(view);

    }

    /**
     *       initial indicator root layout
     * @return indicator
     */
    private View createIndicator(){

        View indicator = new View(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(INDICATOR_WIDTH,INDICATOR_WIDTH,1);
        indicator.setLayoutParams(params);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.setMargins(INDICATOR_PADDING,0,0,0);
        indicator.setId(R.id.banner_indicator);
        indicator.setBackgroundResource(R.drawable.banner_indicator_circle_normal);
        indicator.setPadding(INDICATOR_PADDING,INDICATOR_PADDING,INDICATOR_PADDING,INDICATOR_PADDING);
        indicatorLayout.addView(indicator,params);
        return indicator;
    }

    public boolean isScrolling() {
        return isScrolling;
    }

    public void setScrolling(boolean scrolling) {
        isScrolling = scrolling;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setImageUlr(String url, int position){

        if(bannerViews == null){
            return;
        }
        if(position >= bannerViews.size()){
            return;
        }
        View view = bannerViews.get(position);
        view.setTag(url);
        adapter.notifyDataSetChanged();
    }

    public void setImageResource(int resId,int position){
        if(bannerViews == null){
            return;
        }
        if(position >= bannerViews.size()){
            return;
        }
        View view = bannerViews.get(position);
        view.setTag(resId);
        adapter.notifyDataSetChanged();
    }

    public void setSelected(int position){
        viewPager.setCurrentItem(position);
        setIndicator(position);
    }

    /**
     * 设置指示器
     *
     * @param selectedPosition
     *            默认指示器位置
     */
    private void setIndicator(int selectedPosition) {
        for (int i = 0; i < indicators.length; i++) {
            if(i == selectedPosition)
                continue;
            indicators[i]
                    .setBackgroundResource(R.drawable.banner_indicator_circle_normal);
        }
        indicators[selectedPosition]
                .setBackgroundResource(R.drawable.banner_indicator_circle_highlight);
    }

    @Override
    public void onPageScrolled(int arg0, float v, int arg1) {

    }

    @Override
    public void onPageSelected(int arg0) {
        int max = bannerViews.size() - 1;
        int position = arg0;
        currentPosition = arg0;
//        if (isCycle) {
//            if (arg0 == 0) {
//                currentPosition = max - 1;
//            } else if (arg0 == max) {
//                currentPosition = 1;
//            }
//            position = currentPosition - 1;
//        }
        setIndicator(position);

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

        if (arg0 == ViewPager.SCROLL_STATE_DRAGGING) { // viewPager在滚动
            isScrolling = true;
            return;
        } else if (arg0 == ViewPager.SCROLL_STATE_IDLE) { // viewPager滚动结束
//            if (adapter != null)
//                adapter.setScrollable(true);
//
//            releaseTime = System.currentTimeMillis();

            viewPager.setCurrentItem(currentPosition, false);

        }
        isScrolling = false;

    }

    private class BannerPagerAdapter extends PagerAdapter{

        private List<View> viewList;

        public BannerPagerAdapter(List<View> list){
            viewList = list;
        }


        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            View v = viewList.get(position);
            if (mImageCycleViewListener != null) {
                v.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mImageCycleViewListener.onItemClick(position, v);
                    }
                });
            }
            container.addView(v);

            if(v.getTag() instanceof Integer){
                int resId = (Integer) v.getTag();
                setImageResource(resId,v);
                return v;
            }
            if(v.getTag() instanceof  String){
                String url = (String)v.getTag();
                setImageUrl(url,v);
            }

            return v;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        private void setImageResource(int resId,View parent){
            if(parent.findViewById(R.id.banner_img) == null)
                return;
            ImageView image = (ImageView) parent.findViewById(R.id.banner_img);
//            if(image.getTag() == null)
              image.setImageResource(resId);
//            image.setTag(resId);
        }

        private void setImageUrl(String url,View parent){
            if(parent.findViewById(R.id.banner_img) == null)
                return;
            ImageView image = (ImageView) parent.findViewById(R.id.banner_img);
//            if(image.getTag() == null)
                image.setImageURI(Uri.parse(url));
//            image.setTag(Uri.parse(url));
        }
    }


    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public static interface ImageCycleViewListener {

        /**
         * 单击图片事件
         *
         * @param position
         * @param view
         */
        public void onItemClick(int position, View view);
    }
}
