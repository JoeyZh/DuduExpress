package com.joey.expresscall.file;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.joey.expresscall.R;
import com.joey.expresscall.common.CheckableLayout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/2.
 */
public class ECFileItemAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private int to[];
    private String from[];
    private ArrayList<HashMap<String,Object>> mData;
    private SwipeItemOnClickListener swipeItemOnClickListener;
    private SimpleSwipeListener swipeListener;
    public ECFileItemAdapter(Context mContext,ArrayList<HashMap<String,Object>> data,String[] from,int []to) {
        this.mContext = mContext;
        this.from = from;
        this.to = to;
        this.mData = data;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_file_list, null);
        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.setClickToClose(true);
        if(swipeListener != null){
            swipeLayout.addSwipeListener(swipeListener);
        }
//        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
//            @Override
//            public void onOpen(SwipeLayout layout) {
////                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
//            }
//        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        ViewHolder holder;
        if(v.getTag() == null){
            holder = new ViewHolder();
            v.setTag(holder);
        }
        holder = (ViewHolder)v.getTag();
        holder.imgDelete = (ImageView) v.findViewById(R.id.trash);
        holder.imgDelete.setTag(position);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getTag() instanceof  Integer){
                    int position = (Integer) view.getTag();
                    if(swipeItemOnClickListener != null){
                        swipeItemOnClickListener.onItemClick(view,position,getItem(position));
                    }
                }
                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        CheckableLayout checkableLayout = (CheckableLayout)convertView.findViewById(R.id.item_root);
        checkableLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
        HashMap<String,Object> item = getItem(position);
        for(int i=0;i<to.length;i++){
            View v = checkableLayout.findViewById(to[i]);
            Object obj = item.get(from[i]);
            if(v instanceof  TextView){
                if(obj instanceof  CharSequence){
                    ((TextView) v).setText(obj.toString());
                    continue;
                }
                if(obj instanceof Integer){
                    ((TextView) v).setText((Integer)obj);
                }
            }
            if(v instanceof  ImageView){
                if(obj instanceof Bitmap){
                    ((ImageView) v).setImageBitmap((Bitmap) obj);
                    continue;
                }
                if(obj instanceof Integer){
                    ((ImageView) v).setImageResource((Integer)obj);
                }
            }
        }
    }

    @Override
    public int getCount() {
        if(mData == null)
            return 0;
        return mData.size();
    }

    @Override
    public HashMap<String, Object> getItem(int position) {
        if(position>=getCount()){
            return  null;
        }
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSwipeItemOnClickListener(SwipeItemOnClickListener l){
        swipeItemOnClickListener = l;
    }

    public void setSimpleSwipeListener (SimpleSwipeListener listener){
        this.swipeListener = listener;
    }
    private class ViewHolder{
        ImageView imgDelete;
    }

    public interface SwipeItemOnClickListener{
        void onItemClick(View view,int postion,HashMap<String,Object> map);
    }
}
