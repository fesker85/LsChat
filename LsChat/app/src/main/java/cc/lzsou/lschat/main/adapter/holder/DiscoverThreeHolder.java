package cc.lzsou.lschat.main.adapter.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.data.bean.ArticleEntity;
import cc.lzsou.lschat.manager.ImageLoaderManager;

public class DiscoverThreeHolder extends BaseViewHolder<ArticleEntity> {

    TextView titleView;
    ImageView imageView;
    ImageView playImage;
    TextView sourceView;
    TextView hitsView;
    LinearLayout itemLayout;

    public DiscoverThreeHolder(ViewGroup parentView) {
        super(parentView, R.layout.item_discover_stylethree);
        titleView=(TextView)itemView.findViewById(R.id.titleView);
        imageView=(ImageView)itemView.findViewById(R.id.imageView);
        playImage=(ImageView)itemView.findViewById(R.id.playImage);
        sourceView=(TextView)itemView.findViewById(R.id.sourceView);
        hitsView=(TextView)itemView.findViewById(R.id.hitsView);
        itemLayout=(LinearLayout)itemView.findViewById(R.id.itemLayout);
    }

    @Override
    public void setData(ArticleEntity data) {
        titleView.setText(data.getTitle());
        ImageLoaderManager.getInstance().displayFromNetwork(data.getImage1(),imageView);
        if(data.getMode()==ArticleEntity.MODE_ADIMAGE)  playImage.setVisibility(View.GONE);
        else if(data.getMode()==ArticleEntity.MODE_ADVIDEO) playImage.setVisibility(View.VISIBLE);
        else playImage.setVisibility(View.VISIBLE);
        sourceView.setText(data.getSource());
        hitsView.setText(data.getHits());
    }
}
