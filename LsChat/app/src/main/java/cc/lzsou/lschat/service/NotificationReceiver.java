package cc.lzsou.lschat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cc.lzsou.lschat.chat.activity.ChatActivity;
import cc.lzsou.lschat.friend.activity.FriendActivity;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String ACTION_SHOWCHATACTIVITY="cc.lzsou.lschat.service.NOTIFICATION_SHOWCHAT";
    public static final String ACTION_SHOWFRIENDACTIVITY="cc.lzsou.lschat.service.NOTIFICATION_SHOWFRIEND";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_SHOWCHATACTIVITY)){
            Intent i= new Intent();
            i.putExtra("fid",intent.getIntExtra("fid",0));
            i.setClass(context, ChatActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setAction(intent.getAction());
            context.startActivity(i);
        }
        
        if(intent.getAction().equals(ACTION_SHOWFRIENDACTIVITY)){
            Intent i= new Intent();
            i.putExtra("fid",intent.getIntExtra("fid",0));
            i.setClass(context, FriendActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setAction(intent.getAction());
            context.startActivity(i);
        }
    }
}
