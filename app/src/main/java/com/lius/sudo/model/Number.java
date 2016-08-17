package com.lius.sudo.model;

/**
 * Created by Administrator on 2016/4/10 0010.
 */
public class Number {
    private int value;
    private boolean isDefault;
    private boolean isRepeated;
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
    public void setIsRepeated(boolean isRepeated){
        this.isRepeated=isRepeated;
    }
    public boolean getIsRepeated(){
        return isRepeated;
    }
}
