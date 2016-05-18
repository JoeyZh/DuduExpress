package com.joey.expresscall.common;

import com.joey.expresscall.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ECSearchView extends RelativeLayout {

	private TextView cancelButton;
	private ImageButton clearButton;
	private EditText inputText;
	private ListView results;
	private View searchParent;

	public static final int SEARCH_VIEW_IDLE = 0;
	public static final int SEARCH_VIEW_INPUT = 1;
	private int state;

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
//			clearButton.setVisibility(View.VISIBLE);

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}
	};

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_search_view_cancel:
				initState();
				break;
			case R.id.btn_search_view_clear:
//				clearInput();
				break;
			case R.id.search_view_root:
				if (inputText.isEnabled())
					return;
				startSearch();
				break;
			}

		}
	};

	public ECSearchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initUI();
	}

	public ECSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUI();
	}

	public ECSearchView(Context context) {
		super(context);
		initUI();
	}

	private void initUI() {
		View.inflate(getContext(), R.layout.search_view_layout, this);
		cancelButton = (TextView) findViewById(R.id.btn_search_view_cancel);
		clearButton = (ImageButton) findViewById(R.id.btn_search_view_clear);
		results = (ListView) findViewById(R.id.list_search_result);
		inputText = (EditText) findViewById(R.id.edit_search_view);
		searchParent = findViewById(R.id.search_view_root);
		inputText.addTextChangedListener(watcher);
		cancelButton.setOnClickListener(mOnClickListener);
		clearButton.setOnClickListener(mOnClickListener);
		searchParent.setOnClickListener(mOnClickListener);
		initState();
	}

	private void initState() {
		clearButton.setVisibility(View.GONE);
		results.setVisibility(View.GONE);
		cancelButton.setText("");
		inputText.setEnabled(false);
		state = SEARCH_VIEW_IDLE;
		RelativeLayout.LayoutParams params = (LayoutParams) inputText.getLayoutParams();
		params.width = LayoutParams.WRAP_CONTENT;
		inputText.setLayoutParams(params);
		RelativeLayout.LayoutParams params2 = (LayoutParams) cancelButton.getLayoutParams();
		params2.leftMargin = 0;
		cancelButton.setLayoutParams(params2);
		
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
		
		clearInput();
	}

	public void startSearch() {
		results.setVisibility(View.VISIBLE);
		cancelButton.setText(R.string.cancel);
		state = SEARCH_VIEW_INPUT;
		inputText.setEnabled(true);
		inputText.requestFocus();

		RelativeLayout.LayoutParams params = (LayoutParams) inputText.getLayoutParams();
		params.width = LayoutParams.MATCH_PARENT;
		inputText.setLayoutParams(params);
		RelativeLayout.LayoutParams params2 = (LayoutParams) cancelButton.getLayoutParams();
		params2.leftMargin = getResources().getDimensionPixelSize(R.dimen.layout_mid_margin);
		cancelButton.setLayoutParams(params2);

	}
	
	private void clearInput(){
		inputText.setText("");
	}

	public interface SearchListener {

	}

}
