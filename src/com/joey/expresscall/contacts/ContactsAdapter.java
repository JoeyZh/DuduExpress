package com.joey.expresscall.contacts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.joey.expresscall.contacts.bean.ContactBean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/5/19.
 */
public class ContactsAdapter extends BaseAdapter{

    private ArrayList<ContactBean> list;
    private Inflater inflater;
    private Context context;
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ContactBean getItem(int position) {
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
        return null;
    }

    public ContactsAdapter() {

    }
}
