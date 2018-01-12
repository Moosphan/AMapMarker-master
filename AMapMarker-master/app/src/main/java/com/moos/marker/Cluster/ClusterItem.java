package com.moos.marker.Cluster;

import com.amap.api.maps.model.LatLng;

/**
 * Created by yiyi.qi on 16/10/10.
 * Updated by moos on 18/01/12
 */

public interface ClusterItem {
    /**
     * 返回聚合元素的地理位置
     *
     * @return
     */
     LatLng getPosition();

    /**
     * 获取聚合点的类型
     * @return
     */
     String getClusterType();

}
