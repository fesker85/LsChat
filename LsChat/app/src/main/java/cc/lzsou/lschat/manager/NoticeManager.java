package cc.lzsou.lschat.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.service.NotificationReceiver;

import static cc.lzsou.lschat.service.NotificationReceiver.ACTION_SHOWCHATACTIVITY;
import static cc.lzsou.lschat.service.NotificationReceiver.ACTION_SHOWFRIENDACTIVITY;

/**
 * 通知管理
 */
public class NoticeManager {
    public static final int NOTIFICATION_NONE=-1;
    public static final int NOTIFICATION_ALL = 0;
    public static final int NOTIFICATION_SOUND = 1;
    public static final int NOTIFICATION_VIBRATE = 2;
    public static final int NOTIFICATION_SOUNDANDLIGHTS = 3;
    public static final int NOTIFICATION_SOUNDANDVIBRATE = 4;

    public static final int NOTIFICATION_CHAT_ID=0;
    public static final int NOTIFICATION_FRIEND_ADD=1;

    public static void friendAddNotificatoin(Context context,int friendid,String title,String content,int defaults,boolean cancel){
        if(defaults==NOTIFICATION_NONE)return;
        Intent intent = new Intent();
        intent.putExtra("fid",friendid);
        intent.setClass(context, NotificationReceiver.class);
        intent.setAction(ACTION_SHOWFRIENDACTIVITY);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);
        builder.setTicker("New message");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText(content);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);

        switch (defaults){
            case NOTIFICATION_ALL:
                builder.setDefaults(Notification.DEFAULT_ALL);break;
            case NOTIFICATION_SOUND:
                builder.setDefaults(NOTIFICATION_SOUND);
                break;
            case NOTIFICATION_SOUNDANDLIGHTS:
                builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS);
                break;
            case NOTIFICATION_SOUNDANDVIBRATE:
                builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
                break;
            case NOTIFICATION_VIBRATE:
                builder.setDefaults(Notification.DEFAULT_VIBRATE);
                break;
        }
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_FRIEND_ADD,builder.build());
        if(cancel){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    manager.cancel(NOTIFICATION_FRIEND_ADD);
                }
            },2000);
        }
    }

    public static void chatNotification(Context context,int friendid,String title, String content,int defaults,boolean cancel) {
        if(defaults==NOTIFICATION_NONE)return;
        Intent intent = new Intent();
        intent.putExtra("fid",friendid);
        intent.setClass(context, NotificationReceiver.class);
        intent.setAction(ACTION_SHOWCHATACTIVITY);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);
        builder.setTicker("New message");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText(content);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);

        switch (defaults){
            case NOTIFICATION_ALL:
                builder.setDefaults(Notification.DEFAULT_ALL);break;
            case NOTIFICATION_SOUND:
                builder.setDefaults(NOTIFICATION_SOUND);
                break;
            case NOTIFICATION_SOUNDANDLIGHTS:
                builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS);
                break;
            case NOTIFICATION_SOUNDANDVIBRATE:
                builder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);
                break;
            case NOTIFICATION_VIBRATE:
                builder.setDefaults(Notification.DEFAULT_VIBRATE);
                break;
        }
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_CHAT_ID,builder.build());
        if(cancel){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    manager.cancel(NOTIFICATION_CHAT_ID);
                }
            },2000);
        }
    }








    //     mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);//声音 震动
//        mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS);//声音 呼吸灯
//        mBuilder.setDefaults(Notification.DEFAULT_ALL);//声音 震动 呼吸灯
//        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS);//震动 呼吸灯
//        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);//震动
//        mBuilder.setDefaults(Notification.DEFAULT_SOUND);//声音
//        mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);//呼吸灯
//        notificationManager.notify(NOTICE_MESSAGE_ID, mBuilder.build());
}
