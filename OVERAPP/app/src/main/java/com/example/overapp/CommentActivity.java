package com.example.overapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.com.overapp.model.Comment;
import com.com.overapp.model.CommentAdapter;
import com.com.overapp.model.CommentItemBean;
import com.com.overapp.model.LatLot;
import com.com.overapp.model.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class CommentActivity extends AppCompatActivity {
    private List<CommentItemBean> commentItemBeansList = new ArrayList<>();

    private List<String> commentlist = new ArrayList<String>(); //接收查询菜单图片结果的list
    private List<String> commentuser = new ArrayList<String>();//接收查询菜单名字结果的List
    private List<String> commenttime = new ArrayList<String>();//接收查询菜单名字结果的List

    private ListView cmlistview;
    private EditText commentInput;
    private Button comment_send_button;

    private String comment_text;
    private String userName;
    //出发地与目的地的地址
    private String str_shopname;
    private String str_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getView();

        final LatLot latLot = (LatLot) getIntent().getSerializableExtra(MainActivity.SER_KEY);
        //接收进入的店铺名字以及用户帐号
        str_shopname = latLot.getShopname();
        str_account = latLot.getStr_account();
        System.out.println("66666" + str_account);

        //取得ActionBar对象
        ActionBar actionBar = getSupportActionBar();
        //调用show方法，展示actionbar
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("评论");


        queryUserName();
        System.out.println("+++++" + userName);
        queryNmae();


        //评论发送按钮的监听
        comment_send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入框中的内容
                comment_text = commentInput.getText().toString();

                Comment comment = new Comment();
                comment.setUserAccount(str_account);
                comment.setUserName(userName);
                comment.setComment(comment_text);
                comment.setShopName(str_shopname);
                comment.save(new SaveListener<String>() {

                    @Override
                    public void done(String s, BmobException e) {
                        Toast.makeText(CommentActivity.this, "评论成功", Toast.LENGTH_LONG).show();
                        // 点击button跳转到导航页面
                        commentItemBeansList.removeAll(commentItemBeansList);
                        CommentAdapter commentAdapter = new CommentAdapter(CommentActivity.this, commentItemBeansList);
                        commentAdapter.notifyDataSetChanged();
                    }
                });


                finish();
            }
        });

    }

    //查询该帐号的用户名称
    private void queryUserName() {
        BmobQuery<User> query = new BmobQuery<User>();
        //查询用户信息
        query.addWhereEqualTo("userAccount", str_account);
        query.setLimit(1);
        //执行查询方法
        query.findObjects(new FindListener<User>() {
                              @Override
                              public void done(List<User> list, BmobException e) {
                                  if (e == null && list.size() > 0) {
                                      for (User user : list) {
                                          //获得nickanme的信息
                                          userName = user.getUserNickName();
                                          System.out.println("++++++1213" + userName);
                                      }
                                  } else {
                                      System.out.println("+++++++2" + e);
                                  }
                              }
                          }
        );

    }

    private void getView() {
        cmlistview = (ListView) findViewById(R.id.commentlistView);
        commentInput = (EditText) findViewById(R.id.commentInput);
        comment_send_button = (Button) findViewById(R.id.comment_send_button);

    }

    //查询评论的用户名，内容和发布时间
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
                                          commentuser.add(user.getUserName());
                                          commentlist.add(user.getComment());
                                          commenttime.add(user.getCreatedAt());
//                                          System.out.println("++++"+commentuser);

                                      }


                                      for (int i = 0; i < commentlist.size(); i++) {
                                          commentItemBeansList.add(new CommentItemBean(commentuser.get(i).toString(),
                                                  commenttime.get(i).toString(), commentlist.get(i).toString()));
                                      }
                                      CommentAdapter commentAdapter = new CommentAdapter(CommentActivity.this, commentItemBeansList);
                                      cmlistview.setAdapter(commentAdapter);
                                  } else {
                                      System.out.println("+++++++1" + e);
                                  }
                              }
                          }
        );
    }

    //点击返回键推出
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
