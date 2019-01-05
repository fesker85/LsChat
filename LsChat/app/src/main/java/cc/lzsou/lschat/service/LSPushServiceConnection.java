package cc.lzsou.lschat.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class LSPushServiceConnection implements ServiceConnection {

    private Context context;
    public LSPushServiceConnection(Context context){
        this.context=context;
    }
    private PushService mService;
    public PushService getService() {
        return mService;
    }
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        PushService.LocalBinder binder = (PushService.LocalBinder) service;
        mService = binder.getService();
        System.out.println("绑定推送服务"+componentName);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mService =null;
        System.out.println("解绑推送服务"+componentName);
        Intent i = new Intent();
        i.setClass(context,PushService.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.bindService(i,this,0);
    }
}