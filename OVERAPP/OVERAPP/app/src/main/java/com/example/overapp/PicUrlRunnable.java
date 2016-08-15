package com.example.overapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by acer on 2016/8/8.
 */
public class PicUrlRunnable implements Runnable {
   public   int mThread1;
    private Handler mHandler;
    private String sUrl;

    public PicUrlRunnable(Handler h,int id,String str){
        mHandler=h;
        mThread1=id;
        sUrl=str;
    }

    @Override
    public void run()
    {
        Message msg = new Message();
        msg.what = 01;
        msg.obj = loadImageFromNetwork();
        mHandler.sendMessage(msg);
        System.out.println("LoadImageRunnable-----"+Thread.currentThread().getName());

    }
    // 从外部链接加载图片
    private Bitmap loadImageFromNetwork()
    {
        Bitmap bm = null;
        try
        {
            URL url = new URL(sUrl);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            bm = BitmapFactory.decodeStream(is);
        }
        catch (MalformedURLException e){

            e.printStackTrace();
            mHandler.sendEmptyMessage(10+mThread1);

        }catch(IOException e)
        {
            mHandler.sendEmptyMessage(10+mThread1);
            e.printStackTrace();
        }
        return bm;
    }

}