package com.joey.expresscall.main;

import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.joey.expresscall.R;
import com.joey.general.BaseActivity;

/**
 * Created by Administrator on 2016/5/20.
 */
public class ECBillListActivity extends BaseActivity {

    private ListView listBill;
    private SimpleAdapter adapter;
    @Override
    public void initSettings() {

    }

    @Override
    public void initUi() {
        setContentView(R.layout.activity_bill_list_layout);
        listBill = (ListView)findViewById(R.id.bill_list);
//        adapter = new SimpleAdapter(this,R.layout.simple_item_layout_1,);
        setTitle(R.string.bill_title);
    }

    @Override
    public void saveSettings() {

    }

    @Override
    public void freeMe() {

    }
}
