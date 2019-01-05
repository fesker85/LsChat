package cc.lzsou.lschat.friend.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.flag.ActionFlag;
import cc.lzsou.lschat.data.impl.TempFriendEntityImpl;
import cc.lzsou.lschat.friend.adapter.NewFriendAdapter;

public class NewFriendActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener {

    private ListView listView;
    private NewFriendAdapter adapter;
    private NewFriendBroadcateReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        receiver = new NewFriendBroadcateReceiver();
        getApplication().registerReceiver(receiver,new IntentFilter(ActionFlag.ACTION_FLAG_FRIEND_CHANGE));
        listView=(ListView)findViewById(R.id.mListView);
        adapter=new NewFriendAdapter(NewFriendActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        initData();

        TempFriendEntityImpl.getInstance().setRead();
        sendBroadcast(new Intent(ActionFlag.ACTION_FLAG_FRIEND_CHANGE));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_newfriend,menu);
        return true;
    }

    private void initData(){
        adapter.clear();
        adapter.addAll(TempFriendEntityImpl.getInstance().select());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.setClass(NewFriendActivity.this,FriendActivity.class);
        intent.putExtra("fid",adapter.getItem(i).getId());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//        PopupMenu popupMenu = new PopupMenu(NewFriendActivity.this,view);
//        popupMenu.getMenuInflater().inflate(R.menu.menu_verifyfriend,popupMenu.getMenu());
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                if(menuItem.getItemId()==R.id.nav_delete){
//                    Toast.makeText(NewFriendActivity.this,"删除新好友",Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            }
//        });
//        popupMenu.show();
        return false;
    }

    private class NewFriendBroadcateReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ActionFlag.ACTION_FLAG_FRIEND_CHANGE))
                initData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(receiver!=null)getApplication().unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            NewFriendActivity.this.finish();
            return true;
        }
        if(item.getItemId()==R.id.action_addfriend){
            Intent intent = new Intent();
            intent.setClass(NewFriendActivity.this,FriendAddActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
