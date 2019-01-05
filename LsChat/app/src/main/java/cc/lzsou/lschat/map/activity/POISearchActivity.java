package cc.lzsou.lschat.map.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.SuggestionParam;
import com.tencent.lbssearch.object.result.SuggestionResultObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.core.helper.Common;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.data.bean.POIEntity;
import cc.lzsou.lschat.map.adapter.POILocationAdapter;
import cc.lzsou.lschat.map.adapter.POISearchAdapter;

public class POISearchActivity extends AppCompatActivity implements RecyclerArrayAdapter.OnItemClickListener {

    @BindView(R.id.backButton)
    ImageButton backButton;
    @BindView(R.id.searchText)
    EditText searchText;
    @BindView(R.id.listView)
    EasyRecyclerView listView;

    private LinearLayoutManager layoutManager;
    private POISearchAdapter adapter;

    private TencentSearch tencentSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poisearch);
        ButterKnife.bind(this);
        tencentSearch = new TencentSearch(this);
        adapter = new POISearchAdapter(this);
        adapter.setError(R.layout.view_error);
        adapter.setOnItemClickListener(this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerDecoration itemDecoration = new DividerDecoration(Color.parseColor("#EEEEEE"), Common.dp2px(this, 1f), 0, 0);
        listView.addItemDecoration(itemDecoration);
        listView.setLayoutManager(layoutManager);
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapterWithProgress(adapter);
    }
    @OnTextChanged(value = R.id.searchText,callback = OnTextChanged.Callback.TEXT_CHANGED)
    void onSearchTextChanged(CharSequence s, int start, int before, int count) {
        String keyword = String.valueOf(s);
       if(keyword==null||keyword.length()<1||keyword.equals(""))return;
        doSearch(keyword);
    }

    private void doSearch(String keyword){
        SuggestionParam param = new SuggestionParam();
        param.keyword(keyword);
        listView.setRefreshing(true);
        tencentSearch.suggestion(param, new HttpResponseListener() {
            @Override
            public void onSuccess(int i, BaseObject baseObject) {
                SuggestionResultObject so = (SuggestionResultObject)baseObject;
                adapter.clear();
                for(SuggestionResultObject.SuggestionData data:so.data){
                    POIEntity entity = new POIEntity();
                    entity.setSelect(0);
                    entity.setUid(data.id);
                    entity.setName(data.title);
                    entity.setLongitude(data.location.lng);
                    entity.setLatitude(data.location.lat);
                    entity.setDistance(data.type);
                    entity.setDirection(data.adcode);
                    entity.setCatalog(data.province);
                    entity.setAddress(data.address);
                    adapter.add(entity);
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onFailure(int i, String s, Throwable throwable) {

            }
        });
    }

    @OnClick(R.id.backButton)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onItemClick(int position) {
        if(adapter.getAllData().size()>position){
            Intent intent = new Intent();
            intent.putExtra("data", JsonHelper.objectToJson(adapter.getAllData().get(position)));
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}
