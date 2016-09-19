package com.example.overapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.com.overapp.model.Collection;
import com.com.overapp.model.LatLot;
import com.com.overapp.model.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class CorrectActivity extends AppCompatActivity {

    private String user_obj;

    public final static String SER_KEY = "com.andy.ser";
    private String str_account;
    private String mpsd;
    private String compsd;
    private String mnickname;

    private EditText text_psd;
    private EditText text_compsd;
    private EditText text_nickname;
    private Button btn_correct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct);
        // 新页面接收数据
        LatLot latLot = (LatLot) getIntent().getSerializableExtra(MainActivity.SER_KEY);
        str_account = latLot.getStr_account();
        getView();
        queryUserOBJ();
        btn_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpsd = text_psd.getText().toString();
                compsd = text_compsd.getText().toString();
                mnickname = text_nickname.getText().toString();

                if (!mpsd.equals(compsd)) {
                    Toast.makeText(CorrectActivity.this, "修改有误，两次输入密码不一致", Toast.LENGTH_LONG).show();
                } else if (mpsd.equals("") || compsd.equals("") || mnickname.equals("")) {
                    Toast.makeText(CorrectActivity.this, "修改有误，请全部填写", Toast.LENGTH_LONG).show();
                } else {
                    updateUser();
                    Toast.makeText(CorrectActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                    // 点击button跳转到导航页面
                    Intent intent = new Intent();
                    intent.setClass(CorrectActivity.this, MainActivity.class);
                    // 用Bundle携带数据
                    Bundle bundle = new Bundle();
                    LatLot latlot = new LatLot();
                    latlot.setStr_account(str_account);
                    bundle.putSerializable(SER_KEY, latlot);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void queryUserOBJ() {
        final BmobQuery<User> user = new BmobQuery<User>();
        //用店铺id进行查询
        user.addWhereEqualTo("userAccount", str_account);
        user.setLimit(100);
        user.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    for (User userobj : list) {
                        user_obj = userobj.getObjectId();
                    }
                }
            }
        });
    }

    //更新用户昵称
    private void updateUser() {
        User user = new User();
        user.setUserNickName(mnickname);
        user.setUserPassWord(mpsd);
        user.update(user_obj, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    System.out.println("成功");
                } else {
                    Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void getView() {
        text_psd = (EditText) findViewById(R.id.password);
        text_compsd = (EditText) findViewById(R.id.com_password);
        text_nickname = (EditText) findViewById(R.id.nickname);
        btn_correct = (Button) findViewById(R.id.register);
    }
}
