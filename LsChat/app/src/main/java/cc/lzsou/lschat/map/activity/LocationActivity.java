package cc.lzsou.lschat.map.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
//import com.tencent.lbssearch.TencentSearch;
//import com.tencent.lbssearch.httpresponse.BaseObject;
//import com.tencent.lbssearch.httpresponse.HttpResponseListener;
//import com.tencent.lbssearch.object.Location;
//import com.tencent.lbssearch.object.param.SearchParam;
//import com.tencent.lbssearch.object.result.SearchResultObject;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.BaseObject;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.Location;
import com.tencent.lbssearch.object.param.Geo2AddressParam;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.map.geolocation.TencentPoi;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.lzsou.lschat.R;
import cc.lzsou.lschat.core.handler.AsynHandler;
import cc.lzsou.lschat.core.handler.MessageHandler;
import cc.lzsou.lschat.core.helper.Common;
import cc.lzsou.lschat.core.helper.JsonHelper;
import cc.lzsou.lschat.data.bean.POIEntity;
import cc.lzsou.lschat.manager.FileManager;
import cc.lzsou.lschat.manager.ImageManager;
import cc.lzsou.lschat.map.adapter.POILocationAdapter;
import cc.lzsou.lschat.map.adapter.POISearchAdapter;

public class LocationActivity extends AppCompatActivity implements TencentLocationListener, RecyclerArrayAdapter.OnItemClickListener {

    public static final int REQUEST_LOCATION_SEARCH=100;

