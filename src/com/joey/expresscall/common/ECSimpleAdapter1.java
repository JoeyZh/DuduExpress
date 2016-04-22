package com.joey.expresscall.common;

import java.util.List;
import java.util.Map;

import com.joey.expresscall.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class ECSimpleAdapter1 extends SimpleAdapter{
	
	public static final int SIMPLE_ADAPTER_TYPE_LOGO = 0;
	public static final int SIMPLE_ADAPTER_TYPE_TAG = 1;
	
	private int adapterType = SIMPLE_ADAPTER_TYPE_LOGO;
	protected List<? extends Map<String, ?>> mData;
	private Context mContext;

	public ECSimpleAdapter1(Context context,
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
		switch(adapterType){
			case SIMPLE_ADAPTER_TYPE_LOGO:
				view.findViewById(R.id.item_text_tag).setVisibility(View.GONE);
				view.findViewById(R.id.item_img_logo).setVisibility(View.VISIBLE);
				break;
			case SIMPLE_ADAPTER_TYPE_TAG:
				view.findViewById(R.id.item_img_logo).setVisibility(View.GONE);
				view.findViewById(R.id.item_text_tag).setVisibility(View.VISIBLE);

				break;
		}
		
		return view;
	}



	/**
	 * 
	 * @param type 
	 * {@link #SIMPLE_ADAPTER_TYPE_LOGO }
	 * {@link #SIMPLE_ADAPTER_TYPE_TAG }
	 */
	public void setType(int type){
		this.adapterType = type;
	}
	
}
