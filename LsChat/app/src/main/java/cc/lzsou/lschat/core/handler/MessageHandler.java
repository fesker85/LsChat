package cc.lzsou.lschat.core.handler;

import android.widget.Toast;

import cc.lzsou.lschat.base.BaseApplication;

public class MessageHandler {

    public static void showToask(String msg){
        Toast.makeText(BaseApplication.getInstance(),msg,Toast.LENGTH_SHORT).show();
    }
}
