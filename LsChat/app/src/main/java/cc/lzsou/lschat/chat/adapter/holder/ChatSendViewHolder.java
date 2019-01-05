package cc.lzsou.lschat.chat.adapter.holder;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.flag.MessageFlag;
import cc.lzsou.lschat.core.helper.Common;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.base.BaseApplication;
import cc.lzsou.lschat.data.bean.MessageEntity;
import cc.lzsou.lschat.chat.listener.OnChatItemListener;
import cc.lzsou.lschat.core.helper.DateHelper;
import cc.lzsou.lschat.manager.FileManager;
import cc.lzsou.lschat.manager.ImageLoaderManager;
import cc.lzsou.lschat.manager.ImageManager;
import cc.lzsou.lschat.views.expression.ExpressionUtil;
import cc.lzsou.servercore.LinkServer;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class ChatSendViewHolder extends BaseViewHolder<MessageEntity> {

    private TextView timeView;
    private ImageView avatarView;
    private ImageView imageView;
    private GifImageView gifView;
    private TextView textView;
    private ImageView voiceView;
    private TextView voiceTime;
    private ImageView failView;
    private ProgressBar progressView;
    private LinearLayout gifLayout;
    private RelativeLayout imageLayout;
    private LinearLayout locationLayout;
    private TextView locationText;
    private ImageView locationImage;
    private RelativeLayout videoLayout;
    private ImageView videoImage;
    private TextView videoTime;

    private OnChatItemListener onItemClickListener;
    private Handler handler;
    private MemberEntity memberEntity;

    public ChatSendViewHolder(ViewGroup parent, OnChatItemListener onItemClickListener, Handler handler, MemberEntity memberEntity) {
        super(parent, R.layout.item_chat_send);
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
        this.memberEntity=memberEntity;
        init();
    }

    @Override
    public void setData(final MessageEntity data) {
        super.setData(data);
        timeView.setText(data.getCurtime());
        ImageLoaderManager.getInstance().displayAvatar(LinkServer.FileAction.getImageAddress(memberEntity.getAvatar()), avatarView);
        if(data.getMsg().contains(MessageFlag.MESSAGE_FLAG_PASSEDFRIEND)){
            timeView.setVisibility(View.VISIBLE);
            timeView.setText(data.getMsg().split("]")[1].substring(1));
            avatarView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
        else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_VOICE)) setVoice(data);
        else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_GIFIMAGE)) setGifImage(data);
        else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_IMAGE)) setImage(data);
        else if(data.getMsg().contains(MessageFlag.MESSAGE_FLAG_LOCATION))setLocation(data);
        else if(data.getMsg().contains(MessageFlag.MESSAGE_FLAG_VIDEO))setVideo(data);
        else setText(data);

    }
    //设置位置
    private void setLocation(MessageEntity data){
        hideView();
        locationLayout.setVisibility(View.VISIBLE);
        String[] arrString = data.getMsg().split("]");
        String name = arrString[1].substring(1);
        String addr = arrString[2].substring(1);
        String lng = arrString[3].substring(1);
        String base64String = arrString[4].substring(1);
        String fileName= FileManager.getInstance().getImagePath()+"/"+data.getId();
        if(base64String==null||base64String.equals(""))locationImage.setImageResource(R.mipmap.default_map);
        else ImageLoaderManager.getInstance().displayFromBase64String(base64String,fileName,locationImage);
        locationText.setText(name+"("+addr+")");
        setState(data);
    }
    //设置视频
    private void setVideo(MessageEntity data){
        hideView();
        videoLayout.setVisibility(View.VISIBLE);
        String[] arrString = data.getMsg().split("]");
        String time = arrString[1].substring(1);
        videoTime.setText(DateHelper.longToTime(Long.parseLong(time)));
        String base64String = arrString[2].substring(1);
        String fileName = FileManager.getInstance().getImagePath()+"/"+data.getId();
        if(base64String==null||base64String.equals(""))videoImage.setImageResource(R.mipmap.icon_image_error);
        else ImageLoaderManager.getInstance().displayFromBase64String(base64String,fileName,videoImage);
        Bitmap bitmap =ImageManager.base64String2Bitmap(base64String);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        params.setMargins(0,0,8,0);
        videoLayout.setLayoutParams(params);
        setState(data);
        if(!bitmap.isRecycled())bitmap.recycle();
        bitmap =null;
    }
    //设置图片
    private void setImage(MessageEntity data) {
        hideView();
        String[] arrString =data.getMsg().split("]");
        String base64String = arrString[1].substring(1);
        imageLayout.setVisibility(View.VISIBLE);
        String fileName = FileManager.getInstance().getImagePath()+"/"+data.getId();
        if(base64String==null||base64String.equals(""))imageView.setImageResource(R.mipmap.icon_image_error);
        else ImageLoaderManager.getInstance().displayFromBase64String(base64String,fileName,imageView);

        Bitmap bitmap =ImageManager.base64String2Bitmap(base64String);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        params.setMargins(0,0,8,0);
        imageLayout.setLayoutParams(params);
        setState(data);
        if(!bitmap.isRecycled())bitmap.recycle();
        bitmap =null;
    }

    //设置文字
    private void setText(MessageEntity data) {
        hideView();
        textView.setVisibility(View.VISIBLE);
        if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_EXPRESSION))
            textView.setText(ExpressionUtil.getText(BaseApplication.getInstance(), data.getMsg()));
        else textView.setText(data.getMsg());
        setState(data);
    }


    //gif图片
    private void setGifImage(final MessageEntity data) {
        hideView();
        int resId = R.drawable.logo;
        try {
            Field field = R.mipmap.class.getDeclaredField(data.getMsg().substring(2, data.getMsg().indexOf("]")));
            resId = Integer.parseInt(field.get(null).toString());
            GifDrawable gifDrawable = new GifDrawable(BaseApplication.getInstance().getResources(), resId);
            gifLayout.setVisibility(View.VISIBLE);
            gifView.setImageDrawable(gifDrawable);

        } catch (IOException | IllegalAccessException | NoSuchFieldException e) {
            imageLayout.setVisibility(View.VISIBLE);
            imageView.setImageResource(resId);
        }
        setState(data);
    }

    //语音
    private void setVoice(final MessageEntity data) {
        hideView();
        voiceView.setVisibility(View.VISIBLE);
        voiceTime.setVisibility(View.VISIBLE);
        voiceTime.setText(DateHelper.longToTime(data.getRectime()));
        voiceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null)
                    onItemClickListener.onVoiceClick(voiceView, getDataPosition(), data);
            }
        });
        voiceView.getLayoutParams().width = (int) (BaseApplication.minChatVoiceItemWidth + (BaseApplication.maxChatVoiceItemWidth / 60f) * Math.floor((data.getRectime() / 1000)));
        setState(data);
    }

    private void setState(MessageEntity data) {
        if (data.getStatus() == MessageEntity.STATE_SEND_SENDING) {
            failView.setVisibility(View.GONE);
            progressView.setVisibility(View.VISIBLE);
        }
        if (data.getStatus() == MessageEntity.STATE_SEND_FAILED) {
            failView.setVisibility(View.VISIBLE);
            progressView.setVisibility(View.GONE);
        }
        if (data.getStatus() == MessageEntity.STATE_SEND_SUCCESS) {
            failView.setVisibility(View.GONE);
            progressView.setVisibility(View.GONE);
        }
    }

    private void init() {
        timeView = (TextView) itemView.findViewById(R.id.timeView);
        avatarView = (ImageView) itemView.findViewById(R.id.avatarView);
        imageLayout=(RelativeLayout)itemView.findViewById(R.id.imageLayout);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        gifLayout=(LinearLayout)itemView.findViewById(R.id.gifLayout);
        gifView = (GifImageView) itemView.findViewById(R.id.gifView);
        textView = (TextView) itemView.findViewById(R.id.textView);
        voiceView = (ImageView) itemView.findViewById(R.id.voiceView);
        voiceTime = (TextView) itemView.findViewById(R.id.voiceTime);
        failView = (ImageView) itemView.findViewById(R.id.failView);
        progressView = (ProgressBar) itemView.findViewById(R.id.progressView);
        locationLayout = (LinearLayout) itemView.findViewById(R.id.locationLayout);
        locationText = (TextView) itemView.findViewById(R.id.locationText);
        locationImage=(ImageView) itemView.findViewById(R.id.locationImage);
        videoLayout =(RelativeLayout)itemView.findViewById(R.id.videoLayout);
        videoImage=(ImageView)itemView.findViewById(R.id.videoImage);
        videoTime=(TextView)itemView.findViewById(R.id.videoTime);

        textView.setMaxWidth(BaseApplication.maxChatVoiceItemWidth);
        voiceView.setMinimumWidth(BaseApplication.minChatVoiceItemWidth);
        voiceView.setMaxWidth(BaseApplication.maxChatVoiceItemWidth);
        imageView.setMaxWidth((BaseApplication.maxChatVoiceItemWidth/3)*2);
        videoImage.setMaxWidth((BaseApplication.maxChatVoiceItemWidth/3)*2);
    }

    private void hideView() {
        timeView.setVisibility(View.GONE);
        imageLayout.setVisibility(View.GONE);
        gifLayout.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        voiceView.setVisibility(View.GONE);
        voiceTime.setVisibility(View.GONE);
        failView.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
        locationLayout.setVisibility(View.GONE);
        videoLayout.setVisibility(View.GONE);
    }


}
