package com.joey.expresscall.main;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECAccountManager;
import com.joey.expresscall.account.ECCallManager;
import com.joey.expresscall.addfile.ECAddFileActivity;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.general.BaseFragment;
import com.joey.general.utils.MyLog;

public class ECMainFragment extends BaseFragment {

    private final String keys[] = new String[]{"type",
            "name", "lastCall", "time", "color"};
    private View currentView;
    private View addFileView;
    private ListView fileListView;
    private View header;
    private ECFileItemAdapter mAdapter;
    private ArrayList<HashMap<String, Object>> mCallList;
    private TextView tvUserName;
    private TextView tvRetain;
    private TextView tvWarning;
    private TextView tvCostDetail;
    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_new_file_layout:
                    Intent intent = new Intent(getActivity(), ECAddFileActivity.class);
                    startActivity(intent);
                    break;
                case R.id.text_cost_info:
                    intent = new Intent(getActivity(), ECBillListActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    public ECMainFragment() {
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        currentView = inflater.inflate(R.layout.main_fragment_layout,
                container, false);
        setTopBarVisiable(-1);
        header = inflater.inflate(R.layout.main_add_file_layout, null);
        fileListView = (ListView) currentView.findViewById(R.id.main_file_list);
        addFileView = header.findViewById(R.id.add_new_file_layout);
        addFileView.setOnClickListener(mOnClickListener);
        fileListView.addHeaderView(header);

        return currentView;
    }

    @Override
    public void initSettings() {
        loadData();
        getUseInfo();
        getCallList();
    }

    @Override
    public void initUi() {
        mCallList = new ArrayList<HashMap<String, Object>>();
        test();
        MyLog.fmt("mActicity %s mCallList %s", (mActivity == null) + "", (mCallList == null) + "");
        mAdapter = new ECFileItemAdapter(fileListView.getContext(), mCallList,
                R.layout.simple_item_extra_layout, keys, new int[]{
                R.id.text_logo, R.id.text_content, R.id.text_desc,
                R.id.text_extra});

        fileListView.setAdapter(mAdapter);
//		初始化头部
        tvUserName = (TextView) currentView.findViewById(R.id.text_user_info);
        tvRetain = (TextView) currentView.findViewById(R.id.text_cost_retain);
        tvWarning = (TextView) currentView.findViewById(R.id.text_cost_warning);
        tvCostDetail = (TextView) currentView.findViewById(R.id.text_cost_info);
        tvCostDetail.setOnClickListener(mOnClickListener);
    }

    @Override
    public void saveSettings() {
        // TODO Auto-generated method stub

    }

    @Override
    public void freeMe() {
        // TODO Auto-generated method stub

    }

    private void loadData() {
        // TODO Auto-generated method stub

    }

    private void getUseInfo() {
        // TODO Auto-generated method stubs
        ECAccountManager.getInstance().getUserInfo(new ResponseListener<JSONObject>() {

            @Override
            public void onSuccess(JSONObject json) {
                MyLog.i("");
                final String nickName = json.getString("nickName");
                final float retain = json.getFloat("totalMoney");
                final String mobile = json.getString("mobile");
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (nickName != null)
                            tvUserName.setText(nickName);
                        else {
                            tvUserName.setText(mobile);
                        }
                        tvRetain.setText(retain + "元");
                        if (retain <= 0) {
                            tvWarning.setVisibility(View.VISIBLE);
                            return;
                        }
                        tvWarning.setVisibility(View.GONE);
                    }

                });
            }

            @Override
            public void onError(RequestError error) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

            }
        });
    }

    private void getCallList() {
        ECCallManager.getInstance().getCallList(new ResponseListener<JSONArray>() {

            @Override
            public void onSuccess(JSONArray json) {
                MyLog.i("callList = " + json.toString());
            }

            @Override
            public void onError(RequestError error) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub

            }
        });
    }

    private void test() {
        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (i % 2 == 0) {
                map.put(keys[0], "录");
                map.put(keys[4], "blue");
            } else {
                map.put(keys[0], "文");
                map.put(keys[4], "red");
            }
            map.put(keys[1], "通知XX小区拿快递");
            map.put(keys[2], "18666663333,已通知 3/10");
            map.put(keys[3], "18:00");
            mCallList.add(map);
        }
    }

}
