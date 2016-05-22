package com.joey.expresscall.setting;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECAccountManager;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.general.BaseActivity;
import com.joey.general.utils.CommonUtil;
import com.joey.general.utils.MySharedPreference;
import com.joey.general.utils.ToastUtil;
import com.joey.general.views.TopBarLayout;

/**
 * 修改昵称
 */
public class ECModifyNicknameActivity extends BaseActivity implements
		OnClickListener {

	private EditText mNewNick;
	private Button mConfirm;
	/**
	 * 账号操作对象句柄
	 */
	private ECAccountManager mAccontHandle;
	/**
	 * 标题栏
	 */
	private TopBarLayout mTopBarView;
	// 输入正确的图标
	private Drawable mInputRightIcon;
	// 信息提示文本
	private TextView mTips;

	@Override
	public void initSettings() {
		mAccontHandle = ECAccountManager.getInstance();
		mInputRightIcon = getResources().getDrawable(R.drawable.icon_right_arrow);
		mInputRightIcon.setBounds(0, 0, mInputRightIcon.getIntrinsicWidth(),
				mInputRightIcon.getIntrinsicHeight());
	}

	@Override
	public void initUi() {
		setContentView(R.layout.activity_modify_nickname);
		mTopBarView = getTopBarView();
		mTopBarView.setTopBar(R.drawable.icon_back, -1, "修改昵称", this);
		mNewNick = (EditText) this.findViewById(R.id.nickname_new);
		mTips = (TextView) findViewById(R.id.tv_error_tips);
		mConfirm = (Button) this.findViewById(R.id.nickname_confirm);
		mConfirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.nickname_confirm:
			modifyNickname();
			break;
		case R.id.left_btn:
			closeActivity();
			break;
		}
	}

	/**
	 * 修改昵称
	 */
	private void modifyNickname() {
		final String nick = CommonUtil.stringFilter(mNewNick.getText()
				.toString());
		if (TextUtils.isEmpty(nick)) {
			setErrorTipsVisible(R.string.tips_nick_not_empty);
			return;
		}

		if (nick.length() > 10) {
			setErrorTipsVisible(R.string.tips_nick_length_error);
			return;
		}

		// 显示正确图标
		setRightIconVisible(true);
		// 调用修改昵称接口
		createDialog("", false);
		mAccontHandle.modifyNickName(nick, new ResponseListener<JSONObject>() {

			public void onSuccess(JSONObject json) {
				// 关闭Progress提示框
				MySharedPreference.getInstance().putString("nickname", nick);

				closeActivity();
			}

			@Override
			public void onError(RequestError error) {
				// 关闭Progress提示框

				ToastUtil.show(ECModifyNicknameActivity.this, error.errmsg);
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						createDialog(R.string.waiting, true);
					}
				});
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						dismissDialog();
					}
				});
			}
		});
	}

	/**
	 * 关闭Activity
	 */
	private void closeActivity() {
		finish();
		overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
	}

	/**
	 * 设置右侧正确图标的显示与隐藏
	 * 
	 * @param visible
	 */
	private void setRightIconVisible(boolean visible) {
		Drawable right = visible ? mInputRightIcon : null;
		Drawable[] drawable = mNewNick.getCompoundDrawables();
		mNewNick.setCompoundDrawables(drawable[0], drawable[1], right,
				drawable[3]);
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

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
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
