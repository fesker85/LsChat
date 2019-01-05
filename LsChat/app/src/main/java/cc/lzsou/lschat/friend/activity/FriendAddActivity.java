package cc.lzsou.lschat.friend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.AppEntity;
import cc.lzsou.lschat.data.bean.MemberEntity;
import cc.lzsou.lschat.core.handler.HttpPostHandler;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.servercore.LinkServer;

public class FriendAddActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView searchView;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView=(TextView)findViewById(R.id.textFlag);
        searchView=(SearchView)findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
    //  //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
    //  searchView.setIconified(false);
    ////设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
    //  searchView.setIconifiedByDefault(false);
    ////设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
    //   searchView.onActionViewExpanded();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if(s==null||s.equals(""))return true;
        Map<String,String> map = new HashMap<>();
        map.put("key",s);
        new HttpPostHandler(FriendAddActivity.this, LinkServer.MemberAction.URL_MEMBER_EXACT, map, true, "搜索中") {
            @Override
            protected void onResult(String result) {
                MemberEntity entity = JsonHelper.jsonToObject(result,MemberEntity.class);
                if(entity==null||entity.getId()<0){
                    textView.setVisibility(View.VISIBLE);
                    return;
                }
                Intent intent  = new Intent();
                intent.putExtra("fid",entity.getId());
                intent.setClass(FriendAddActivity.this,FriendActivity.class);
                startActivity(intent);
                FriendAddActivity.this.finish();
            }
        };
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}
