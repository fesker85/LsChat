package cc.lzsou.lschat.manager;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import cc.lzsou.lschat.base.BaseApplication;

public class KeyboradManager {

    public static void hidden(Context context,View view){
        view.clearFocus();
        InputMethodManager imm=(InputMethodManager) BaseApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
    public static void show(final Context context, final View view){
        view.setVisibility(View.VISIBLE);
        view.requestFocus();
        view.post(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view,0);
            }
        });
    }

    public static boolean isActive(Context context){
        return ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).isActive();//isOpen若返回true，则表示输入法打开
    }
}
