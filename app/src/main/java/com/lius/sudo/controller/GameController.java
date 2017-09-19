package com.lius.sudo.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.lius.sudo.business.SudokuGenerator;
import com.lius.sudo.model.SudokuNumber;
import com.lius.sudo.model.SudokuPuzzle;
import com.lius.sudo.view.SudokuView;

/**
 * Created by UsielLau on 2017/9/14 0014 17:42.
 *
 * 游戏入口，游戏控制器
 */

public class GameController {

    final int SUDOKU_GENERATE_FINISHED=0;


    private boolean ifNewGame;
    private int archiveNumber;
    private int level;
    private SudokuPuzzle sudokuPuzzle;
    private SudokuView sudokuView;
    private SudokuGenerator sudokuGenerator;
    private Context context;
    private ProgressDialog progressDialog;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUDOKU_GENERATE_FINISHED:
                    SudokuPuzzle puzzle=(SudokuPuzzle)msg.obj;
                    sudokuGenerateFinished(puzzle);
                    break;
                default:
                    break;
            }
        }
    };

    public GameController(Context context,SudokuView sudokuView){
        this.context=context;
        this.sudokuView=sudokuView;
    }

    public void setIfNewGame(boolean ifNewGame){
        this.ifNewGame=ifNewGame;
    }

    public void setArchiveNumber(int archiveNumber){
        this.archiveNumber=archiveNumber;
    }

    public void setLevel(int level){
        this.level=level;
    }

    public void startGame(){
        if(ifNewGame){
            startNewGame();
        }else{
            startGameFromArchive();
        }
        setSudokuViewListener();
    }

    private void setSudokuViewListener(){
        sudokuView.setOnClickCellListener(new SudokuView.OnClickCellListener() {
            @Override
            public void onClick(int cellX, int cellY) {
                Toast.makeText(context, "click X:"+cellX+" Y:"+cellY, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startNewGame(){
        if(sudokuGenerator==null){
            sudokuGenerator=SudokuGenerator.getInstance();
        }
        showLoadingDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SudokuPuzzle puzzle=sudokuGenerator.getSudokuPuzzle(level);
                Message message=handler.obtainMessage(SUDOKU_GENERATE_FINISHED);
                message.obj=puzzle;
                handler.sendMessage(message);
            }
        }).start();

    }

    private void sudokuGenerateFinished(SudokuPuzzle puzzle){
        closeLoadingDialog();
        sudokuView.setPuzzleData(turnPuzzleToNumberArrType(puzzle));
    }

    private void startGameFromArchive(){

    }

    private SudokuNumber[][] turnPuzzleToNumberArrType(SudokuPuzzle puzzle){
        SudokuNumber[][] data=new SudokuNumber[9][9];
        int[][] puzzleArray=puzzle.getPuzzleArray();
        int currentValue;
        SudokuNumber.NumberType currentType;
        for(int i=0;i<9;++i)
            for(int j=0;j<9;++j){
                currentValue=puzzleArray[i][j];
                if(currentValue==0){
                    currentType= SudokuNumber.NumberType.USER;
                }else{
                    currentType= SudokuNumber.NumberType.DEFAULT;
                }
                data[i][j]=new SudokuNumber(currentValue,currentType);
            }
        return data;
    }

    private void showLoadingDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("正在生成数独");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void closeLoadingDialog(){
        progressDialog.cancel();
    }



}
