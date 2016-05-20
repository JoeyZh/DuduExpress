package com.joey.general.tab;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joey.expresscall.R;
import com.joey.general.views.BadgeView;

/**
 * Created by Joey on 16/3/30.
 */
public class TabBarItem extends RelativeLayout {

    TextView title;
    ImageView image;
    private BadgeView tagView;
    private int resAll;
    private int resOnFocus;
    private int textNormalColor;
    private int textFocusColor;

    public TabBarItem(Context context) {
        super(context);
        init(context);
    }

    public TabBarItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabBarItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.item_tab_bar, this);
        title = (TextView) findViewById(R.id.tab_item_text);
        image = (ImageView) findViewById(R.id.tab_item_image);
        textFocusColor = context.getResources().getColor(R.color.dot_orange);
        textNormalColor = context.getResources().getColor(R.color.text_color_gray68);
//        tagView = new BadgeView(context);
//        tagView.setTargetView(image);
//        tagView.setBadgeMargin(10, 5, 0, 0);
//        tagView.setBadgeCount(0);
    }

    public void setTextColor(int color,int focusColor){
        textNormalColor = color;
        textFocusColor = focusColor;
    }
    public void setTagCount(int count) {
//        tagView.setBadgeCount(count);
    }

    public void setOnFocus() {
        if (resOnFocus != 0) {
            image.setImageResource(resOnFocus);
            title.setTextColor(textFocusColor);
        }
    }

    public void clearFocus() {
        if (resAll != 0) {
            image.setImageResource(resAll);
            title.setTextColor(textNormalColor);
        }
    }

    public void setText(int resid) {
        title.setText(resid);
    }

    public void setText(CharSequence text) {
        title.setText(text);
    }

    public void setImageResource(int resid) {
        resAll = resid;
        image.setImageResource(resid);
    }

    public void setOnFocusResource(int resId) {
        resOnFocus = resId;
    }
}
