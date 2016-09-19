package com.com.overapp.model;

import java.io.Serializable;

/**
 * Created by acer on 2016/8/11.
 */
public class LatLot implements Serializable {
    //路线规划输入框中的终点地址
    private double end_navi_lat;
    private double end_navi_lot;

    //poi搜索的经纬度坐标
    private String poi_Add;
    private double poi_latitude;
    private double poi_longitude;
    //收藏夹的店铺名字
    private String cot_shopname;
    private String cot_shopadd;
    //用户的账号密码
    private String str_account;
    private String str_psd;

    //路线规划的起点，终点
    private double loc_latitude;
    private double loc_longitude;
    private double lbs_latitude;
    private double lbs_longitide;
    private String lbs_Add;
    private static final long serialVersionUID = -7620435178023928252L;

    public double getEnd_navi_lat() {
        return end_navi_lat;
    }

    public void setEnd_navi_lat(double end_navi_lat) {
        this.end_navi_lat = end_navi_lat;
    }

    public double getEnd_navi_lot() {
        return end_navi_lot;
    }

    public void setEnd_navi_lot(double end_navi_lot) {
        this.end_navi_lot = end_navi_lot;
    }

    public String getPoi_Add() {
        return poi_Add;
    }

    public void setPoi_Add(String poi_Add) {
        this.poi_Add = poi_Add;
    }

    public double getPoi_latitude() {
        return poi_latitude;
    }

    public void setPoi_latitude(double poi_latitude) {
        this.poi_latitude = poi_latitude;
    }

    public double getPoi_longitude() {
        return poi_longitude;
    }

    public void setPoi_longitude(double poi_longitude) {
        this.poi_longitude = poi_longitude;
    }

    public String getCot_shopname() {
        return cot_shopname;
    }

    public void setCot_shopname(String cot_shopname) {
        this.cot_shopname = cot_shopname;
    }

    public String getCot_shopadd() {
        return cot_shopadd;
    }

    public void setCot_shopadd(String cot_shopadd) {
        this.cot_shopadd = cot_shopadd;
    }

    public String getStr_account() {
        return str_account;
    }

    public void setStr_account(String str_account) {
        this.str_account = str_account;
    }

    public String getStr_psd() {
        return str_psd;
    }

    public void setStr_psd(String str_psd) {
        this.str_psd = str_psd;
    }

    public double getLoc_latitude() {
        return loc_latitude;
    }

    public void setLoc_latitude(double loc_latitude) {
        this.loc_latitude = loc_latitude;
    }

    public double getLoc_longitude() {
        return loc_longitude;
    }

    public void setLoc_longitude(double loc_longitude) {
        this.loc_longitude = loc_longitude;
    }

    public double getLbs_latitude() {
        return lbs_latitude;
    }

    public void setLbs_latitude(double lbs_latitude) {
        this.lbs_latitude = lbs_latitude;
    }

    public double getLbs_longitide() {
        return lbs_longitide;
    }

    public void setLbs_longitide(double lbs_longitide) {
        this.lbs_longitide = lbs_longitide;
    }

    public String getLbs_Add() {
        return lbs_Add;
    }

    public void setLbs_Add(String lbs_Add) {
        this.lbs_Add = lbs_Add;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
