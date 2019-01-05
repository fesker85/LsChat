package cc.lzsou.lschat.region.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.data.bean.RegionEntity;

public class RegionAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<RegionEntity> list;
    public RegionAdapter(Context context, List<RegionEntity> list){
        this.inflater=LayoutInflater.from(context);
        this.list=list;
    }
    @Override
    public int getCount() {
        if(list!=null)return list.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(list!=null)return list.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        if(list!=null)return list.size();
        return 0;
    }

    @Override
    public View getView(int i, View currentView, ViewGroup viewGroup) {
        View view = currentView;
        if(view==null)
            view=inflater.inflate(R.layout.region_list_item,null);
        TextView textView = (TextView)view.findViewById(R.id.textView);
        RegionEntity entity = list.get(i);
        if(entity!=null)textView.setText(entity.getName());
        return view;
    }
}
