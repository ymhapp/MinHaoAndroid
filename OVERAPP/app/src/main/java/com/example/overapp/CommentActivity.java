package com.example.overapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.com.overapp.model.Collection;
import com.com.overapp.model.Comment;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class CommentActivity extends AppCompatActivity {

    private String str_shopname;
    private String comment_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
//        queryCommentOBJ();
    }

    //查询收藏店铺的ID
//    private void queryCommentOBJ() {
//        final BmobQuery<Comment> comment = new BmobQuery<Comment>();
//        //用店铺id进行查询
//        comment.addWhereEqualTo("shopName", str_shopname);
//        comment.setLimit(100);
//        comment.findObjects(new FindListener<Comment>() {
//            @Override
//            public void done(List<Comment> list, BmobException e) {
//                if (e == null) {
//                    for (Comment collectionobj : list) {
//                        comment_obj = collectionobj.getObjectId();
//                    }
//                }
//            }
//        });
//    }


}
