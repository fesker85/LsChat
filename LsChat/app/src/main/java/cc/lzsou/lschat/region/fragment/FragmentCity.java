package cc.lzsou.lschat.region.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.base.database.DBRegionHelper;
import cc.lzsou.lschat.data.bean.RegionEntity;
import cc.lzsou.lschat.region.activity.RegionActivity;
import cc.lzsou.lschat.region.adapter.RegionAdapter;

public class FragmentCity extends Fragment {
    @BindView(R.id.listView)
    ListView listView;
    Unbinder unbinder;

    private RegionAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_region_city, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    List<RegionEntity> list;
    private void initData() {
        list= DBRegionHelper.getInstance().select(RegionActivity.getInstace().getInfo()[0]);
        adapter = new RegionAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @OnItemClick(R.id.listView)
    void onItemClick(int position){
        RegionEntity entity = list.get(position);
        RegionActivity.getInstace().setInfo(entity.getCode(),RegionActivity.getInstace().getInfo()[1]+","+entity.getName());
        RegionActivity.getInstace().showFragment(2);
    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initData();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.backView, R.id.complateButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backView:
                RegionActivity.getInstace().setInfo("","");
                RegionActivity.getInstace().showFragment(0);
                break;
            case R.id.complateButton:
                RegionActivity.getInstace().doComplated();
                break;
        }
    }
}
