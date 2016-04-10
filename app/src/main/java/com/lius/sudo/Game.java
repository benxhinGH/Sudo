package com.lius.sudo;

/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class Game {
    private final String str="360000000004230800000004200"
            +"070460003820000014500013020"
            +"001900000007048300000000045";

    private Number sudoku []=new Number[9*9];

    public Game(){
        sudoku=fromPuzzleString(str);
    }
    private int getNumber(int x,int y){
        return sudoku[x+y*9].getValue();
    }
    //根据坐标返回单元格中应该填写的数字，如果是0就返回空字符串
    public String getNumberString(int x,int y){
        int v=getNumber(x,y);
        if(v==0)return "";
        else return String.valueOf(v);
    }

    //根据一个字符串生成一个整形数组，作为数独游戏的初始化数据
    protected Number[] fromPuzzleString(String src){
        Number[] sudo=new Number[src.length()];
        for(int i=0;i<src.length();++i){
            //sudo数组中的每一个对象在使用前都要分派给他一个引用，否则会报NullPointerException
            sudo[i]=new Number();
            int n=src.charAt(i)-'0';
            if(n!=0){
                sudo[i].setValue(n);
                sudo[i].setIsDefault(true);
            }else{
                sudo[i].setValue(0);
                sudo[i].setIsDefault(false);
            }
        }
        return sudo;
    }

    public void setNum(int x, int y, int num){
        sudoku[y*9+x].setValue(num);
    }
    public boolean ifIsDefault(int x,int y){
        return sudoku[x+y*9].getIsDefault();
    }
    private int[] getValueFromNumber(Number[] numbers){
        int[] values=new int[numbers.length];
        for(int i=0;i<numbers.length;++i)values[i]=numbers[i].getValue();
        return values;
    }
    private boolean judgeResult(){
        return true;
    }
}
