package com.com.overapp.model;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by acer on 2016/8/14.
 */
public class Collection extends BmobObject {
    private String ctShopName;
    private String ctShopadd;
    private String ctShopBest;
    private String userAccount;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getCtShopName() {
        return ctShopName;
    }

    public void setCtShopName(String ctShopName) {
        this.ctShopName = ctShopName;
    }

    public String getCtShopadd() {
        return ctShopadd;
    }

    public void setCtShopadd(String ctShopadd) {
        this.ctShopadd = ctShopadd;
    }

    public String getCtShopBest() {
        return ctShopBest;
    }

    public void setCtShopBest(String ctShopBest) {
        this.ctShopBest = ctShopBest;
    }


}
