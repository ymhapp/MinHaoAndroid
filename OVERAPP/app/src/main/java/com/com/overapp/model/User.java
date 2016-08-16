package com.com.overapp.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by acer on 2016/8/14.
 */
public class User extends BmobObject {
    private String userAccount;
    private String userPassWord;
    private String userNickName;




    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPassWord() {
        return userPassWord;
    }

    public void setUserPassWord(String userPassWord) {
        this.userPassWord = userPassWord;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }
}
