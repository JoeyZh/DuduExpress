package com.joey.expresscall.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/19.
 */
public class ECGroupAdapter extends ECSimpleAdapter1{

    private ArrayList<HashMap<String,Object>> list;
    private LayoutInflater inflater;
    private Context context;
    private int groupResource;
    private String[] groupFrom;
    private int [] groupTo;
    private int itemResource;
    private String[] itemFrom;
    private int [] itemTo;

    public final static int ECGROUP_TYPE_TITLE = 0;
    public final static int ECGROUP_TYPE_ITEM = 1;
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public  HashMap<String,Object> getItem(int position) {
        if(list == null){
            return  null;
        }
        if(list.size()>position ){
            return list.get(position);
        }
        return null ;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HashMap<String,Object> map = getItem(position);
        View view = super.getView(position,convertView,parent);
        if(map == null )
             return super.getView(position,convertView,parent);
        int itemType = (Integer) map.get("type");
        switch (itemType){
            case ECGROUP_TYPE_ITEM:
                return  view;
            case ECGROUP_TYPE_TITLE:
                view = createGroupView(position,convertView,parent);
                break;
        }
        return  view;
    }

    public View createGroupView(int position,View convertView,ViewGroup parent){
        if(convertView == null){
            convertView = inflater.inflate(groupResource, null) ;
        }
        HashMap<String,Object> item = getItem(position);
        for(int i = 0;i<groupTo.length;i++){
            View child = convertView.findViewById(groupTo[i]);
            Object obj = item.get(groupFrom[i]);
            if(child instanceof ImageView){
                if(obj instanceof  Integer){
                    ((ImageView) child).setImageResource((Integer) obj);
                }
                else if(obj instanceof Bitmap){
                    ((ImageView) child).setImageBitmap((Bitmap)obj);
                }
            }
            else if(child instanceof  TextView){
                if(obj instanceof  Integer){
                    ((TextView) child).setText((Integer) obj);
                }
                else if(obj instanceof String){
                    ((TextView) child).setText((String)obj);
                }
            }
        }
        return convertView;
    }

    public ECGroupAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        itemResource = resource;
        itemFrom = from;
        itemTo = to;
        list = ( ArrayList<HashMap<String,Object>>)data;
        inflater =  LayoutInflater.from(context);
    }

    public void setGroupInfo(int resource,String []from, int [] to){
        groupFrom = from;
        groupResource = resource;
        groupTo = to;
    }
}
