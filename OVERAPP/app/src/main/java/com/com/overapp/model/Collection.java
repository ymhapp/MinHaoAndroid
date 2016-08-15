package com.com.overapp.model;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by acer on 2016/8/14.
 */
public class Collection extends BmobObject {
    private String ctShopName;
    private String ctShopAdd;
    private String ctShopBest;

    public String getCtShopName() {
        return ctShopName;
    }

    public void setCtShopName(String ctShopName) {
        this.ctShopName = ctShopName;
    }

    public String getCtShopAdd() {
        return ctShopAdd;
    }

    public void setCtShopAdd(String ctShopAdd) {
        this.ctShopAdd = ctShopAdd;
    }

    public String getCtShopBest() {
        return ctShopBest;
    }

    public void setCtShopBest(String ctShopBest) {
        this.ctShopBest = ctShopBest;
    }


}
