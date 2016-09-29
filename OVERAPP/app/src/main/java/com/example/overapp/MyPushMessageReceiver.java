package com.example.overapp;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.push.PushConstants;

public class MyPushMessageReceiver extends BroadcastReceiver {

//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//    //     TODO Auto-generated method stub
//        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
//            Log.d("bmob", "BmobPushDemo收到消息："+intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING));
//            Toast.makeText(context, "BmobPushDemo收到消息："+intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING), Toast.LENGTH_SHORT).show();
//        }
//    }
//
//}

        @Override
        public void onReceive (Context context, Intent intent){
            if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
                // 收到广播时,发送一个通知
                String jsonStr = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
                String content = null;
                try {
                    // 处理JSON
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    content = jsonObject.getString("alert");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                Notification notify = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("收到一条推送")
                        .setContentText(content)
                        .build();
                manager.notify(1, notify);
            }
        }
    }
