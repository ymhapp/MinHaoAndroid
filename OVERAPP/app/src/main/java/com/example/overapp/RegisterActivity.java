package com.example.overapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.com.overapp.model.LatLot;
import com.com.overapp.model.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {
    //Bomb中的帐号
    private String bm_account;
    //用户注册信息
    private String reg_compsd;
    private String reg_account;
    private String reg_password;
    private String reg_nickname;

    private Button mregister;
    private AutoCompleteTextView macccount;
    private EditText mpsd;
    private EditText complete_psd;
    private EditText mnickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // 新页面接收数据
        Bundle bundle = this.getIntent().getExtras();

        getview();

        //取得ActionBar对象
        ActionBar actionBar = getSupportActionBar();
        //调用show方法，展示actionbar
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");


        //注册button的监听
        mregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取注册内容
                reg_account = macccount.getText().toString();
                reg_password = mpsd.getText().toString();
                reg_compsd = complete_psd.getText().toString();
                reg_nickname = mnickname.getText().toString();

                //查询是否已经存在相同注册邮箱
                final BmobQuery<User> useraccount = new BmobQuery<User>();
                useraccount.addWhereEqualTo("userAccount", reg_account);
                useraccount.setLimit(1);
                useraccount.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> list, BmobException e) {
                        if (e == null) {
                            Toast.makeText(RegisterActivity.this, "此帐号已被注册", Toast.LENGTH_LONG).show();
                    }
                }
                });

                //进行注册验证
                if (!reg_compsd.equals(reg_password)) {
                    Toast.makeText(RegisterActivity.this, "注册有误，两次输入密码不一致", Toast.LENGTH_LONG).show();

                } else if (reg_account.equals("") || reg_password.equals("") || reg_nickname.equals("")) {
                    Toast.makeText(RegisterActivity.this, "不能为为空，请全部填写", Toast.LENGTH_LONG).show();
                } else {
                    //注册成功，上传到Bmob
                    User user = new User();
                    user.setUserAccount(reg_account);
                    user.setUserPassWord(reg_password);
                    user.setUserNickName(reg_nickname);
                    user.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.setClass(RegisterActivity.this, LoginActivity.class);
                            // 将用户信息传输到首页
                            Bundle bundle = new Bundle();
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });

    }

    private void getview() {
        mregister = (Button) findViewById(R.id.register);
        macccount = (AutoCompleteTextView) findViewById(R.id.account);
        mpsd = (EditText) findViewById(R.id.password);
        complete_psd = (EditText) findViewById(R.id.com_password);
        mnickname = (EditText) findViewById(R.id.nickname);
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
