package com.joey.expresscall.common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joey.expresscall.R;
import com.joey.expresscall.contacts.library.ContactsSortAdapter;
import com.joey.general.utils.MyLog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SimpleAdapter;

public class ECSimpleAdapter1 extends SimpleAdapter{
	
	public static final int SIMPLE_ADAPTER_TYPE_LOGO = 0;
	public static final int SIMPLE_ADAPTER_TYPE_TAG = 1;
	
	protected int adapterType = SIMPLE_ADAPTER_TYPE_LOGO;
	protected List<? extends Map<String, ?>> mData;
	private Context mContext;
	protected boolean enable;
	protected boolean isAllChecked;

	private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			MyLog.e("$$$$$$$$$ ="+isChecked);
			if(!isChecked){
				isAllChecked = false;
			}
			if(buttonView.getTag() instanceof  Integer){
				int position = (Integer) buttonView.getTag();
				HashMap<String,Object> map = (HashMap<String,Object>) getItem(position);
				map.put("checked",(Boolean)isChecked);
			}
			MyLog.e("size = "+getSelectList().size()+"\n"+getSelectList().toString());

		}
	};

	public ECSimpleAdapter1(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		mData = data;
		mContext = context;
	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		if( view instanceof  CheckableLayout){
			((CheckableLayout) view).setOnCheckedChangeListener(checkedChangeListener);
			ViewHolder holder;
			if(view.getTag() == null){
				holder = new ViewHolder();
				holder.checkBox = (CheckBox) view.findViewById(R.id.item_checkbox);
				view.setTag(holder);
			}
			holder = (ViewHolder) view.getTag();
			holder.checkBox.setTag(position);
		}
		if(enable){
			view.findViewById(R.id.item_checkbox).setVisibility(View.VISIBLE);
		}else{
			view.findViewById(R.id.item_checkbox).setVisibility(View.GONE);
		}
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
		if(view instanceof CheckableLayout){
			HashMap<String,Object> map = (HashMap<String,Object>) getItem(position);
			boolean checked = false;
			if(map.containsKey("checked"))
				 checked = (Boolean) map.get("checked");
			((CheckableLayout) view).setChecked(checked);
			map.put("checked",checked);
		}
		return view;
	}

	private class ViewHolder{
		private CheckBox checkBox;
	}
	public void upDateList(ArrayList<HashMap<String,Object>> list){
		mData = list;
		notifyDataSetChanged();
	}
	public void setEnableChecked(boolean enable ){
		this.enable = enable;
		notifyDataSetChanged();
	}

	public void setAllChecked(){
		isAllChecked = true;
		for(HashMap<String,Object> map :(ArrayList<HashMap<String,Object>>)mData )
		{
			map.put("checked",true);
		}
		notifyDataSetChanged();

	}

	public void clearChecked(){
		isAllChecked = false;
		for(HashMap<String,Object> map :(ArrayList<HashMap<String,Object>>)mData )
		{
			map.put("checked",false);
		}
		notifyDataSetChanged();
	}
	public ArrayList<HashMap<String,Object>> getSelectList(){
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
		for(HashMap<String,Object> map :(ArrayList<HashMap<String,Object>>)mData ){
			if(map.containsKey("checked")){
				if((Boolean) map.get("checked")||isAllChecked){
					list.add(map);
				}
			}
		}
		return list;
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
