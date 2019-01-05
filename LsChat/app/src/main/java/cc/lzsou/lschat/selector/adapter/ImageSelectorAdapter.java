package cc.lzsou.lschat.selector.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.data.bean.ImageEntity;
import cc.lzsou.lschat.manager.ImageLoaderManager;
import cc.lzsou.lschat.selector.listener.OnImageChangeListener;
import cc.lzsou.lschat.selector.listener.OnImageItemClickListener;

public class ImageSelectorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //相机布局
    private static final int CAMERA_TYPE = 0;
    //普通布局
    private static final int LAYOUT_TYPE = 1;


    private List<ImageEntity> mList;
    private Context mContext;
    private float mScreenWidth;
    //可选择的图片数，动态控制选择图片
    private int mMaxImageCount;
    private OnImageItemClickListener  mOnItemClickListener;
    private OnImageChangeListener  mOnChangeListener;
    private List<ImageEntity> mSelectImages;


    public ImageSelectorAdapter(Context context, int maxImageCount, List<ImageEntity> list, OnImageChangeListener onChangeListener,
                                OnImageItemClickListener onItemClickListener) {
        //获取屏幕宽度
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;


        this.mContext = context;
        this.mMaxImageCount = maxImageCount;
        this.mOnItemClickListener = onItemClickListener;
        this.mOnChangeListener = onChangeListener;
        this.mList = list;
        Log.e("list",mList.toString()+"");
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return CAMERA_TYPE;
        }
        return LAYOUT_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == CAMERA_TYPE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_imageselector_camera, parent, false);
            return new CameraViewHolder(view, mOnItemClickListener);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_imageselector, parent, false);
            return new ImageSelectorViewHolder(view, mOnItemClickListener, mOnChangeListener);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (getItemViewType(position)==LAYOUT_TYPE) {
            ImageSelectorViewHolder holder = (ImageSelectorViewHolder) viewHolder;
            holder.cbSelect.setVisibility(View.VISIBLE);
            ImageLoaderManager.getInstance().displayFromSdcard(mList.get(position).getPath(),holder.imageView);
            if (mList.get(position).isSelect()) {
                holder.cbSelect.setChecked(true);
                holder.canSelect();
            } else {
                if (mSelectImages == null) {
                    holder.canSelect();
                } else if (mSelectImages.size() == mMaxImageCount) {
                    holder.cannotSelect();
                } else {
                    holder.canSelect();
                }
                holder.cbSelect.setChecked(false);
            }

            if (mMaxImageCount == 1) {
                holder.cbSelect.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //当选择的图片大于可选择图片数的时候，就不能继续选择
    public void notifyData(List<ImageEntity> list) {
        mSelectImages = list;
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    class ImageSelectorViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        private ImageView imageView;
        private CheckBox cbSelect;
        private OnImageItemClickListener mOnItemClickListener;
        private OnImageChangeListener mOnChangeListener;

        public ImageSelectorViewHolder(View itemView, OnImageItemClickListener onItemClickListener,
                            OnImageChangeListener onChangeListener) {
            super(itemView);
            this.mOnItemClickListener = onItemClickListener;
            this.mOnChangeListener = onChangeListener;
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            cbSelect = (CheckBox) itemView.findViewById(R.id.ch_select);
            //适配imageView，正方形，宽和高都是屏幕宽度的1/3
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            params.width = (int) mScreenWidth / 3 - params.rightMargin - params.leftMargin;
            params.height = (int) mScreenWidth / 3 - params.topMargin - params.bottomMargin;
            imageView.setLayoutParams(params);
            if (onItemClickListener != null) {
                itemView.setOnClickListener(this);
            }
            if (onChangeListener != null) {
                cbSelect.setOnCheckedChangeListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClickListener(v, getAdapterPosition());
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mOnChangeListener != null) {
                mOnChangeListener.OnChangeListener(getAdapterPosition(), isChecked);
            }
        }

        public void cannotSelect() {
            imageView.setAlpha(0.3f);
            cbSelect.setClickable(false);
        }

        public void canSelect() {
            imageView.setAlpha(1.0f);
            cbSelect.setClickable(true);
        }
    }

    class CameraViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private ImageView imageView;
        private OnImageItemClickListener mOnItemClickListener;

        public CameraViewHolder(View itemView, OnImageItemClickListener onItemClickListener) {
            super(itemView);
            this.mOnItemClickListener = onItemClickListener;
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
//            适配imageView，正方形，宽和高都是屏幕宽度的1/3
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.width = (int) mScreenWidth / 3 - params.rightMargin - params.leftMargin;
            params.height = (int) mScreenWidth / 3 - params.topMargin - params.bottomMargin;
            imageView.setLayoutParams(params);
            if (onItemClickListener != null) {
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClickListener(v, getAdapterPosition());
            }
        }
    }
}