package cc.lzsou.lschat.core.helper;

import cc.lzsou.lschat.base.BaseApplication;

public class UIHelper {

    public static int getStatusBarHeight(){
        int result = 0;
        int resourceId = BaseApplication.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result =  BaseApplication.getInstance().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
