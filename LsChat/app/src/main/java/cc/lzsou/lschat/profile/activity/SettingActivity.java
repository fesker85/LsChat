package cc.lzsou.lschat.profile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.ActivityStarter;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.backButton)
    ImageButton backButton;
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.topLayout)
    RelativeLayout topLayout;
    @BindView(R.id.infoLayout)
    RelativeLayout infoLayout;
    @BindView(R.id.safeLayout)
    RelativeLayout safeLayout;
    @BindView(R.id.noticeLayout)
    RelativeLayout noticeLayout;
    @BindView(R.id.disturbLayout)
    RelativeLayout disturbLayout;
    @BindView(R.id.privacyLayout)
    RelativeLayout privacyLayout;
    @BindView(R.id.generalLayout)
    RelativeLayout generalLayout;
    @BindView(R.id.aboutLayout)
    RelativeLayout aboutLayout;
    @BindView(R.id.exitLayout)
    RelativeLayout exitLayout;
    @BindView(R.id.clearLayout)
    RelativeLayout clearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.backButton, R.id.infoLayout, R.id.safeLayout, R.id.noticeLayout, R.id.disturbLayout, R.id.privacyLayout, R.id.generalLayout,R.id.clearLayout, R.id.aboutLayout, R.id.exitLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backButton://返回
                finish();
                break;
            case R.id.infoLayout://个人信息
                ActivityStarter.startProfileActivity(SettingActivity.this);
                break;
            case R.id.safeLayout://帐号与安全
                ActivityStarter.startSafeActivity(SettingActivity.this);
                break;
            case R.id.noticeLayout://新消息通知
                ActivityStarter.startNotiSettingActivity(SettingActivity.this);
                break;
            case R.id.disturbLayout://勿扰模式
                ActivityStarter.startDisturbActivity(SettingActivity.this);
                break;
            case R.id.privacyLayout://隐私
                ActivityStarter.startPrivacyActivity(SettingActivity.this);
                break;
            case R.id.clearLayout://一键清理
                ActivityStarter.startClearActivity(SettingActivity.this);
                break;
            case R.id.generalLayout://通用
                break;
            case R.id.aboutLayout://关于
                ActivityStarter.startWebActivity(SettingActivity.this,"关于","http://sntianyuan.com");
                break;
            case R.id.exitLayout://退出
                break;
        }
    }
}
