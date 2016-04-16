package com.joey.expresscall.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.joey.expresscall.storage.JVAccount;
import com.joey.expresscall.R;

import java.util.ArrayList;

public class JVAccountAdapter extends BaseAdapter {

    private ArrayList<JVAccount> data;
    private Context mContext;
    private LayoutInflater inflater;
    private OnClickListener deleteListener;

    public JVAccountAdapter(Context context, int layout,
                            ArrayList<JVAccount> data) {
        mContext = context;
        this.data = data;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<JVAccount> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (data == null)
            return 0;
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (data == null || position > data.size())
            return null;
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.login_account_item, null);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.content = (TextView) convertView
                    .findViewById(R.id.account_item_content_text);
            holder.deleteBtn = (ImageButton) convertView
                    .findViewById(R.id.account_item_delete_btn);
        }
        convertView.setTag(holder);
        holder.content.setText(data.get(position).getUsername());
        holder.deleteBtn.setOnClickListener(deleteListener);
        holder.deleteBtn.setTag(data.get(position));
        return convertView;
    }

    public void setOnClickListener(OnClickListener l) {
        deleteListener = l;
    }

    private class ViewHolder {
        TextView content;
        ImageButton deleteBtn;
    }
}
