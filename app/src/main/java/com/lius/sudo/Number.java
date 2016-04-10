package com.lius.sudo;

/**
 * Created by Administrator on 2016/4/10 0010.
 */
public class Number {
    private int value;
    private boolean isDefault;
    public void setValue(int value){
        this.value=value;
    }
    public int getValue(){
        return value;
    }
    public void setIsDefault(boolean isDefault){
        this.isDefault=isDefault;
    }
    public boolean getIsDefault(){
        return isDefault;
    }
}
