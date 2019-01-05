package cc.lzsou.lschat.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.activity.ForgetActivity;
import cc.lzsou.lschat.activity.RegisterActivity;
import cc.lzsou.lschat.base.AjaxResult;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.core.handler.HttpPostHandler;
import cc.lzsou.lschat.core.handler.MessageHandler;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.data.impl.MemberEntityImpl;
import cc.lzsou.lschat.main.activity.MainActivity;
import cc.lzsou.lschat.manager.PhoneManager;
import cc.lzsou.lschat.profile.activity.NickActivity;
import cc.lzsou.servercore.LinkServer;

public class FragmentLoginPassword extends Fragment {
    @BindView(R.id.registerView)
    TextView registerView;
    @BindView(R.id.phoneView)
    EditText phoneView;
    @BindView(R.id.passwordView)
    EditText passwordView;
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.codeView)
    TextView codeView;
    @BindView(R.id.forgetView)
    TextView forgetView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_password,container,false);
        ButterKnife.bind(this,view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String phoneNumber = PhoneManager.getInstance().getPhoneNumber();
        if (phoneNumber != null&&phoneNumber.length()==11) phoneView.setText(phoneNumber);
        phoneView.setSelection(phoneView.length());
    }

    @OnTextChanged(value = R.id.phoneView, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onPhoneNumberTextChanged(CharSequence s, int start, int before, int count) {
        String password = passwordView.getText().toString();
        if (count > 0 && (password != null && password.length() > 0)) loginButton.setEnabled(true);
        else loginButton.setEnabled(false);
    }

    @OnTextChanged(value = R.id.passwordView, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onPasswordTextChanged(CharSequence s, int start, int before, int count) {
        String phoneNumber = phoneView.getText().toString();
        if (count > 0 && (phoneNumber != null && phoneNumber.length() > 0))
            loginButton.setEnabled(true);
        else loginButton.setEnabled(false);
    }

    private void doLogin() {
        final String phoneNumber = phoneView.getText().toString();
        String password = passwordView.getText().toString();
        if (phoneNumber == null || phoneNumber.equals("")) {
            MessageHandler.showToask("请输入手机号码");
            phoneView.findFocus();
            return;
        }
        if (password == null || password.equals("")) {
            MessageHandler.showToask("请输入登录密码");
            passwordView.findFocus();
            return;
        }
        final Map<String, String> map = new HashMap<>();
        map.put("username", phoneNumber);
        map.put("password", password);
        new HttpPostHandler(getActivity(), LinkServer.PassportAction.URL_LOGIN, map, true, "登录中") {
            @Override
            protected void onResult(String result) {
                AjaxResult ajaxResult = JsonHelper.jsonToObject(result, AjaxResult.class);
                if (ajaxResult == null) {
                    MessageHandler.showToask("登录失败");
                    return;
                }
                if (!ajaxResult.isSuccess()) {
                    MessageHandler.showToask(ajaxResult.getMsg());
                    return;
                }

                MemberEntity memberEntity = new MemberEntity();
                memberEntity.setId(Integer.parseInt(ajaxResult.getContent().trim()));
                memberEntity.setNickname(ajaxResult.getMsg().trim());
                MemberEntityImpl.getInstance().insertRow(memberEntity);
                Intent intent = new Intent();
                if (memberEntity.getNickname() == null || memberEntity.getNickname().equals("")) {
                    intent.setClass(getActivity(), NickActivity.class);
                    intent.putExtra("from", "login");
                } else intent.setClass(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        };
    }


    @OnClick({R.id.registerView, R.id.loginButton, R.id.codeView, R.id.forgetView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.registerView:
                onRegister();
                break;
            case R.id.loginButton:
                doLogin();
                break;
            case R.id.codeView:
                break;
            case R.id.forgetView:
                onForget();
                break;
        }
    }


    private void onRegister() {
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }



    private void onForget() {
        Intent intent = new Intent(getActivity(), ForgetActivity.class);
        startActivity(intent);
    }
}
