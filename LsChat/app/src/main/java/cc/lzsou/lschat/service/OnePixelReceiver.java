package cc.lzsou.lschat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cc.lzsou.lschat.activity.OnePiexlActivity;
import cc.lzsou.lschat.manager.AppStatusManager;

public class OnePixelReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {    //屏幕关闭启动1像素Activity
            Intent it = new Intent(context, OnePiexlActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {   //屏幕打开 结束1像素
            context.sendBroadcast(new Intent("finish"));
            if(!AppStatusManager.isServiceRunning(context,IMService.class)){
                Intent intent1 = new Intent(context,IMService.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(intent);
            }
            if(!AppStatusManager.isServiceRunning(context,PushService.class)){
                Intent intent1 = new Intent(context,PushService.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startService(intent);
            }
//            Intent main = new Intent(context, MainActivity.class);
//            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(main);
        }
    }
}
