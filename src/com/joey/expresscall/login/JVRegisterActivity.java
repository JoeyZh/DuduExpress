package com.joey.expresscall.login;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.account.ECAccountManager;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.general.BaseActivity;
import com.joey.general.utils.CheckPhoneNumber;
import com.joey.general.utils.CommonUtil;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.RegularUtil;
import com.joey.general.utils.ToastUtil;
import com.joey.general.views.TopBarLayout;
import com.joey.expresscall.R;
import com.joey.expresscall.protocol.ResponseListener;


/**
 * 注册界面
 *
 * @author Joey
 */
public class JVRegisterActivity extends BaseActivity {
    private final int FITST_TIP = 0;
    private final int SECOND_TIP = 1;
    private final int MAX_TIMES = 60;

    /**
     * 标题栏
     */
    private TopBarLayout mTopBarView;
    private LinearLayout firstLayout;
    private EditText userInputEdit;
    private ProgressBar checkLoadingBar;
    private TextView hasAccountTV;
    private Button nextTipBtn;
    private TextView inputWarnText;
    private LinearLayout secondLayout;
    private Button registBtn;
    private Button getCodeBtn;
    private CheckBox checkAgreementBtn;
    private TextView agreeProfileText;
    private boolean isExist;
    private boolean isCheckedExist;
    // 标记是否阅读小维注册协议
    private boolean hasRead;
    // 标记是否点击checkBox
    private boolean isCheckedRead;
    // 是否获取到验证码
    private boolean hasCode;
    private int lostTimes;
    private boolean isShowNotification = false;
    private EditText userNameText;
    private EditText passwordEdit;
    private EditText confirmEdit;
    private EditText validateCodeEdit;
    private int tipIndex = 0;
    private String registerUserName;
    private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            isCheckedRead = isChecked;
        }
    };
    private Runnable resetCodeNotificationRunnalble = new Runnable() {

        @Override
        public void run() {
            lostTimes--;
            if (isShowNotification) {
                if (lostTimes > 0) {
                    getCodeBtn.setEnabled(false);
                    getCodeBtn.setText(lostTimes + "S");
                    handler.postDelayed(resetCodeNotificationRunnalble, 1000);
                    return;
                }
            }
            getCodeBtn.setEnabled(true);
            getCodeBtn.setText(R.string.send_validate_code);
        }
    };
    /**
     * 点击事件
     */
    OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.left_btn: {// top左键
                    onBackPressed();
                    break;
                }
                case R.id.right_btn: {// top右键
                    break;

                }

                // 下一步按钮
                case R.id.register_next_tip_button:
                    if (!checkoutUserInput()) {
                        return;
                    }
                    checkLoadingBar.setVisibility(View.VISIBLE);
                    checkUserExist();
                    break;
                // 获取验证码
                case R.id.register_code_button:
                    view.setEnabled(false);
                    lostTimes = MAX_TIMES;
                    isShowNotification = true;
                    handler.removeCallbacks(resetCodeNotificationRunnalble);
                    handler.post(resetCodeNotificationRunnalble);
                    getValidateCode();
                    break;
                // 注册按钮
                case R.id.register_button:
                    MyLog.fmt("hasRead %s,isCheckedRead %s", hasRead + "",
                            isCheckedRead + "");
//                    if (!hasRead) {
//                        ToastUtil.show(JVRegisterActivity.this,
//                                R.string.register_read_first);
//                        return;
//                    }
//                    if (!isCheckedRead) {
//                        ToastUtil.show(JVRegisterActivity.this,
//                                R.string.register_not_agree);
//                        return;
//                    }
                    if (!hasCode) {
                        ToastUtil.show(JVRegisterActivity.this,
                                R.string.register_got_code_first);
                        return;
                    }
                    register();
                    break;
