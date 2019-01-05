package cc.lzsou.lschat.main.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cc.lzsou.lschat.data.bean.ArticleEntity;
import cc.lzsou.lschat.main.adapter.holder.DiscoverOneHolder;
import cc.lzsou.lschat.main.adapter.holder.DiscoverThreeHolder;
import cc.lzsou.lschat.main.adapter.holder.DiscoverTwoHolder;

public class DiscoverAdapter extends RecyclerArrayAdapter<ArticleEntity> {
    public DiscoverAdapter(Context context) {
        super(context);
    }
    @Override
    public int getViewType(int position) {
        return getAllData().get(position).getMode();
    }
    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder<ArticleEntity> viewHolder = null;
        switch (viewType) {
            case ArticleEntity.MODE_ADIMAGE:
                viewHolder = new DiscoverThreeHolder(parent);
                break;
            case ArticleEntity.MODE_ADVIDEO:
                viewHolder = new DiscoverThreeHolder(parent);
                break;
            case ArticleEntity.MODE_NORMAL:
                viewHolder = new DiscoverOneHolder(parent);
                break;
            case ArticleEntity.MODE_IMAGE:
                viewHolder = new DiscoverTwoHolder(parent);
                break;
            case ArticleEntity.MODE_VIDEO:
                viewHolder = new DiscoverThreeHolder(parent);
                break;
            default:
                viewHolder = null;
                break;
        }
        return viewHolder;
    }


}
