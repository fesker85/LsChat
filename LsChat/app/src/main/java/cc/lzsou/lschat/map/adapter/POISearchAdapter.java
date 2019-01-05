package cc.lzsou.lschat.map.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cc.lzsou.lschat.data.bean.POIEntity;
import cc.lzsou.lschat.map.adapter.holder.POISearchHolder;

public class POISearchAdapter extends RecyclerArrayAdapter<POIEntity> {
    public POISearchAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        POISearchHolder viewHolder=null;
        if(viewHolder==null)
            viewHolder=new POISearchHolder(parent);
        return viewHolder;
    }
}
