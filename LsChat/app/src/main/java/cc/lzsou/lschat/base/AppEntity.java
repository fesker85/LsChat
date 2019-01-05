package cc.lzsou.lschat.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import cc.lzsou.lschat.data.impl.MemberEntityImpl;
import cc.lzsou.webbrowser.activity.WebActivity;

public class AppEntity {

    //存储用户登录信息
    private static int memberid = 0;

    public static int getMemberid() {
        if (memberid < 1) {
            memberid = MemberEntityImpl.getInstance().selectRow().getId();
        }
        return memberid;
    }

    public static final String QRCODE_FLAG_MEMBER = "[F1675B5E6AMEMBER,";//用户二维码标记


}
