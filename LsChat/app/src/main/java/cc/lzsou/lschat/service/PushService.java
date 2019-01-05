package cc.lzsou.lschat.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import cc.lzsou.lschat.chat.activity.ChatActivity;

import static cc.lzsou.lschat.service.NotificationReceiver.ACTION_SHOWCHATACTIVITY;
import static cc.lzsou.lschat.service.ShowNotificationReceiver.ACTION_SHOWNOTIFICATOIN;
public class PushService extends Service {

    private final NotificationReceiver receiver = new NotificationReceiver();
    private final ShowNotificationReceiver showReceiver = new ShowNotificationReceiver();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    @Override
    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SHOWCHATACTIVITY);
        registerReceiver(receiver,intentFilter);
        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(ACTION_SHOWNOTIFICATOIN);
        registerReceiver(showReceiver,intentFilter1);
        keepAlive();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        unregisterReceiver(showReceiver);
        super.onDestroy();
    }
    private void keepAlive() {
        //用AlarmManager定时发送广播
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, PushService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.currentThreadTimeMillis(), pendingIntent);
    }

    public class LocalBinder extends Binder {
        public PushService getService() {
            return PushService.this;
        }
    }
}
