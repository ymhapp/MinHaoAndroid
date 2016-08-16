package com.example.overapp;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.com.overapp.model.LatLot;
import com.com.overapp.model.Menu;
import com.com.overapp.model.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends AppCompatActivity {
    //传递选中的目的地
    public final static String SER_KEY = "com.andy.ser";
    private String login_account;
    private String login_psd;
    private String bm_psd;

    private EditText mlogin_account;
    private EditText mlogin_psd;
    private Button mlogin;
    private Button mregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Bmob
        //第一：默认初始化
        Bmob.initialize(this, "37167ef9a0a7b5611191400982366aa9");


        setContentView(R.layout.activity_login);

        getView();

        //登录button的监听
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取EditText中的内容
                login_account = mlogin_account.getText().toString();
                login_psd = mlogin_psd.getText().toString();
                System.out.println("信息" + login_account);
;


                final BmobQuery<User> user = new BmobQuery<User>();
                user.addWhereEqualTo("userAccount", login_account);
                user.setLimit(1);
                user.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {
                            for (User user : list) {
                                bm_psd = user.getUserPassWord();
                            }
                            if (bm_psd.equals(login_psd)) {
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MainActivity.class);

                                // 用Bundle携带数据
                                Bundle bundle = new Bundle();
                                LatLot latlot = new LatLot();
                                latlot.setStr_account(login_account);
                                latlot.setStr_psd(login_psd);
                                bundle.putSerializable(SER_KEY, latlot);

                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "请输入正确的账号密码", Toast.LENGTH_LONG).show();
                        }
                    }
                });

//                //--and条件1
//                BmobQuery<User> eq1 = new BmobQuery<User>();
//                eq1.addWhereEqualTo("userAccount", login_account);//年龄<=29
//                //--and条件2
//                BmobQuery<User> eq2 = new BmobQuery<User>();
//                eq2.addWhereEqualTo("userPassWord", login_psd);//年龄>=6
//
//
//                //最后组装完整的and条件
//                List<BmobQuery<User>> andQuerys = new ArrayList<BmobQuery<User>>();
//                andQuerys.add(eq1);
//                andQuerys.add(eq2);
//
//
//                //查询符合整个and条件的人
//                BmobQuery<User> query = new BmobQuery<User>();
//                query.and(andQuerys);
//                query.findObjects(new FindListener<User>() {
//                    @Override
//                    public void done(List<User> list, BmobException e) {
//                        if (e == null) {
//                            System.out.println("成功");
//                            Intent intent = new Intent();
//                            intent.setClass(LoginActivity.this, MainActivity.class);
//
////                             // 用Bundle携带数据
////                            Bundle bundle = new Bundle();
////                            LatLot latlot = new LatLot();
////                            latlot.setStr_account(login_account);
////                            latlot.setStr_psd(login_psd);
////                            bundle.putSerializable(SER_KEY, latlot);
////
////                            intent.putExtras(bundle);
////                            startActivity(intent);
////                            finish();
//                        } else {
//                            Toast.makeText(LoginActivity.this, "登录错误", Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                });

            }


        });

        //注册button的监听
        mregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getView() {
        mlogin_account = (EditText) findViewById(R.id.login_account);
        mlogin_psd = (EditText) findViewById(R.id.login_password);
        mlogin = (Button) findViewById(R.id.login);
        mregister = (Button) findViewById(R.id.register);
    }
}
