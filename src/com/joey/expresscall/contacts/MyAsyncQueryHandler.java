package com.joey.expresscall.contacts;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

import com.joey.expresscall.contacts.bean.ContactBean;
import com.joey.general.utils.MyLog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Administrator
 */
public class MyAsyncQueryHandler extends AsyncQueryHandler {

    private ArrayList<ContactBean> list = new ArrayList<ContactBean>();
    private ArrayList<HashMap<String,Object>> contactMapList = new ArrayList<HashMap<String, Object>>();

    private OnQueryListener listener;
    public MyAsyncQueryHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            HashMap<Integer, ContactBean> contactIdMap = new HashMap<Integer, ContactBean>();
            list.clear();
            contactMapList.clear();
            cursor.moveToFirst(); // 游标移动到第一项
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                String name = cursor.getString(1);
                String number = cursor.getString(2);
                String sortKey = cursor.getString(3);
                int contactId = cursor.getInt(4);
                Long photoId = cursor.getLong(5);
                String lookUpKey = cursor.getString(6);
                MyLog.fmt("name %s,number %s, sortKey %s,lookUpKey %s",name,number,sortKey,lookUpKey);
                if (contactIdMap.containsKey(contactId)) {
                    // 无操作
                } else {
                    // 创建联系人对象
                    ContactBean contact = new ContactBean();
                    contact.setDesplayName(name);
                    contact.setPhoneNum(number);
                    contact.setSortKey(sortKey);
                    contact.setPhotoId(photoId);
                    contact.setLookUpKey(lookUpKey);
                    list.add(contact);
                    contactMapList.add(contact.getMap());
                    contactIdMap.put(contactId, contact);
                }
            }
            if(listener != null){
                listener.finishQuery();
            }
        }
        super.onQueryComplete(token, cookie, cursor);
    }

    public void setOnQueryListener(OnQueryListener listener){
        this.listener = listener;
    }
    public ArrayList<ContactBean> getContactList(){
        return list;
    }

    public ArrayList<HashMap<String,Object>> getContactMapList(){
        return contactMapList;
    }
    public interface OnQueryListener{
        public void finishQuery();
    }
}
