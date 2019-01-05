package cc.lzsou.lschat.region.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.AjaxResult;
import cc.lzsou.lschat.base.AppEntity;
import cc.lzsou.lschat.base.flag.ActionFlag;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.core.handler.HttpPostHandler;
import cc.lzsou.lschat.core.handler.MessageHandler;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.data.impl.MemberEntityImpl;
import cc.lzsou.lschat.profile.activity.ProfileActivity;
import cc.lzsou.lschat.region.fragment.FragmentCity;
import cc.lzsou.lschat.region.fragment.FragmentProv;
import cc.lzsou.lschat.region.fragment.FragmentZone;
import cc.lzsou.servercore.LinkServer;

public class RegionActivity extends AppCompatActivity {

    private static RegionActivity instace;
    public static RegionActivity getInstace(){
        return  instace;
    }
    private Fragment currentFragment;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private String flag;
    private String code="";
    private String region="";

    public void setInfo(String code,String region){
        this.code=code;
        this.region=region;
    }
    public String[] getInfo(){
        return  new String[]{code,region};
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);
        instace=this;
        flag = getIntent().getStringExtra("flag");
        if(flag==null)flag="";
        init();
    }

    private void init() {
        fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentProv());
        fragmentList.add(new FragmentCity());
        fragmentList.add(new FragmentZone());
        showFragment(0);
    }

    public void showFragment(int position){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(!fragmentList.get(position).isAdded()){
            if(currentFragment==null)transaction.add(R.id.frameLayout,fragmentList.get(position));
            else  transaction.hide(currentFragment).add(R.id.frameLayout,fragmentList.get(position));
        }
        else transaction.hide(currentFragment).show(fragmentList.get(position));
        currentFragment= fragmentList.get(position);
        transaction.commit();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(currentFragment.equals(fragmentList.get(2))){
                String c = code.substring(0,2)+"0000";
                String r = region.split(",")[0];
                setInfo(c,r);
                showFragment(1);
            }
            else if(currentFragment.equals(fragmentList.get(1))){
                setInfo("","");
                showFragment(0);
            }else finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void doComplated(){
        if(code.equals("")){
            MessageHandler.showToask("请选择地区");
            return;
        }
        String regionAddr ="";
        for(String str:region.split(",")){
            regionAddr+=str;
        }
        if(flag.toUpperCase().equals("PROFILE"))
            setMemberRegion(regionAddr);
        else finish();
    }

    private void setMemberRegion(final String regionAddr){
        Map<String,String> map = new HashMap<>();
        map.put("id", String.valueOf(AppEntity.getMemberid()));
        map.put("regionid",code);
        map.put("region",regionAddr);
        new HttpPostHandler(this, LinkServer.MemberAction.URL_MEMBER_REGION, map, true, "设置中") {
            @Override
            protected void onResult(String r) {
                AjaxResult ajaxResult = JsonHelper.jsonToObject(r,AjaxResult.class);
                String msg ="设置失败";
                if(ajaxResult==null){
                    MessageHandler.showToask(msg);
                    return;
                }
                if(!ajaxResult.isSuccess()){
                    if(ajaxResult.getMsg()!=null)msg=ajaxResult.getMsg();
                    MessageHandler.showToask(msg);
                    return;
                }

                MemberEntity memberEntity = MemberEntityImpl.getInstance().selectRow();
                memberEntity.setRegion(regionAddr);
                memberEntity.setRegionid(code);
                MemberEntityImpl.getInstance().updateRow(memberEntity);
                sendBroadcast(new Intent(ProfileActivity.REFLASH_UI_PROFILE));
                finish();
            }
        };
    }
}
