package cc.lzsou.lschat.chat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lzsou.lschat.R;

public class FragmentChatVoice extends Fragment {
    @BindView(R.id.btnVoice)
    TextView btnVoice;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_voice, container, false);
        unbinder = ButterKnife.bind(this, view);
        if(onViewCreated!=null)
            onViewCreated.onCreated(btnVoice);
        btnVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(onViewCreated!=null)return onViewCreated.onVoiceTouch(event);
                else  return false;
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



    private OnViewCreated onViewCreated;
    public void addOnViewCreated(OnViewCreated onViewCreated){
        this.onViewCreated=onViewCreated;
    }
    public interface OnViewCreated{
        void onCreated(TextView textView);
        boolean onVoiceTouch(MotionEvent event);
    }

}
