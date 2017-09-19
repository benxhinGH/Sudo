package com.lius.sudo.utilities;

import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by UsielLau on 2017/9/14 0014 18:06.
 *
 * 工具类
 */

public class Util {

    /**
     * 以字符串形式返回整形数组的数据
     * @param array
     * @return
     */
    public String getArrayString(int[][] array){
        String s="";
        for(int i=0;i<9;++i)
            for(int j=0;j<9;++j){
                s=s+array[i][j];
            }
        return s;
    }

    /**
     * dp转px
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }

    /**
     * 获取屏幕尺寸
     * @param context
     * @return
     */
    public static Point getScreenSize(Context context){
        WindowManager windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Point size=new Point();
        windowManager.getDefaultDisplay().getSize(size);
        return size;
    }

    /**
     * 获取系统状态栏的高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context){
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

}
