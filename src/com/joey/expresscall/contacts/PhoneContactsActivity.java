package com.joey.expresscall.contacts;

import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.joey.expresscall.R;
import com.joey.expresscall.contacts.bean.ContactBean;
import com.joey.general.BaseActivity;
import com.joey.general.utils.MyLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/19.
 */
public class PhoneContactsActivity extends BaseActivity{

    private ArrayList<HashMap<String,Object>> mapList;
    private ListView listContact;
    private MyAsyncQueryHandler asyncQueryHandler ;
    private MyAsyncQueryHandler.OnQueryListener queryListener = new MyAsyncQueryHandler.OnQueryListener() {
        @Override
        public void finishQuery() {
            ContactBean bean = new ContactBean();
            ArrayList<String> keys  = new ArrayList<String>(bean.getMap().keySet());
            String[] from =new String[3] ;
            from = keys.toArray(from);
            adapter = new SimpleAdapter(PhoneContactsActivity.this,asyncQueryHandler.getContactMapList(),R.layout.simple_item_layout_1,from,new int[]{R.id.item_text_tag,R.id.item_text,R.id.item_extra});
            PhoneContactsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listContact.setAdapter(adapter);
                }
            });
        }
    };
    private SimpleAdapter adapter;
    @Override
    public void initSettings() {
        asyncQueryHandler = new MyAsyncQueryHandler(getContentResolver());
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
        // 查询的字段
        String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
        // 按照sort_key升序查詢
        asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
                "sort_key COLLATE LOCALIZED asc");
        asyncQueryHandler.setOnQueryListener(queryListener);
    }

    @Override
    public void initUi() {
        setContentView(R.layout.activity_contact_list);
        listContact = (ListView) findViewById(R.id.contact_list);
    }

    @Override
    public void saveSettings() {

    }

    @Override
    public void freeMe() {

    }

    private void test(){

    }

}
