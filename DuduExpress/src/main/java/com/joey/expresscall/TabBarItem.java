package com.joey.expresscall;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joey.general.views.BadgeView;

/**
 * Created by Joey on 16/3/30.
 */
public class TabBarItem extends RelativeLayout{

    TextView title;
    ImageView image;
    private BadgeView tagView;

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

    private void init(Context context){
        View.inflate(context,R.layout.item_tab_bar,this);
        title = (TextView)findViewById(R.id.tab_item_text);
        image = (ImageView)findViewById(R.id.tab_item_image);
//        tagView = new BadgeView(context);
//        tagView.setTargetView(image);
//        tagView.setBadgeMargin(10, 5, 0, 0);
//        tagView.setBadgeCount(0);
    }

    public void setTagCount(int count){
//        tagView.setBadgeCount(count);
    }

    public void setText(int resid){
        title.setText(resid);
    }

    public void setText(CharSequence text){
        title.setText(text);
    }

    public void setImageResource(int resid){
        image.setImageResource(resid);
    }
    
}
