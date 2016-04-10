package com.lius.sudo;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);





    }

    private int calculateViewHeight(){
        //Rect frame=new Rect();
        //getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        //状态栏高度
        //int statusBarHeight=frame.top;
        View v=getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        //状态栏和标题栏的总高度
        int contentTop=v.getTop();
        //标题栏的高度
        //int titleBarHeight=contentTop-statusBarHeight;

        //point对象用来保存屏幕的尺寸信息
        Point point=new Point();
        Display display=getWindowManager().getDefaultDisplay();
        display.getSize(point);
        //屏幕的高减去状态栏和标题栏的总高度即为View的高度
        int viewHeight=point.y-contentTop;

        Log.d("MainActivity","屏幕高度"+point.y+"contentTop"+contentTop+"viewHeight"+viewHeight);
        return viewHeight;
    }
}
