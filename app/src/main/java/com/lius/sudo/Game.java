package com.lius.sudo;

/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class Game {
    private final String str="360000000004230800000004200"
            +"070460003820000014500013020"
            +"001900000007048300000000045";

    private int sudoku []=new int [9*9];

    public Game(){
        sudoku=fromPuzzleString(str);
    }
    private int getNumber(int x,int y){
        return sudoku[x+y*9];
    }
    //根据坐标返回单元格中应该填写的数字，如果是0就返回空字符串
    public String getNumberString(int x,int y){
        int v=getNumber(x,y);
        if(v==0)return "";
        else return String.valueOf(v);
    }

    //根据一个字符串生成一个整形数组，作为数独游戏的初始化数据
    protected int[] fromPuzzleString(String src){
        int[] sudo=new int[src.length()];
        for(int i=0;i<src.length();++i){
            sudo[i]=src.charAt(i)-'0';
        }
        return sudo;
    }

    public void setNum(int x, int y, int num){
        sudoku[y*9+x]=num;
    }
}
