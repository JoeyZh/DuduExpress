package com.joey.expresscall.setting;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECAccountManager;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.general.BaseActivity;
import com.joey.general.utils.MySharedPreference;
import com.joey.general.utils.MySharedPreferencesConsts;
import com.joey.general.utils.RegularUtil;
import com.joey.general.utils.ToastUtil;
import com.joey.general.views.TopBarLayout;
/**
 * 修改密码
 */
public class ECModifyPwdActivity extends BaseActivity implements OnClickListener {

    /**
     * 标题栏
     */
    private TopBarLayout mTopBarView;
    private EditText mOriginPwd;
    private EditText mNewPwd;
    private EditText mConfirmPwd;
    private Button mSend;
    /**
     * 账号操作对象句柄
     */
    private ECAccountManager mAccontHandle;
    // 输入正确的图标
    private Drawable mInputRightIcon;
    // 信息提示文本
    private TextView mTips;
    // 验证信息
    private boolean isOriginRight, isNewRight, isConfirmRight;
    /**
     * EditText监听事件
     */
    private OnFocusChangeListener mFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                return;
            }

            switch (v.getId()) {
                case R.id.password_origin:
                    checkOriginPwd();
                    break;
                case R.id.password_new:
                    checkNewPwd();
                    break;
                case R.id.password_confirm:
                    checkConfirmPwd();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
	public void initSettings() {
        mAccontHandle = ECAccountManager.getInstance();
        mInputRightIcon = getResources().getDrawable(R.drawable.icon_right_arrow);
        mInputRightIcon.setBounds(0, 0, mInputRightIcon.getIntrinsicWidth(),
                mInputRightIcon.getIntrinsicHeight());
    }

    @Override
	public void initUi() {
        setContentView(R.layout.activity_modify_password);
        /** TopBar设置 */
        mTopBarView = getTopBarView();
        mTopBarView.setTopBar(R.drawable.icon_back, -1, "修改密码", this);

        mOriginPwd = (EditText) this.findViewById(R.id.password_origin);
        mNewPwd = (EditText) this.findViewById(R.id.password_new);
        mConfirmPwd = (EditText) this.findViewById(R.id.password_confirm);
        mTips = (TextView) findViewById(R.id.tv_error_tips);
        mSend = (Button) this.findViewById(R.id.send);
        mSend.setOnClickListener(this);

        mOriginPwd.setOnFocusChangeListener(mFocusChangeListener);
        mNewPwd.setOnFocusChangeListener(mFocusChangeListener);
        mConfirmPwd.setOnFocusChangeListener(mFocusChangeListener);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.send:
                modifyPassword();
                break;
            case R.id.left_btn:
                finish();
                overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
    }

    /**
     * 修改密码
     */
    private void modifyPassword() {

        // 检测表单信息
        View view = getWindow().getDecorView().findFocus();
        view.clearFocus();
        if (!(isOriginRight && isNewRight && isConfirmRight)) {
            // 哪一步出错了,重新进行检查
            if (!isOriginRight) {
                return;
            } else if (!isNewRight) {
                checkNewPwd();
                return;
            } else if (!isConfirmRight) {
                checkConfirmPwd();
                return;
            }
        }

        String originPwd = mOriginPwd.getText().toString();
        String newPwd = mNewPwd.getText().toString();
        String againPwd = mConfirmPwd.getText().toString();
        // 账号名称
        String accountName = (String) statusHashMap.get(MySharedPreferencesConsts.USERNAME);
        // 调用修改密码的接口
        createDialog(R.string.setting, false);
        mAccontHandle.modifyPwd(originPwd, newPwd,
                new ResponseListener<JSONObject>() {
                    public void onSuccess(JSONObject jsonData) {
                        // 关闭Progress提示框
         

                    }

                    @Override
                    public void onError(RequestError error) {
                        // 关闭Progress提示框
                        ToastUtil.show(ECModifyPwdActivity.this, error.errmsg);
                    }

					@Override
					public void onStart() {
												
					}

					@Override
					public void onFinish() {
						
					}
                });
    }

    /**
     * 检测旧密码
     */
    private void checkOriginPwd() {
        isOriginRight = false;
        setRightIconVisible(mOriginPwd, false);
        String originPwd = mOriginPwd.getText().toString();
        if (TextUtils.isEmpty(originPwd)) {
            setErrorTipsVisible(R.string.tips_old_pwd_not_empty);
            return;
        }

        if (!originPwd.equals(MySharedPreference.getInstance()
                .getString(MySharedPreferencesConsts.PASSWORD))) {
            setErrorTipsVisible(R.string.tips_old_pwd_error);
            return;
        }
        isOriginRight = true;
        setRightIconVisible(mOriginPwd, true);
        setErrorTipsVisible(-1);
    }

    /**
     * 检测新密码
     */
    private boolean checkNewPwd() {
        if (!isOriginRight) {
            return false;
        }

        String originPwd = mOriginPwd.getText().toString();
        String newPwd = mNewPwd.getText().toString();


        isNewRight = false;
        setRightIconVisible(mNewPwd, false);
        if (TextUtils.isEmpty(newPwd)) {
            setErrorTipsVisible(R.string.tips_new_pwd_not_empty);
            return isNewRight;
        }
//        if (!newPwd.matches("^[A-Za-z0-9_]+$")) {
//            setErrorTipsVisible(R.string.tips_new_pwd_format_error);
//            return isNewRight;
//        }
//        if (newPwd.length() < 6 || newPwd.length() > 20) {
//            setErrorTipsVisible(R.string.tips_new_pwd_length_error);
//            return isNewRight;
//        }

        if (RegularUtil.checkUserPwd(newPwd)) {

        } else {
            ToastUtil.show(ECModifyPwdActivity.this,
                    R.string.register_password_too_short);
            return isNewRight;
        }
        if (newPwd.equals(originPwd)) {
            setErrorTipsVisible(R.string.tips_old_new_pwd_equal);
            return isNewRight;
        }
        isNewRight = true;
        setRightIconVisible(mNewPwd, true);
        setErrorTipsVisible(-1);
        return isNewRight;
    }

    /**
     * 检测确认新密码
     */
    private boolean checkConfirmPwd() {
        if (!isOriginRight || !isNewRight) {
            return false;
        }

        String newPwd = mNewPwd.getText().toString();
        String againPwd = mConfirmPwd.getText().toString();
        isConfirmRight = false;
        setRightIconVisible(mConfirmPwd, false);
        if (!newPwd.equals(againPwd)) {
            setErrorTipsVisible(R.string.tips_new_confirm_pwd_not_equal);
            return isConfirmRight;
        }
        isConfirmRight = true;
        setRightIconVisible(mConfirmPwd, true);
        setErrorTipsVisible(-1);
        return isConfirmRight;
    }

    /**
     * 设置右侧正确图标的显示与隐藏
     *
     * @param visible
     */
    private void setRightIconVisible(EditText view, boolean visible) {
        Drawable right = visible ? mInputRightIcon : null;
        Drawable[] drawable = view.getCompoundDrawables();
        view.setCompoundDrawables(drawable[0], drawable[1], right, drawable[3]);
    }

    /**
     * 设置错误提示信息的显示与隐藏
     */
    private void setErrorTipsVisible(int errorResId) {
        String error = "";
        if (errorResId != -1) {
            error = getResources().getString(errorResId);
        }
        setErrorTipsVisible(error);
    }

    private void setErrorTipsVisible(String error) {
        if (TextUtils.isEmpty(error)) {
            mTips.setText("");
            if (mTips.getVisibility() != View.INVISIBLE) {
                mTips.setVisibility(View.INVISIBLE);
            }
        } else {
            mTips.setText(error);
            if (mTips.getVisibility() != View.VISIBLE) {
                mTips.setVisibility(View.VISIBLE);
            }
        }
    }

    // --------------------------------------------------------
    // ## 继承的一些方法
    // --------------------------------------------------------

    @Override
	public void saveSettings() {

    }

    @Override
	public void freeMe() {
    }

}
