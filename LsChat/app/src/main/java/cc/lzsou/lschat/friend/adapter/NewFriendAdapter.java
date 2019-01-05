package cc.lzsou.lschat.friend.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.data.bean.TempFriendEntity;
import cc.lzsou.lschat.friend.activity.FriendAttestActivity;
import cc.lzsou.lschat.manager.ImageLoaderManager;
import cc.lzsou.lschat.views.image.CircleImage;

public class NewFriendAdapter extends ArrayAdapter<TempFriendEntity> implements View.OnClickListener{
    private LayoutInflater inflater;
    private Context context;
    public NewFriendAdapter(Context context){
        super(context,0);
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view==null)view=inflater.inflate(R.layout.list_newfirend_item,null);
        CircleImage avatarImage = (CircleImage)view.findViewById(R.id.newfriend_avatar);
        TextView nickView = (TextView)view.findViewById(R.id.newfirend_nick);
        TextView msgView = (TextView)view.findViewById(R.id.newfriend_msg);
        TextView stateView = (TextView)view.findViewById(R.id.newfriend_state);
        Button btn = (Button)view.findViewById(R.id.newfirend_btn);
        btn.setOnClickListener(this);
        stateView.setVisibility(View.GONE);
        btn.setVisibility(View.GONE);
        TempFriendEntity entity = getItem(position);
        if(entity!=null){
            btn.setTag(entity.getId());
            ImageLoaderManager.getInstance().displayAvatar(entity.getAvatar(),avatarImage);
            nickView.setText(entity.getNickname());
            msgView.setText(entity.getMsg());
            if(entity.getStatus()==TempFriendEntity.STATUS_NEW||entity.getStatus()==TempFriendEntity.STATUS_READED){
                stateView.setVisibility(View.GONE);
                btn.setVisibility(View.VISIBLE);
            }
            else if(entity.getStatus()==TempFriendEntity.STATUS_ADDED) {
                stateView.setVisibility(View.VISIBLE);
                stateView.setText("已添加");
                btn.setVisibility(View.GONE);
            }
            else  if(entity.getStatus()==TempFriendEntity.STATUS_L_REFUSE){
                stateView.setVisibility(View.VISIBLE);
                stateView.setText("已拒绝");
                btn.setVisibility(View.GONE);
            }
            else if(entity.getStatus()==TempFriendEntity.STATUS_F_REFUSE){
                stateView.setVisibility(View.VISIBLE);
                stateView.setText("对方已拒绝");
                btn.setVisibility(View.GONE);
            }
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getTag()==null)return;
        int friendId = Integer.parseInt(view.getTag().toString());
        Intent intent = new Intent();
        intent.setClass(context, FriendAttestActivity.class);
        intent.putExtra("fid",friendId);
        context.startActivity(intent);
    }
}
