package com.example.overapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.com.overapp.model.MenuItemBean;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by acer on 2016/8/8.
 */
public class MyAdapter extends BaseAdapter {
    public List<MenuItemBean> mList;
    public LayoutInflater mInflater;

    public MyAdapter(Context context, List<MenuItemBean> list) {
        mList = list;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
        }
        TextView imageView = (TextView) convertView.findViewById(R.id.pic);
        TextView text=(TextView) convertView.findViewById(R.id.text);
        TextView itemPrice=(TextView)convertView.findViewById(R.id.price);

        MenuItemBean bean=mList.get(position);
        imageView.setText(bean.ItemImageResid);
        text.setText(bean.ItemContent);
        itemPrice.setText(bean.ItemPrice);
        return convertView;
    }
}
