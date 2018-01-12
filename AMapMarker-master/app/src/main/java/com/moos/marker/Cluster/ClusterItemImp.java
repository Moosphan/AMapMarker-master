package com.moos.marker.Cluster;

import com.amap.api.maps.model.LatLng;

/**
 * Created by moos on 2018/1/11.
 */

public class ClusterItemImp implements ClusterItem{
    private LatLng latLng;
    private String clusterType;

    public ClusterItemImp(LatLng latLng, String clusterType) {
        this.latLng = latLng;
        this.clusterType = clusterType;
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    @Override
    public String getClusterType() {
        return clusterType;
    }
}
