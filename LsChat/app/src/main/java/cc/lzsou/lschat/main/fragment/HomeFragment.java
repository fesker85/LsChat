package cc.lzsou.lschat.main.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.List;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.activity.scan.activity.CaptureActivity;
import cc.lzsou.lschat.base.AppEntity;
import cc.lzsou.lschat.base.flag.ActionFlag;
import cc.lzsou.lschat.data.bean.MessageEntity;
import cc.lzsou.lschat.chat.activity.ChatActivity;
import cc.lzsou.lschat.core.helper.Common;
import cc.lzsou.lschat.data.impl.MessageEntityImpl;
import cc.lzsou.lschat.data.impl.TempMessageEntityImpl;
import cc.lzsou.lschat.friend.activity.FriendAddActivity;
import cc.lzsou.lschat.main.adapter.HomeAdapter;
import cc.lzsou.lschat.manager.PopupWindowManager;
import cc.lzsou.lschat.views.widget.BaseFragment;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseFragment implements View.OnClickListener,RecyclerArrayAdapter.OnErrorListener,HomeAdapter.OnItemClickListener {

    private MessageReciver messageReciver;

    private EasyRecyclerView listView;
    private HomeAdapter adapter;
    private LinearLayoutManager layoutManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view){
        view.findViewById(R.id.plusButton).setOnClickListener(this);
        view.findViewById(R.id.scanButton).setOnClickListener(this);
        listView = (EasyRecyclerView)view.findViewById(R.id.listView);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        messageReciver = new MessageReciver();
        getActivity().registerReceiver(messageReciver, new IntentFilter(ActionFlag.ACTION_MESSAGE));
        adapter=new HomeAdapter(getActivity());
        adapter.setError(R.layout.view_error,this);
        adapter.addOnItemClickListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerDecoration itemDecoration = new DividerDecoration(Color.parseColor("#EEEEEE"), Common.dp2px(getActivity(),1f), 0,0);
        listView.addItemDecoration(itemDecoration);
        listView.setLayoutManager(layoutManager);
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapterWithProgress(adapter);
        initData();
    }

    @Override
    protected void onFragmentFirstVisible() {

    }

    private void initData(){
        adapter.clear();
        List<MessageEntity> list = MessageEntityImpl.getInstance().selectLast();
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        if (messageReciver != null) getActivity().unregisterReceiver(messageReciver);
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.plusButton:
                startActivity(new Intent(getActivity(),FriendAddActivity.class));
                break;
            case R.id.scanButton:
                getRuntimeRight();
                break;
        }

    }

    @Override
    public void onErrorShow() {
        adapter.resumeMore();
    }

    @Override
    public void onErrorClick() {
        adapter.resumeMore();
    }

    @Override
    public void onItemClickListener(MessageEntity data) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ChatActivity.class);
        intent.putExtra("fid",data.getUid());
        intent.putExtra("mid",data.getMid());
        startActivity(intent);
    }
    private PopupWindowManager manager;
    @Override
    public boolean onLongItemClickListener(View view, MessageEntity data) {
        if(data==null||view==null)return false;
        manager = new PopupWindowManager(getActivity(),R.layout.menu_message_delete,Common.dp2px(getActivity(),80), Common.dp2px(getActivity(),42)) {
            @Override
            protected void initView(View contentView) {
                contentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MessageEntityImpl.getInstance().delete(data.getMid());
                        TempMessageEntityImpl.getInstance().delete(data.getMid());
                        getActivity().sendBroadcast(new Intent(ActionFlag.ACTION_MESSAGE));
                        manager.dismiss();
                    }
                });
            }

            @Override
            protected void initEvent() {

            }
        };

        manager.showBashOfAnchor(view,new PopupWindowManager.LayoutGravity(PopupWindowManager.LayoutGravity.CENTER_HORI| PopupWindowManager.LayoutGravity.TO_BOTTOM),0,0);

        return false;
    }

    private class MessageReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ActionFlag.ACTION_MESSAGE)){
                //刷新数据
                initData();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    jumpScanPage();
                } else {
                    Toast.makeText(getActivity(), "拒绝", Toast.LENGTH_LONG).show();
                }
            default:
                break;
        }
    }
    /**
     * 获得运行时权限
     */
    private void getRuntimeRight() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            jumpScanPage();
        }
    }
    /**
     * 跳转到扫码页
     */
    private void jumpScanPage() {
        startActivityForResult(new Intent(getActivity(), CaptureActivity.class),REQUEST_SCAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SCAN&&resultCode==RESULT_OK){
            Toast.makeText(getActivity(),data.getStringExtra("barCode"),Toast.LENGTH_LONG).show();
        }
    }

    private static final int REQUEST_SCAN = 0;
}
