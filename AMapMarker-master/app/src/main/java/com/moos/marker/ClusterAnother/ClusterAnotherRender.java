package com.moos.marker.ClusterAnother;

import android.graphics.drawable.Drawable;

/**
 * Created by moos on 2018/1/12.
 */

public interface ClusterAnotherRender {
    /**
     * 根据聚合点的元素数目返回渲染背景样式
     *
     * @param clusterNum
     * @return
     */
    Drawable getAnotherDrawAble(int clusterNum);
}
