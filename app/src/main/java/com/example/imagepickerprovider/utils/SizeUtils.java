package com.example.imagepickerprovider.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import androidx.recyclerview.widget.RecyclerView;

/**
 * description 适配屏幕大小
 * create by xiaocai on 2020/6/28
 */
public class SizeUtils {
    public static Point getScreenSize(Context context){
        Point point=new Point();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
        return point;
    }
}
