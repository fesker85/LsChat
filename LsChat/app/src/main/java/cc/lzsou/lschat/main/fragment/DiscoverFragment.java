package cc.lzsou.lschat.main.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.AppEntity;
import cc.lzsou.lschat.data.impl.ArticleClassEntityImpl;
import cc.lzsou.lschat.data.bean.ArticleClassEntity;
import cc.lzsou.lschat.data.bean.ArticleEntity;
import cc.lzsou.lschat.core.handler.HttpPostHandler;
import cc.lzsou.lschat.core.handler.MessageHandler;
import cc.lzsou.lschat.core.helper.Common;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.main.adapter.DiscoverAdapter;
import cc.lzsou.lschat.views.widget.BaseFragment;
import cc.lzsou.servercore.LinkServer;

public class DiscoverFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnLoadMoreListener,View.OnClickListener,RecyclerArrayAdapter.OnErrorListener,RecyclerArrayAdapter.OnItemClickListener {

    private static final int classNormal = Color.parseColor("#333333");
    private static final int classPressed = Color.parseColor("#FF7F00");
    private static final int TEXTVIEW_SELECT_YES = 1;
    private static final int TEXTVIEW_SELECT_NO = 0;
    private long pageindex = 0;
    @BindView(R.id.classLayout)
    LinearLayout classLayout;
    @BindView(R.id.listView)
    EasyRecyclerView listView;
    @BindView(R.id.searchText)
    EditText searchText;
    Unbinder unbinder;

    private List<TextView> listClass = new ArrayList<>();
    private DiscoverAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }


    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    @Override
    protected void onFragmentFirstVisible() {
        initClass();
        onRefresh();
    }

    public void init() {
        adapter = new DiscoverAdapter(getActivity());
        adapter.setNoMore(R.layout.view_nomore);
        adapter.setMore(R.layout.view_more, this);
        adapter.setError(R.layout.view_error, this);
        adapter.setOnItemClickListener(this);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        DividerDecoration itemDecoration = new DividerDecoration(Color.parseColor("#EEEEEE"), Common.dp2px(getActivity(),1f), 0,0);
        itemDecoration.setDrawLastItem(false);
        listView.addItemDecoration(itemDecoration);
        listView.setLayoutManager(layoutManager);
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapterWithProgress(adapter);
        listView.setRefreshListener(this);
    }

    private void initClass() {
        listClass.clear();
        List<ArticleClassEntity> list = ArticleClassEntityImpl.getInstance().selectAll();
        for (int i = 0; i < list.size(); i++) {
            ArticleClassEntity entity = list.get(i);
            TextView tv = new TextView(getActivity());
            tv.setTextSize(17);
            tv.setText(entity.getName());
            tv.setPadding(32, 20, 32, 20);
            tv.setBackgroundResource(R.drawable.btn_style_transparent);
            tv.setClickable(true);
            tv.setTag(R.id.view_tag_id, entity.getId());
            tv.setGravity(Gravity.CENTER);
            if (i == 0) {
                tv.setTextColor(classPressed);
                tv.setTag(R.id.view_tag_selected, TEXTVIEW_SELECT_YES);
            } else {
                tv.setTextColor(classNormal);
                tv.setTag(R.id.view_tag_selected, TEXTVIEW_SELECT_NO);
            }
            tv.setOnClickListener(this);
            classLayout.addView(tv, i);
            listClass.add(tv);
        }
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onRefresh() {
        String classid = "";
        String keyword = "";
        if (listClass.size() < 1) {
            classid = ArticleClassEntity.ID_RECOMMAND;
            keyword = "";
        } else {
            for (TextView tv : listClass) {
                if (Integer.parseInt(tv.getTag(R.id.view_tag_selected).toString()) == TEXTVIEW_SELECT_YES) {
                    classid = tv.getTag(R.id.view_tag_id).toString();
                    keyword = searchText.getText().toString();
                    break;
                }
            }
            if (classid.equals("")) classid = ArticleClassEntity.ID_RECOMMAND;
        }
        Map<String, String> map = new HashMap<>();
        map.put("classid", classid);
        map.put("lastid", "0");
        map.put("pagesize", "10");
        map.put("userid", String.valueOf(AppEntity.getMemberid()));
        map.put("keyword", keyword);
        new HttpPostHandler(getActivity(), LinkServer.ArticleAction.URL_ARTICLE_LIST, map) {
            @Override
            protected void onResult(String r) {
                List<ArticleEntity> temp = JsonHelper.jsonToObjectList(r, ArticleEntity.class);
                adapter.clear();
                adapter.addAll(temp);
                if (temp != null && temp.size() > 0) pageindex = temp.get(temp.size() - 1).getId();
            }
        };
    }

    @Override
    public void onLoadMore() {
        String classid = "";
        String keyword = "";
        if (listClass.size() < 1) {
            classid = ArticleClassEntity.ID_RECOMMAND;
            keyword = "";
        } else {
            for (TextView tv : listClass) {
                if (Integer.parseInt(tv.getTag(R.id.view_tag_selected).toString()) == TEXTVIEW_SELECT_YES) {
                    classid = tv.getTag(R.id.view_tag_id).toString();
                    keyword = searchText.getText().toString();
                    break;
                }
            }
            if (classid.equals("")) classid = ArticleClassEntity.ID_RECOMMAND;
        }
        long lastid = 0;
        if (adapter.getAllData() != null && adapter.getAllData().size() > 0)
            lastid = adapter.getAllData().get(adapter.getAllData().size() - 1).getId();

        if (lastid == pageindex) {
            adapter.stopMore();
            return;
        }
        pageindex = lastid;

        Map<String, String> map = new HashMap<>();
        map.put("classid", classid);
        map.put("lastid", pageindex + "");
        map.put("pagesize", "10");
        map.put("userid", String.valueOf(AppEntity.getMemberid()));
        map.put("keyword", keyword);
        new HttpPostHandler(getActivity(), LinkServer.ArticleAction.URL_ARTICLE_LIST, map) {
            @Override
            protected void onResult(String r) {
                searchText.setText("");
                List<ArticleEntity> temp = JsonHelper.jsonToObjectList(r, ArticleEntity.class);
                if (temp != null) adapter.addAll(temp);
            }
        };
    }

    @Override
    public void onClick(View v) {
        for (TextView tv : listClass) {
            if (v.equals(tv)) continue;
            tv.setTextColor(classNormal);
            tv.setTag(R.id.view_tag_selected, TEXTVIEW_SELECT_NO);
            tv.invalidate();
        }
        TextView textView = (TextView) v;
        textView.setTextColor(classPressed);
        textView.invalidate();
        if (Integer.parseInt(textView.getTag(R.id.view_tag_selected).toString()) != TEXTVIEW_SELECT_YES) {
            textView.setTag(R.id.view_tag_selected, TEXTVIEW_SELECT_YES);
            listView.setRefreshing(true,true);
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
    public void onItemClick(int position) {
        MessageHandler.showToask("点击位置："+position);
    }
}
