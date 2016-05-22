package com.joey.expresscall.login;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.account.ECAccountManager;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.general.BaseActivity;
import com.joey.expresscall.R;
import com.joey.general.views.TopBarLayout;
import com.joey.general.utils.*;

public class JVFoundPasswordActivity extends BaseActivity {
	private final int FIRST_TIP = 0;
	private final int SEND_TIP = 1;
	private final int RESET_TIP = 2;
	private final int MAX_TIMES = 60;
	/**
	 * 标题栏
	 */
	private TopBarLayout mTopBarView;
	private View inputUserLayout;
	private ProgressBar checkLoadingBar;
	private EditText usernameEdit;
	private Button firstNextButton;
	private TextView inputUserWarnText;
	private EditText validateCodeEdit;
	private Button sendCodeBtn;
	private TextView codeWarnText;
	private View resetPwdLayout;
	private EditText resetUsernameText;
	private EditText passwordEdit;
	private EditText confirmEdit;
	// private Button sendNextButton;
	private Button foundPwdButton;
	private int currentIndex;
	private String userName;
	private boolean accountIsPhone;
	private boolean isShowNotification;
	private int lostTimes;
	private String codeWarnStr;
	private Runnable resetCodeNotificationRunnalble = new Runnable() {

		@Override
		public void run() {
			if (!codeWarnText.isShown())
				codeWarnText.setVisibility(View.VISIBLE);
			lostTimes--;
			if (isShowNotification) {
				if (lostTimes > 0) {
					sendCodeBtn.setEnabled(false);
					sendCodeBtn.setText(lostTimes + "S");
					handler.postDelayed(resetCodeNotificationRunnalble, 1000);
					return;
				}
			}
			sendCodeBtn.setEnabled(true);
			sendCodeBtn.setText(R.string.send_validate_code);
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
			// 第一步中的【下一步】按钮
			case R.id.found_pwd_first_tip_button:
				userName = usernameEdit.getText().toString();
				if (userName.isEmpty()) {
					showInputWarn(R.string.found_user_empty, true);
					return;
				}
				checkLoadingBar.setVisibility(View.VISIBLE);
				inputUserWarnText.setVisibility(View.GONE);
				checkoutUserExists(userName);
				break;
			// 【找回密码】按钮
			case R.id.found_pwd_ok_button:
				String pwd = passwordEdit.getText().toString();
				String confirm = confirmEdit.getText().toString();
				String code = validateCodeEdit.getText().toString();
				if (checkoutPassword(pwd, confirm)) {
					if (code.isEmpty()) {
						return;
					}
					createDialog(R.string.setting, true);
					resetPassword(pwd, confirm, code);
				}
				break;
			// 发送验证码
			case R.id.found_pwd_code_button:
				view.setEnabled(false);
				isShowNotification = true;
				lostTimes = MAX_TIMES;
				codeWarnStr = getResources().getString(
						R.string.found_send_code_waiting);
				handler.post(resetCodeNotificationRunnalble);
				getValidateCode();
				break;
			}
		}

	};
	private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				Drawable right = getResources().getDrawable(
						R.drawable.icon_edit_right_arrow);
				String password = passwordEdit.getText().toString();
				String confirm = confirmEdit.getText().toString();
				String code = validateCodeEdit.getText().toString();
				switch (v.getId()) {
				case R.id.found_pwd_edittext:
					if (password == null || password.length() < 6) {
						ToastUtil.show(JVFoundPasswordActivity.this,
								R.string.register_password_too_short);
						passwordEdit.setCompoundDrawablesWithIntrinsicBounds(
								null, null, null, null);
						return;
					}

					if (RegularUtil.checkUserPwd(password)) {
						passwordEdit.setCompoundDrawablesWithIntrinsicBounds(
								null, null, right, null);
					} else {
						ToastUtil.show(JVFoundPasswordActivity.this,
								R.string.register_password_too_short);
						passwordEdit.setCompoundDrawablesWithIntrinsicBounds(
								null, null, null, null);
						return;
					}

					break;
				case R.id.found_pwd_confirm_edittext:
					if (!password.equals(confirm)) {
						ToastUtil.show(JVFoundPasswordActivity.this,
								R.string.register_password_error);
						confirmEdit.setCompoundDrawablesWithIntrinsicBounds(
								null, null, null, null);
						return;
					}
					confirmEdit.setCompoundDrawablesWithIntrinsicBounds(null,
							null, right, null);
					break;
				case R.id.found_pwd_code_edittext:
					break;
				}
			}
		}
	};

	@Override
	public void initSettings() {
		userName = getIntent().getStringExtra(
				MySharedPreferencesConsts.USERNAME);
	}

	@Override
	public void initUi() {
		inputUserLayout = View.inflate(this,
				R.layout.foundpwd_first_tip_layout, null);
		resetPwdLayout = View.inflate(this, R.layout.foundpwd_reset_pwd_layout,
				null);
		/**
		 * 输入账号布局
		 */
		firstNextButton = (Button) inputUserLayout
				.findViewById(R.id.found_pwd_first_tip_button);
		usernameEdit = (EditText) inputUserLayout
				.findViewById(R.id.found_pwd_username_edittext);
		inputUserWarnText = (TextView) inputUserLayout
				.findViewById(R.id.found_pwd_warn_textview);
		checkLoadingBar = (ProgressBar) inputUserLayout
				.findViewById(R.id.found_pwd_check_progressbar);
		if (userName != null) {
			usernameEdit.setText(userName);
		}

		/**
		 * 重设密码布局
		 */
		validateCodeEdit = (EditText) resetPwdLayout
				.findViewById(R.id.found_pwd_code_edittext);
		sendCodeBtn = (Button) resetPwdLayout
				.findViewById(R.id.found_pwd_code_button);
		codeWarnText = (TextView) resetPwdLayout
				.findViewById(R.id.found_pwd_code_warn_textview);
		resetUsernameText = (EditText) resetPwdLayout
				.findViewById(R.id.found_pwd_reset_user_textview);
		passwordEdit = (EditText) resetPwdLayout
				.findViewById(R.id.found_pwd_edittext);
		confirmEdit = (EditText) resetPwdLayout
				.findViewById(R.id.found_pwd_confirm_edittext);
		foundPwdButton = (Button) resetPwdLayout
				.findViewById(R.id.found_pwd_ok_button);
		foundPwdButton.setEnabled(false);
		sendCodeBtn.setOnClickListener(mOnClickListener);
		firstNextButton.setOnClickListener(mOnClickListener);
		foundPwdButton.setOnClickListener(mOnClickListener);
		passwordEdit.setOnFocusChangeListener(mOnFocusChangeListener);
		confirmEdit.setOnFocusChangeListener(mOnFocusChangeListener);
		validateCodeEdit.setOnFocusChangeListener(mOnFocusChangeListener);

		mTopBarView = getTopBarView();
		mTopBarView.setTopBar(R.drawable.icon_back, -1, R.string.findpass,
				mOnClickListener);
		gotoFirstLayout();
	}

	// private void gotoSendCodeLayout() {
	// currentIndex = SEND_TIP;
	// runOnUiThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// setContentView(inputCodeLayout);
	// if (accountIsPhone)
	// usernameText.setText("+86    " + userName);
	// else {
	// usernameText.setText(userName);
	// }
	// isShowNotification = false;
	// lostTimes = MAX_TIMES;
	// handler.removeCallbacks(resetCodeNotificationRunnalble);
	// codeWarnText.setVisibility(View.GONE);
	// }
	//
	// });
	//
	// }

	private void gotoFirstLayout() {
		currentIndex = FIRST_TIP;
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				setContentView(inputUserLayout);
				checkLoadingBar.setVisibility(View.GONE);
				inputUserWarnText.setVisibility(View.GONE);
			}

		});
	}

	private void gotoResetPwdLayout() {
		currentIndex = RESET_TIP;
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				setContentView(resetPwdLayout);
				if (accountIsPhone)
					resetUsernameText.setText("+86  " + userName);
				else {
					resetUsernameText.setText(userName);
				}
				sendCodeBtn.setEnabled(true);
				isShowNotification = false;
				lostTimes = MAX_TIMES;
				handler.removeCallbacks(resetCodeNotificationRunnalble);
				codeWarnText.setVisibility(View.GONE);
			}

		});
	}

	private void checkoutUserExists(final String user) {
		ECAccountManager.getInstance().isAccountExist(user,
				new ResponseListener<JSONObject>() {
					// 账号库反馈
					@Override
					public void onSuccess(JSONObject jsonData) {
						MyLog.i("jsonData = " + jsonData.toString());
						if (jsonData instanceof JSONObject) {
							// 账号不存在！
							if (!jsonData.containsKey("isExist")) {
								showInputWarn(R.string.found_user_not_exist,
										false);
								return;
							}
							boolean isExist = (jsonData)
									.getBooleanValue("isExist");
							// // 账号存在，提示
							if (isExist) {
								accountIsPhone = isPhoneNum(user);
								// if (accountIsPhone) {
								codeWarnStr = getResources().getString(
										R.string.found_send_code2phone);
								codeWarnText
										.setText(R.string.found_send_code2phone);
								// } else {
								// codeWarnStr = getResources().getString(
								// R.string.found_send_code2mail);
								// codeWarnText
								// .setText(R.string.found_send_code2mail);
								// }
								return;
							}
							showInputWarn(R.string.found_user_not_exist, false);
						}
					}

					@Override
					public void onError(RequestError error) {
						if (error != null) {
							ToastUtil.show(JVFoundPasswordActivity.this,
									error.errmsg);
						}
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
	 * 获取验证码
	 */
	private void getValidateCode() {
		ECAccountManager.getInstance().validateCode(userName,
				new ResponseListener<JSONObject>() {

					public void onSuccess(JSONObject jsonData) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								foundPwdButton.setEnabled(true);
								if (accountIsPhone) {
									codeWarnStr = getResources().getString(
											R.string.found_send_code2phone);
									codeWarnText
											.setText(R.string.found_send_code2phone);
								} else {
									codeWarnStr = getResources().getString(
											R.string.found_send_code2mail);
									codeWarnText
											.setText(R.string.found_send_code2mail);
								}
								if (!codeWarnText.isShown())
									codeWarnText.setVisibility(View.VISIBLE);
							}
						});
					}

					@Override
					public void onError(RequestError error) {
						MyLog.i("error = " + error.errmsg + ",code = "
								+ error.errcode);
						// SDK返回的json格式不对，出异常，超时返回的是null
						isShowNotification = false;
						String msg = error.errmsg + ":"
								+ getString(R.string.register_code_send_error);
						ToastUtil.show(JVFoundPasswordActivity.this, msg);
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

	public boolean checkoutPassword(String password, String confirm) {
		if (RegularUtil.checkUserPwd(password)) {

		} else {
			ToastUtil.show(JVFoundPasswordActivity.this,
					R.string.register_password_too_short);
			return false;
		}
		if (!password.equals(confirm)) {
			ToastUtil.show(JVFoundPasswordActivity.this,
					R.string.register_password_error);
			return false;
		}
		return true;
	}

	/**
	 * 重置密码
	 * 
	 * @param newPwd
	 *            新密码
	 * @param confirmPwd
	 *            确认密码
	 * @param validateCode
	 *            验证码
	 */
	private void resetPassword(String newPwd, String confirmPwd,
			String validateCode) {
		ECAccountManager.getInstance().forgetPassword(userName, newPwd,
				validateCode, new ResponseListener<JSONObject>() {

					@Override
					public void onSuccess(JSONObject json) {
						statusHashMap.put(MySharedPreferencesConsts.USERNAME,
								userName);
						statusHashMap.put(MySharedPreferencesConsts.PASSWORD,
								"");
						/**
						 * 判断忘记密码的账号是否是当前登录的账号
						 */
						String lastUser = MySharedPreference.getInstance()
								.getString(MySharedPreferencesConsts.USERNAME,
										null);
						if (userName.equals(lastUser)) {
							MySharedPreference.getInstance().putString(
									MySharedPreferencesConsts.PASSWORD, "");
						}
						dismissDialog();
						ToastUtil.show(JVFoundPasswordActivity.this,
								R.string.found_pwd_success);
						finish();
					}

					@Override
					public void onError(RequestError error) {
						dismissDialog();
						if (error != null) {
							ToastUtil.show(JVFoundPasswordActivity.this,
									error.errmsg);
						}
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

	@Override
	public void onBackPressed() {
		switch (currentIndex) {
		case FIRST_TIP:
			JVFoundPasswordActivity.this.finish();
		case SEND_TIP:
			gotoFirstLayout();
			break;
		case RESET_TIP:
			// gotoSendCodeLayout();
			gotoFirstLayout();
			break;
		}
	}

	@Override
	public void saveSettings() {

	}

	@Override
	public void freeMe() {

	}

	/**
	 * 检查是否为手机号
	 * 
	 * @param user
	 * @return
	 */
	private boolean isPhoneNum(String user) {
		MyLog.i("user = " + user + ",length = " + user.length());
		if (user.length() != 11) {
			return false;
		}
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
			return true;
		}
		return false;
	}

	/**
	 * 在第一步界面上，inputWarnText 显示的信息
	 * 
	 * @param resId
	 */
	private void showInputWarn(final int resId, final boolean isAnim) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				checkLoadingBar.setVisibility(View.GONE);
				inputUserWarnText.setText(resId);
				inputUserWarnText.setVisibility(View.VISIBLE);
			}
		});
	}
}
