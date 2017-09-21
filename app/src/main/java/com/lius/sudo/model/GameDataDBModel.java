package com.lius.sudo.model;

import java.io.Serializable;

/**
 * Created by UsielLau on 2017/9/21 0021 19:38.
 *
 * 用于保存进数据库的数独题模型对象
 */

public class GameDataDBModel implements Serializable{

    static final long serialVersionUID=1l;

    //用户已填数据
    private SudokuNumber[][] sudokuNumbers;
    //答案
    private int[][] answer;
    //等级
    private int level;
    //已用时,秒数表示
    private int time;

    public GameDataDBModel(SudokuNumber[][] sudokuNumbers, int[][] answer,int level,int time) {
        this.sudokuNumbers = sudokuNumbers;
        this.answer = answer;
        this.level=level;
        this.time=time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public SudokuNumber[][] getSudokuNumbers() {
        return sudokuNumbers;
    }

    public void setSudokuNumbers(SudokuNumber[][] sudokuNumbers) {
        this.sudokuNumbers = sudokuNumbers;
    }

    public int[][] getAnswer() {
        return answer;
    }

    public void setAnswer(int[][] answer) {
        this.answer = answer;
    }
}
