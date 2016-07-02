package com.joey.expresscall.test;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.joey.expresscall.R;
import com.joey.expresscall.common.widget.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.changwentao.ad.AdEntity;
import cn.changwentao.ad.BannerAdView;
import cn.changwentao.ad.EmptyAd;
import cn.changwentao.ad.ShowTostAd;
import cn.changwentao.ad.ShowUrlAd;

/**
 * Created by Joey on 16/7/2.
 */
public class BannerViewTest extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BannerViewPager banner = new BannerViewPager(this);
        banner.setBackgroundColor(Color.RED);

        banner.setBanner(3, ViewPager.LayoutParams.MATCH_PARENT,200);
        banner.setImageResource(R.drawable.guide_1,0);
        banner.setImageResource(R.drawable.guide_2,1);
        banner.setImageResource(R.drawable.guide_3,2);
        banner.start(3000);
//
//        banner.setSelected(0);

//        BannerAdView banner = new BannerAdView(this);
//        List<AdEntity> list = new ArrayList<AdEntity>();
//        list.add(new EmptyAd(this));
//        list.add(new ShowUrlAd(this));
//        list.add(new ShowTostAd(this));
//
//        banner.setAdList(list);

        setContentView(banner);

    }
}
