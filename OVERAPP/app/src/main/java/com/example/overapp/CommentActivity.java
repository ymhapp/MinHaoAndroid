package com.example.overapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.com.overapp.model.Comment;
import com.com.overapp.model.LatLot;

import com.com.overapp.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class CommentActivity extends AppCompatActivity {
    private List<String> commentlist = new ArrayList<String>(); //接收查询菜单图片结果的list
    private List<String> commentuser = new ArrayList<String>();//接收查询菜单名字结果的List
    //出发地与目的地的地址
    private String str_shopname;
    private String str_account;
    private String str_nickname;

    private String str_allcomment;
    private String str_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        final LatLot latLot = (LatLot) getIntent().getSerializableExtra(MainActivity.SER_KEY);

        str_shopname = latLot.getShopname();
        str_account = latLot.getStr_account();
        System.out.println("+++++++" + str_account);
        System.out.println("+++++++" + str_shopname);

        queryNmae();
        System.out.println("+++++++666" + str_nickname);
        Toast.makeText(CommentActivity.this, str_comment, Toast.LENGTH_LONG).show();


    }

    //查询用户昵称
    private void queryNmae() {
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        //查询用户信息
        query.addWhereEqualTo("shopName", str_shopname);
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<Comment>() {
                              @Override
                              public void done(List<Comment> list, BmobException e) {
                                  if (e == null) {
                                      for (Comment user : list) {
                                          //获得nickanme的信息
                                          str_nickname = user.getUserName();
                                          commentlist.add(user.getComment());

                                          System.out.println("+++++++123" + str_nickname);


                                          System.out.println("+++++++123" + commentlist);
                                      }
                                  } else {
                                      System.out.println("+++++++123" + e);
                                  }
                              }
                          }
        );
    }


}
