package com.example.overapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MeActivity extends AppCompatActivity {
    //修改后的用户昵称
    private String cort_nickname;

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
        //Bundle bundle = this.getIntent().getExtras();
        //cort_nickname = bundle.getString("cort_nickname");

        // 新页面接收数据
        LatLot latLot = (LatLot) getIntent().getSerializableExtra(MainActivity.SER_KEY);
        str_account = latLot.getStr_account();
        str_psd = latLot.getStr_psd();
        System.out.println("用户ID" + str_account);
        getView();
        queryNmae();
        queryCollection();

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


//        ctlistview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 点击button跳转到导航页面
//                Intent intent = new Intent();
//                intent.setClass(MeActivity.this, Introduce.class);
//                startActivity(intent);
//            }
//        });
//        ctlistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Log.e("MeActivity", view.toString() + "position=" + position);
//                CharSequence number = ((TextView) view).getText();
//               System.out.println("dfkjnslkdfjsd");
//                return true;
//            }
//        });
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


                        CollectionAdapter collectionAdapter = new CollectionAdapter(MeActivity.this, collectionItemBeanList);
                        ctlistview.setAdapter(collectionAdapter);

                        for (int i = 0; i < ctnameList.size(); i++) {
                            collectionItemBeanList.add(new CollectionItemBean(ctnameList.get(i).toString(),
                                    ctaddList.get(i).toString(), ctbestList.get(i).toString()));
                        }
                    }
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
                                  }
// else if(!cort_nickname.equals("")){
//                    musernickname.setText(cort_nickname);
//                }

                                  else {
                                      System.out.println("查询失败");
                                  }
                              }
                          }

        );
    }

    private void getView() {
        mcorrect = (Button) findViewById(R.id.btn_correct);
        ctlistview = (ListView) findViewById(R.id.collectionlist);
        musernickname = (TextView) findViewById(R.id.usernickname);
    }

}
