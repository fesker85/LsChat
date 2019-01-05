package cc.lzsou.lschat.friend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.chat.activity.ChatActivity;
import cc.lzsou.lschat.core.handler.AsynHandler;
import cc.lzsou.lschat.core.helper.HttpHelper;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.data.bean.FriendEntity;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.manager.ImageLoaderManager;
import cc.lzsou.lschat.data.bean.TempFriendEntity;
import cc.lzsou.lschat.data.impl.FriendEntityImpl;
import cc.lzsou.lschat.data.impl.TempFriendEntityImpl;
import cc.lzsou.servercore.LinkServer;

public class FriendActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView avatarView,sexView;
    private TextView nickView,tagView,remarkView,regionView;
    private Button chatButton,addFriendButton,passFriendButton;
    private int fid=0;
    private String nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fid = getIntent().getIntExtra("fid",0);
        init();
    }

    private void init(){
        avatarView=(ImageView)findViewById(R.id.avatarView);
        sexView=(ImageView)findViewById(R.id.sexView);
        nickView=(TextView)findViewById(R.id.nickView);
        nickView.setText("");
        tagView=(TextView)findViewById(R.id.tagView);
        tagView.setText("");
        remarkView=(TextView)findViewById(R.id.remarkView);
        regionView=(TextView)findViewById(R.id.regionView);
        regionView.setText("");
        chatButton=(Button)findViewById(R.id.chatButton);
        addFriendButton=(Button)findViewById(R.id.addFriendButton);
        passFriendButton=(Button)findViewById(R.id.passFriendButton);
        chatButton.setVisibility(View.GONE);
        addFriendButton.setVisibility(View.GONE);
        passFriendButton.setVisibility(View.GONE);
        chatButton.setOnClickListener(this);
        addFriendButton.setOnClickListener(this);
        passFriendButton.setOnClickListener(this);
        initData();
    }

    private void initData(){

        new AsynHandler(this, true, "加载中") {
            @Override
            protected Object onProcess() {
                FriendEntity entity=FriendEntityImpl.getInstance().selectRow(fid);
                if(entity==null||entity.getId()<1){
                    //不是好友
                    TempFriendEntity tempFriendEntity = TempFriendEntityImpl.getInstance().selectRow(fid);
                    if(tempFriendEntity==null||tempFriendEntity.getId()<1){
                        Map<String,String> map = new HashMap<>();
                        map.put("id",String.valueOf(fid));
                        String result = HttpHelper.doPost(LinkServer.MemberAction.URL_MEMBER_INFO,map);
                        MemberEntity memberEntity = JsonHelper.jsonToObject(result,MemberEntity.class);
                        if(memberEntity!=null&&memberEntity.getId()>0){
                            nickname=memberEntity.getNickname();
                            return  new String[]{memberEntity.getAvatar(),
                                    memberEntity.getNickname(),
                                    ((memberEntity.getUsername()==null||memberEntity.getUsername().equals("null"))?"":memberEntity.getUsername()),
                                    (memberEntity.getRegion()==null||memberEntity.getRegion().equals("null"))?"":memberEntity.getRegion(),
                                    "3"
                            };
                        }
                        else return null;

                    }
                    else {
                        //是未通过的新朋友
                        nickname=tempFriendEntity.getNickname();
                        return  new String[]{tempFriendEntity.getAvatar(),
                                tempFriendEntity.getNickname(),
                                ((tempFriendEntity.getUsername()==null||tempFriendEntity.getUsername().equals("null"))?"":tempFriendEntity.getUsername()),
                                (tempFriendEntity.getRegion()==null||tempFriendEntity.getRegion().equals("null"))?"":tempFriendEntity.getRegion(),
                                "2"
                        };
                    }
                }
                else {
                    //是好友
                    nickname=entity.getNickname();
                    return  new String[]{entity.getAvatar(),
                            entity.getNickname(),
                            ((entity.getUsername()==null||entity.getUsername().equals("null"))?"":entity.getUsername()),
                            (entity.getRegion()==null||entity.getRegion().equals("null"))?"":entity.getRegion(),
                            "1"
                            };
                }
            }

            @Override
            protected void onResult(Object object) {
                String[] arrString=(String[])object;
                if(arrString==null||arrString.length!=5)return;
                ImageLoaderManager.getInstance().displayAvatar(arrString[0],avatarView);
                nickView.setText(arrString[1]);
                tagView.setText("帐号："+arrString[2]);
                regionView.setText(arrString[3]);
                if(Integer.parseInt(arrString[4])==1)
                    chatButton.setVisibility(View.VISIBLE);
                if(Integer.parseInt(arrString[4])==2)
                    passFriendButton.setVisibility(View.VISIBLE);
                if(Integer.parseInt(arrString[4])==3)
                    addFriendButton.setVisibility(View.VISIBLE);
            }
        };


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
        Intent intent = new Intent();
        intent.putExtra("fid",fid);
        intent.putExtra("nick",nickname);
        if(view.getId()==R.id.chatButton){
            //聊天
            intent.setClass(FriendActivity.this,ChatActivity.class);
        }
        if(view.getId()==R.id.addFriendButton){
            //添加好友
            intent.setClass(FriendActivity.this,FriendApplyActivity.class);
        }else  if(view.getId()==R.id.passFriendButton){
            intent.setClass(FriendActivity.this,FriendAttestActivity.class);
        }
        startActivity(intent);
        this.finish();
    }
}
