package cc.lzsou.lschat.main.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.AppEntity;
import cc.lzsou.lschat.base.BaseApplication;
import cc.lzsou.lschat.base.flag.ActionFlag;
import cc.lzsou.lschat.data.bean.TempFriendEntity;
import cc.lzsou.lschat.data.impl.ArticleClassEntityImpl;
import cc.lzsou.lschat.data.bean.ArticleClassEntity;
import cc.lzsou.lschat.data.bean.FriendEntity;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.core.handler.AsynHandler;
import cc.lzsou.lschat.core.helper.HttpHelper;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.data.impl.FriendEntityImpl;
import cc.lzsou.lschat.data.impl.MemberEntityImpl;
import cc.lzsou.lschat.data.impl.TempFriendEntityImpl;
import cc.lzsou.lschat.data.impl.TempMessageEntityImpl;
import cc.lzsou.lschat.main.fragment.DiscoverFragment;
import cc.lzsou.lschat.main.fragment.FriendFragment;
import cc.lzsou.lschat.main.fragment.HomeFragment;
import cc.lzsou.lschat.main.fragment.ProfileFragment;
import cc.lzsou.lschat.main.fragment.SchoolFragment;
import cc.lzsou.lschat.service.IMService;
import cc.lzsou.lschat.service.PushService;
import cc.lzsou.lschat.service.OnePixelReceiver;
import cc.lzsou.servercore.LinkServer;

