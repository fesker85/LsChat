package cc.lzsou.lschat.main.adapter.holder;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.data.bean.ArticleEntity;
import cc.lzsou.lschat.manager.ImageLoaderManager;


public class DiscoverTwoHolder extends BaseViewHolder<ArticleEntity> {

    TextView titleView;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    TextView sourceView;
    TextView hitsView;
    LinearLayout itemLayout;

    public DiscoverTwoHolder(ViewGroup parentView) {
        super(parentView, R.layout.item_discover_styletwo);
        titleView = (TextView) itemView.findViewById(R.id.titleView);
        imageView1 = (ImageView) itemView.findViewById(R.id.imageView1);
        imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
        imageView3 = (ImageView) itemView.findViewById(R.id.imageView3);
        sourceView = (TextView) itemView.findViewById(R.id.sourceView);
        hitsView = (TextView) itemView.findViewById(R.id.hitsView);
        itemLayout = (LinearLayout) itemView.findViewById(R.id.itemLayout);
    }

    @Override
    public void setData(ArticleEntity data) {
        titleView.setText(data.getTitle());
        ImageLoaderManager.getInstance().displayFromNetwork(data.getImage1(), imageView1);
        ImageLoaderManager.getInstance().displayFromNetwork(data.getImage2(), imageView2);
        ImageLoaderManager.getInstance().displayFromNetwork(data.getImage3(), imageView3);
        sourceView.setText(data.getSource());
        hitsView.setText(data.getHits());
    }
}
