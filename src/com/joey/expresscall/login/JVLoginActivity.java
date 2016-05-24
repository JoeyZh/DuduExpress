package com.joey.expresscall.login;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.MainActivity;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECAccountManager;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.expresscall.storage.JVAccount;
import com.joey.expresscall.storage.JVDbHelper;
import com.joey.general.BaseActivity;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.MySharedPreference;
import com.joey.general.utils.MySharedPreferencesConsts;
import com.joey.general.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;

public class JVLoginActivity extends BaseActivity {

    private final int MIN_PWD_LENGTH = 6;
    private final int MAX_PWD_LENGHT = 20;
    private EditText userNameET;
    private EditText userPwdET;
    private Button loginBtn;
    private TextView registerTV;
    private TextView findPassTV;
    private String userName;
    private String password;

    private PopupWindow popWin;
    private JVAccountAdapter adapter;
    private ListView accountListView;
    private ArrayList<JVAccount> accountList = new ArrayList<JVAccount>();
    private ImageButton spinnerBtn;
    // Intent
    private boolean isGoBack;// 登录完成是否回到跳转之前的界面
    /**
     * 账号库状态监听
     */
    private ResponseListener<JSONObject> listener = new ResponseListener<JSONObject>() {
        public void onSuccess(JSONObject jsonData) {
        	String token = jsonData.getString("token");
        	ECAccountManager.getInstance().setToken(token);
            // 保存账号信息
            MySharedPreference.getInstance().putString(MySharedPreferencesConsts.USERNAME,
                    userName);
            MySharedPreference.getInstance().putString(MySharedPreferencesConsts.PASSWORD,
                    password);
            statusHashMap.put(MySharedPreferencesConsts.USERNAME, userName);
            statusHashMap.put(MySharedPreferencesConsts.PASSWORD, password);

            JVDbHelper.getInstance().insertAccount(
                    new JVAccount(userName, password, ""));
            // 跳转
            if (!isGoBack) {
                // 跳转到首页
                Intent loginIntent = new Intent(JVLoginActivity.this,
                        MainActivity.class);
                JVLoginActivity.this.startActivity(loginIntent);
            } else {
                // 从哪来回哪去
                setResult(RESULT_OK, null);
            }
            JVLoginActivity.this.finish();
        }

        @Override
        public void onError(RequestError error) {
            if (error == null) {
                ToastUtil.show(JVLoginActivity.this, "error is null");
                return;
            }
            MyLog.e("error = " + error.errmsg);
            switch (error.errcode) {
//                case Account.BIZ_ACC_STATUS_AUTH:
//                    ToastUtil.show(JVLoginActivity.this, R.string.error_status_auth);
//                    break;
//                case Account.BIZ_ACC_STATUS_AUTH_USR:
//                    ToastUtil.show(JVLoginActivity.this, R.string.error_status_auth_usr);
//                    break;
//                case Account.BIZ_ACC_STATUS_AUTH_PWD:
//                    ToastUtil.show(JVLoginActivity.this, R.string.error_status_auth_pwd);
//                    break;
                default:
                    ToastUtil.show(JVLoginActivity.this, error.errmsg);
                    break;
            }
        }

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFinish() {
            dismissDialog();
			
		}
    };
    /**
     * 按钮点击事件
     */
    OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.login_button: {// 登陆
//                    Analysis.analysisClickEvent(JVLoginActivity.this, "Login");
                    // 登录的时候用户名进行去掉空格处理
                    userName = userNameET.getText().toString().trim();
                    password = userPwdET.getText().toString();
                    login(userName, password);
                    break;
                }
                case R.id.register_layout:
                case R.id.register_textview: {// 注册
//                    Analysis.analysisClickEvent(JVLoginActivity.this, "Register");
                    Intent registerIntent = new Intent(JVLoginActivity.this,
                            JVRegisterActivity.class);
                    JVLoginActivity.this.startActivity(registerIntent);
                    break;
                }
                case R.id.findpass_textview: {// 找回密码
//                    Analysis.analysisClickEvent(JVLoginActivity.this, "FindPass");
                    Intent findPassIntent = new Intent(JVLoginActivity.this,
                            JVFoundPasswordActivity.class);
                    findPassIntent.putExtra(MySharedPreferencesConsts.USERNAME, userNameET
                            .getText().toString().trim());
                    JVLoginActivity.this.startActivity(findPassIntent);
                    break;
                }
                case R.id.login_spinner_btn:
                    MyLog.i("");
                    showPopWindow();
                    spinnerBtn.setEnabled(false);
                case R.id.account_item_delete_btn:
                    if (view.getTag() != null) {
                        MyLog.i("");
                        JVAccount account = (JVAccount) view.getTag();
                        deleteAccount(account);
                    }
            }

        }

    };

    @Override
    public void initSettings() {
        isGoBack = getIntent() != null ? getIntent().getBooleanExtra("isGoBack", false) : false;
        userName = MySharedPreference.getInstance()
                .getString(MySharedPreferencesConsts.USERNAME);
        password = MySharedPreference.getInstance()
                .getString(MySharedPreferencesConsts.PASSWORD);
    }

    @Override
    public void initUi() {
        setContentView(R.layout.login_layout);
        setTopBarVisiable(-1);

        userNameET = (EditText) findViewById(R.id.username_edittext);
        userPwdET = (EditText) findViewById(R.id.password_edittext);
        loginBtn = (Button) findViewById(R.id.login_button);
        loginBtn.setOnClickListener(mOnClickListener);

        registerTV = (TextView) findViewById(R.id.register_textview);
        registerTV.setOnClickListener(mOnClickListener);
        View registerLayout = findViewById(R.id.register_layout);
        registerLayout.setOnClickListener(mOnClickListener);
        findPassTV = (TextView) findViewById(R.id.findpass_textview);
        findPassTV.setOnClickListener(mOnClickListener);

        initAccountPopWindow();

    }

    @Override
    public void onResume() {
        super.onResume();
        // 通过statusHashMap 来传递值，如果此时有用名的值传递过来，就不加载share prefenrences 的内容了
        if (statusHashMap.containsKey(MySharedPreferencesConsts.USERNAME)) {
            String tmpUserName =(String) statusHashMap
                    .get(MySharedPreferencesConsts.USERNAME);
            if (tmpUserName != null && !tmpUserName.isEmpty()) {
                userName = tmpUserName;
                password = "";
            }
        }
        userNameET.setText(userName);
        userPwdET.setText(password);
        if (!password.isEmpty() && !userName.isEmpty()) {
            login(userName, password);
        }
    }

    private void initAccountPopWindow() {
        spinnerBtn = (ImageButton) findViewById(R.id.login_spinner_btn);
        spinnerBtn.setOnClickListener(mOnClickListener);
        /**
         * 初始化帐号数据
         */
        Serializable list[] = JVDbHelper.getInstance().getList(
                JVDbHelper.ACCOUNT_TABLE, JVDbHelper.COLUMN_DATA);
        for (int i = 0; i < list.length; i++) {
            accountList.add((JVAccount) list[i]);
        }
        if (accountList.isEmpty()) {
            spinnerBtn.setVisibility(View.GONE);
            return;
        }
        accountListView = new ListView(JVLoginActivity.this);
        adapter = new JVAccountAdapter(this, R.layout.login_account_item,
                accountList);
        adapter.setOnClickListener(mOnClickListener);
        accountListView.setAdapter(adapter);
        accountListView
                .setBackgroundResource(R.drawable.bg_shape_with_corner);
        popWin = new PopupWindow(accountListView, -1, -2);
        popWin.setOutsideTouchable(true);
        /**
         * 默认PopupWindow是没有焦点和不可点击的<br/>
         * 不加的话,在某些手机上可以无法点击
         */
        popWin.setFocusable(true);
        popWin.setBackgroundDrawable(new BitmapDrawable());
        accountListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                MyLog.i("");
                JVAccount account = accountList.get(arg2);
                if (!account.getUsername().equals(userName)) {
                    userPwdET.setText("");

                } else {
                    userPwdET.setText(password);
                }
                userNameET.setText(account.getUsername());
                dismissPopWindow();
            }
        });
        popWin.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                Animation rotate = AnimationUtils.loadAnimation(
                        JVLoginActivity.this, R.anim.rotate_ccw_180);
                spinnerBtn.startAnimation(rotate);
                /**
                 * 这样修改，现象看来是popupWindow点击小三角消失不了的操作（其实是消失了又显示了
                 * 因为popUpWindow显示时，点击其他非popUp区域popUp都会消失,之后又点击spinnerBtn导致再次显示）
                 * 处理方式 点击spinnerBtn，将其设为不可用状态，直到PopUpWindow消失之后500毫秒，
                 * 才将spinnerBtn设置可用
                 */
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        spinnerBtn.setEnabled(true);
                    }
                }, 500);
            }
        });
    }

    /**
     * 打开
     */
    private void showPopWindow() {

        Animation rotate = AnimationUtils.loadAnimation(JVLoginActivity.this,
                R.anim.rotate_180);
        rotate.setFillAfter(true);
        spinnerBtn.startAnimation(rotate);
        MyLog.i("userNameET width = " + userNameET.getWidth());
        popWin.setWidth(userNameET.getWidth());
        popWin.showAsDropDown(userNameET);

    }

    private void dismissPopWindow() {

        if (popWin.isShowing()) {
            popWin.dismiss();
        }
    }

    @Override
    public void saveSettings() {
        statusHashMap.put(MySharedPreferencesConsts.USERNAME, userNameET.getText().toString());
    }

    @Override
    public void freeMe() {

    }

    @Override
    public void onBackPressed() {
        exitTip();
    }

    /**
     * 善纯账号操作，点击账号列表之后的X按钮
     *
     * @param account
     */
    public void deleteAccount(final JVAccount account) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // 列表删除，数据库删除
                accountList.remove(account);
                JVDbHelper.getInstance().deleteAccount(account);
                adapter.notifyDataSetChanged();
                // 刷新UI，下拉列表刷新
                if (accountList.isEmpty()) {
                    MySharedPreference.getInstance().putString(
                            MySharedPreferencesConsts.USERNAME, "");
                    MySharedPreference.getInstance().putString(
                            MySharedPreferencesConsts.PASSWORD, "");
                    userName = "";
                    password = "";
                    spinnerBtn.setVisibility(View.GONE);
                    popWin.dismiss();
                }
                // 用户名输入框刷新
                if (userNameET.getText().toString()
                        .equals(account.getUsername())) {
                    userNameET.setText("");
                }
                userPwdET.setText("");
            }
        });

    }

    public void login(String userName, String password) {

        if (checkoutUsername(userName) && checkoutPassword(password)) {
            createDialog(R.string.login_loading, true);
            ECAccountManager.getInstance().login(userName, password, listener);
        }
    }

    /**
     * 检测用户名是否合法
     *
     * @return
     */
    private boolean checkoutUsername(String userName) {
        if (userName == null || userName.isEmpty()) {
            ToastUtil.show(this, R.string.username_null);
            return false;
        }
        return true;
    }

    /**
     * 检测密码是否合法
     *
     * @return
     */
    private boolean checkoutPassword(String password) {
        if (password == null || password.isEmpty()) {
            ToastUtil.show(this, R.string.password_null);
            return false;
        }
        if (password.length() < MIN_PWD_LENGTH) {
            ToastUtil.show(this, R.string.login_password_error);
            return false;
        } else if (password.length() > MAX_PWD_LENGHT) {
            ToastUtil.show(this, R.string.login_password_error);
            return false;
        }
        return true;
    }

}