//                case R.id.register_agreement_textview2:
//                    Intent intent = new Intent(JVRegisterActivity.this,
//                            JVRegiserSignActivity.class);
//                    startActivity(intent);
//                    hasRead = true;
//                    break;
            }
        }

    };
    private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                Drawable right = getResources().getDrawable(R.drawable.icon_edit_right_arrow);
                String password = passwordEdit.getText().toString();
                String confirm = confirmEdit.getText().toString();
                String code = validateCodeEdit.getText().toString();
                switch (v.getId()) {
                    case R.id.register_password_edittext:
                        if (RegularUtil.checkUserPwd(password)) {
                            passwordEdit
                                    .setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
                        } else {
                            ToastUtil.show(JVRegisterActivity.this,
                                    R.string.register_password_too_short);
                            passwordEdit
                                    .setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            return;
                        }
                        break;
                    case R.id.register_confirm_edittext:
                        if (!password.equals(confirm)) {
                            ToastUtil.show(JVRegisterActivity.this,
                                    R.string.register_password_error);
                            confirmEdit
                                    .setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            return;
                        }
                        confirmEdit
                                .setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
                        break;
                    case R.id.register_code_edittext:
                        break;
                }
            }
        }
    };

    @Override
    public void initSettings() {

    }


    @Override
    public void initUi() {
        setContentView(R.layout.register_layout);
        mTopBarView = getTopBarView();
        mTopBarView.setTopBar(R.drawable.icon_back, -1, R.string.register,
                mOnClickListener);
        firstLayout = (LinearLayout) findViewById(R.id.register_first_tip);
        userInputEdit = (EditText) findViewById(R.id.register_username_edittext);
        checkLoadingBar = (ProgressBar) findViewById(R.id.register_check_progressbar);
        inputWarnText = (TextView) findViewById(R.id.register_warn_textview);
        nextTipBtn = (Button) findViewById(R.id.register_next_tip_button);
        nextTipBtn.setOnClickListener(mOnClickListener);

        secondLayout = (LinearLayout) findViewById(R.id.register_second_tip);
        userNameText = (EditText) findViewById(R.id.register_username_text);
        passwordEdit = (EditText) findViewById(R.id.register_password_edittext);
        confirmEdit = (EditText) findViewById(R.id.register_confirm_edittext);
        validateCodeEdit = (EditText) findViewById(R.id.register_code_edittext);
//        checkAgreementBtn = (CheckBox) findViewById(R.id.register_agreement_checkbox);
//        checkAgreementBtn.setOnCheckedChangeListener(checkedChangeListener);
//        agreeProfileText = (TextView) findViewById(R.id.register_agreement_textview2);
//        agreeProfileText.setOnClickListener(mOnClickListener);
        getCodeBtn = (Button) findViewById(R.id.register_code_button);
        registBtn = (Button) findViewById(R.id.register_button);
        registBtn.setEnabled(false);
        getCodeBtn.setOnClickListener(mOnClickListener);
        registBtn.setOnClickListener(mOnClickListener);

        passwordEdit.setOnFocusChangeListener(mOnFocusChangeListener);
        confirmEdit.setOnFocusChangeListener(mOnFocusChangeListener);
        validateCodeEdit.setOnFocusChangeListener(mOnFocusChangeListener);
    }

    @Override
    public void onBackPressed() {
        MyLog.i("tipIndex = " + tipIndex);
        if (tipIndex == FITST_TIP)
            JVRegisterActivity.this.finish();
        else {
            gotoFirstTip();
        }
    }

    @Override
    public void saveSettings() {

    }

    @Override
    public void freeMe() {

    }

    private void checkUserExist() {
        String user = userInputEdit.getText().toString();
        /**
         * 判断账号存在不
         */
        createDialog(R.string.tips_account_vertifying, true);
        ECAccountManager.getInstance().isAccountExist(user,
                new ResponseListener<JSONObject>() {
                    // 账号库反馈
                    @Override
                    public void onSuccess(JSONObject jsonData) {
                        MyLog.i("jsonData = " + jsonData.toString());
//                        if (jsonData instanceof JSONObject) {
//                            isExist = ((JSONObject) jsonData)
//                                    .getBooleanValue("isExist");
//                            isCheckedExist = true;
//                            // 账号存在，出提示
//                            if (isExist) {
//                                showInputWarn(R.string.register_conflict, false);
//                            } else {// 进行注册第二部
                                showInputWarn(R.string.register_no_conflict,
                                        false);
                                gotoSecondTip();
//                            }
//                        }
                    }

                    @Override
                    public void onError(RequestError error) {
                        // 关闭Progress提示框

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

    /**
     * 跳转到注册第二步的界面
     */
    private void gotoSecondTip() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                tipIndex = SECOND_TIP;
                registerUserName = userInputEdit.getText().toString();
                userNameText.setText(registerUserName);
                lostTimes = MAX_TIMES;
                isShowNotification = false;
                handler.removeCallbacks(resetCodeNotificationRunnalble);
                getCodeBtn.setEnabled(true);
                firstLayout.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 检查输入手机号/邮箱的合法性
     *
     * @return
     */
    private boolean checkoutUserInput() {
        final String user = userInputEdit.getText().toString();
        if (user == null || user.isEmpty()) {
            showInputWarn(R.string.username_null, false);
            return false;
        }
        if (user.contains("@")) {
            if (!isEmail(user)) {
                showInputWarn(R.string.register_email_input_error, false);
                return false;
            }
            return true;
        }
        if (!isPhoneNum(user)) {
            showInputWarn(R.string.register_phone_input_error, false);
            return false;
        }

        return true;
    }

    /**
     * 检查是否为手机号
     *
     * @param user
     * @return
     */
    private boolean isPhoneNum(String user) {
        MyLog.i("user = " + user + ",length = " + user.length());
        try {
            Double.parseDouble(user);
            int checkResult = CheckPhoneNumber.matchNum(user);
            if (checkResult == CheckPhoneNumber.TYPE_ERROR
                    || checkResult == CheckPhoneNumber.TYPE_UNKNOW)
                return false;
        } catch (Exception e) {
            MyLog.e("error = " + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 检查是否是email
     *
     * @param user
     * @return
     */
    private boolean isEmail(String user) {
        if (user.contains("@")) {
            MyLog.i("");
            String value[] = user.split("@");
            MyLog.i("value.length = " + value.length);
            if (value.length != 2)
                return false;
            MyLog.i("value[1] = " + value[1]);
            String DNSs[] = value[1].split("\\.");
            MyLog.i("DNSs.length = " + DNSs.length);

            if (DNSs.length < 2) {
                return false;
            }
            if (CommonUtil.isEmailFormatLegal(user))
                return true;
        }
        return false;
    }

    /**
     * 跳转到注册第一步的界面
     */
    private void gotoFirstTip() {
        inputWarnText.setVisibility(View.GONE);
        tipIndex = FITST_TIP;
        checkLoadingBar.setVisibility(View.GONE);
        firstLayout.setVisibility(View.VISIBLE);
    }

    private void register() {

        String password = passwordEdit.getText().toString();
        String confirm = confirmEdit.getText().toString();
        if (checkoutPassword(password, confirm)) {
            String validateCode = validateCodeEdit.getText().toString();
            if (validateCode == null || validateCode.isEmpty()) {
                ToastUtil.show(JVRegisterActivity.this,
                        R.string.register_code_input);
                return;
            }
            createDialog("", true);
            ECAccountManager.getInstance().register(registerUserName, password,
                    validateCode, new ResponseListener<JSONObject>() {

                        public void onSuccess(JSONObject jsonData) {
                            // 关闭Progress提示框

                            ToastUtil.show(JVRegisterActivity.this,
                                    R.string.register_success);

                            finish();
                        }

                        @Override
                        public void onError(RequestError error) {
                            // 关闭Progress提示框

                            ToastUtil.show(JVRegisterActivity.this, error.errmsg);
                            MyLog.e("error = " + error.errmsg);
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
    }

    public boolean checkoutPassword(String password, String confirm) {
        if (RegularUtil.checkUserPwd(password)) {

        } else {
            ToastUtil.show(JVRegisterActivity.this,
                    R.string.register_password_too_short);
            return false;
        }
        if (!password.equals(confirm)) {
            ToastUtil.show(JVRegisterActivity.this,
                    R.string.register_password_error);
            return false;
        }
        return true;
    }

    /**
     * 获取验证码
     */
    private void getValidateCode() {
        registBtn.setEnabled(true);
        hasCode = true;
        createDialog("", true);
        ECAccountManager.getInstance().validateCode(registerUserName,
                new ResponseListener<JSONObject>() {
                    public void onSuccess(JSONObject jsonData) {
                        // 关闭Progress提示框

                            ToastUtil.show(JVRegisterActivity.this,
                                        R.string.register_code_send_success);
                    }

                    @Override
                    public void onError(RequestError error) {
                        // 关闭Progress提示框

                        MyLog.i("error = " + error.errmsg + ",code = "
                                + error.errcode);
                        isShowNotification = false;
                        String msg = error.errmsg + ":"
                                + getString(R.string.register_code_send_error);
                        ToastUtil.show(JVRegisterActivity.this, msg);
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

    /**
     * 在第一步界面上，inputWarnText 显示的信息
     *
     * @param resId
     */
    public void showInputWarn(final int resId, final boolean hasAnim) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                checkLoadingBar.setVisibility(View.GONE);
                inputWarnText.setText(resId);
                inputWarnText.setVisibility(View.VISIBLE);
            }
        });
    }
}
