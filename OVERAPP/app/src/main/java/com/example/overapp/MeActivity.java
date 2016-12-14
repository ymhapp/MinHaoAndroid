package com.example.overapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.com.overapp.model.Collection;
import com.com.overapp.model.CollectionAdapter;
import com.com.overapp.model.CollectionItemBean;
import com.com.overapp.model.LatLot;
import com.com.overapp.model.Menu;
import com.com.overapp.model.MenuItemBean;
import com.com.overapp.model.MyAdapter;
import com.com.overapp.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class MeActivity extends AppCompatActivity {
    private String strlang = "langye@163.com";
    private String stralin = "alin@163.com";


    private ImageButton imabtn;
    private String str_shopname;
    private String str_shopadd;
    private String cor_obj;

    //修改后的用户昵称
    private String cort_nickname;




    private List<String> ctshop_obj = new ArrayList<String>();
    private List<String> ctnameList = new ArrayList<String>(); //接收查询菜单图片结果的list
    private List<String> ctaddList = new ArrayList<String>();
    private List<String> ctbestList = new ArrayList<String>();
    private List<CollectionItemBean> collectionItemBeanList = new ArrayList<>();

    public final static String SER_KEY = "com.andy.ser";
    private String str_account;
    private String str_psd;
    private String str_nickname;

    private TextView musernickname;
    private ListView ctlistview;
    private Button mcorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        // 新页面接收数据
        LatLot latLot = (LatLot) getIntent().getSerializableExtra(MainActivity.SER_KEY);
        str_account = latLot.getStr_account();
        str_psd = latLot.getStr_psd();
        System.out.println("用户ID" + str_account);
        getView();
        queryNmae();
        queryCollection();


        //取得ActionBar对象
        ActionBar actionBar = getSupportActionBar();
        //调用show方法，展示actionbar
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("个人中心");

        //btn_correct的监听
        mcorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击button跳转到导航页面
                Intent intent = new Intent();
                intent.setClass(MeActivity.this, CorrectActivity.class);
                // 用Bundle携带数据
                Bundle bundle = new Bundle();
                LatLot latlot = new LatLot();
                latlot.setStr_account(str_account);
                bundle.putSerializable(SER_KEY, latlot);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    //查询用户收藏的店铺
    private void queryCollection() {
        final BmobQuery<Collection> collection = new BmobQuery<Collection>();
        //用店铺id进行查询
        collection.addWhereEqualTo("userAccount", str_account);
        collection.setLimit(100);
        collection.findObjects(new FindListener<Collection>() {
            @Override
            public void done(List<Collection> list, BmobException e) {
                if (e == null) {
                    for (Collection collection_shop : list) {
                        ctnameList.add(collection_shop.getCtShopName());
                        ctaddList.add(collection_shop.getCtShopadd());
                        ctbestList.add(collection_shop.getCtShopBest());
                        ctshop_obj.add(collection_shop.getObjectId());
                    }

                    for (int i = 0; i < ctbestList.size(); i++) {
                        collectionItemBeanList.add(new CollectionItemBean(ctnameList.get(i).toString(),
                                ctaddList.get(i).toString(), ctbestList.get(i).toString()));

                    }
                    CollectionAdapter collectionAdapter = new CollectionAdapter(MeActivity.this, collectionItemBeanList);
                    ctlistview.setAdapter(collectionAdapter);

                    //点击item的监听
                    ctlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            for (int i = 0; i < ctbestList.size(); i++) {
                                str_shopname = ctnameList.get(position).toString();
                                str_shopadd = ctaddList.get(position).toString();
                                System.out.println("item:" + str_shopname);
                            }
                            // 点击button跳转到导航页面
                            Intent intent = new Intent();
                            intent.setClass(MeActivity.this, Introduce.class);
                            // 用Bundle携带数据
                            Bundle bundle = new Bundle();
                            LatLot latlot = new LatLot();
                            latlot.setCot_shopname(str_shopname);
                            latlot.setCot_shopadd(str_shopadd);
                            bundle.putSerializable(SER_KEY, latlot);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                    //长按item的监听
                    ctlistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            //获取点击的item的店名以及店铺id
                            str_shopname = ctnameList.get(position);
                            cor_obj=ctshop_obj.get(position);

                            //长按弹出dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(MeActivity.this);
                            builder.setTitle("提示");
                            builder.setMessage("是否删除");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    //删除收藏店铺
                                    deleCollection();
                                    Toast.makeText(MeActivity.this, "成功移除", Toast.LENGTH_SHORT).show();
                                    // 点击button跳转到导航页面
                                    Intent intent = new Intent();
                                    intent.setClass(MeActivity.this, MainActivity.class);
//                                    // 用Bundle携带数据
                                    Bundle bundle = new Bundle();
                                    LatLot latlot = new LatLot();
                                    latlot.setStr_account(str_account);
                                    bundle.putSerializable(SER_KEY, latlot);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                            return false;
                        }
                    });
                }
            }
        });
    }

    //查询用户昵称
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
                        str_nickname = user.getUserNickName();
                        System.out.println("nickname:" + str_nickname);
                        musernickname.setText(str_nickname);
                    }
                } else {
                    System.out.println("查询失败");
                }
            }
        });
    }

    //删除收藏店铺
    private void deleCollection() {
        Collection collection = new Collection();
        collection.setObjectId(cor_obj);
        collection.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "成功");
                } else {
                    System.out.println("+++9977" + e);
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }




    private void getView() {
        mcorrect = (Button) findViewById(R.id.btn_correct);
        ctlistview = (ListView) findViewById(R.id.collectionlist);
        musernickname = (TextView) findViewById(R.id.usernickname);
        imabtn = (ImageButton) findViewById(R.id.userpic);
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
