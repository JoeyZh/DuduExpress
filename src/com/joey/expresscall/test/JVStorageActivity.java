package com.joey.expresscall.test;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.Random;

import com.joey.expresscall.main.bean.CallListBean;
import com.joey.expresscall.storage.JVAccount;
import com.joey.expresscall.storage.JVDbHelper;
import com.joey.general.utils.MyLog;

public class JVStorageActivity extends ListActivity {

    Serializable[] list = null;
    String testArray[] = {
            "0账号添加", "1账号删除", "2帐号列表", "3查找一个设备", "4设备添加",
            "5设备删除", "6最后一个账号设备列表", "7设备修改", "8读取一个设备", "9所有设备列表"
    };

    CallListBean bean = null;
    JVAccount mAccount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show the Up button in the action bar.
        JVDbHelper.getInstance().init(this);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, testArray));
        // mAccount = (JVAccount)JVDbHelper.getInstance().getObject("account",
        // "data", 0);
        mAccount = new JVAccount("18663753236", "123456", "");

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        MyLog.i("");

        switch (position) {
            case 0:
                Random ran = new Random();
                String no = "" + ran.nextInt(100000);
                MyLog.i("no = " + no);
                mAccount = new JVAccount(no, "123", "lalala");
                JVDbHelper.getInstance().insertAccount(mAccount);
                break;
            case 1:
                JVAccount account2 = (JVAccount) list[0];
                MyLog.i("account = " + account2.toString());
                MyLog.i("delete result = "
                        + JVDbHelper.getInstance().deleteAccount(account2));
                break;
            case 2:
                list = JVDbHelper.getInstance().getList("account", "data");
                break;
            case 3:
                MyLog.i(JVDbHelper
                        .getInstance()
                        .getObject("device", "data", JVDbHelper.ACCOUNT_ID, "18663753236", "id",
                                "device_test2")
                        .toString());
                break;
            case 4:
                ran = new Random();
                no = "" + ran.nextInt(100000);
                MyLog.i("no = " + no);
                bean = new CallListBean();
                bean.setId("A" + no);
                JVDbHelper.getInstance().insert(bean,JVDbHelper.GROUP_TABLE, mAccount.getUsername());
                break;
            case 5:
                if (bean != null)
                    JVDbHelper.getInstance().delete(bean,JVDbHelper.GROUP_TABLE,mAccount.getUsername());
                break;
            case 6:
                JVDbHelper.getInstance().getList("device", "data", JVDbHelper.ACCOUNT_ID,
                        mAccount.getUsername());
                break;
            case 7:
                JVDbHelper.getInstance().update(bean,JVDbHelper.GROUP_TABLE, mAccount.getUsername());
            case 8:
                mAccount = (JVAccount) JVDbHelper.getInstance().getObject("account", "data"
                );
                MyLog.i("account is " + mAccount.toString());
                break;
            case 9:
                JVDbHelper.getInstance().getList("device", "data", JVDbHelper.ACCOUNT_ID,
                        "18663753236");
                break;

        }
    }

}
