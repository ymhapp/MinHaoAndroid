package com.com.overapp.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by acer on 2016/11/26.
 */
public class Comment extends BmobObject {

    private String userName;
    private String comment;
    private String shopName;



    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
