package com.joey.general.tab;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.joey.expresscall.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Joey on 16/3/30.
 */
public class TabHost {

    private LinearLayout mTabRoot;
    private FrameLayout mContentRoot;
    private Activity mActivity;
    private ArrayList<HashMap<String,Object>> mTabArray;
    private OnTabHostChangeListener tabHostChangeListener;
    private int currentIndex = -1;
    private View.OnClickListener tabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mContentRoot == null){
                return;
            }
            if(view instanceof TabBarItem){
                Integer index = (Integer)view.getTag();
                changeToIndex(index);
            }
        }
    };

    private TabHost(){};

    /**
     * @param context
     * @param tabRoot
     * @param contentRoot
     */
    public TabHost(Activity activity,LinearLayout tabRoot,FrameLayout contentRoot){
        mTabRoot = tabRoot;
        mContentRoot = contentRoot;
        mActivity = activity;
        mTabArray = new ArrayList<HashMap<String, Object>>();
    }

    public void addTab(String text,int imgRes,int resFocus,Fragment content){
        TabBarItem item = new TabBarItem(mActivity);
        item.setText(text);
        item.setImageResource(imgRes);
        item.setOnFocusResource(resFocus);
        item.setTag(mTabRoot.getChildCount());
        HashMap<String,Object> tabmap = new HashMap<String, Object>();
        tabmap.put("tab",item);
        tabmap.put("content", content);
        mTabArray.add(tabmap);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT,1);
        mTabRoot.addView(item,params);

        item.setOnClickListener(tabOnClickListener);
    }

    public void changeToIndex(int index){
    	 if(index >= mTabArray.size()){
             return;
         }
    	 if(index == currentIndex){
    		 return;
    	 }
        if(currentIndex >= 0 && currentIndex<mTabArray.size()){
            TabBarItem lastView = (TabBarItem)mTabArray.get(currentIndex).get("tab");
            lastView.clearFocus();
        }
    	 currentIndex = index;
         HashMap<String,Object> tabMap = mTabArray.get(index);
         Fragment content = (Fragment)tabMap.get("content");
         TabBarItem view = (TabBarItem)tabMap.get("tab");
         FragmentTransaction transaction = mActivity.getFragmentManager() 
                 .beginTransaction();
         transaction.replace(R.id.tab_content, content);
         transaction.commit();
         if (tabHostChangeListener !=null){
             tabHostChangeListener.onTagChange((TabBarItem)view,index,content);
         }
        view.setOnFocus();
    }
    
    public interface  OnTabHostChangeListener{
        public void onTagChange(TabBarItem item, int index, Fragment content);
    }
}
