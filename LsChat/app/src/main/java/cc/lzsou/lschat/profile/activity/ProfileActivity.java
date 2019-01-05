package cc.lzsou.lschat.profile.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.AjaxResult;
import cc.lzsou.lschat.base.AppEntity;
import cc.lzsou.lschat.base.flag.ActionFlag;
import cc.lzsou.lschat.base.flag.MessageFlag;
import cc.lzsou.lschat.core.handler.HttpPostHandler;
import cc.lzsou.lschat.core.handler.MessageHandler;
import cc.lzsou.lschat.core.handler.UploadHandler;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.core.handler.AsynHandler;
import cc.lzsou.lschat.core.helper.Common;
import cc.lzsou.lschat.manager.FileManager;
import cc.lzsou.lschat.core.helper.HttpHelper;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.manager.ImageLoaderManager;
import cc.lzsou.lschat.data.impl.MemberEntityImpl;
import cc.lzsou.lschat.region.activity.RegionActivity;
import cc.lzsou.lschat.selector.activity.ImageSelectorActivity;
import cc.lzsou.lschat.service.IMService;
import cc.lzsou.lschat.service.MessageSender;
import cc.lzsou.lschat.views.dialog.LoadingDialog;
import cc.lzsou.lschat.views.dialog.SheetDialog;
import cc.lzsou.media.CameraActivity;
import cc.lzsou.media.CropActivity;
import cc.lzsou.media.PickerActivity;
import cc.lzsou.media.PickerConfig;
import cc.lzsou.media.entity.Media;
import cc.lzsou.servercore.LinkServer;

public class ProfileActivity extends AppCompatActivity {

    /**
     * 刷新个人UI
     */
    public static final String REFLASH_UI_PROFILE="cc.lzsou.lschat.profile.REFLASHUIPROFILE";//

    @BindView(R.id.backView)
    ImageButton backView;
    @BindView(R.id.avatarView)
    ImageView avatarView;
    @BindView(R.id.avatarLayout)
    RelativeLayout avatarLayout;
    @BindView(R.id.nickView)
    TextView nickView;
    @BindView(R.id.nickLayout)
    RelativeLayout nickLayout;
    @BindView(R.id.accountView)
    TextView accountView;
    @BindView(R.id.accountLayout)
    RelativeLayout accountLayout;
    @BindView(R.id.mobileView)
    TextView mobileView;
    @BindView(R.id.mobileLayout)
    RelativeLayout mobileLayout;
    @BindView(R.id.sexView)
    TextView sexView;
    @BindView(R.id.sexLayout)
    RelativeLayout sexLayout;
    @BindView(R.id.regionView)
    TextView regionView;
    @BindView(R.id.regionLayout)
    RelativeLayout regionLayout;
    @BindView(R.id.moreLayout)
    RelativeLayout moreLayout;
    private MemberEntity memberEntity;
    private UpdateUIReceiver receiver;

