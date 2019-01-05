package cc.lzsou.lschat.chat.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cc.lzsou.lschat.data.bean.FriendEntity;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.chat.adapter.holder.ChatFromViewHolder;
import cc.lzsou.lschat.chat.adapter.holder.ChatSendViewHolder;
import cc.lzsou.lschat.data.bean.MessageEntity;
import cc.lzsou.lschat.chat.listener.OnChatItemListener;

public class ChatAdapter extends RecyclerArrayAdapter<MessageEntity>{


    private OnChatItemListener onChatItemListener;
    public Handler handler;
    private Context context;
    private MemberEntity memberEntity;
    private FriendEntity friendEntity;
    public ChatAdapter(Context context, MemberEntity memberEntity, FriendEntity friendEntity) {
        super(context);
        this.context=context;
        handler = new Handler();
        this.memberEntity=memberEntity;
        this.friendEntity=friendEntity;
    }

    @Override
    public int getViewType(int position) {
        return getAllData().get(position).getPath();
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        switch (viewType){
            case MessageEntity.PATH_FTOM:
                viewHolder = new ChatFromViewHolder(parent,this.onChatItemListener,handler,friendEntity);
                break;
            case MessageEntity.PATH_TO:
                viewHolder=new ChatSendViewHolder(parent,this.onChatItemListener,handler,memberEntity);
        }
        return viewHolder;
    }

    public void addItemClickListener(OnChatItemListener onChatItemListener) {
        this.onChatItemListener = onChatItemListener;
    }




}
