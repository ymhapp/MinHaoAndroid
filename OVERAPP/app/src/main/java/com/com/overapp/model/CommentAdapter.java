package com.com.overapp.model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.overapp.R;

import java.util.List;

/**
 * Created by acer on 2016/11/26.
 */
public class CommentAdapter extends BaseAdapter {
    public List<CommentItemBean> mList;
    public LayoutInflater mInflater;

    public CommentAdapter(Context context, List<CommentItemBean> list) {
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
            convertView = mInflater.inflate(R.layout.comment_list, null);
        }

        TextView usernaem = (TextView) convertView.findViewById(R.id.text_username);
        TextView time = (TextView) convertView.findViewById(R.id.text_time);
        TextView comment = (TextView) convertView.findViewById(R.id.text_comment);

        CommentItemBean bean = mList.get(position);
        usernaem.setText(bean.UserName);
        time.setText(bean.Time);
        comment.setText(bean.Comment);

        return convertView;
    }
}

