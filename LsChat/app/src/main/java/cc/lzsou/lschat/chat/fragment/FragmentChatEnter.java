package cc.lzsou.lschat.chat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cc.lzsou.lschat.R;

public class FragmentChatEnter extends Fragment {

    @BindView(R.id.editText)
    public EditText editText;
    @BindView(R.id.sendButton)
    LinearLayout sendButton;
    @BindView(R.id.btnMic)
    ImageView btnMic;
    @BindView(R.id.btnPhoto)
    ImageView btnPhoto;
    @BindView(R.id.btnCamera)
    ImageView btnCamera;
    @BindView(R.id.btnLocation)
    ImageView btnLocation;
    @BindView(R.id.btnGif)
    ImageView btnGif;
    @BindView(R.id.btnRedEnvelope)
    ImageView btnRedEnvelope;
    @BindView(R.id.btnGift)
    ImageView btnGift;
    @BindView(R.id.btnMood)
    ImageView btnMood;
    @BindView(R.id.btnPlus)
    ImageView btnPlus;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_enter, container, false);
        unbinder = ButterKnife.bind(this, view);
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

    @OnClick({R.id.sendButton, R.id.btnMic, R.id.btnPhoto, R.id.btnCamera, R.id.btnLocation, R.id.btnGif, R.id.btnRedEnvelope, R.id.btnGift, R.id.btnMood, R.id.btnPlus})
    public void onViewClicked(View view) {
        int tag = view.getTag()==null?0:Integer.parseInt(view.getTag().toString());
        view.setTag(tag==0?1:0);
        switch (view.getId()) {
            case R.id.sendButton:
                if(listener!=null)listener.onSendClick(editText);
                break;
            case R.id.btnMic:
                if(listener!=null)listener.onMicphoneClick(tag==0);
                defaultBackground(view,btnMic);
                if(tag==0)btnMic.setImageResource(R.drawable.svg_micphone_blue);
                break;
            case R.id.btnPhoto:
                if(listener!=null)listener.onPhotoClick(tag==0);
                break;
            case R.id.btnCamera:
                if(listener!=null)listener.onCameraClick(tag==0);
                break;
            case R.id.btnLocation:
                if(listener!=null)listener.onLocationClick(tag==0);
                break;
            case R.id.btnGif:
                if(listener!=null)listener.onGifClick(tag==0);
                defaultBackground(view,btnGif);
                if(tag==0)btnGif.setImageResource(R.drawable.svg_gif_blue);
                break;
            case R.id.btnRedEnvelope:
                if(listener!=null)listener.onRedEnvelopeClick(tag==0);
                break;
            case R.id.btnGift:
                if(listener!=null)listener.onGiftClick(tag==0);
                defaultBackground(view,btnGift);
                if(tag==0)btnGift.setImageResource(R.drawable.svg_gift_blue);
                break;
            case R.id.btnMood:
                if(listener!=null)listener.onMoodClick(tag==0);
                defaultBackground(view,btnMood);
                if(tag==0)btnMood.setImageResource(R.drawable.svg_mood_blue);
                break;
            case R.id.btnPlus:
                if(listener!=null)listener.onPlusClick(tag==0);
                defaultBackground(view,btnPlus);
                if(tag==0)btnPlus.setImageResource(R.drawable.svg_plus_blue);
                break;
        }
    }


    private void defaultBackground(View view,View self){
        if(view != self)btnPlus.setTag(0);
        btnPlus.setImageResource(R.drawable.svg_plus_gray);
        btnMood.setImageResource(R.drawable.svg_mood_gray);
        btnGift.setImageResource(R.drawable.svg_gift_gray);
        btnGif.setImageResource(R.drawable.svg_gif_gray);
        btnMic.setImageResource(R.drawable.svg_micphone_gray);
    }
    private OnEnterClickListener listener;
    public void addOnEnterClickListener(OnEnterClickListener onEnterClickListener){
        this.listener=onEnterClickListener;
    }

    public interface OnEnterClickListener{
        void onSendClick(EditText editText);
        void onMicphoneClick(boolean visibility);
        void onPhotoClick(boolean visibility);
        void onCameraClick(boolean visibility);
        void onLocationClick(boolean visibility);
        void onGifClick(boolean visibility);
        void onRedEnvelopeClick(boolean visibility);
        void onGiftClick(boolean visibility);
        void onMoodClick(boolean visibility);
        void onPlusClick(boolean visibility);
    }

}
