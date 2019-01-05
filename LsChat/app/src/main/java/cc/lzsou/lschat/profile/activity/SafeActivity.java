package cc.lzsou.lschat.profile.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.data.impl.MemberEntityImpl;

public class SafeActivity extends AppCompatActivity {

    @BindView(R.id.backButton)
    ImageButton backButton;
    @BindView(R.id.mobileView)
    TextView mobileView;
    @BindView(R.id.mobileLayout)
    RelativeLayout mobileLayout;
    @BindView(R.id.accountView)
    TextView accountView;
    @BindView(R.id.accountLayout)
    RelativeLayout accountLayout;
    @BindView(R.id.emailView)
    TextView emailView;
    @BindView(R.id.emailLayout)
    RelativeLayout emailLayout;
    @BindView(R.id.passwordView)
    TextView passwordView;
    @BindView(R.id.passwordLayout)
    RelativeLayout passwordLayout;
    @BindView(R.id.paypwdView)
    TextView paypwdView;
    @BindView(R.id.paypwdLayout)
    RelativeLayout paypwdLayout;

    private MemberEntity entity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);
        ButterKnife.bind(this);
        entity= MemberEntityImpl.getInstance().selectRow();
        init();
    }

    @SuppressLint("NewApi")
    private void init(){
        mobileView.setText(entity.getMobile());
        String username=TextUtils.isEmpty(entity.getUsername())?"未设置":(entity.getUsername().equals("null")?"未设置":entity.getUsername());
        accountView.setText(username);
        if(username.equals("未设置"))accountView.setTextColor(getColor(R.color.colorRed));

        String email=TextUtils.isEmpty(entity.getEmail())?"未设置":(entity.getEmail().equals("null")?"未设置":entity.getEmail());
        emailView.setText(email);
        if(email.equals("未设置"))emailView.setTextColor(getColor(R.color.colorRed));

        passwordView.setText("已设置");
        passwordView.setTextColor(getColor(R.color.colorGreen));
        String paypwd = TextUtils.isEmpty(entity.getPaypwd())?"未设置":(entity.getPaypwd().equals("null")?"未设置":entity.getPaypwd());
        paypwdView.setText(paypwd);
        if(paypwd.equals("未设置"))paypwdView.setTextColor(getColor(R.color.colorRed));
        else paypwdView.setTextColor(getColor(R.color.colorGreen));
    }

    @OnClick({R.id.backButton, R.id.mobileLayout, R.id.accountLayout, R.id.emailLayout, R.id.passwordLayout, R.id.paypwdLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.mobileLayout:
                break;
            case R.id.accountLayout:
                break;
            case R.id.emailLayout:
                break;
            case R.id.passwordLayout:
                break;
            case R.id.paypwdLayout:
                break;
        }
    }
}
