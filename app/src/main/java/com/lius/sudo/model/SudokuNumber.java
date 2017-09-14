package com.lius.sudo.model;

/**
 * Created by UsielLau on 2017/9/14 0014 22:13.
 */

public class SudokuNumber {
    private int value;
    private NumberType type;

    public SudokuNumber(int value,NumberType type){
        this.value=value;
        this.type=type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public NumberType getType() {
        return type;
    }

    public void setType(NumberType type) {
        this.type = type;
    }

    public enum NumberType{
        DEFAULT,USER;
    }

}
