package com.moos.marker.Cluster;

/**
 * Created by moos on 2018/1/11.
 * func:marker的唯一标记，用于处理点击事件
 */

public class MarkerSign {
    private int markerId;

    public MarkerSign(int markerId) {
        this.markerId = markerId;
    }

    public int getMarkerId() {
        return markerId;
    }

    public void setMarkerId(int markerId) {
        this.markerId = markerId;
    }
}
