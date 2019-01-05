package cc.lzsou.lschat.friend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.AjaxResult;
import cc.lzsou.lschat.base.AppEntity;
import cc.lzsou.lschat.base.flag.ActionFlag;
import cc.lzsou.lschat.base.flag.MessageFlag;
import cc.lzsou.lschat.core.helper.Common;
import cc.lzsou.lschat.data.bean.MessageEntity;
import cc.lzsou.lschat.data.bean.TempFriendEntity;
import cc.lzsou.lschat.data.bean.FriendEntity;
import cc.lzsou.lschat.core.handler.HttpPostHandler;
import cc.lzsou.lschat.core.helper.DateHelper;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.data.impl.FriendEntityImpl;
import cc.lzsou.lschat.data.impl.MessageEntityImpl;
import cc.lzsou.lschat.data.impl.TempFriendEntityImpl;
import cc.lzsou.lschat.manager.NoticeManager;
import cc.lzsou.lschat.service.ShowNotificationReceiver;
import cc.lzsou.servercore.LinkServer;

public class FriendAttestActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText remarkView;
    private TextView msgView;
    private Button submitButton;
    private int fid = 0;
    private TempFriendEntity entity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_attest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        remarkView = (EditText)findViewById(R.id.remarkView);
        msgView=(TextView)findViewById(R.id.msgView);
        submitButton=(Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        fid = getIntent().getIntExtra("fid",0);
        entity= TempFriendEntityImpl.getInstance().selectRow(fid);
        msgView.setText("对方发来的信息为“"+entity.getMsg()+"”");
        remarkView.setHint(entity.getNickname());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int from = AppEntity.getMemberid();
        String fromRemak = remarkView.getText().toString();
        if(fromRemak==null||fromRemak.equals(""))
            fromRemak=remarkView.getHint().toString();
        int to = entity.getId();
        String toRemark = entity.getRemark();
        String msg = MessageFlag.MESSAGE_FLAG_FRIEND_AGREE+"]["+AppEntity.getMemberid()+"]["+to+"][通过了朋友验证]";
        Map<String,String> map = new HashMap<>();
        map.put("from",String.valueOf(from));
        map.put("fromremark",fromRemak);
        map.put("to",String.valueOf(to));
        map.put("toremark",toRemark);
        map.put("msg",msg);
        final String finalFromRemak = fromRemak;
        new HttpPostHandler(FriendAttestActivity.this, LinkServer.BroadcastAction.URL_BROADCAST_SEND_ADD_FRIEND, map, true, "发送中") {
            @Override
            protected void onResult(String result) {
                System.out.println("同意好友返回结果："+result);
                AjaxResult ajaxResult= JsonHelper.jsonToObject(result,AjaxResult.class);
                if(ajaxResult==null){
                    Toast.makeText(FriendAttestActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!ajaxResult.isSuccess()){
                    Toast.makeText(FriendAttestActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
                    return;
                }

                FriendEntity friendEntity = new FriendEntity();
                friendEntity.setVipexpire(entity.getVipexpire());
                friendEntity.setVip(entity.getVip());
                friendEntity.setSex(entity.getSex());
                friendEntity.setRemark(finalFromRemak);
                friendEntity.setRelation(FriendEntity.RELATION_FRIEND_YES);
                friendEntity.setRegionid(entity.getRegionid());
                friendEntity.setRegion(entity.getRegion());
                friendEntity.setPlatid(entity.getPlatid());
                friendEntity.setPhone(entity.getPhone());
                friendEntity.setNickname(entity.getNickname());
                friendEntity.setName(entity.getName());
                friendEntity.setMode(FriendEntity.MODE_PERSION);
                friendEntity.setMobile(entity.getMobile());
                friendEntity.setJobunit(entity.getJobunit());
                friendEntity.setIdnumber(entity.getIdnumber());
                friendEntity.setEmail(entity.getEmail());
                friendEntity.setAvatar(entity.getAvatar());
                friendEntity.setAddress(entity.getAddress());
                friendEntity.setUsername(entity.getUsername());
                friendEntity.setExid("");
                friendEntity.setId(entity.getId());

                FriendEntityImpl.getInstance().insertRow(friendEntity);
                entity.setStatus(TempFriendEntity.STATUS_ADDED);
                TempFriendEntityImpl.getInstance().updateRow(entity);


                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setRectime(0);
                messageEntity.setCurtime(DateHelper.now_yyyy_MM_dd_HH_mm_ss());
                messageEntity.setMsg(MessageFlag.MESSAGE_FLAG_PASSEDFRIEND+"][您已通过了"+friendEntity.getRemark()+"朋友申请]");
                messageEntity.setMid("chat"+friendEntity.getId());
                messageEntity.setId(Common.getId());
                messageEntity.setMode(MessageEntity.MODE_CHAT);
                messageEntity.setPath(MessageEntity.PATH_TO);
                messageEntity.setStatus(MessageEntity.STATE_SEND_SUCCESS);
                messageEntity.setUid(friendEntity.getId());

                //给自己发信息 说明我已添加好友
                MessageEntityImpl.getInstance().insertRow(messageEntity);
                //发送通知
                Intent intent = new Intent(ShowNotificationReceiver.ACTION_SHOWNOTIFICATOIN);
                intent.putExtra(ShowNotificationReceiver.INTENT_KEY_FRIEND,friendEntity.getId());
                intent.putExtra(ShowNotificationReceiver.INTENT_KEY_TITLE,getString(R.string.app_name));
                intent.putExtra(ShowNotificationReceiver.INTENT_KEY_CONTENT,"您已通过了"+friendEntity.getRemark()+"朋友申请");
                intent.putExtra(ShowNotificationReceiver.INTENT_KEY_FLAG,ShowNotificationReceiver.FLAG_CHAT);
                intent.putExtra(ShowNotificationReceiver.INTENT_KEY_DEFAULTS, NoticeManager.NOTIFICATION_SOUND);
                intent.putExtra(ShowNotificationReceiver.INTENT_KEY_CANCEL,true);
                getApplication().sendBroadcast(new Intent(ActionFlag.ACTION_MESSAGE));
                getApplication().sendBroadcast(intent);
                //发送好友添加成功通知
                getApplication().sendBroadcast(new Intent(ActionFlag.ACTION_FLAG_FRIEND_CHANGE));
                FriendAttestActivity.this.finish();
            }
        };
    }
}
