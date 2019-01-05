package cc.lzsou.lschat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cc.lzsou.lschat.manager.NoticeManager;

public class ShowNotificationReceiver extends BroadcastReceiver {
    public static final String ACTION_SHOWNOTIFICATOIN="cc.lzsou.lschat.service.SHOW_NOTIFICATION";
    public static final int FLAG_CHAT=0;
    public static final int FLAG_FRIEND_ADD=1;

    public static final String INTENT_KEY_FRIEND="fid";
    public static final String INTENT_KEY_TITLE="title";
    public static final String INTENT_KEY_CONTENT="content";
    public static final String INTENT_KEY_DEFAULTS="def";
    public static final String INTENT_KEY_FLAG="flag";
    public static final String INTENT_KEY_CANCEL="cancel";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_SHOWNOTIFICATOIN)){
            int fid =intent.getIntExtra(INTENT_KEY_FRIEND,0);
            String title=intent.getStringExtra(INTENT_KEY_TITLE);
            String content = intent.getStringExtra(INTENT_KEY_CONTENT);
            int def = intent.getIntExtra(INTENT_KEY_DEFAULTS,0);
            int flag = intent.getIntExtra(INTENT_KEY_FLAG,-1);
            boolean cancel = intent.getBooleanExtra(INTENT_KEY_CANCEL,false);
            switch (flag){
                case FLAG_CHAT:
                    NoticeManager.chatNotification(context,fid, title,  content, def, cancel);
                    break;
                case FLAG_FRIEND_ADD:
                    NoticeManager.friendAddNotificatoin(context,fid, title,  content, def, cancel);
                    break;
            }

        }
    }
}
