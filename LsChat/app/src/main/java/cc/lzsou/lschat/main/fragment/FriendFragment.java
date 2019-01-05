package cc.lzsou.lschat.main.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.flag.ActionFlag;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.data.bean.TempFriendEntity;
import cc.lzsou.lschat.data.impl.FriendEntityImpl;
import cc.lzsou.lschat.data.impl.TempFriendEntityImpl;
import cc.lzsou.lschat.friend.activity.FriendActivity;
import cc.lzsou.lschat.friend.activity.FriendAddActivity;
import cc.lzsou.lschat.group.GroupActivity;
import cc.lzsou.lschat.friend.activity.NewFriendActivity;
import cc.lzsou.lschat.main.adapter.FriendAdapter;
import cc.lzsou.lschat.base.AppEntity;
import cc.lzsou.lschat.data.bean.FriendEntity;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.core.helper.LsPinyinHelper;
import cc.lzsou.lschat.views.widget.LsLetterListView;

public class FriendFragment extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener,View.OnClickListener {


    private FriendBroadcastReceiver receiver;
    private FriendAdapter adapter;
    private List<FriendEntity> list;
    private ListView listView;
    private LsLetterListView letterListView;
    private TextView letterView;
    private HashMap<String,Integer> positionMap;
    private TextView coutView;
    private ImageButton plusButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend,container,false);
        listView=(ListView)view.findViewById(R.id.list_friendsList);
        View headerView = inflater.inflate(R.layout.list_friend_header,null);
        coutView = (TextView)headerView.findViewById(R.id.countView);
        ((RelativeLayout)headerView.findViewById(R.id.newfriend_layout)).setOnClickListener(this);
        ((RelativeLayout)headerView.findViewById(R.id.group_layout)).setOnClickListener(this);
        ((RelativeLayout)headerView.findViewById(R.id.school_layout)).setOnClickListener(this);
        listView.addHeaderView(headerView);
        letterListView=(LsLetterListView)view.findViewById(R.id.view_leter);
        letterView=(TextView)view.findViewById(R.id.tv_letter);
        plusButton=(ImageButton)view.findViewById(R.id.plusButton);
        plusButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        receiver=new FriendBroadcastReceiver();
        getActivity().registerReceiver(receiver,new IntentFilter(ActionFlag.ACTION_FLAG_FRIEND_CHANGE));

        list = new ArrayList<>();
        adapter = new FriendAdapter(getActivity(),list);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
        letterListView.setListener(onTouchLetterListListener);
        initData();
    }

    public void initData(){
        list.clear();
        list.addAll(FriendEntityImpl.getInstance().selectFriendList());
        sortList(list);
        adapter.mNotifyDataChange();
        positionMap = adapter.getPositionMap();
        int count = TempFriendEntityImpl.getInstance().getStatusCount(TempFriendEntity.STATUS_NEW);
        if(count>0){
            coutView.setVisibility(View.VISIBLE);
            coutView.setText(""+count);
        }
        else coutView.setVisibility(View.GONE);

    }
    private void sortList(List<FriendEntity> friendEntities) {
        PinyinComparator comp = new PinyinComparator();
        Collections.sort(friendEntities, comp);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if(view.getId()==R.id.newfriend_layout){
            intent.setClass(getActivity(), NewFriendActivity.class);
        }
        if(view.getId()==R.id.group_layout){
            intent.setClass(getActivity(), GroupActivity.class);
        }
        if(view.getId()==R.id.school_layout){
        }
        if(view.getId()==R.id.plusButton){
            intent.setClass(getActivity(),FriendAddActivity.class);
        }
        getActivity().startActivity(intent);
    }

    public class PinyinComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            String str1 = LsPinyinHelper.getPingYin(((FriendEntity) o1).getNickname());
            String str2 = LsPinyinHelper.getPingYin(((FriendEntity) o2).getNickname());
            return str1.compareToIgnoreCase(str2);
        }
    }
    @Override
    public void onResume() {//启动
        super.onResume();
    }
    @Override
    public void onPause() {//暂停
        super.onPause();
    }

    @Override
    public void onDestroy() {//销毁
        if(receiver!=null)getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    private LsLetterListView.onTouchLetterListListener onTouchLetterListListener = new LsLetterListView.onTouchLetterListListener(){
        @Override
        public void selectedDown(String letter) {
            letterView.setText(letter);
            letterView.setVisibility(View.VISIBLE);
            if (positionMap != null) {
                if (positionMap.containsKey(letter)) {
                    listView.setSelection(positionMap.get(letter));
                }
            }
        }
        @Override
        public void selectedUp() {
            letterView.setVisibility(View.GONE);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FriendEntity entity = list.get(i-1);
        if(entity!=null&&entity.getId()>0){
            Intent intent = new Intent();
            intent.setClass(getActivity(), FriendActivity.class);
            intent.putExtra("fid",entity.getId());
            startActivity(intent);
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return false;
    }

    private class FriendBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ActionFlag.ACTION_FLAG_FRIEND_CHANGE)){
                initData();
            }
        }
    }


}
