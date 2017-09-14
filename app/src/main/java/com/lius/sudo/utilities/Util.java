package com.lius.sudo.utilities;

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
}
