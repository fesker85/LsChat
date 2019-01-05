package cc.lzsou.lschat.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.activity.fragment.FragmentRegisterStepThree;
import cc.lzsou.lschat.activity.fragment.FragmentRegisterStepTwo;
import cc.lzsou.lschat.activity.fragment.FragmentRegsiterStepOne;

public class RegisterActivity extends AppCompatActivity {


    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private List<Fragment> fragmentList;


    private String nickname;
    private String mobile;
    private String password;

    public void setRegisterInfo(String nick,String mobile,String password){
        this.nickname = nick;
        this.mobile=mobile;
        this.password=password;
    }

    public String[] getRegisterInfo(){
        return  new String[]{this.nickname,this.mobile,this.password};
    }

    private static RegisterActivity instance;
    public static RegisterActivity getInstance() {
        return instance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        instance=this;
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentRegsiterStepOne());
        fragmentList.add(new FragmentRegisterStepTwo());
        fragmentList.add(new FragmentRegisterStepThree());
        showFragment(0);
    }

    public void showFragment(int position){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(!fragmentList.get(position).isAdded()){
            if(currentFragment==null)transaction.add(R.id.frameLayout,fragmentList.get(position));
            else  transaction.hide(currentFragment).add(R.id.frameLayout,fragmentList.get(position));
        }
        else transaction.hide(currentFragment).show(fragmentList.get(position));
        currentFragment= fragmentList.get(position);
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(currentFragment.equals(fragmentList.get(0))
                    ||currentFragment.equals(fragmentList.get(2)))
                finish();
            else showFragment(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
