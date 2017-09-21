package com.lius.sudo.utilities;

import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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


    /**
     * 根据数据等级返回文字等级
     * @param level
     * @return
     */
    public static String getGameLevelText(int level){
        String res="";
        switch (level){
            case 1:
                res="入门级";
                break;
            case 2:
                res="初级";
                break;
            case 3:
                res="普通";
                break;
            case 4:
                res="高级";
                break;
            case 5:
                res="骨灰级";
                break;
            default:
                break;
        }
        return res;
    }


    /**
     * 以X年X月X日X:X:Xde形式返回当前系统时间
     * @return
     */
    public static String getSystemTime(){
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        Date curDate=new Date(System.currentTimeMillis());//获取当前时间
        String time=formatter.format(curDate);
        return time;
    }

}
