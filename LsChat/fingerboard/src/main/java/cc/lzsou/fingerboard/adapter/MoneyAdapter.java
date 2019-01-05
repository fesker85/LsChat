package cc.lzsou.fingerboard.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.lzsou.fingerboard.R;

public class MoneyAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private String[] arrString = new String[]{"1","2","3","4","5","6","7","8","9",".","0","C"};
    private int width;
    private int height;
    private LinearLayout.LayoutParams params;
    public MoneyAdapter(Context context){
        inflater=LayoutInflater.from(context);
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        width = dm.widthPixels/3;
        height=width/2;
    }
    @Override
    public int getCount() {
        return arrString.length;
    }

    @Override
    public Object getItem(int position) {
        return arrString[position];
    }

    @Override
    public long getItemId(int position) {
        return arrString.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view==null){
            view=inflater.inflate(R.layout.item_paypwd,null);
            params = (LinearLayout.LayoutParams)view.getLayoutParams();
            if(params==null){
                params = new LinearLayout.LayoutParams(width,height);
                view.setLayoutParams(params);
            }
            else {
                params.height=height;
                params.width=width;
            }
        }

        TextView textView=(TextView)view.findViewById(R.id.textView);
        ImageView imageView =(ImageView)view.findViewById(R.id.imageView);
        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        final String str = arrString[position];
        if(str.toUpperCase().equals("C")){
            textView.setText("完成");
        }
        else {
            textView.setText(str);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)listener.onItemClick(str);
            }
        });
        return view;
    }

    private PayPwdAdapter.OnItemClickListener listener;
    public void addOnItemClickListener(PayPwdAdapter.OnItemClickListener listener){
        this.listener=listener;
    }
    public interface OnItemClickListener{
        void onItemClick(String text);
    }
}
