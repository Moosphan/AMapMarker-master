package com.moos.marker.Utils;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by moos on 2018/1/11.
 */

public class ViewUtil {
    /**
     * by mos on 2018.01.12
     * func:view转bitmap
     */
    public static Bitmap convertViewToBitmap(View view) {

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache();

        Bitmap bitmap = view.getDrawingCache();

        return bitmap;

    }
}
