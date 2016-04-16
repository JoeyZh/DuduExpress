package com.joey.expresscall;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Joey on 16/3/30.
 */
public class TabHost {

    private LinearLayout mTabRoot;
    private FrameLayout mContentRoot;
    private Context mContext;
    private ArrayList<HashMap<String,Object>> mTabArray;
    private OnTabHostChangeListener tabHostChangeListener;
    private View.OnClickListener tabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mContentRoot == null){
                return;
            }
            mContentRoot.removeAllViews();
            if(view instanceof TabBarItem){
                Integer index = (Integer)view.getTag();
                if(index >= mTabArray.size()){
                    return;
                }
                HashMap<String,Object> tabMap = mTabArray.get(index);
                ViewGroup content = (ViewGroup)tabMap.get("content");
                mContentRoot.addView(content);
                if (tabHostChangeListener !=null){
                    tabHostChangeListener.onTagChange((TabBarItem)view,index,content);
                }
            }
        }
    };

    private TabHost(){};

    /**
     * @param context
     * @param tabRoot
     * @param contentRoot
     */
    public TabHost(Context context,LinearLayout tabRoot,FrameLayout contentRoot){
        mTabRoot = tabRoot;
        mContentRoot = contentRoot;
        mContext = context;
        mTabArray = new ArrayList<HashMap<String, Object>>();
    }

    public void addTab(String text,int imgRes,ViewGroup content){
        TabBarItem item = new TabBarItem(mContext);
        item.setTag(mTabRoot.getChildCount());
        HashMap<String,Object> tabmap = new HashMap<String, Object>();
        tabmap.put("tab",item);
        tabmap.put("content", content);
        mTabArray.add(tabmap);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,100,1);
        mTabRoot.addView(item,params);

        item.setOnClickListener(tabOnClickListener);
    }

    public interface  OnTabHostChangeListener{
        public void onTagChange(TabBarItem item,int index,ViewGroup content);
    }
}
