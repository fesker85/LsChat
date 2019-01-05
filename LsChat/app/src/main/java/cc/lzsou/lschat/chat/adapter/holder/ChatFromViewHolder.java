package cc.lzsou.lschat.chat.adapter.holder;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.flag.MessageFlag;
import cc.lzsou.lschat.data.bean.FriendEntity;
import cc.lzsou.lschat.base.BaseApplication;
import cc.lzsou.lschat.data.bean.MessageEntity;
import cc.lzsou.lschat.chat.listener.OnChatItemListener;
import cc.lzsou.lschat.core.helper.DateHelper;
import cc.lzsou.lschat.manager.FileManager;
import cc.lzsou.lschat.manager.ImageLoaderManager;
import cc.lzsou.lschat.views.expression.ExpressionUtil;
import cc.lzsou.servercore.LinkServer;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class ChatFromViewHolder extends BaseViewHolder<MessageEntity> {

    private TextView timeView;
    private ImageView avatarView;
    private LinearLayout imageLayout;
    private ImageView imageView;
    private LinearLayout gifLayout;
    private GifImageView gifView;
    private TextView textView;
    private ImageView voiceView;
    private TextView voiceTime;
    private LinearLayout locationLayout;
    private TextView locationText;
    private ImageView locationImage;


    private OnChatItemListener onItemClickListener;
    private Handler handler;
    private FriendEntity friendEntity;
    public ChatFromViewHolder(ViewGroup parent, OnChatItemListener onItemClickListener, Handler handler, FriendEntity friendEntity) {
        super(parent, R.layout.item_chat_from);
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
        this.friendEntity=friendEntity;
        init();
    }

    @Override
    public void setData(final MessageEntity data) {
        timeView.setText(data.getCurtime());
        ImageLoaderManager.getInstance().displayAvatar(LinkServer.FileAction.getImageAddress(friendEntity.getAvatar()), avatarView);
        if(data.getMsg().contains(MessageFlag.MESSAGE_FLAG_FRIENDAGREE)){
            timeView.setVisibility(View.VISIBLE);
            timeView.setText(data.getMsg().split("]")[1].substring(1));
            avatarView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
        else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_VOICE)) setVoice(data);
        else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_GIFIMAGE)) setGifView(data);
        else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_IMAGE)) setImage(data);
        else if(data.getMsg().contains(MessageFlag.MESSAGE_FLAG_LOCATION))setLocation(data);
        else setText(data);
    }

    private void setLocation(MessageEntity data){
        hideView();
        locationLayout.setVisibility(View.VISIBLE);
        String[] arrString = data.getMsg().split("]");
        String name = arrString[1].substring(1);
        String addr = arrString[2].substring(1);
        String lng = arrString[3].substring(1);
        String imgStr = arrString[4].substring(1);
        String fileName= FileManager.getInstance().getImagePath()+"/"+data.getId()+".png";
        if(imgStr==null||imgStr.equals(""))locationImage.setImageResource(R.mipmap.default_map);
        else ImageLoaderManager.getInstance().displayFromBase64String(imgStr,fileName,locationImage);
        locationText.setText(name+"("+addr+")");
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
    }

    private void setText(MessageEntity data) {
        hideView();
        textView.setVisibility(View.VISIBLE);
        if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_EXPRESSION))
            textView.setText(ExpressionUtil.getText(BaseApplication.getInstance(), data.getMsg()));
        else textView.setText(data.getMsg());
    }

    private void setGifView(MessageEntity data) {
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
    }

    private void setVoice(final MessageEntity data) {
        hideView();
        voiceTime.setVisibility(View.VISIBLE);
        voiceView.setVisibility(View.VISIBLE);
        voiceTime.setText(DateHelper.longToTime(data.getRectime()));
        voiceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null)
                    onItemClickListener.onVoiceClick(voiceView, getDataPosition(), data);
            }
        });
        voiceView.getLayoutParams().width = (int) (BaseApplication.minChatVoiceItemWidth + (BaseApplication.maxChatVoiceItemWidth / 60f) * Math.floor((data.getRectime() / 1000)));
    }

    private void init() {
        timeView = (TextView) itemView.findViewById(R.id.timeView);
        avatarView = (ImageView) itemView.findViewById(R.id.avatarView);
        imageLayout=(LinearLayout)itemView.findViewById(R.id.imageLayout);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        gifLayout=(LinearLayout)itemView.findViewById(R.id.gifLayout);
        gifView = (GifImageView) itemView.findViewById(R.id.gifView);
        textView = (TextView) itemView.findViewById(R.id.textView);
        voiceView = (ImageView) itemView.findViewById(R.id.voiceView);
        voiceTime = (TextView) itemView.findViewById(R.id.voiceTime);
        locationLayout=(LinearLayout)itemView.findViewById(R.id.locationLayout);
        locationText=(TextView)itemView.findViewById(R.id.locationText);
        locationImage=(ImageView) itemView.findViewById(R.id.locationImage);
        textView.setMaxWidth(BaseApplication.maxChatVoiceItemWidth);
        voiceView.setMinimumWidth(BaseApplication.minChatVoiceItemWidth);
        voiceView.setMaxWidth(BaseApplication.maxChatVoiceItemWidth);
        imageView.setMaxWidth(BaseApplication.maxChatVoiceItemWidth);
    }

    private void hideView() {
        timeView.setVisibility(View.GONE);
        imageLayout.setVisibility(View.GONE);
        gifLayout.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        voiceView.setVisibility(View.GONE);
        voiceTime.setVisibility(View.GONE);
        locationLayout.setVisibility(View.GONE);
    }
}
