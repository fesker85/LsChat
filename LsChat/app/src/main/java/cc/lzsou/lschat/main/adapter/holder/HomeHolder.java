package cc.lzsou.lschat.main.adapter.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.flag.MessageFlag;
import cc.lzsou.lschat.data.bean.FriendEntity;
import cc.lzsou.lschat.data.bean.MessageEntity;
import cc.lzsou.lschat.core.helper.DateHelper;
import cc.lzsou.lschat.manager.ImageLoaderManager;
import cc.lzsou.lschat.data.impl.FriendEntityImpl;
import cc.lzsou.lschat.data.impl.TempMessageEntityImpl;
import cc.lzsou.lschat.main.adapter.HomeAdapter;
import cc.lzsou.lschat.views.expression.ExpressionUtil;

public class HomeHolder extends BaseViewHolder<MessageEntity> {
    private ImageView headImg;
    private TextView nickView;
    private TextView dateView;
    private TextView msgView;
    private TextView countView;
    private Context context;
    private RelativeLayout itemLayout;
    private HomeAdapter.OnItemClickListener onItemClickListener;

    private FriendEntity entity;

    public HomeHolder(Context context, ViewGroup parentView, HomeAdapter.OnItemClickListener onItemClickListener) {
        super(parentView, R.layout.item_home);
        this.onItemClickListener = onItemClickListener;
        this.context = context;
        headImg = (ImageView) itemView.findViewById(R.id.headImg);
        nickView = (TextView) itemView.findViewById(R.id.nickView);
        dateView = (TextView) itemView.findViewById(R.id.dateView);
        msgView = (TextView) itemView.findViewById(R.id.msgView);
        countView = (TextView) itemView.findViewById(R.id.countView);
        itemLayout = (RelativeLayout) itemView.findViewById(R.id.itemLayout);

    }

    @Override
    public void setData(MessageEntity data) {
        if (data == null) return;
        if (entity == null) {
            if (data.getMode() == MessageEntity.MODE_CHAT)
                entity = FriendEntityImpl.getInstance().selectRow(data.getUid());
            else if (data.getMode() == MessageEntity.MODE_GROUP)
                entity = FriendEntityImpl.getInstance().selectRow(data.getUid(), data.getMid(), FriendEntity.MODE_GROUP);
            else
                entity = FriendEntityImpl.getInstance().selectRow(data.getUid(), data.getMid(), FriendEntity.MODE_NOTICE);
        }
        if (data.getMsg() != null) {
            if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_PASSEDFRIEND) || data.getMsg().contains(MessageFlag.MESSAGE_FLAG_FRIENDAGREE))
                msgView.setText(data.getMsg().split("]")[1].substring(1));
            else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_IMAGE))//图片
                msgView.setText("[图片]");
            else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_VOICE))//语音
                msgView.setText("[语音]");
            else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_GIFIMAGE))//动画表情
                msgView.setText("[动画表情]");
            else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_EXPRESSION))//表情
                msgView.setText(ExpressionUtil.getText(context, data.getMsg()));
            else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_LOCATION))//位置
                msgView.setText("[位置]");
            else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_ENVELOPE))//红包
                msgView.setText("[红包]");
            else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_GIVEMONEY))//转账
                msgView.setText("[转账]");
            else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_GIFT))//礼物
                msgView.setText("[礼物]");
            else if (data.getMsg().contains(MessageFlag.MESSAGE_FLAG_VIDEO))//视频
                msgView.setText("[视频]");
            else msgView.setText(data.getMsg());
        }

        nickView.setText(entity.getNickname());
        dateView.setText(DateHelper.getInterval(data.getCurtime()));
        if (data.getMode() == MessageEntity.MODE_CHAT)
            ImageLoaderManager.getInstance().displayAvatar(entity.getAvatar(), headImg);
        else if (data.getMode() == MessageEntity.MODE_GROUP)
            headImg.setImageResource(R.drawable.default_group);
        else if (data.getMode() == MessageEntity.MODE_NOTICE)
            headImg.setImageResource(R.mipmap.ic_launcher);
        else {
        }
        countView.setVisibility(View.GONE);
        long count = TempMessageEntityImpl.getInstance().getCount(data.getMid());
        if (count > 0) {
            countView.setVisibility(View.VISIBLE);
            countView.setText("" + count);
        }

        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) onItemClickListener.onItemClickListener(data);
            }
        });
        itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null)
                    return onItemClickListener.onLongItemClickListener(v, data);
                return false;
            }
        });
    }
}
