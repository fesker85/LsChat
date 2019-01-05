package cc.lzsou.lschat.selector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.data.bean.ImageDirEntity;
import cc.lzsou.lschat.manager.ImageLoaderManager;
import cc.lzsou.lschat.selector.listener.OnImageDirItemListener;

public class ImageDirAdapter extends RecyclerView.Adapter<ImageDirAdapter.ImageDirViewHolder> {
    private Context mContext;
    private List<ImageDirEntity> mList;
    private OnImageDirItemListener mListener;

    public ImageDirAdapter(Context context, List<ImageDirEntity> list, OnImageDirItemListener listener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = listener;
    }

    @Override
    public ImageDirViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageDirViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.item_image_dir, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(ImageDirViewHolder holder, int position) {
        ImageLoaderManager.getInstance().displayFromSdcard(mList.get(position).getImagePath(),holder.imageDir);
        holder.tvImageDirName.setText(mList.get(position).getImageName());
        holder.tvImageCount.setText(mList.get(position).getImageCount() + "张");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ImageDirViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageDir;
        TextView tvImageDirName;
        ImageView imageSelect;
        TextView tvImageCount;

        OnImageDirItemListener mListener;

        public ImageDirViewHolder(View itemView, OnImageDirItemListener listener) {
            super(itemView);
            this.mListener = listener;
            imageDir = (ImageView) itemView.findViewById(R.id.imageView);
            tvImageDirName = (TextView) itemView.findViewById(R.id.dirView);
            tvImageCount = (TextView) itemView.findViewById(R.id.countView);
            imageSelect = (ImageView) itemView.findViewById(R.id.selectView);
            if (mListener != null) {
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onImageDirItemListener(v, getAdapterPosition());
            }
        }
    }
}
