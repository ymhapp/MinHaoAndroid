package com.example.overapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.com.overapp.model.LatLot;
import com.com.overapp.model.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MeActivity extends AppCompatActivity {
    public final static String SER_KEY = "com.andy.ser";
    private String str_account;
    private String str_psd;
    private String str_nickname;

    private TextView musernickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        // 新页面接收数据
        LatLot latLot = (LatLot) getIntent().getSerializableExtra(MainActivity.SER_KEY);
        str_account = latLot.getStr_account();
        str_psd = latLot.getStr_psd();
        getView();
        queryNmae();


    }

    private void queryNmae() {
        BmobQuery<User> query = new BmobQuery<User>();
        //查询用户信息
        query.addWhereEqualTo("userAccount", str_account);
        query.setLimit(1);
        //执行查询方法
        query.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    for (User user : list) {
                        //获得nickanme的信息
                        str_nickname=user.getUserNickName();
                        System.out.println("nickname:" + str_nickname);
                        musernickname.setText(str_nickname);
                    }
                } else {
                      System.out.println("查询失败");
                }
            }
        });
    }

    private void getView() {
        musernickname = (TextView) findViewById(R.id.usernickname);
    }

}
