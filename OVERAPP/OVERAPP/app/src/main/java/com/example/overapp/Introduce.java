package com.example.overapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.com.overapp.model.LatLot;
import com.com.overapp.model.Menu;
import com.com.overapp.model.MenuItemBean;
import com.com.overapp.model.MyAdapter;


import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Introduce extends AppCompatActivity {
    public final static String SER_KEY = "com.andy.ser";
    //接收传送过来的坐标
    private double endlat;
    private double endlot;


    private static final int THREAD_1 = 1;
    private static final int THREAD_2 = 2;

    private ProgressDialog mDialog;
    private List<MenuItemBean> menuItemBean = new ArrayList<>();
    private List<String> menuPrice = new ArrayList<String>(); //接收查询菜单图片结果的list
    private List<String> menuName = new ArrayList<String>();//接收查询菜单名字结果的List
    private ListView listView;                              //显示图片和菜单名的listview
    private TextView tad;                                   //店铺地址栏
    private TextView tname;                                //店铺名字
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
    private String shopid;
    private String shopname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        ListView listView = (ListView) findViewById(R.id.listView);


        getView();
        // 新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        // 接收店铺ID
        shopid = bundle.getString("shopid");
        shopad = bundle.getString("shopad");
        shopname = bundle.getString("shopname");
        shopurl = bundle.getString("shopurl");

        LatLot latLot = (LatLot) getIntent().getSerializableExtra(MainActivity.SER_KEY);
        endlat = latLot.getMarklat();
        endlot = latLot.getMarklot();

        System.out.println("这是传送的经纬度" + endlat);
        System.out.println("这是传送的经纬度" + endlot);

        mDialog = ProgressDialog.show(Introduce.this, "",
                "Loading. Please wait...", true);

        //开启线程
        new Thread(new PicUrlRunnable(mHandler, THREAD_1, shopurl)).start();
        //  new Thread(new PicUrlRunnable(mHandler, THREAD_3, menuPic.get(i))).start();


        //设置店名和菜单名
        tname.setText(shopname);
        tad.setText(shopad);

        dataList = new ArrayList<Map<String, Object>>();

        //Bmob查询menu
        final BmobQuery<Menu> menu = new BmobQuery<Menu>();
        //用店铺id进行查询
        menu.addWhereEqualTo("shopid", shopid);
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


        //button的监听
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击button跳转到导航页面
                Intent intent = new Intent();
                intent.setClass(Introduce.this,
                        Routeplan.class);
                // 用Bundle携带数据
                Bundle bundle = new Bundle();
                LatLot latlot=new LatLot();
                latlot.setMarklat(endlat);
                latlot.setMarklot(endlot);
                latlot.setEndAdd(shopad);
                bundle.putSerializable(SER_KEY, latlot);


                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    private void getView() {
        listPic = (ImageView) findViewById(R.id.pic);
        imageView = (ImageView) findViewById(R.id.shoppic);
        btn_go = (Button) findViewById(R.id.shop_go);
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


            }


        }
    };

}