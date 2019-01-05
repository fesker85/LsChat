package cc.lzsou.lschat.chat.listener;

import android.view.View;
import android.widget.ImageView;

import cc.lzsou.lschat.data.bean.MessageEntity;

public interface OnChatItemListener {
    void onHeaderClick(int position);
    void onImageClick(View view, int position, MessageEntity data);
    void onVoiceClick(ImageView imageView, int position, MessageEntity data);
    void onVideoClick(ImageView imageView,int position,MessageEntity data);
}
