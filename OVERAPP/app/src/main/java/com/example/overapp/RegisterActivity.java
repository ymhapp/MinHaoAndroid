package com.example.overapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.com.overapp.model.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {
    private String str_compsd;
    private String str_account;
    private String str_password;
    private String str_nickname;

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
        //注册button的监听
        mregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取注册内容
                str_account = macccount.getText().toString();
                str_password = mpsd.getText().toString();
                str_compsd = complete_psd.getText().toString();
                str_nickname = mnickname.getText().toString();

                if (!str_compsd.equals(str_password)) {
                    Toast.makeText(RegisterActivity.this, "注册有误，两次输入密码不一致", Toast.LENGTH_LONG).show();

                } else if (str_account.equals("") || str_password.equals("") || str_nickname.equals("")) {
                    Toast.makeText(RegisterActivity.this, "不能为为空，请全部填写", Toast.LENGTH_LONG).show();
                } else {
                    //上传到Bmob
                    User user = new User();
                    user.setUserAccount(str_account);
                    user.setUserPassWord(str_password);
                    user.setUserNickName(str_nickname);
                    user.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            System.out.println("注册成功");
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


}
