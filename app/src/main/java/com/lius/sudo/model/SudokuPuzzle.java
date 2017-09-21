package com.lius.sudo.model;

/**
 * Created by UsielLau on 2017/9/14 0014 17:54.
 *
 * 数独题数据模型
 */

public class SudokuPuzzle {

    //谜题数组
    private int[][] puzzleArray;
    //题解数组
    private int[][] answerArray;
    //难度等级
    private int level;

    public SudokuPuzzle(int[][] answerArray){
        this.answerArray=answerArray;
    }

    public SudokuPuzzle(int[][] puzzleArray, int[][] answerArray, int level) {
        this.puzzleArray = puzzleArray;
        this.answerArray = answerArray;
        this.level = level;
    }

    public int[][] getPuzzleArray() {
        return puzzleArray;
    }

    public void setPuzzleArray(int[][] puzzleArray) {
        this.puzzleArray = puzzleArray;
    }

    public int[][] getAnswerArray() {
        return answerArray;
    }

    public void setAnswerArray(int[][] answerArray) {
        this.answerArray = answerArray;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
