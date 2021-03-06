package com.example.overapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.LatLngBounds.Builder;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.com.overapp.model.LatLot;
import com.com.overapp.model.Shop;
import com.example.overapp.MyOrientationListener.OnOrientationListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends ActionBarActivity implements CloudListener,
        OnGetPoiSearchResultListener, OnGetSuggestionResultListener {


    private String strlang = "langye@163.com";
    private String stralin = "alin@163.com";


    private ImageButton mylocation;
    private ImageButton lbs_search;
    private ToggleButton traffic_button;
    //接收用户信息
    private String str_account;
    private String str_psd;

    //传递选中的目的地
    public final static String SER_KEY = "com.andy.ser";
    private double lbs_lat;
    private double lbs_lot;
    private LatLng poilalot;
    private String poiAdd;
    private double poi_lat;
    private double poi_lot;

    public static String TAG = "bmob";

    //传递店铺信息的参数
    private String straddress;
    private String markAdress;
    private String strshopname;
    private String strurl;
    private String strbest;

    // 存储当前定位信息
    public BDLocation currlocation = null;


    // 存储LBS数据库的遍历结果
    List<CloudPoiInfo> lbs_poiList;

    // poi相关
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;

    //搜索关键字输入窗口
    private AutoCompleteTextView keyWorldsView = null;
    private ArrayAdapter<String> sugAdapter = null;
    private int load_Index = 0;

    //
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Context context;

    // 定位相关
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    private boolean isFirstIn = true;
    private double mLatitude;
    private double mLongtitude;

    private BitmapDescriptor mIconLocation;
    private float mCurrentX;
    private com.baidu.mapapi.map.MyLocationConfiguration.LocationMode mLocationMode;
    private MyOrientationListener myOrientationListener;

    // LBS检索相关
    private static final String LTAG = MainActivity.class.getSimpleName();
    private static final List<com.baidu.mapapi.cloud.CloudPoiInfo> poiList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        this.context = this;
        //接收用户信息
        LatLot latLot = (LatLot) getIntent().getSerializableExtra(LoginActivity.SER_KEY);
        str_account = latLot.getStr_account();
        str_psd = latLot.getStr_psd();


        //初始化百度地图SDK
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        CloudManager.getInstance().init(MainActivity.this);


        // 获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();


        // 初始化覆盖物
        initOverlay();
        // 初始化定位
        initLocation();


        //交通图按钮
        traffic_button = (ToggleButton) findViewById(R.id.traffic_button);
        //设置交通图图标
        traffic_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                traffic_button.setChecked(isChecked);
                traffic_button.setBackgroundResource(isChecked ? R.drawable.traffic_start : R.drawable.traffic);
            }
        });

        //交通图button的监听
        traffic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (traffic_button.isChecked()) {
                    mBaiduMap.setTrafficEnabled(true);
                } else {
                    mBaiduMap.setTrafficEnabled(false);
                }

            }
        });

        //lbs搜索button的监听
        lbs_search = (ImageButton) findViewById(R.id.lbs_search);
        lbs_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearbySearchInfo info = new NearbySearchInfo();
                info.ak = "hA4E4f7L0sumUOLd6mVS7TL0NfF6YC3n";
                info.geoTableId = 140125;
                info.radius = 1500;
                info.location = (currlocation.getLongitude() + "," + currlocation
                        .getLatitude());
                CloudManager.getInstance().nearbySearch(info);
            }
        });


        mylocation = (ImageButton) findViewById(R.id.mylocation);
        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = new LatLng(mLatitude, mLongtitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
            }
        });


        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
        sugAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        keyWorldsView.setAdapter(sugAdapter);

        /**
         * marker点击事件
         */
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
            //点击marker
            public boolean onMarkerClick(Marker marker) {

                // 获取marker的经纬度
                LatLng position = marker.getPosition();
                lbs_lat = position.latitude;
                lbs_lot = position.longitude;


                //遍历LBS中的marker
                for (CloudPoiInfo cloudPoiInfo : lbs_poiList) {
                    CloudPoiInfo cpi = null;
                    if (cloudPoiInfo.latitude == lbs_lat && cloudPoiInfo.longitude == lbs_lot) {
                        cpi = cloudPoiInfo;
                        Toast.makeText(MainActivity.this, cpi.address, Toast.LENGTH_LONG).show();

                        //  Bmob查询
                        final BmobQuery<Shop> shopad = new BmobQuery<Shop>();
                        //查询地址与marker相同的店铺
                        shopad.addWhereEqualTo("address", cpi.address);
                        shopad.setLimit(1);
                        //执行查询方法
                        shopad.findObjects(new FindListener<Shop>() {
                            @Override
                            public void done(List<Shop> list, BmobException e) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("店铺名称");
                                if (e == null && list.size() != 0) {
                                    for (Shop shop : list) {
                                        straddress = shop.getAddress();
                                        strshopname = shop.getShopName();
                                        strurl = shop.getShopUrl();
                                        strbest = shop.getTheBest();
                                        System.out.println("店名" + strshopname);
                                    }
                                }
                                builder.setMessage(strshopname);
                                builder.setPositiveButton("详情", new DialogInterface.OnClickListener() { //设置确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //点击button跳转到导航页面
                                        Intent intent = new Intent();
                                        intent.setClass(MainActivity.this, Introduce.class);
                                        // 用Bundle携带数据
                                        Bundle bundle = new Bundle();


                                        LatLot latlot = new LatLot();
                                        latlot.setShopad(straddress);
                                        latlot.setShopbest(strbest);
                                        latlot.setShopname(strshopname);
                                        latlot.setShopurl(strurl);

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
                            }
                        });


                        //InfoWindow的监听
                        OnInfoWindowClickListener oinfolistener = null;
                        oinfolistener = new OnInfoWindowClickListener() {
                            public void onInfoWindowClick() {
//
                            }
                        };
                    }
                }
                return true;
            }
        });

        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        keyWorldsView.addTextChangedListener(new TextWatcher() {
                                                 @Override
                                                 public void afterTextChanged(Editable arg0) {
                                                 }

                                                 @Override
                                                 public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                                                 }

                                                 @Override
                                                 public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                                                     if (cs.length() <= 0) {
                                                         return;
                                                     }
                                                     String city = ((EditText) findViewById(R.id.city)).getText().toString();
                                                     /**
                                                      * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                                                      */
                                                     mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                                                             .keyword(cs.toString()).city(city));
                                                 }
                                             }
        );
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog();
            return true;
        }
        return true;
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //AccoutList.this.finish();
                        //System.exit(1);
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * 添加覆盖物
     */
    private void initOverlay() {

    }


    /**
     * 进行定位
     */
    private void initLocation() {
        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        mMapView.removeViewAt(1); // 去掉百度logo

        // 初始化图标
        mIconLocation = BitmapDescriptorFactory.fromResource(R.drawable.jiantou);
        myOrientationListener = new MyOrientationListener(context);
        myOrientationListener.setOnOrientationListener(new OnOrientationListener() {
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });
    }

    /**
     * 菜单栏
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 普通地图
            case R.id.map_common:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            // 卫星地图
            case R.id.map_site:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            //个人中心
            case R.id.me:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MeActivity.class);
                //将用户数据传输到个人中心
                Bundle bundle = new Bundle();
                LatLot latlot = new LatLot();
                latlot.setStr_account(str_account);
                latlot.setStr_psd(str_psd);
                bundle.putSerializable(SER_KEY, latlot);
                intent.putExtras(bundle);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 实现定位接口的方法
     *
     * @param
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            // 设置自定义图标
            MyLocationConfiguration config = new MyLocationConfiguration(
                    mLocationMode, true, mIconLocation);
            mBaiduMap.setMyLocationConfigeration(config);

            // mMapview 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            currlocation = location;
            MyLocationData locData = new MyLocationData.Builder()
                    .direction(mCurrentX)//
                    .accuracy(location.getRadius())//
                    .latitude(location.getLatitude())//
                    .longitude(location.getLongitude())//
                    .build();
            mBaiduMap.setMyLocationData(locData); // 设置定位数据

            // 更新经纬度
            mLatitude = location.getLatitude();
            mLongtitude = location.getLongitude();

            if (isFirstIn) {
                isFirstIn = false;
                LatLng mylocation = new LatLng(location.getLatitude(), location.getLongitude());
                // 设置地图中心点以及缩放级别
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(mylocation, 17);
                mBaiduMap.animateMapStatus(u);
                Toast.makeText(context, location.getAddrStr(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // 开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
            mLocationClient.start();
        // 开启方向传感
        myOrientationListener.start();
    }

    protected void onStop() {
        super.onStop();
        // 停止定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        // 停止方向传感
        myOrientationListener.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //恢复
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    /**
     * 将获取到的遍历集合存到str
     */

