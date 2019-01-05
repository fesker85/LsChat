package cc.lzsou.lschat.profile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.AjaxResult;
import cc.lzsou.lschat.base.flag.ActionFlag;
import cc.lzsou.lschat.base.flag.MessageFlag;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.core.handler.HttpPostHandler;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.data.impl.MemberEntityImpl;
import cc.lzsou.lschat.main.activity.MainActivity;
import cc.lzsou.lschat.service.IMService;
import cc.lzsou.lschat.service.MessageSender;
import cc.lzsou.servercore.LinkServer;

//设置用户信息 统一使用nick
public class NickActivity extends AppCompatActivity {

    public static final int INPUT_TYPE_NICKNAME = 0;
    public static final int INPUT_TYPE_USERNAME = 1;
    public static final int INPUT_TYPE_SEX = 2;
    private String ACTIVITY_FROM = "";//来源
    private int INPUT_FROM = 0;// 0 设置昵称

    private MemberEntity memberEntity;
    @BindView(R.id.backView)
    ImageButton backView;
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.complateButton)
    Button complateButton;
    @BindView(R.id.nickView)
    EditText nickView;
    @BindView(R.id.nickLayout)
    LinearLayout nickLayout;
    @BindView(R.id.accountView)
    EditText accountView;
    @BindView(R.id.accountLayout)
    LinearLayout accountLayout;
    @BindView(R.id.sexMale)
    RadioButton sexMale;
    @BindView(R.id.sexFamale)
    RadioButton sexFamale;
    @BindView(R.id.sexLayout)
    LinearLayout sexLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick);
        ButterKnife.bind(this);
        ACTIVITY_FROM = getIntent().getStringExtra("from");
        if (ACTIVITY_FROM == null) ACTIVITY_FROM = "";
        INPUT_FROM = getIntent().getIntExtra("input", 0);
        memberEntity = MemberEntityImpl.getInstance().selectRow();
        init();
    }

    private void init() {
        switch (INPUT_FROM) {
            case INPUT_TYPE_NICKNAME:
                nickLayout.setVisibility(View.VISIBLE);
                accountLayout.setVisibility(View.GONE);
                sexLayout.setVisibility(View.GONE);
                titleView.setText("设置名字");
                break;
            case INPUT_TYPE_USERNAME:
                nickLayout.setVisibility(View.GONE);
                accountLayout.setVisibility(View.VISIBLE);
                sexLayout.setVisibility(View.GONE);
                titleView.setText("设置帐号");
                break;
            case INPUT_TYPE_SEX:
                nickLayout.setVisibility(View.GONE);
                accountLayout.setVisibility(View.GONE);
                sexLayout.setVisibility(View.VISIBLE);
                titleView.setText("设置性别");
                break;
        }
        if (ACTIVITY_FROM.toUpperCase().equals("LOGIN")) backView.setVisibility(View.GONE);
        nickView.setText(memberEntity.getNickname() == null ? "" : memberEntity.getNickname());
        accountView.setText(memberEntity.getUsername() == null ? "" : memberEntity.getUsername());
        if (memberEntity.getSex() == 1) sexMale.setChecked(true);
        if (memberEntity.getSex() == 2) sexFamale.setChecked(true);
    }


    @OnTextChanged(value = R.id.accountView, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTextChanged(Editable s) {
        if (s.length() > 0) {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c >= 0x4e00 && c <= 0X9fff) {
                    s.delete(i,i+1);
                }
            }
        }
    }

    @OnClick({R.id.backView, R.id.complateButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backView:
                finish();
                break;
            case R.id.complateButton:
                switch (INPUT_FROM) {
                    case INPUT_TYPE_NICKNAME:
                        setNick();
                        break;
                    case INPUT_TYPE_USERNAME:
                        setAccount();
                        break;
                    case INPUT_TYPE_SEX:
                        setSex();
                        break;
                }
                break;
        }
    }


    //设置性别
    private void setSex() {
        int sex = 0;
        if (sexFamale.isChecked()) sex = 2;
        if (sexMale.isChecked()) sex = 1;
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(memberEntity.getId()));
        map.put("sex", sex + "");
        final int finalSex = sex;
        new HttpPostHandler(NickActivity.this, LinkServer.MemberAction.URL_MEMBER_SEX, map, true, "设置中") {
            @Override
            protected void onResult(String r) {
                AjaxResult ajaxResult = JsonHelper.jsonToObject(r, AjaxResult.class);
                String msg = "设置失败";
                if (ajaxResult == null) {
                    showTask(msg);
                    return;
                }
                if (!ajaxResult.isSuccess()) {
                    if (ajaxResult.getMsg() != null) msg = ajaxResult.getMsg();
                    showTask(msg);
                    return;
                }
                memberEntity.setSex(finalSex);
                MemberEntityImpl.getInstance().updateRow(memberEntity);
                sendBroadcast(new Intent(ProfileActivity.REFLASH_UI_PROFILE));

                Intent intent = new Intent(IMService.SEND_MESSAGE_TO_ALLFRIENDS);
                intent.setClass(NickActivity.this,IMService.class);
                intent.putExtra(MessageSender.MESSAGE_MODE,MessageSender.MESSAGE_MODE_NORMAL);
                intent.putExtra("msg", MessageFlag.MESSAGE_FLAG_FRIEND_INFOCHANGE + "][" + memberEntity.getId() + "]");
                startService(intent);
                finish();
            }
        };
    }

    //设置帐号
    private void setAccount() {
        final String username = accountView.getText().toString();
        if (username == null || username.equals("")) {
            showTask("请输入帐号");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(memberEntity.getId()));
        map.put("name", username);
        new HttpPostHandler(NickActivity.this, LinkServer.MemberAction.URL_MEMBER_USERNAME, map, true, "设置中") {
            @Override
            protected void onResult(String r) {
                AjaxResult ajaxResult = JsonHelper.jsonToObject(r, AjaxResult.class);
                String msg = "设置失败";
                if (ajaxResult == null) {
                    showTask(msg);
                    return;
                }
                if (!ajaxResult.isSuccess()) {
                    if (ajaxResult.getMsg() != null) msg = ajaxResult.getMsg();
                    showTask(msg);
                    return;
                }
                memberEntity.setUsername(username);
                MemberEntityImpl.getInstance().updateRow(memberEntity);
                sendBroadcast(new Intent(ProfileActivity.REFLASH_UI_PROFILE));

                Intent intent = new Intent(IMService.SEND_MESSAGE_TO_ALLFRIENDS);
                intent.setClass(NickActivity.this,IMService.class);
                intent.putExtra(MessageSender.MESSAGE_MODE,MessageSender.MESSAGE_MODE_NORMAL);
                intent.putExtra("msg", MessageFlag.MESSAGE_FLAG_FRIEND_INFOCHANGE + "][" + memberEntity.getId() + "]");
                startService(intent);
                finish();
            }
        };
    }

    //设置昵称
    private void setNick() {
        final String nick = nickView.getText().toString();
        if (nick == null || nick.equals("")) {
            showTask("填个名字吧");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(memberEntity.getId()));
        map.put("name", nick);
        new HttpPostHandler(NickActivity.this, LinkServer.MemberAction.URL_MEMBER_NICKNAME, map, true, "设置中") {
            @Override
            protected void onResult(String r) {
                AjaxResult ajaxResult = JsonHelper.jsonToObject(r, AjaxResult.class);
                String msg = "设置失败";
                if (ajaxResult == null) {
                    showTask(msg);
                    return;
                }
                if (!ajaxResult.isSuccess()) {
                    if (ajaxResult.getMsg() != null) msg = ajaxResult.getMsg();
                    showTask(msg);
                    return;
                }
                memberEntity.setNickname(nick);
                MemberEntityImpl.getInstance().updateRow(memberEntity);
                if (ACTIVITY_FROM.toUpperCase().equals("LOGIN")) {
                    Intent intent = new Intent();
                    intent.setClass(NickActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                sendBroadcast(new Intent(ProfileActivity.REFLASH_UI_PROFILE));

                Intent intent = new Intent(IMService.SEND_MESSAGE_TO_ALLFRIENDS);
                intent.setClass(NickActivity.this,IMService.class);
                intent.putExtra(MessageSender.MESSAGE_MODE,MessageSender.MESSAGE_MODE_NORMAL);
                intent.putExtra("msg", MessageFlag.MESSAGE_FLAG_FRIEND_INFOCHANGE + "][" + memberEntity.getId() + "]");
                startService(intent);
                finish();
            }
        };
    }

    private void showTask(String msg) {
        Toast.makeText(NickActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
