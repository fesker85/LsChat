package cc.lzsou.lschat.activity.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.activity.RegisterActivity;

public class FragmentRegisterStepThree extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.nickView)
    EditText nickView;
    @BindView(R.id.phoneView)
    EditText phoneView;
    private String[] stepOneInfo;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_stepthree, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stepOneInfo = RegisterActivity.getInstance().getRegisterInfo();
        nickView.setText(stepOneInfo[0]);
        phoneView.setText(stepOneInfo[1]);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.toLoginButton)
    public void onViewClicked() {
        getActivity().finish();
    }
}
