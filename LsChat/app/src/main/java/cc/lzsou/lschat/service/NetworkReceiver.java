package cc.lzsou.lschat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager!=null){
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                manager.requestNetwork(new NetworkRequest.Builder().build(),new ConnectivityManager.NetworkCallback(){
                    //网络可用的回调
                    @Override
                    public void onAvailable(Network network) {
                        if(onNetworkChanged!=null)onNetworkChanged.onConnected();
                        super.onAvailable(network);
                    }
                    //网络丢失的回调
                    @Override
                    public void onLost(Network network) {
                        if(onNetworkChanged!=null)onNetworkChanged.onLosted();
                        super.onLost(network);
                    }
                });
            }else {
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                //网络可用
                if (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()) {
                    if (onNetworkChanged != null) onNetworkChanged.onConnected();
                }//网络丢失
                else {
                    if (onNetworkChanged != null) onNetworkChanged.onLosted();
                }
            }
        }
    }


    public OnNetworkChanged onNetworkChanged;

    public void setOnNetworkChanged(OnNetworkChanged onNetworkChanged) {
        this.onNetworkChanged = onNetworkChanged;
    }

    public interface OnNetworkChanged {
        void onConnected();

        void onLosted();
    }
}
