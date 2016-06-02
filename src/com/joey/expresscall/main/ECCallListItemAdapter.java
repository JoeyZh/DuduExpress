package com.joey.expresscall.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joey.expresscall.R;
import com.joey.expresscall.common.CheckableLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ECCallListItemAdapter extends SimpleAdapter {

    List<? extends Map<String, ?>> mData;
    Context mContext;
    protected boolean enable;
    protected boolean isAllChecked;

    private final String RED = "red";
    private final String BlUE = "blue";
    private String from[];
    private int to[];

    public ECCallListItemAdapter(Context context,
                                 List<? extends Map<String, ?>> data, int resource, String[] from,
                                 int[] to) {
        super(context, data, resource, from, to);
        mData = data;
        mContext = context;
        this.from = from;
        this.to = to;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = super.getView(position, convertView, parent);
        HashMap<String, Object> item = (HashMap<String, Object>) getItem(position);

        if (item.containsKey("color")) {
            String color = (String) item.get("color");
            if (color.equalsIgnoreCase(RED)) {
                view.findViewById(R.id.text_logo).setBackgroundDrawable(
                        mContext.getResources().getDrawable(
                                R.drawable.bg_circle_red));
            } else if (color.equalsIgnoreCase(BlUE)) {
                view.findViewById(R.id.text_logo).setBackgroundDrawable(
                        mContext.getResources().getDrawable(
                                R.drawable.bg_circle_blue));
            }
        }
        if (enable) {
            view.findViewById(R.id.item_checkbox).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.item_checkbox).setVisibility(View.GONE);
        }
        if (view instanceof CheckableLayout) {
            HashMap<String, Object> map = (HashMap<String, Object>) getItem(position);
            boolean checked = false;
            if (map.containsKey("checked"))
                checked = (Boolean) map.get("checked");
            ((CheckableLayout) view).setChecked(checked);
            map.put("checked", checked);
        }

        for(int i=0;i<to.length;i++) {
            View v = view.findViewById(to[i]);
            Object obj = item.get(from[i]);
            if (v instanceof TextView) {
                if (obj instanceof String) {
                    ((TextView) v).setText(obj.toString());
                    continue;
                }
                if (obj instanceof Integer) {
                    ((TextView) v).setText((Integer) obj);
                }
            }
            if (v instanceof ImageView) {
                if (obj instanceof Bitmap) {
                    ((ImageView) v).setImageBitmap((Bitmap) obj);
                    continue;
                }
                if (obj instanceof Integer) {
                    ((ImageView) v).setImageResource((Integer) obj);
                }
            }
        }
        return view;
    }

    public void setEnableChecked(boolean enable) {
        this.enable = enable;
        notifyDataSetChanged();
    }

    public void setAllChecked() {
        isAllChecked = true;
        for (HashMap<String, Object> map : (ArrayList<HashMap<String, Object>>) mData) {
            map.put("checked", true);
        }
        notifyDataSetChanged();

    }

    public void clearChecked() {
        isAllChecked = false;
        for (HashMap<String, Object> map : (ArrayList<HashMap<String, Object>>) mData) {
            map.put("checked", false);
        }
        notifyDataSetChanged();
    }

    public ArrayList<HashMap<String, Object>> getSelectList() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (HashMap<String, Object> map : (ArrayList<HashMap<String, Object>>) mData) {
            if (map.containsKey("checked")) {
                if ((Boolean) map.get("checked") || isAllChecked) {
                    list.add(map);
                }
            }
        }
        return list;
    }

}
