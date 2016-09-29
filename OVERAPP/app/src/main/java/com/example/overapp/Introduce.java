package com.example.overapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.com.overapp.model.Collection;
import com.com.overapp.model.LatLot;
import com.com.overapp.model.Menu;
import com.com.overapp.model.MenuItemBean;
import com.com.overapp.model.MyAdapter;


import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class Introduce extends AppCompatActivity {
    private String cot_shopname;
    private String cot_shopadd;
    public final static String SER_KEY = "com.andy.ser";
    private String str_account;
    //接收传送过来的坐标
    private double lbs_lat;
    private double lbs_lot;


    private static final int THREAD_1 = 1;
    private static final int THREAD_2 = 2;
    private static final int THREAD_3 = 3;

    private ProgressDialog mDialog;
    private List<MenuItemBean> menuItemBean = new ArrayList<>();
    private List<String> menuPrice = new ArrayList<String>(); //接收查询菜单图片结果的list
    private List<String> menuName = new ArrayList<String>();//接收查询菜单名字结果的List
    private ListView listView;                              //显示图片和菜单名的listview
    private TextView tad;                                   //店铺地址栏
    private TextView tname;                                //店铺名字
    private ImageButton btn_like;
    private Button btn_go;
    private ImageView listPic;
    private ImageView imageView;
    //
    private SimpleAdapter simple_adapter;
    private List<Map<String, Object>> dataList;
    //
    private int i = 0;
    private String shopurl;
    private String shopad;
    private String price;
    private String shopname;
    private String shopBest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        getView();
        // 新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        // 接收店铺信息
        shopad = bundle.getString("shopad");
        shopname = bundle.getString("shopname");
        shopurl = bundle.getString("shopurl");
        shopBest = bundle.getString("shopbest");
        //接收目的地坐标
        LatLot latLot = (LatLot) getIntent().getSerializableExtra(MainActivity.SER_KEY);
        lbs_lat = latLot.getLbs_latitude();
        lbs_lot = latLot.getLbs_longitide();
        str_account = latLot.getStr_account();
        cot_shopname = latLot.getCot_shopname();
        cot_shopadd = latLot.getCot_shopadd();

        System.out.println("这是传送的URL" + shopurl);
        System.out.println("这是传送的经纬度" + lbs_lat);
        System.out.println("这是传送的经纬度" + lbs_lot);
        System.out.println("dianming" + cot_shopname);


        //取得ActionBar对象
        ActionBar actionBar = getSupportActionBar();
//        //调用hide方法，隐藏actionbar
//        actionBar.hide();
        //调用show方法，展示actionbar
        actionBar.show();
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setTitle("");


        //设置店名和菜单名
        if (cot_shopname != null) {
            //如果收藏list有店铺就将店铺信息赋值给shopname,shopadd
            shopname = cot_shopname;
            shopad = cot_shopadd;
            tname.setText(shopname);
            tad.setText(shopad);

            mDialog = ProgressDialog.show(Introduce.this, "",
                    "Loading. Please wait...", true);

            //开启线程
            new Thread(new PicUrlRunnable(mHandler, THREAD_1, shopurl)).start();
            queryMenu();
        } else {
            tname.setText(shopname);
            tad.setText(shopad);

            mDialog = ProgressDialog.show(Introduce.this, "",
                    "Loading. Please wait...", true);

            //开启线程
            new Thread(new PicUrlRunnable(mHandler, THREAD_1, shopurl)).start();
            queryMenu();
        }


        //btn_go的监听
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击button跳转到导航页面
                Intent intent = new Intent();
                intent.setClass(Introduce.this,
                        Routeplan.class);
                // 用Bundle携带数据
                Bundle bundle = new Bundle();
                LatLot latlot = new LatLot();
                latlot.setLbs_latitude(lbs_lat);
                latlot.setLbs_longitide(lbs_lot);
                latlot.setLbs_Add(shopad);
                bundle.putSerializable(SER_KEY, latlot);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //收藏button监听
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collection collection = new Collection();
                collection.setCtShopName(shopname);
                collection.setCtShopBest(shopBest);
                collection.setCtShopadd(shopad);
                collection.setUserAccount(str_account);
                collection.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        Toast.makeText(Introduce.this, "收藏成功", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        //设置点赞button的点击效果
        btn_like.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //重新设置按下时的背景图片
                    ((ImageButton) v).setImageDrawable(getResources().getDrawable(R.drawable.red));
                }
//               else if(event.getAction() == MotionEvent.ACTION_UP){
//                    //再修改为抬起时的正常图片
//                    ((ImageButton)v).setImageDrawable(getResources().getDrawable(R.drawable.grray));
//                }
                return false;
            }
        });

    }


    //查询店铺菜单
    private void queryMenu() {
        //Bmob查询menu
        final BmobQuery<Menu> menu = new BmobQuery<Menu>();
        //用店铺id进行查询
        menu.addWhereEqualTo("shopName", shopname);
        menu.setLimit(10);
        //执行查询方法
        menu.findObjects(new FindListener<Menu>() {
            @Override
            public void done(List<Menu> list, BmobException e) {

                if (e == null) {
                    for (Menu menu : list) {
                        menuName.add(menu.getMenuName());
                        menuPrice.add(menu.getPrice());
                    }

                    for (i = 0; i < menuName.size(); i++) {
                        menuItemBean.add(new MenuItemBean(R.drawable.ic_launcher, menuName.get(i).toString(), menuPrice.get(i).toString() + "元"));
                    }
                    Message message = Message.obtain();
                    message.what = THREAD_2;
                    message.obj = menuItemBean;
                    mHandler.sendMessage(message);

                }
            }
        });
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


    private void getView() {
        listPic = (ImageView) findViewById(R.id.pic);
        imageView = (ImageView) findViewById(R.id.shoppic);
        btn_go = (Button) findViewById(R.id.shop_go);
        btn_like = (ImageButton) findViewById(R.id.btn_like);
        listView = (ListView) findViewById(R.id.listView);
        tname = (TextView) findViewById(R.id.shop_name);
        tad = (TextView) findViewById(R.id.shopaddress);
    }

    private Handler mHandler = new Handler() {

        // 利用handleMessage更新UI
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    imageView.setImageBitmap((Bitmap) msg.obj);
                    //  listPic.setImageBitmap((Bitmap) msg.obj);
                    mDialog.dismiss();
                    break;
                case 2:
                    // 设置适配器
                    MyAdapter myAdapter = new MyAdapter(Introduce.this, menuItemBean);
                    listView.setAdapter(myAdapter);

                    // listPic.setImageBitmap((Bitmap) msg.obj);
                    break;

//                case 3:
//                    // 设置适配器
//                    MyAdapter myAdapter = new MyAdapter(Introduce.this, menuItemBean);
//                    listView.setAdapter(myAdapter);
//
//                    // listPic.setImageBitmap((Bitmap) msg.obj);
//                    break;

            }


        }
    };

}