    private static final int REQUEST_AVATAR_CAMERA=100;
    private static final int REQUEST_AVATAR_ABLUM=101;
    private static final int REQUEST_AVATAR_CROP=102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        initData();
        receiver = new UpdateUIReceiver();
        getApplication().registerReceiver(receiver,new IntentFilter(REFLASH_UI_PROFILE));
    }
    private void initData(){
        memberEntity= MemberEntityImpl.getInstance().selectRow();
        ImageLoaderManager.getInstance().displayAvatar(LinkServer.FileAction.getImageAddress(memberEntity.getAvatar()),avatarView);
        nickView.setText(memberEntity.getNickname()==null?"":memberEntity.getNickname());
        accountView.setText(memberEntity.getUsername()==null?"":memberEntity.getUsername());
        mobileView.setText(memberEntity.getMobile()==null?"":memberEntity.getMobile());
        sexView.setText(memberEntity.getSexString());
        regionView.setText(memberEntity.getRegion()==null?"":memberEntity.getRegion());
    }

    @Override
    protected void onDestroy() {
        if(receiver!=null)getApplication().unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=RESULT_OK)return;
        switch (requestCode){
            case REQUEST_AVATAR_CAMERA:
                cropImage(data);
                break;
            case REQUEST_AVATAR_ABLUM:
                cropImage(data);
                break;
            case REQUEST_AVATAR_CROP:
                setAvatar(data);
        }
    }

    private void cropImage(Intent data){
        ArrayList<Media> list = data.getParcelableArrayListExtra("data");
        if(list==null||list.size()<1){
            MessageHandler.showToask("请选择图片");
            return;
        }
        Intent intent = new Intent();
        intent.setClass(ProfileActivity.this, CropActivity.class);
        intent.putExtra(PickerConfig.DEFAULT_CROP_FILEPATH,list.get(0).path);
        startActivityForResult(intent,REQUEST_AVATAR_CROP);
    }


    @OnClick({R.id.backView, R.id.avatarView, R.id.avatarLayout, R.id.nickLayout, R.id.accountLayout, R.id.mobileLayout, R.id.sexLayout, R.id.regionLayout, R.id.moreLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backView:
                this.finish();
                break;
            case R.id.avatarView:
                showImageSelector();
                break;
            case R.id.avatarLayout:
                showImageSelector();
                break;
            case R.id.nickLayout:
                setNick();
                break;
            case R.id.accountLayout:
                setUsername();
                break;
            case R.id.mobileLayout:
                break;
            case R.id.sexLayout:
                setSex();
                break;
            case R.id.regionLayout:
                setRegion();
                break;
            case R.id.moreLayout:
                break;
        }

    }

    private void setRegion(){
        Intent intent = new Intent();
        intent.setClass(this, RegionActivity.class);
        intent.putExtra("flag","profile");
        startActivity(intent);
    }

    //设置帐号
    private void setSex(){
        Intent intent = new Intent();
        intent.setClass(ProfileActivity.this,NickActivity.class);
        intent.putExtra("input",NickActivity.INPUT_TYPE_SEX);
        intent.putExtra("from","profile");
        startActivity(intent);
    }
    //设置帐号
    private void setUsername(){
        Intent intent = new Intent();
        intent.setClass(ProfileActivity.this,NickActivity.class);
        intent.putExtra("input",NickActivity.INPUT_TYPE_USERNAME);
        intent.putExtra("from","profile");
        startActivity(intent);
    }
    //设置昵称
    private void setNick(){
        Intent intent = new Intent();
        intent.setClass(ProfileActivity.this,NickActivity.class);
        intent.putExtra("input",NickActivity.INPUT_TYPE_NICKNAME);
        intent.putExtra("from","profile");
        startActivity(intent);
    }
    //选择图片
    private void showImageSelector(){

        new SheetDialog(ProfileActivity.this).builder().addSheetItem("相册", SheetDialog.SheetItemColor.Blue, new SheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {

                Intent intent = new Intent();
                intent.putExtra(PickerConfig.SELECT_MODE,PickerConfig.PICKER_IMAGE);
                intent.putExtra(PickerConfig.MAX_SELECT_COUNT,1);
                intent.setClass(ProfileActivity.this, PickerActivity.class);
                ProfileActivity.this.startActivityForResult(intent,REQUEST_AVATAR_ABLUM);

            }
        }).addSheetItem("拍照", SheetDialog.SheetItemColor.Blue, new SheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                Intent intent = new Intent();
                intent.putExtra(PickerConfig.SELECT_MODE,PickerConfig.PICKER_IMAGE);
                intent.putExtra(PickerConfig.MAX_SELECT_COUNT,1);
                intent.setClass(ProfileActivity.this, CameraActivity.class);
                ProfileActivity.this.startActivityForResult(intent,REQUEST_AVATAR_CAMERA);
            }
        }).show();

    }
    //设置头像
    private void setAvatar(Intent data) {
        ArrayList<Media> list = data.getParcelableArrayListExtra("data");
        if (list == null || list.size() < 1) {
            MessageHandler.showToask("请选择图片");
            return;
        }

        String path = list.get(0).path;
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(AppEntity.getMemberid()));
        map.put("ext", path.substring(path.lastIndexOf(".")));
        LoadingDialog dialog = new LoadingDialog.Builder(ProfileActivity.this).setCancelable(true).setMessage("上传中").create();
        dialog.show();
        new UploadHandler(LinkServer.FileAction.URL_FILE_IMAGE_UPLOAD, map, path) {
            @Override
            protected void onResult(AjaxResult ajaxResult) {
                if (ajaxResult != null && ajaxResult.isSuccess()) {
                    String avatarUrl = ajaxResult.getMsg();
                    Map<String,String> map1 = new HashMap<>();
                    map1.put("id",""+memberEntity.getId());
                    map1.put("avatar",avatarUrl);

                    new HttpPostHandler(ProfileActivity.this, LinkServer.MemberAction.URL_MEMBER_AVATAR, map1) {
                        @Override
                        protected void onResult(String r) {
                            dialog.dismiss();
                            AjaxResult result = JsonHelper.jsonToObject(r,AjaxResult.class);
                            if(result==null||!result.isSuccess()){
                                MessageHandler.showToask("上传失败");
                                return;
                            }
                            memberEntity.setAvatar(avatarUrl);
                            MemberEntityImpl.getInstance().updateRow(memberEntity);
                            sendBroadcast(new Intent(REFLASH_UI_PROFILE));
                            Intent intent = new Intent(IMService.SEND_MESSAGE_TO_ALLFRIENDS);
                            intent.setClass(ProfileActivity.this,IMService.class);
                            intent.putExtra(MessageSender.MESSAGE_MODE,MessageSender.MESSAGE_MODE_NORMAL);
                            intent.putExtra("msg", MessageFlag.MESSAGE_FLAG_FRIEND_INFOCHANGE + "][" + memberEntity.getId() + "]");
                            startService(intent);
                        }
                    };

                } else {
                    MessageHandler.showToask("上传失败");
                }
            }
        }.run();
    }

    class UpdateUIReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(REFLASH_UI_PROFILE))
                initData();
        }
    }
}
