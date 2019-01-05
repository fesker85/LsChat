package cc.lzsou.lschat.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

import cc.lzsou.lschat.base.BaseApplication;

public class PhoneManager {
    private TelephonyManager tm;
    private static PhoneManager instance;
    public PhoneManager(){
        tm = (TelephonyManager) BaseApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static PhoneManager getInstance(){
        if(instance==null)
            instance=new PhoneManager();
        return  instance;
    }
    @SuppressLint("MissingPermission")
    public String getDeviceid() {
        return tm.getDeviceId();
    }
    @SuppressLint("MissingPermission")
    public String getPhoneNumber(){
        return  tm.getLine1Number();
    }
    @SuppressLint("MissingPermission")
    public String getSerialNumber(){
        return tm.getSimSerialNumber();
    }

}
