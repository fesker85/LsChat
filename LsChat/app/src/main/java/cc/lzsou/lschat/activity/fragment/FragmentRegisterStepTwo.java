package cc.lzsou.lschat.activity.fragment;

import android.os.Bundle;
import android.os.Handler;
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
import cc.lzsou.servercore.LinkServer;

public class FragmentRegisterStepTwo extends Fragment {
    @BindView(R.id.backView)
    ImageButton backView;
    @BindView(R.id.nickView)
    EditText nickView;
    @BindView(R.id.phoneView)
    EditText phoneView;
    @BindView(R.id.codeButton)
    Button codeButton;
    @BindView(R.id.codeView)
    EditText codeView;
    @BindView(R.id.registerButton)
    Button registerButton;
    Unbinder unbinder;

    private String[] stepOneInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_steptwo, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stepOneInfo =RegisterActivity.getInstance().getRegisterInfo();
        nickView.setText(stepOneInfo[0]);
        phoneView.setText(stepOneInfo[1]);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }




    @OnTextChanged(value = R.id.codeView, callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onCodeTextChanged(CharSequence s, int start, int before, int count){
        if(count>0)registerButton.setEnabled(true);
        else registerButton.setEnabled(false);
    }



    @OnClick({R.id.backView, R.id.codeButton, R.id.registerButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backView:
                RegisterActivity.getInstance().showFragment(0);
                break;
            case R.id.codeButton:
                doGetCode();
                break;
            case R.id.registerButton:
                doRegister();
                break;
        }
    }

    private void doRegister(){
        Map<String,String> map = new HashMap<>();
        map.put("nick",stepOneInfo[0]);
        map.put("mobile",stepOneInfo[1]);
        map.put("password",stepOneInfo[2]);
        map.put("code",codeView.getText().toString());
        new HttpPostHandler(getActivity(), LinkServer.PassportAction.URL_REGISTER, map, true, "注册中") {
            @Override
            protected void onResult(String r) {
                String msg = "注册失败";
                AjaxResult ajaxResult = JsonHelper.jsonToObject(r,AjaxResult.class);
                if(ajaxResult==null){
                    MessageHandler.showToask(msg);
                    return;
                }
                if(!ajaxResult.isSuccess()){
                    if(ajaxResult.getMsg()!=null)msg=ajaxResult.getMsg();
                    MessageHandler.showToask(msg);
                    return;
                }

                RegisterActivity.getInstance().showFragment(2);
            }
        };
    }

    private void doGetCode(){
        Map<String,String > map = new HashMap<>();
        map.put("mobile",stepOneInfo[1]);
        new HttpPostHandler(getActivity(), LinkServer.PassportAction.URL_SMSCODE, map, true, "获取中") {
            @Override
            protected void onResult(String r) {
                String msg ="获取失败";
                AjaxResult ajaxResult = JsonHelper.jsonToObject(r,AjaxResult.class);
                if(ajaxResult==null){
                    MessageHandler.showToask(msg);
                    return;
                }
                if(!ajaxResult.isSuccess()){
                    if(ajaxResult.getMsg()!=null)msg=ajaxResult.getMsg();
                    MessageHandler.showToask(msg);
                    return;
                }
                MessageHandler.showToask("短信验证码已发送至您的手机");
                handler.postDelayed(runnable,1000);
            }
        };
    }

    int timeout=120;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try{
                timeout--;
                if(timeout<=0){
                    timeout=120;
                    codeButton.setText("获取");
                    codeButton.setEnabled(true);
                }
                else {
                    codeButton.setEnabled(false);
                    codeButton.setText(timeout+"");
                    handler.postDelayed(runnable,1000);
                }
            }
            catch (Exception e){

            }

        }
    };
}
