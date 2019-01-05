package cc.lzsou.lschat.map.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import butterknife.BindView;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.data.bean.POIEntity;

public class POILocatoinHolder extends BaseViewHolder<POIEntity> {

    private TextView titleView;
    private TextView subjectView;
    private ImageView locationView;

    public POILocatoinHolder(ViewGroup parent) {
        super(parent, R.layout.item_location_poi);
        titleView=(TextView)itemView.findViewById(R.id.titleView);
        subjectView=(TextView)itemView.findViewById(R.id.subjectView);
        locationView=(ImageView)itemView.findViewById(R.id.locationView);
    }

    @Override
    public void setData(POIEntity data) {
        titleView.setText(data.getName());
        subjectView.setText(data.getAddress());
        if(data.getSelect()==1)locationView.setVisibility(View.VISIBLE);
        else locationView.setVisibility(View.GONE);
    }
}
