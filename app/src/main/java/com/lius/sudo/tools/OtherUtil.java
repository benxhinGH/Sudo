package com.lius.sudo.tools;

import com.lius.sudo.Activity.StartActivity;

/**
 * Created by 刘有泽 on 2016/8/17.
 */
public class OtherUtil {
    public static String getStringLevel(){
        String level="";
        switch (StartActivity.level){
            case 1:
                level="入门级";
                break;
            case 2:
                level="初级";
                break;
            case 3:
                level="普通";
                break;
            case 4:
                level="高级";
                break;
            case 5:
                level="骨灰级";
                break;
            default:
                break;
        }
        return level;
    }
}
