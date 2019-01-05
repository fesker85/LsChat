package cc.lzsou.lschat.map.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import cc.lzsou.lschat.data.bean.POIEntity;
import cc.lzsou.lschat.map.adapter.holder.POILocatoinHolder;

public class POILocationAdapter extends RecyclerArrayAdapter<POIEntity> {
    public POILocationAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        POILocatoinHolder holder = null;
        if(holder==null)
            holder=new POILocatoinHolder(parent);
        return holder;
    }
}
