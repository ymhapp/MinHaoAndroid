package com.com.overapp.model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.overapp.MainActivity;
import com.example.overapp.MeActivity;
import com.example.overapp.R;

import java.util.List;

/**
 * Created by acer on 2016/8/15.
 */
public class CollectionAdapter extends BaseAdapter {
    public List<CollectionItemBean> mList;
    public LayoutInflater mInflater;

    public CollectionAdapter(Context context, List<CollectionItemBean> list) {
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
            convertView = mInflater.inflate(R.layout.collection_item, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.text_shop);
        TextView best = (TextView) convertView.findViewById(R.id.text_best);
        TextView add = (TextView) convertView.findViewById(R.id.text_add);

        CollectionItemBean bean = mList.get(position);
        name.setText(bean.ItemName);
        best.setText(bean.ItemBest);
        add.setText(bean.ItemAddress);

        return convertView;
    }

}
