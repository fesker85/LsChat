package cc.lzsou.lschat.activity.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.activity.RegisterActivity;
import cc.lzsou.lschat.base.AjaxResult;
import cc.lzsou.lschat.core.handler.HttpPostHandler;
import cc.lzsou.lschat.core.handler.MessageHandler;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.manager.PhoneManager;
import cc.lzsou.servercore.LinkServer;

public class FragmentRegsiterStepOne extends Fragment {
    @BindView(R.id.backView)
    ImageButton backView;
    @BindView(R.id.nickView)
    EditText nickView;
    @BindView(R.id.phoneView)
    EditText phoneView;
    @BindView(R.id.passwordView)
    EditText passwordView;
    @BindView(R.id.nextButton)
    Button nextButton;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_stepone, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String phoneNumber=PhoneManager.getInstance().getPhoneNumber();
        if(phoneNumber!=null&&phoneNumber.length()==11)
            phoneView.setText(phoneNumber);
        phoneView.setSelection(phoneView.length());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.backView, R.id.nextButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backView:
                RegisterActivity.getInstance().finish();
                break;
            case R.id.nextButton:
                doCheck();
                break;
        }
    }

    @OnTextChanged(value = R.id.nickView,callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onNickTextChanged(CharSequence s, int start, int before, int count) {
        if(nickView.length()>0&&phoneView.length()>0&&passwordView.length()>0)nextButton.setEnabled(true);
        else nextButton.setEnabled(false);
    }
    @OnTextChanged(value = R.id.phoneView,callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onPhoneTextChanged(CharSequence s, int start, int before, int count) {
        if(nickView.length()>0&&phoneView.length()>0&&passwordView.length()>0)nextButton.setEnabled(true);
        else nextButton.setEnabled(false);
    }
    @OnTextChanged(value = R.id.passwordView,callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onPasswordTextChanged(CharSequence s, int start, int before, int count) {
        if(nickView.length()>0&&phoneView.length()>0&&passwordView.length()>0)nextButton.setEnabled(true);
        else nextButton.setEnabled(false);
    }

    private void doCheck(){
        final String nick = nickView.getText().toString();
        final String phoneNumber = phoneView.getText().toString();
        final String password = passwordView.getText().toString();
        Map<String,String> map = new HashMap<>();
        map.put("nick",nick);
        map.put("mobile",phoneNumber);
        map.put("password",password);
        new HttpPostHandler(getActivity(), LinkServer.PassportAction.URL_REGISTER_CHECK, map, true, "验证中") {
            @Override
            protected void onResult(String r) {
                AjaxResult ajaxResult = JsonHelper.jsonToObject(r,AjaxResult.class);
                String msg ="验证失败";
                if(ajaxResult==null) {
                    MessageHandler.showToask(msg);
                    return;
                }

                if(!ajaxResult.isSuccess()){
                    if(ajaxResult.getMsg()!=null)msg=ajaxResult.getMsg();
                    MessageHandler.showToask(msg);
                    return;
                }
                RegisterActivity.getInstance().setRegisterInfo(nick,phoneNumber,password);
                RegisterActivity.getInstance().showFragment(1);
            }
        };
    }


}
