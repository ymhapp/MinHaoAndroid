package com.com.overapp.model;

import java.io.Serializable;

/**
 * Created by acer on 2016/8/11.
 */
public class LatLot implements Serializable {

    private String cot_shopname;
    private String cot_shopadd;
    private String str_account;
    private String str_psd;

    //路线规划的起点，终点
    private double marklat;
    private double marklot;
    private double endlat;
    private double endlot;
    private String endAdd;
    private static final long serialVersionUID = -7620435178023928252L;

    public String getCot_shopadd() {
        return cot_shopadd;
    }

    public void setCot_shopadd(String cot_shopadd) {
        this.cot_shopadd = cot_shopadd;
    }

    public String getCot_shopname() {
        return cot_shopname;
    }

    public void setCot_shopname(String cot_shopname) {
        this.cot_shopname = cot_shopname;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEndAdd() {
        return endAdd;
    }

    public void setEndAdd(String endAdd) {
        this.endAdd = endAdd;
    }

    public double getEndlot() {
        return endlot;
    }

    public void setEndlot(double endlot) {
        this.endlot = endlot;
    }

    public double getEndlat() {
        return endlat;
    }

    public void setEndlat(double endlat) {
        this.endlat = endlat;
    }

    public double getMarklat() {
        return marklat;
    }

    public void setMarklat(double marklat) {
        this.marklat = marklat;
    }

    public double getMarklot() {
        return marklot;
    }

    public void setMarklot(double marklot) {
        this.marklot = marklot;
    }
}
