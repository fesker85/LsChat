package cc.lzsou.lschat.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.activity.fragment.FragmentLoginPassword;

public class LoginActivity extends AppCompatActivity {


    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout,new FragmentLoginPassword()).commit();
    }


}
