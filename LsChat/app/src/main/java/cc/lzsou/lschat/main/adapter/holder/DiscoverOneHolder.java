package cc.lzsou.lschat.main.adapter.holder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.data.bean.ArticleEntity;
import cc.lzsou.lschat.manager.ImageLoaderManager;

public class DiscoverOneHolder extends BaseViewHolder<ArticleEntity> {
    ImageView imageView;
    TextView titleView;
    TextView sourceView;
    TextView hitsView;
    RelativeLayout itemLayout;

    public DiscoverOneHolder(ViewGroup parentView) {
        super(parentView, R.layout.item_discover_styleone);
        imageView=(ImageView)itemView.findViewById(R.id.imageView);
        titleView=(TextView)itemView.findViewById(R.id.titleView);
        sourceView=(TextView)itemView.findViewById(R.id.sourceView);
        hitsView=(TextView)itemView.findViewById(R.id.hitsView);
        itemLayout=(RelativeLayout)itemView.findViewById(R.id.itemLayout);
    }

    @Override
    public void setData(ArticleEntity data) {
        ImageLoaderManager.getInstance().displayFromNetwork(data.getImage1(),imageView);
        titleView.setText(data.getTitle());
        sourceView.setText(data.getSource());
        hitsView.setText(data.getHits());
    }


}