//    public String getAddress(List<CloudPoiInfo> poiList) {
//        String str = null;
//        for (CloudPoiInfo cloudPoiInfo : poiList) {
//            System.out.println(cloudPoiInfo.address);
//            str += cloudPoiInfo.address;
//        }
//        return str;
//    }

    /**
     * 实现LBS搜索的两个方法，获取回调信息
     *
     * @param
     */
    @Override
    public void onGetDetailSearchResult(DetailSearchResult arg0, int arg1) {
    }

    @Override
    public void onGetSearchResult(CloudSearchResult result, int error) {

        if (result != null && result.poiList != null
                && result.poiList.size() > 0) {
            Log.d(LTAG, "onGetSearchResult, result length: " + result.poiList.size());
            mBaiduMap.clear();
            BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.lbs_mark);
            LatLng ll;
            LatLngBounds.Builder builder = new Builder();

            // 将遍历的结果传给lbs_poiList
            lbs_poiList = result.poiList;
            for (CloudPoiInfo info : result.poiList) {
                ll = new LatLng(info.latitude, info.longitude);
                OverlayOptions oo = new MarkerOptions().icon(bd).position(ll);
                mBaiduMap.addOverlay(oo);
                builder.include(ll);
            }
            LatLngBounds bounds = builder.build();
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
            mBaiduMap.animateMapStatus(u);
        }

    }

    @Override
    // 状态保存
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 影响搜索按钮点击事件
     *
     * @param v
     */
    public void searchButtonProcess(View v) {
        EditText editCity = (EditText) findViewById(R.id.city);
        EditText editSearchKey = (EditText) findViewById(R.id.searchkey);
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(editCity.getText().toString())
                .keyword(editSearchKey.getText().toString())
                .pageNum(load_Index));
    }

    public void goToNextPage(View v) {
        load_Index++;
        searchButtonProcess(null);
    }

    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(MainActivity.this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result);
            overlay.addToMap();
            overlay.zoomToSpan();
            return;
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";
            for (CityInfo cityInfo : result.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }
            strInfo += "找到结果";
            Toast.makeText(MainActivity.this, strInfo, Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * 实现poi搜索的两个方法，获取回调信息
     *
     * @param
     */
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            //显示点击的marker的名称与地址
            Toast.makeText(MainActivity.this,
                    result.getName() + ": " + result.getAddress(),
                    Toast.LENGTH_SHORT).show();

            poiAdd = result.getAddress();
            //获取点击的marker的坐标
            poilalot = result.getLocation();
            poi_lat = poilalot.latitude;
            poi_lot = poilalot.longitude;

            // 点击poimarker跳转到导航页面
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Routeplan.class);
            // 将起点坐标与终点坐标传输到导航页面
            Bundle bundle = new Bundle();
            LatLot latlot = new LatLot();
            latlot.setPoi_Add(poiAdd);
            latlot.setPoi_latitude(poi_lat);
            latlot.setPoi_longitude(poi_lot);
            bundle.putSerializable(SER_KEY, latlot);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
        sugAdapter.clear();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null)
                sugAdapter.add(info.key);
        }
        sugAdapter.notifyDataSetChanged();
    }

    private class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }
        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            return true;
        }
    }
}