    @BindView(R.id.backButton)
    ImageButton backButton;
    @BindView(R.id.titleView)
    TextView titleView;
    @BindView(R.id.complateButton)
    Button complateButton;
    @BindView(R.id.listView)
    EasyRecyclerView listView;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.searchButton)
    ImageButton searchButton;
    @BindView(R.id.locationButton)
    ImageButton locationButton;

    private LinearLayoutManager layoutManager;
    private POILocationAdapter adapter;
    private boolean isItemClick=false;

    private TencentMap tencentMap;
    private Marker marker;
    private Marker selfMaker;
    private TencentSearch tencentSearch;
    private TencentLocationRequest locationRequest;
    private TencentLocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        adapter = new POILocationAdapter(this);
        adapter.setError(R.layout.view_error);
        adapter.setOnItemClickListener(this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerDecoration itemDecoration = new DividerDecoration(Color.parseColor("#EEEEEE"), Common.dp2px(this, 1f), 0, 0);
        listView.addItemDecoration(itemDecoration);
        listView.setLayoutManager(layoutManager);
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapterWithProgress(adapter);
        listView.setRefreshing(true);

        tencentSearch = new TencentSearch(this);
        tencentMap = mapView.getMap();
        tencentMap.setOnCameraChangeListener(new TencentMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (marker != null) {
                    marker.setPosition(cameraPosition.target);
                }
            }

            @Override
            public void onCameraChangeFinished(CameraPosition cameraPosition) {
                if(isItemClick){
                    isItemClick=false;
                    return;
                }
                searchByLocatoin(cameraPosition.target);
            }
        });
        locationRequest = TencentLocationRequest.create();
        locationRequest.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_POI);
        locationRequest.setInterval(2000);
        locationManager = TencentLocationManager.getInstance(this);
        locationManager.requestLocationUpdates(locationRequest, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @OnClick({R.id.backButton, R.id.complateButton, R.id.searchButton, R.id.locationButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                finish();
                break;
            case R.id.searchButton:
                startActivityForResult(new Intent(this, POISearchActivity.class),REQUEST_LOCATION_SEARCH);
                break;
            case R.id.locationButton:
                listView.setRefreshing(true);
                locationManager.requestLocationUpdates(locationRequest, this);
                break;
            case R.id.complateButton:
                if(adapter.getAllData().size()<1)break;
                if(selfMaker!=null)selfMaker.remove();
                tencentMap.snapshot(new TencentMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        Intent intent = new Intent();
                        new AsynHandler(LocationActivity.this, true, "发送中") {
                            @Override
                            protected Object onProcess() {
                                Bitmap map= ImageManager.createThumbnail(bitmap);
                                String id = Common.getId();
                                intent.putExtra("img", ImageManager.bitmap2Base64String(map));
                                intent.putExtra("id",id);
                                POIEntity entity = null;
                                for(POIEntity item:adapter.getAllData()){
                                    if(item.getSelect()==1){
                                        entity=item;
                                        break;
                                    }
                                }
                                if(entity==null)entity=adapter.getAllData().get(0);
                                intent.putExtra("data",JsonHelper.objectToJson(entity));
                                return "success";
                            }
                            @Override
                            protected void onResult(Object object) {
                                if(object==null){
                                    MessageHandler.showToask("发送失败");
                                    return;
                                }
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        };

                    }
                });
                break;
        }
    }


    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if (TencentLocation.ERROR_OK == i) {
            adapter.clear();
            for (TencentPoi p : tencentLocation.getPoiList()) {
                POIEntity entity = new POIEntity();
                entity.setAddress(p.getAddress());
                entity.setCatalog(p.getCatalog());
                entity.setDirection(p.getDirection());
                entity.setDistance(p.getDistance());
                entity.setLatitude(p.getLatitude());
                entity.setLongitude(p.getLongitude());
                entity.setName(p.getName());
                entity.setUid(p.getUid());
                entity.setSelect(0);
                adapter.add(entity);
            }
            if (adapter.getAllData().size() > 0) {
                adapter.getAllData().get(0).setSelect(1);
                addSelfMarker(adapter.getAllData().get(0));
                LatLng latLng = new LatLng(adapter.getAllData().get(0).getLatitude(), adapter.getAllData().get(0).getLongitude());
                addMarker(latLng);
                CameraUpdate cameraSigma = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, //新的中心点坐标
                        15,  //新的缩放级别
                        0, //俯仰角 0~45° (垂直地图时为0)
                        0)); //偏航角 0~360° (正北方为0)
                //移动地图
                tencentMap.moveCamera(cameraSigma);
            }
            locationManager.removeUpdates(this);
        } else {
            System.out.println("定位失败");
        }
    }


    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    @Override
    public void onItemClick(int position) {
        if (adapter.getAllData().size() > 0) {
            POIEntity entity = adapter.getAllData().get(position);
            if (entity == null) return;
            for (POIEntity item : adapter.getAllData()) {
                item.setSelect(0);
            }
            entity.setSelect(1);
            isItemClick=true;
            CameraUpdate cameraSigma = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(entity.getLatitude(),entity.getLongitude()), //新的中心点坐标
                    15,  //新的缩放级别
                    0, //俯仰角 0~45° (垂直地图时为0)
                    0)); //偏航角 0~360° (正北方为0)
            //移动地图
            tencentMap.moveCamera(cameraSigma);
            adapter.notifyDataSetChanged();
        }

    }

    private void addSelfMarker(POIEntity entity) {
        LatLng latLng = new LatLng(entity.getLatitude(), entity.getLongitude());
        if (selfMaker != null) selfMaker.remove();
        selfMaker = tencentMap.addMarker(new MarkerOptions().
                position(latLng).
                title(entity.getName()).zIndex(1).
                icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_circle_blue_32dp)).
                snippet(entity.getAddress()));
    }

    private void addMarker(LatLng latLng) {
        if (marker != null) marker.remove();
        marker = tencentMap.addMarker(new MarkerOptions().
                position(latLng).zIndex(9).
                icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_blue_48dp)));
    }

    private void searchByLocatoin(LatLng latLng) {
        addMarker(latLng);
        listView.setRefreshing(true);
        Geo2AddressParam param = new Geo2AddressParam().location(new Location().lat((float) latLng.latitude).lng((float) latLng.longitude));
        param.get_poi(true);
        tencentSearch.geo2address(param, new HttpResponseListener() {
            @Override
            public void onSuccess(int i, BaseObject baseObject) {
                Geo2AddressResultObject oj = (Geo2AddressResultObject) baseObject;
                adapter.clear();
                for (Geo2AddressResultObject.ReverseAddressResult.Poi p : oj.result.pois) {
                    POIEntity entity = new POIEntity();
                    entity.setAddress(p.address);
                    entity.setCatalog(p.category);
                    entity.setDirection(oj.message);
                    entity.setDistance(p._distance);
                    entity.setLatitude(p.location.lat);
                    entity.setLongitude(p.location.lng);
                    entity.setName(p.title);
                    entity.setUid(p.id);
                    entity.setSelect(0);
                    adapter.add(entity);
                }
                if (adapter.getAllData().size() > 0) {
                    adapter.getAllData().get(0).setSelect(1);
                }
            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK&&requestCode==REQUEST_LOCATION_SEARCH){
            POIEntity entity = JsonHelper.jsonToObject(data.getStringExtra("data"),POIEntity.class);
            CameraUpdate cameraSigma = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(entity.getLatitude(),entity.getLongitude()), //新的中心点坐标
                    15,  //新的缩放级别
                    0, //俯仰角 0~45° (垂直地图时为0)
                    0)); //偏航角 0~360° (正北方为0)
            //移动地图
            tencentMap.moveCamera(cameraSigma);
        }
    }
}
