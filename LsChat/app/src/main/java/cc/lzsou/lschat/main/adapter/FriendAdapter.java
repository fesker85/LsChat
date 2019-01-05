package cc.lzsou.lschat.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.data.bean.FriendEntity;
import cc.lzsou.lschat.core.helper.LsPinyinHelper;
import cc.lzsou.lschat.manager.ImageLoaderManager;
import cc.lzsou.lschat.core.helper.ThreadPoolHelper;

public class FriendAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<FriendEntity> list;
    private HolderView holderView = null;
    public HashMap<String, Integer> positionMap;
    public FriendAdapter(Context context,List<FriendEntity> list) {
        this.context = context;
        this.list=list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(list!=null)return list.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(list!=null)return  list.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        if(list!=null)return list.size();
        return 0;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            holderView = new HolderView();
            convertView = inflater.inflate(R.layout.list_friend_item, null);
            holderView.tv = (TextView) convertView.findViewById(R.id.tv_user_name);
            holderView.img = (ImageView) convertView.findViewById(R.id.img_user_head);
            holderView.tv_letter = (TextView) convertView.findViewById(R.id.tv_letter);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        String letter = "";
        // 获取首字母
        String pinyin=LsPinyinHelper.converterToFirstSpell(list.get(position).getNickname());
        if(pinyin.length()>0)
        letter = pinyin.substring(0, 1);
        if (position == 0) {// 第一行必须是字母提示栏
            holderView.tv_letter.setVisibility(View.VISIBLE);
            holderView.tv_letter.setText(letter);
            positionMap.put(letter, position);
        } else {// 从第二行开始就要判断
            String lastLetter = "";
            // 获取首字母
            lastLetter = LsPinyinHelper.converterToFirstSpell(list.get(position - 1).getNickname())
                    .substring(0, 1);
            if (letter.equals(lastLetter)) {
                // 首字母相同
                holderView.tv_letter.setVisibility(View.GONE);
            } else {
                // 首字母不相同
                holderView.tv_letter.setVisibility(View.VISIBLE);
                holderView.tv_letter.setText(letter);
                positionMap.put(letter, position);
            }
        }
        ImageLoaderManager.getInstance().displayAvatar(list.get(position).getAvatar(),holderView.img);
        holderView.tv.setText(list.get(position).getNickname());
        return convertView;
    }

    private class HolderView {
        private TextView tv;
        private ImageView img;
        private TextView tv_letter;
    }
    // 刷新ListView
    public void mNotifyDataChange() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                positionMap = new HashMap<String, Integer>();
                for (int position = 0; position < list.size(); position++) {

                    String letter = "";
                    String pinyin =LsPinyinHelper.converterToFirstSpell(list.get(position).getNickname());
                    // 获取首字母
                   if(pinyin.length()>0)
                       letter=pinyin.substring(0,1);
                    if (position == 0) {// 第一行必须是字母提示栏
                        positionMap.put(letter, position);
                    } else {// 从第二行开始就要判断
                        String lastLetter = "";
                        // 获取首字母
                        lastLetter = LsPinyinHelper.converterToFirstSpell(
                                list.get(position - 1).getNickname()).substring(0, 1);
                        if (!letter.equals(lastLetter)) {
                            // 首字母不相同
                            positionMap.put(letter, position);
                        }
                    }
                }
            }
        };
        ThreadPoolHelper.insertTaskToCatchPool(runnable);
        this.notifyDataSetChanged();
    }

    /**
     * 返回坐标记录器
     *
     * @return
     * @version V1.0
     * @return HashMap<String,Integer>
     */
    public HashMap<String, Integer> getPositionMap() {
        return positionMap;
    }
}
