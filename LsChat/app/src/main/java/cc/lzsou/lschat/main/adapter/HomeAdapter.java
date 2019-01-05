package cc.lzsou.lschat.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cc.lzsou.lschat.data.bean.MessageEntity;
import cc.lzsou.lschat.main.adapter.holder.HomeHolder;

public class HomeAdapter extends RecyclerArrayAdapter<MessageEntity> {
    private Context context;
    public HomeAdapter(Context context) {
        super(context);
        this.context=context;
    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder<MessageEntity> viewHolder = new HomeHolder(context,parent,onItemClickListener);
        return viewHolder;
    }
    private OnItemClickListener onItemClickListener;
    public void addOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClickListener(MessageEntity data);
        boolean onLongItemClickListener(View view, MessageEntity data);
    }
}
