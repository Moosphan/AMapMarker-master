package com.moos.marker.Sample;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.moos.marker.Cluster.Cluster;
import com.moos.marker.Cluster.ClusterClickListener;
import com.moos.marker.Cluster.ClusterItem;
import com.moos.marker.Cluster.ClusterItemImp;
import com.moos.marker.Cluster.ClusterOverlay;
import com.moos.marker.Cluster.ClusterRender;
import com.moos.marker.Cluster.MarkerSign;
import com.moos.marker.ClusterAnother.ClusterAnotherClickListener;
import com.moos.marker.ClusterAnother.ClusterAnotherRender;
import com.moos.marker.ClusterAnother.ClusterOverlayMerchant;
import com.moos.marker.R;
import com.moos.marker.Utils.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author moos
 * @date 2018/01/13
 * @function display ui
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MapView mapView = null;
    private Button bt_add_cluster_user, bt_add_cluster_merchant, bt_add_custom_marker;
    private AMap aMap;
    private ClusterOverlay clusterOverlayUser;
    private ClusterOverlayMerchant clusterOverlayMerchant;
    private List<ClusterItem> clusterItems = new ArrayList<>();
    private List<ClusterItem> clusterItemsMerchant = new ArrayList<>();
    private Map<Integer, Drawable> mBackDrawAbles = new HashMap<Integer, Drawable>();
    private Map<Integer, Drawable> mBackDrawAblesMerchant = new HashMap<Integer, Drawable>();
    private final LatLng centerLocation = new LatLng(31.206078,121.602948);
    private final String TYPE_MERCHANT = "02";
    private final String TYPE_USER = "01";
    private final String TAG = "Moos";
    private int clusterRadius = 48;

    BitmapDescriptor bitmapDescriptor ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        initView();
        initMap();
    }


    private void initView() {
        bt_add_cluster_user = (Button) findViewById(R.id.add_cluster_user);
        bt_add_cluster_merchant = (Button) findViewById(R.id.add_cluster_merchant);
        bt_add_custom_marker = (Button) findViewById(R.id.add_custom_marker);
        bt_add_cluster_user.setOnClickListener(this);
        bt_add_cluster_merchant.setOnClickListener(this);
        bt_add_custom_marker.setOnClickListener(this);
    }

    private void initMap() {
        aMap.setMinZoomLevel(8);
        aMap.setMaxZoomLevel(20);
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {


            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

                clusterOverlayUser.updateClusters();
                clusterOverlayMerchant.updateClusters();
            }
        });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Log.e(TAG,">>>>>>>>Marker点击事件响应了");
                if(marker.getObject().getClass().equals(Cluster.class)){
                    //是聚合点
                    Cluster cluster = (Cluster) marker.getObject();
                    String type = cluster.getClusterItems().get(0).getClusterType();
                    Log.e(TAG,">>>>>>>点击了聚合点类型为"+type);
                    if(type.equals(TYPE_USER)){
                        //user cluster

                        //Toast.makeText(MainActivity.this,"click user",Toast.LENGTH_SHORT).show();
                        clusterOverlayUser.responseClusterClickEvent(marker);
                        return true;
                    }else if(type.equals(TYPE_MERCHANT)){
                        //merchant cluster

                        //Toast.makeText(MainActivity.this,"click merchant",Toast.LENGTH_SHORT).show();
                        clusterOverlayMerchant.responseClusterClickEvent(marker);
                        return true;
                    }



                }else if(marker.getObject().getClass().equals(MarkerSign.class)){
                    //是自定义marker
                    Log.e(TAG,">>>>>>>点击了自定义marker");
                    MarkerSign sign = (MarkerSign) marker.getObject();
                    Toast.makeText(MainActivity.this,"这是自定义marker，代号"+sign.getMarkerId(),Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        moveMapToPosition(centerLocation);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * by moos on 2018/01/12
     * func:移动地图视角到某个精确位置
     * @param latLng 坐标
     */
    private void moveMapToPosition(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(
                new CameraPosition(
                        latLng,//新的中心点坐标
                        16,    //新的缩放级别
                        0,     //俯仰角0°~45°（垂直与地图时为0）
                        0      //偏航角 0~360° (正北方为0)
                ));
        aMap.animateCamera(cameraUpdate, 300, new AMap.CancelableCallback() {
            @Override
            public void onFinish() {


            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * by moos on 2018/01/12
     * func:添加位置模拟数据
     * @param centerPoint 中心点
     * @param num 数量
     * @param offset 经纬度模拟的可调偏移参数
     * @return
     */
    private List<LatLng> addSimulatedData(LatLng centerPoint, int num, double offset){
        List<LatLng> data = new ArrayList<>();
        if(num>0){
            for(int i=0;i<num;i++){
                double lat = centerPoint.latitude + (Math.random()-0.5)*offset;
                double lon = centerPoint.longitude + (Math.random()-0.5)*offset;
                LatLng latlng = new LatLng(lat,lon);
                data.add(latlng);
            }
        }
        return data;
    }

    /**
     * by moos on 2018/01/12
     * func:添加个人聚合点
     * @param locations
     */
    private void addUserClustersToMap(final List<LatLng> locations){

        for(int i=0;i<locations.size();i++){
            ClusterItemImp clusterImp = new ClusterItemImp(locations.get(i), TYPE_USER);
            clusterItems.add(clusterImp);
        }
        if(clusterOverlayUser == null){
            clusterOverlayUser = new ClusterOverlay(aMap,clusterItems,ViewUtil.dp2px(getApplicationContext(), clusterRadius),getApplicationContext());
        }else {
            clusterOverlayUser.onDestroy();
            clusterOverlayUser = null;
            clusterOverlayUser = new ClusterOverlay(aMap,clusterItems,ViewUtil.dp2px(getApplicationContext(), clusterRadius),getApplicationContext());
        }

        clusterOverlayUser.setClusterRenderer(new ClusterRender() {
            @Override
            public Drawable getDrawAble(int clusterNum) {
                if (clusterNum <= 5) {
                    Drawable bitmapDrawable = mBackDrawAbles.get(2);
                    if (bitmapDrawable == null) {
                        bitmapDrawable =
                                getApplication().getResources().getDrawable(
                                        R.mipmap.marker_bg);
                        mBackDrawAbles.put(2, bitmapDrawable);
                    }
                    return bitmapDrawable;
                } else {
                    Drawable bitmapDrawable = mBackDrawAbles.get(3);
                    if (bitmapDrawable == null) {
                        bitmapDrawable =
                                getApplication().getResources().getDrawable(
                                        R.mipmap.markers_bg);
                        mBackDrawAbles.put(3, bitmapDrawable);
                    }
                    return bitmapDrawable;
                }
            }
        });
        clusterOverlayUser.setOnClusterClickListener(new ClusterClickListener() {
            @Override
            public void onClick(Marker marker, List<ClusterItem> clusterItems) {
                Toast.makeText(MainActivity.this,">>>>>>>点击了用户聚合点",Toast.LENGTH_SHORT).show();

                if(aMap.getCameraPosition().zoom<=18){
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (ClusterItem clusterItem : clusterItems) {
                        builder.include(clusterItem.getPosition());
                    }
                    LatLngBounds latLngBounds = builder.build();
                    aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,10 ));
                }

            }
        });

    }

    /**
     * by moos on 2018/01/12
     * func:添加商家聚合点
     * @param locations
     */
    private void addMerchantClustersToMap(final List<LatLng> locations){

        for(int i=0;i<locations.size();i++){
            ClusterItemImp clusterImp = new ClusterItemImp(locations.get(i), TYPE_MERCHANT);
            clusterItemsMerchant.add(clusterImp);
        }
        if(clusterOverlayMerchant == null){
            clusterOverlayMerchant = new ClusterOverlayMerchant(aMap,clusterItemsMerchant,ViewUtil.dp2px(getApplicationContext(), clusterRadius),getApplicationContext());
        }else {
            clusterOverlayMerchant.onDestroy();
            clusterOverlayMerchant = null;
            clusterOverlayMerchant = new ClusterOverlayMerchant(aMap,clusterItemsMerchant,ViewUtil.dp2px(getApplicationContext(), clusterRadius),getApplicationContext());
        }

        clusterOverlayMerchant.setClusterAnotherRenderer(new ClusterAnotherRender() {
            @Override
            public Drawable getAnotherDrawAble(int clusterNum) {
                if (clusterNum <= 5) {
                    Drawable bitmapDrawable = mBackDrawAblesMerchant.get(2);
                    if (bitmapDrawable == null) {
                        bitmapDrawable =
                                getApplication().getResources().getDrawable(
                                        R.mipmap.marker_merchant_bg);
                        mBackDrawAblesMerchant.put(2, bitmapDrawable);
                    }
                    return bitmapDrawable;
                } else {
                    Drawable bitmapDrawable = mBackDrawAblesMerchant.get(3);
                    if (bitmapDrawable == null) {
                        bitmapDrawable =
                                getApplication().getResources().getDrawable(
                                        R.mipmap.markers_merchant_bg);
                        mBackDrawAblesMerchant.put(3, bitmapDrawable);
                    }
                    return bitmapDrawable;
                }
            }
        });
        clusterOverlayMerchant.setOnClusterAnotherClickListener(new ClusterAnotherClickListener() {
            @Override
            public void onAnotherClick(Marker marker, List<ClusterItem> clusterItems) {
                Toast.makeText(MainActivity.this,">>>>>>>点击了商家聚合点",Toast.LENGTH_SHORT).show();
                if(aMap.getCameraPosition().zoom<=18){
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (ClusterItem clusterItem : clusterItems) {
                        builder.include(clusterItem.getPosition());
                    }
                    LatLngBounds latLngBounds = builder.build();
                    aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,10 ));
                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_cluster_user:
                //添加个人聚合点
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        addUserClustersToMap(addSimulatedData(centerLocation, 60, 0.03));
                    }
                }).start();

                break;
            case R.id.add_cluster_merchant:
                //添加聚合点
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        addMerchantClustersToMap(addSimulatedData(centerLocation, 30, 0.022));
                    }
                }).start();

                break;

            case R.id.add_custom_marker:
                //添加自定义Marker
                addCustomMarkersToMap();
                break;
        }
    }


    /**
     * by moos on 2018/01/12
     * func:自定义监听接口,用来marker的icon加载完毕后回调添加marker属性
     */
    public interface OnMarkerIconLoadListener{
        void markerIconLoadingFinished(View view);
    }

    /**
     * by moos on 2018/01/12
     * func:定制化marker的图标
     * @return
     */
    private void customizeMarkerIcon(String url, final OnMarkerIconLoadListener listener) {
        final View markerView = LayoutInflater.from(this).inflate(R.layout.marker_bg, null);
        final CircleImageView icon = (CircleImageView) markerView.findViewById(R.id.marker_item_icon);
        Glide.with(this)
                .load(url +"!/format/webp")
                .asBitmap()
                .thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        //待图片加载完毕后再设置bitmapDes
                        icon.setImageBitmap(bitmap);
                        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(ViewUtil.convertViewToBitmap(markerView));
                        listener.markerIconLoadingFinished(markerView);
                    }
                });
    }

    /**
     * by moos on 2018/01/12
     * func:添加单个自定义marker
     * @param latLng 位置
     * @param sign marker标记
     */
    private void addCustomMarker(final LatLng latLng, final MarkerSign sign) {

        String url = "http://ucardstorevideo.b0.upaiyun.com/test/e8c8472c-d16d-4f0a-8a7b-46416a79f4c6.png";
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.position(new LatLng(latLng.latitude, latLng.longitude));
        customizeMarkerIcon(url, new OnMarkerIconLoadListener() {
            @Override
            public void markerIconLoadingFinished(View view) {
                //bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
                Marker marker;
                markerOptions.icon(bitmapDescriptor);
                marker = aMap.addMarker(markerOptions);
                marker.setObject(sign);
            }
        });
    }

    /**
     * by moos on 2018/01/12
     * func:批量添加自定义marker到地图上
     */
    private void addCustomMarkersToMap(){

        List<LatLng> locations= new ArrayList<>();
        locations = addSimulatedData(centerLocation, 20, 0.02);
        for(int i = 0;i<locations.size();i++){
            addCustomMarker(locations.get(i), new MarkerSign(i));
        }

    }



}
