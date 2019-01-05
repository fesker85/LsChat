package cc.lzsou.lschat.friend.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.AjaxResult;
import cc.lzsou.lschat.base.AppEntity;
import cc.lzsou.lschat.base.flag.MessageFlag;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.data.impl.MemberEntityImpl;
import cc.lzsou.lschat.core.handler.HttpPostHandler;
import cc.lzsou.lschat.core.helper.DateHelper;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.data.bean.FriendEntity;
import cc.lzsou.servercore.LinkServer;

public class FriendApplyActivity extends AppCompatActivity implements View.OnClickListener {

    private int friendid=0;
    private String nickName="";
    private EditText applyView,remarkView;
    private Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_apply);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        friendid = getIntent().getIntExtra("fid",0);
        nickName=getIntent().getStringExtra("nick");
        applyView =(EditText)findViewById(R.id.applyView);
        applyView.setHint("我是"+ MemberEntityImpl.getInstance().selectRow().getNickname());
        remarkView=(EditText)findViewById(R.id.remarkView);
        remarkView.setHint(nickName);
        submitButton=(Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id .home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(friendid==AppEntity.getMemberid()){
            Toast.makeText(FriendApplyActivity.this,"自己不能添加自己为朋友",Toast.LENGTH_SHORT);
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("to",String.valueOf(friendid));
        String apply = applyView.getText().toString();
        if(apply==null||apply.equals(""))
            apply=applyView.getHint().toString();
        String remark=remarkView.getText().toString();
        if(remark==null||remark.equals(""))
            remark=remarkView.getHint().toString();
        map.put("msg", MessageFlag.MESSAGE_FLAG_FRIEND_ADD+"]["+AppEntity.getMemberid()+"]["+friendid+"]["+apply+"]["+remark+"]");
        final String finalRemark = remark;
        new HttpPostHandler(FriendApplyActivity.this, LinkServer.BroadcastAction.URL_BROADCAST_SEND, map, true, "发送中") {
            @Override
            protected void onResult(String result) {
                AjaxResult ajaxResult = JsonHelper.jsonToObject(result,AjaxResult.class);
                if(ajaxResult==null){
                    Toast.makeText(FriendApplyActivity.this,"发送失败",Toast.LENGTH_SHORT);
                    return;
                }
                if(!ajaxResult.isSuccess()){
                    Toast.makeText(FriendApplyActivity.this,"发送失败",Toast.LENGTH_SHORT);
                    return;
                }
                FriendApplyActivity.this.finish();
            }
        };
    }
}