import static cc.lzsou.lschat.service.IMService.CONNECT_SINGLE;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    private List<View> badgeList;

    private FragmentManager fragmentManager;

    private List<Fragment> fragmentList;

    private OnePixelReceiver mOnepxReceiver;
    private MessageBroadcastReceiver msgReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initApp();

    }

    private void init() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initBadge();
        initPopView();
        fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new DiscoverFragment());
        fragmentList.add(new SchoolFragment());
        fragmentList.add(new FriendFragment());
        fragmentList.add(new ProfileFragment());
        showFragment(0);
        updateFriendState();
        updateMessageState();
    }

    private Fragment currentFragment;

    private void showFragment(int position) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!fragmentList.get(position).isAdded()) {
            if (currentFragment == null)
                fragmentTransaction.add(R.id.frameLayout, fragmentList.get(position));
            else
                fragmentTransaction.hide(currentFragment).add(R.id.frameLayout, fragmentList.get(position));


        } else {
            fragmentTransaction.hide(currentFragment).show(fragmentList.get(position));
        }
        currentFragment = fragmentList.get(position);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mOnepxReceiver != null) getApplicationContext().unregisterReceiver(mOnepxReceiver);
        if (msgReceiver != null) getApplicationContext().unregisterReceiver(msgReceiver);
        super.onDestroy();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
            return true;
        }
        return true;
    }


    private void initPopView() {

        disableShiftMode(navigation);
    }


    private void initBadge() {
        badgeList = new ArrayList<>();
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        badgeList.add(LayoutInflater.from(this).inflate(R.layout.main_badge, (BottomNavigationItemView) menuView.getChildAt(0), false));
        badgeList.add(LayoutInflater.from(this).inflate(R.layout.main_badge, (BottomNavigationItemView) menuView.getChildAt(1), false));
        badgeList.add(LayoutInflater.from(this).inflate(R.layout.main_badge, (BottomNavigationItemView) menuView.getChildAt(2), false));
        badgeList.add(LayoutInflater.from(this).inflate(R.layout.main_badge, (BottomNavigationItemView) menuView.getChildAt(3), false));
        badgeList.add(LayoutInflater.from(this).inflate(R.layout.main_badge, (BottomNavigationItemView) menuView.getChildAt(4), false));
        for (int i = 0; i < badgeList.size(); i++) {
            ((BottomNavigationItemView) menuView.getChildAt(i)).addView(badgeList.get(i));
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_message:
                    showFragment(0);
                    return true;
                case R.id.navigation_explore:
                    showFragment(1);
                    return true;
                case R.id.navigation_school:
                    showFragment(2);
                    return true;
                case R.id.navigation_contact:
                    showFragment(3);
                    return true;
                case R.id.navigation_person:
                    showFragment(4);
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("RestrictedApi")
    public void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }


    private void initApp() {
        MemberEntity entity=MemberEntityImpl.getInstance().selectRow();
        if (entity.getMobile()==null||entity.getMobile().toUpperCase().equals("null")||entity.getMobile().equals("")) {
            new AsynHandler(MainActivity.this, true, "初始化中") {
                @Override
                protected Object onProcess() {
                    TempFriendEntityImpl.getInstance().delete();
                    FriendEntityImpl.getInstance().delete();
                    TempMessageEntityImpl.getInstance().delete();
                    ArticleClassEntityImpl.getInstance().delete();
                    //用户信息
                    Map<String, String> mapInfo = new HashMap<>();
                    mapInfo.put("id", String.valueOf(AppEntity.getMemberid()));
                    String result = HttpHelper.doPost(LinkServer.MemberAction.URL_MEMBER_INFO, mapInfo);
                    MemberEntity memberEntity = JsonHelper.jsonToObject(result, MemberEntity.class);
                    if (memberEntity != null &&memberEntity.getId()>0) {
                        MemberEntityImpl.getInstance().updateRow(memberEntity);
                    }
                    //用户好友
                    Map<String, String> mapFriend = new HashMap<>();
                    mapFriend.put("userid", String.valueOf(AppEntity.getMemberid()));
                    result = HttpHelper.doPost(LinkServer.FriendAction.URL_FRIEND_LIST, mapFriend);
                    List<FriendEntity> friendEntityList = JsonHelper.jsonToObjectList(result, FriendEntity.class);
                    if (friendEntityList != null && friendEntityList.size() > 0) {
                        FriendEntityImpl.getInstance().insert(friendEntityList);
                    }
                    //资讯分类
                    result= HttpHelper.doPost(LinkServer.ArticleAction.URL_ARTICLE_CLASS_LIST, new HashMap<>());
                    List<ArticleClassEntity> articleClassList = JsonHelper.jsonToObjectList(result,ArticleClassEntity.class);
                    ArticleClassEntityImpl.getInstance().insert(articleClassList);
                    return null;
                }

                @Override
                protected void onResult(Object object) {
                    regOnePixelReceiver();
                    regMessageReceiver();
                    openServer();
                    init();
                }
            };
        } else {
            regOnePixelReceiver();
            regMessageReceiver();
            openServer();
            init();
        }

    }

    //启动一像素保活activity
    private void regOnePixelReceiver() {
        if (mOnepxReceiver == null) mOnepxReceiver = new OnePixelReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        getApplicationContext().registerReceiver(mOnepxReceiver, intentFilter);
    }

    //打开服务
    private void openServer() {
        Intent intent = new Intent(BaseApplication.getInstance(), IMService.class);
        intent.setAction(CONNECT_SINGLE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startService(intent);
        Intent intent1 = new Intent(BaseApplication.getInstance(), PushService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startService(intent1);
    }

    private void regMessageReceiver() {
        msgReceiver = new MessageBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ActionFlag.ACTION_MESSAGE);
        intentFilter.addAction(ActionFlag.ACTION_FLAG_FRIEND_CHANGE);
        getApplicationContext().registerReceiver(msgReceiver, intentFilter);
    }


    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ActionFlag.ACTION_MESSAGE)) {
                updateMessageState();
            }
            if (intent.getAction().equals(ActionFlag.ACTION_FLAG_FRIEND_CHANGE)) {
                updateFriendState();
            }
        }
    }

    public void updateMessageState() {
        int count = TempMessageEntityImpl.getInstance().getAllCount();
        TextView countView = (TextView) badgeList.get(0).findViewById(R.id.countView);
        if (count > 0) {
            if(count>99){
                countView.setText("...");
                countView.setVisibility(View.VISIBLE);
            }
            else {
                countView.setText(count + "");
                countView.setVisibility(View.VISIBLE);
            }
        } else {
            countView.setVisibility(View.GONE);
        }
    }

    public void updateFriendState() {
        int count = TempFriendEntityImpl.getInstance().getStatusCount(TempFriendEntity.STATUS_NEW);
        TextView countView = (TextView) badgeList.get(3).findViewById(R.id.countView);
        if (count > 0) {
            countView.setText(count + "");
            countView.setVisibility(View.VISIBLE);
        } else countView.setVisibility(View.GONE);
    }

}
