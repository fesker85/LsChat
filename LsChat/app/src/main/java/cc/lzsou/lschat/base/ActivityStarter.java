package cc.lzsou.lschat.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import cc.lzsou.lschat.activity.QRCodeActivity;
import cc.lzsou.lschat.profile.activity.ClearActivity;
import cc.lzsou.lschat.profile.activity.DisturbActivity;
import cc.lzsou.lschat.profile.activity.NotiSettingActivity;
import cc.lzsou.lschat.profile.activity.PrivacyActivity;
import cc.lzsou.lschat.profile.activity.ProfileActivity;
import cc.lzsou.lschat.profile.activity.SafeActivity;
import cc.lzsou.lschat.profile.activity.SettingActivity;
import cc.lzsou.lschat.profile.activity.WalletActivity;
import cc.lzsou.webbrowser.activity.WebActivity;

/**
 * 活动页面管理
 */
public class ActivityStarter {

    /**
     * 一键清理
     * @param context
     */
    public static void startClearActivity(Context context){
        context.startActivity(new Intent(context, ClearActivity.class));
    }

    /**
     * 隐私设置
     * @param context
     */
    public static void startPrivacyActivity(Context context){
        context.startActivity(new Intent(context, PrivacyActivity.class));
    }

    /**
     * 勿扰模式
     * @param context
     */
    public static void startDisturbActivity(Context context){
        context.startActivity(new Intent(context, DisturbActivity.class));
    }
    /**
     * 新消息通知
     * @param context
     */
    public static void startNotiSettingActivity(Context context){
        context.startActivity(new Intent(context, NotiSettingActivity.class));
    }

    /**
     * 帐号与安全
     * @param context
     */
    public static void startSafeActivity(Context context){
        context.startActivity(new Intent(context, SafeActivity.class));
    }
    /**
     * 个人信息
     * @param context
     */
    public static void startProfileActivity(Context context){
        context.startActivity(new Intent(context, ProfileActivity.class));
    }

    /**
     * 二维码
     * @param context
     * @param title
     * @param parms
     */
    public static void startQRCodeActivity(Context context,String title,String parms){
        Intent intent = new Intent(context, QRCodeActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("parm",parms);
        context.startActivity(new Intent(context, WalletActivity.class));
    }
    /**
     * 钱包
     * @param context
     */
    public static void startWalletActivity(Context context){
        context.startActivity(new Intent(context, WalletActivity.class));
    }
    /**
     * 设置
     * @param context
     */
    public static void startSettingActivity(Context context){
        context.startActivity(new Intent(context, SettingActivity.class));
    }


    /**
     * 打开网页
     * @param context
     * @param title
     * @param url
     */
    public static void startWebActivity(Context context, String title, String url) {
        Intent intent = new Intent();
        intent.setClass(context, WebActivity.class);
        intent.putExtra(WebActivity.INTENT_KEY_URL, url);
        intent.putExtra(WebActivity.INTENT_KEY_TITLE, title);
        intent.putExtra(WebActivity.INTENT_KEY_UA, AppEntity.getMemberid());
        intent.putExtra(WebActivity.INTENT_KEY_VERSION, getVersionName(context));
        context.startActivity(intent);
    }




    private static String getVersionName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
}
