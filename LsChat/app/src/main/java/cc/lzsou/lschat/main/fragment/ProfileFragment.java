package cc.lzsou.lschat.main.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.lzsou.fingerboard.PayPwdActivity;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.activity.QRCodeActivity;
import cc.lzsou.lschat.base.ActivityStarter;
import cc.lzsou.lschat.base.AppEntity;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.manager.ImageLoaderManager;
import cc.lzsou.lschat.data.impl.MemberEntityImpl;
import cc.lzsou.lschat.profile.activity.ProfileActivity;
import cc.lzsou.lschat.profile.activity.SettingActivity;
import cc.lzsou.lschat.profile.activity.WalletActivity;
import cc.lzsou.servercore.LinkServer;
import cc.lzsou.servercore.WebAppServer;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private UpdateUIReceiver receiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private ImageView avatarView;
    private TextView nameView;
    private ImageView vipView;
    private TextView accountView;
    private ImageView qrcodeView;
    private RelativeLayout infoLayout;
    private LinearLayout walletLayout;
    private LinearLayout settingLayout;
    private LinearLayout vipLayout;
    private LinearLayout storeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        init(view);
        return  view;
    }

    private void init(View view){
        avatarView =(ImageView)view.findViewById(R.id.avatarView);
        nameView=(TextView)view.findViewById(R.id.nameView);
        vipView=(ImageView)view.findViewById(R.id.vipView);
        accountView=(TextView)view.findViewById(R.id.accountView);
        qrcodeView=(ImageView)view.findViewById(R.id.qrcodeView);
        qrcodeView.setOnClickListener(this);
        infoLayout=(RelativeLayout)view.findViewById(R.id.infoLayout);
        infoLayout.setOnClickListener(this);
        walletLayout = (LinearLayout)view.findViewById(R.id.walletLayout);
        walletLayout.setOnClickListener(this);
        settingLayout=(LinearLayout)view.findViewById(R.id.settingLayout);
        settingLayout.setOnClickListener(this);
        vipLayout=(LinearLayout)view.findViewById(R.id.vipLayout);
        vipLayout.setOnClickListener(this);
        storeLayout=(LinearLayout)view.findViewById(R.id.storeLayout);
        storeLayout.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        receiver=new UpdateUIReceiver();
        getActivity().registerReceiver(receiver,new IntentFilter(ProfileActivity.REFLASH_UI_PROFILE));
        initData();
    }

    @Override
    public void onDestroy() {
        if(receiver!=null)getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }
    public void initData(){
       MemberEntity entity= MemberEntityImpl.getInstance().selectRow();
       ImageLoaderManager.getInstance().displayCircleAvatar(LinkServer.FileAction.getImageAddress(entity.getAvatar()),avatarView);
        nameView.setText(entity.getNickname());
        accountView.setText("帐号："+((entity.getUsername()==null||entity.getUsername().equals("")||entity.getUsername().equals("null"))?"":entity.getUsername()));
        if(entity.getVip()>0)vipView.setImageResource(R.mipmap.ic_vip_t);
        else vipView.setImageResource(R.mipmap.iv_vip_f);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.qrcodeView:
                ActivityStarter.startQRCodeActivity(getActivity(),"我的二维码",AppEntity.QRCODE_FLAG_MEMBER+"]["+AppEntity.getMemberid()+"]");
                break;
            case R.id.infoLayout:
                ActivityStarter.startProfileActivity(getActivity());
                break;
            case R.id.walletLayout:
                ActivityStarter.startWalletActivity(getActivity());
                break;
            case R.id.vipLayout:
                ActivityStarter.startWebActivity(getActivity(),"会员", WebAppServer.WEB_URL_VIP);
                break;
            case R.id.storeLayout:
                ActivityStarter.startWebActivity(getActivity(),"商城", WebAppServer.WEB_URL_STORE);
                break;
            case R.id.settingLayout:
                ActivityStarter.startSettingActivity(getActivity());
                break;
        }
    }


    private class UpdateUIReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ProfileActivity.REFLASH_UI_PROFILE)){
                initData();
            }
        }
    }
}
