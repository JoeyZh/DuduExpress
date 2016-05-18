package com.joey.expresscall.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joey.expresscall.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class ECFileItemAdapter extends SimpleAdapter {

	List<? extends Map<String, ?>> mData;
	Context mContext;

	private final String RED = "red";
	private final String BlUE = "blue";

	public ECFileItemAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		mData = data;
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = super.getView(position, convertView, parent);
		HashMap<String, Object> item = (HashMap<String, Object>) getItem(position);

		if (item.containsKey("color")) {
			String color = (String) item.get("color");
			if (color.equalsIgnoreCase(RED)) {
				view.findViewById(R.id.text_logo).setBackground(
						mContext.getResources().getDrawable(
								R.drawable.bg_circle_red));
			} else if (color.equalsIgnoreCase(BlUE)) {
				view.findViewById(R.id.text_logo).setBackground(
						mContext.getResources().getDrawable(
								R.drawable.bg_circle_blue));
			}
		}
		return view;
	}

}
