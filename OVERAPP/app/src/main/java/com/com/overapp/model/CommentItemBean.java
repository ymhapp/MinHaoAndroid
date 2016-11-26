package com.com.overapp.model;

/**
 * Created by acer on 2016/11/26.
 */
public class CommentItemBean {
    public String UserName;
    public String Time;
    public String Comment;

    public CommentItemBean(String userName, String time, String comment) {
        UserName = userName;
        Time = time;
        Comment = comment;
    }
}
