package cc.lzsou.lschat.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class LSChatServiceConnection implements ServiceConnection {

    private Context context;
    public LSChatServiceConnection(Context context){
        this.context=context;
    };

    private IMService mService;
    public IMService getService() {
        return mService;
    }
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        IMService.LocalBinder binder = (IMService.LocalBinder) service;
        mService = binder.getService();
        System.out.println("绑定IM服务："+componentName);
    }
    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        System.out.println("解绑IM服务："+componentName);
        mService =null;
        Intent i = new Intent();
        i.setClass(context,IMService.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.bindService(i,this,0);
    }
}
