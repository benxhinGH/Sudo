package com.lius.sudo;

import android.util.Log;

/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class Game {
    private String str;
    private GenerateSudoku generateSudoku=new GenerateSudoku();

    private Number sudoku []=new Number[9*9];

    public Game(){
        str=generateSudoku.getStringData();
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
        if(num==10)sudoku[y*9+x].setValue(0);
        else sudoku[y*9+x].setValue(num);
    }
    public boolean ifIsDefault(int x,int y){
        return sudoku[x+y*9].getIsDefault();
    }

    public boolean judgeResult(){
        for(int x=0;x<9;++x){
            while(!judgeOneToNine(sortOneToNine(getIntsByCoordinate(x, 0, x, 8))))return false;
        }
        for(int y=0;y<9;++y){
            while(!judgeOneToNine(sortOneToNine(getIntsByCoordinate(0,y,8,y))))return false;
        }
        for(int startY=0;startY<=6;startY=startY+3)
            for(int startX=0;startX<=6;startX=startX+3){
                while(!judgeOneToNine((sortOneToNine(getIntsByCoordinate(startX,startY,
                        startX+2,startY+2)))))return false;
            }

        return true;
    }
    private int[] getIntsByCoordinate(int startX,int startY,int endX,int endY){
        Log.d("getIntsByCoordinate","传入坐标为"+startX+startY+endX+endY);
        int[] numbers=new int[9];
        int i=0;
        for(int y=startY;y<=endY;++y)
            for(int x=startX;x<=endX;++x){
                numbers[i]=getNumber(x,y);
                ++i;
            }
        Log.d("getIntsByCoordinate","返回结果为"+numbers[0]+numbers[1]+numbers[2]+numbers[3]+numbers[4]
        +numbers[5]+numbers[6]+numbers[7]+numbers[8]);
        return numbers;
    }
    private int[] sortOneToNine(int[] numbers){
        for(int i=0;i<9;++i){
            for(int j=0;j<9-i-1;++j){
                if(numbers[j]>numbers[j+1]){
                    int n=numbers[j];
                    numbers[j]=numbers[j+1];
                    numbers[j+1]=n;
                }
            }
        }
        Log.d("sortOneToNine","返回结果为"+numbers[0]+numbers[1]+numbers[2]+numbers[3]+numbers[4]
                +numbers[5]+numbers[6]+numbers[7]+numbers[8]);
        return numbers;
    }
    private boolean judgeOneToNine(int[] numbers){

        for(int i=0;i<9;++i){
            if(numbers[i]!=i+1)return false;
        }
        return true;
    }

    public void resetSudoku(){
        for(Number n:sudoku){
            if(!n.getIsDefault())n.setValue(0);
        }
    }
    public void generateByLevel(int level){
        str=generateSudoku.changeLevel(level);
        sudoku=fromPuzzleString(str);
    }
}
