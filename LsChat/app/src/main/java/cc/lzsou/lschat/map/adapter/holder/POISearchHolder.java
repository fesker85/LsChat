package cc.lzsou.lschat.map.adapter.holder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.data.bean.POIEntity;

public class POISearchHolder extends BaseViewHolder<POIEntity> {
    private TextView titleView;
    private TextView subjectView;

    public POISearchHolder(ViewGroup parent){
        super(parent, R.layout.item_search_poi);
        titleView=(TextView)itemView.findViewById(R.id.titleView);
        subjectView=(TextView)itemView.findViewById(R.id.subjectView);
    }

    @Override
    public void setData(POIEntity data) {
        titleView.setText(data.getName());
        subjectView.setText(data.getAddress());
    }
}
