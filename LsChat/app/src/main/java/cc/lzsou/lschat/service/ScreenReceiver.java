package cc.lzsou.lschat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.SCREEN_OFF")){
            if(onScreenChanged!=null)onScreenChanged.onScreenOff();
        }else if (intent.getAction().equals("android.intent.action.SCREEN_ON")){
            if(onScreenChanged!=null)onScreenChanged.onScreenOn();
        }
    }

    public OnScreenChanged onScreenChanged;

    public void setOnScreenChanged(OnScreenChanged onScreenChanged) {
        this.onScreenChanged = onScreenChanged;
    }

    public interface OnScreenChanged{
        void onScreenOn();
        void onScreenOff();
    }
}
