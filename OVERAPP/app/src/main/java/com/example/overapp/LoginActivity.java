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


import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;


import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends AppCompatActivity {

    private String strlang = "langye@163.com";
    private String stralin = "alin@163.com";
    //传递用户信息
    public final static String SER_KEY = "com.andy.ser";
    private String login_account;
    private String login_psd;
    private String bm_psd;

    private EditText mlogin_account;
    private EditText mlogin_psd;
    private Button mlogin;
    private Button mregister;
    BmobPushManager bmobPushManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //初始化BmobSDK
        Bmob.initialize(this, "37167ef9a0a7b5611191400982366aa9");
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation().save();
        // 启动推送服务
        BmobPush.startWork(this);

        setContentView(R.layout.activity_login);

        getView();
        //设置button的透明度
        mlogin.getBackground().setAlpha(100);
        mregister.getBackground().setAlpha(0);
        //登录button的监听
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取EditText中的内容
                login_account = mlogin_account.getText().toString();
                login_psd = mlogin_psd.getText().toString();
                System.out.println("信息" + login_account);

                //查询用户的登录信息并进行验证
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
                            if (bm_psd.equals(login_psd) && login_account.equals(strlang)) {
                                Toast.makeText(LoginActivity.this, "管理员登录成功", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                // 将用户信息传输到首页
                                Bundle bundle = new Bundle();
                                LatLot latlot = new LatLot();
                                latlot.setStr_account(login_account);
                                latlot.setStr_psd(login_psd);
                                bundle.putSerializable(SER_KEY, latlot);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            } else if (bm_psd.equals(login_psd) && login_account.equals(stralin)) {
                                Toast.makeText(LoginActivity.this, "管理员登录成功", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                // 将用户信息传输到首页
                                Bundle bundle = new Bundle();
                                LatLot latlot = new LatLot();
                                latlot.setStr_account(login_account);
                                latlot.setStr_psd(login_psd);
                                bundle.putSerializable(SER_KEY, latlot);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            } else if (bm_psd.equals(login_psd)) {
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, MainActivity.class);
                                // 将用户信息传输到首页
                                Bundle bundle = new Bundle();
                                LatLot latlot = new LatLot();
                                latlot.setStr_account(login_account);
                                latlot.setStr_psd(login_psd);
                                bundle.putSerializable(SER_KEY, latlot);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            } else if (!bm_psd.equals(login_psd)) {
                                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "请输入正确的账号密码", Toast.LENGTH_LONG).show();
                        }
                    }
                });

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
