package com.lius.sudo.controller;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.lius.sudo.dialog.GameFailedDialog;
import com.lius.sudo.dialog.GameSuccessDialog;
import com.lius.sudo.dialog.GetUserNameDialog;
import com.lius.sudo.dialog.OnClickDialogBtnListener;
import com.lius.sudo.activity.FinishedActivity;
import com.lius.sudo.business.MyTimer;
import com.lius.sudo.business.SudokuGenerator;
import com.lius.sudo.database.SudokuOpenHelper;
import com.lius.sudo.model.GameDataDBModel;
import com.lius.sudo.model.SudokuNumber;
import com.lius.sudo.model.SudokuPuzzle;
import com.lius.sudo.utilities.Util;
import com.lius.sudo.view.SudokuView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by UsielLau on 2017/9/14 0014 17:42.
 *
 * 游戏入口，游戏控制器
 */

public class GameController {

    private final int SUDOKU_GENERATE_FINISHED=0;
    private final int UPDATE_TIMER=1;
    private final int GET_USERNAME=2;


    private GameSuccessDialog gameSuccessDialog;
    private GameFailedDialog gameFailedDialog;

    private OnGameExitListener onGameExitListener;
    private OnGameStartListener onGameStartListener;
    private OnGameFinishedListener onGameFinishedListener;

    private TextView gameTimeTv;


    private boolean ifNewGame;
    private int archiveNumber;
    private int level;
    private SudokuPuzzle sudokuPuzzle;
    private SudokuView sudokuView;
    private SudokuGenerator sudokuGenerator;
    private Context context;
    private ProgressDialog progressDialog;

    private MyTimer myTimer;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUDOKU_GENERATE_FINISHED:
                    SudokuPuzzle puzzle=(SudokuPuzzle)msg.obj;
                    sudokuGenerateFinished(puzzle);
                    if(onGameStartListener!=null){
                        onGameStartListener.onGameStart();
                    }
                    startTimer();
                    break;
                case UPDATE_TIMER:
                    String time=(String)msg.obj;
                    gameTimeTv.setText(time);
                    break;
                case GET_USERNAME:
                    String username=(String)msg.obj;
                    if(username==null){
                        username="未知玩家";
                    }
                    saveToRankList(username);
                    Intent intent=new Intent(context,FinishedActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("level",level);
                    bundle.putString("time",myTimer.getTime());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    gameExit();
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


    /**
     * 开始计时
     */
    private void startTimer(){
        if(myTimer==null){
            myTimer=new MyTimer();
        }
        myTimer.setOnTimeChangedListener(new MyTimer.OnTimeChangedListener() {
            @Override
            public void onChanged() {
                Message message=handler.obtainMessage(UPDATE_TIMER);
                message.obj=myTimer.getTime();
                handler.sendMessage(message);
            }
        });
        myTimer.start();
    }

    public void setGameTimeTv(TextView gameTimeTv) {
        this.gameTimeTv = gameTimeTv;
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
        this.sudokuPuzzle=puzzle;
        closeLoadingDialog();
        sudokuView.setPuzzleData(turnPuzzleToNumberArrType(puzzle));
    }

    private void startGameFromArchive(){
        SudokuOpenHelper openHelper=new SudokuOpenHelper(context,"Sudoku.db",null,1);
        SQLiteDatabase db=openHelper.getReadableDatabase();
        Cursor cursor=db.query("Archive",null,"id = ?",new String[]{String.valueOf(archiveNumber)},null,null,null);
        Log.d("GameController","查询到数据条数："+cursor.getCount());
        cursor.moveToFirst();
        byte[] gameData=cursor.getBlob(cursor.getColumnIndex("game_data"));
        ByteArrayInputStream bais=new ByteArrayInputStream(gameData);
        try {
            ObjectInputStream ois=new ObjectInputStream(bais);
            GameDataDBModel gameDataDBModel=(GameDataDBModel)ois.readObject();
            level=gameDataDBModel.getLevel();
            myTimer=new MyTimer(gameDataDBModel.getTime());
            sudokuPuzzle=new SudokuPuzzle(gameDataDBModel.getAnswer());
            sudokuView.setPuzzleData(gameDataDBModel.getSudokuNumbers());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(onGameStartListener!=null){
            onGameStartListener.onGameStart();
        }
        startTimer();
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

    public void gameFinished(){
        if(checkAnswer()){
            gameSuccess();
        }else{
            gameFailed();
        }
    }

    private void gameSuccess(){
        myTimer.stop();
        showGameSuccessDialog();
    }

    private void gameFailed(){
        showGameFailedDialog();
    }

    private void showGameSuccessDialog(){
        final GameSuccessDialog dialog=getGameSuccessDialog();
        dialog.show();
        dialog.setMessageText("恭喜你解题成功！是否加入排行榜?");
        dialog.setPositiveBtnListener(new OnClickDialogBtnListener() {
            @Override
            public void onClick() {
                showGetUserNameDialog();
                dialog.dismiss();
            }
        });
        dialog.setNegativeBtnListener(new OnClickDialogBtnListener() {
            @Override
            public void onClick() {
                Intent intent=new Intent(context, FinishedActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("level",level);
                bundle.putString("time",myTimer.getTime());
                intent.putExtras(bundle);
                context.startActivity(intent);
                gameExit();
                dialog.dismiss();
            }
        });
    }

    private void showGetUserNameDialog(){
        final GetUserNameDialog mDialog=new GetUserNameDialog(context);
        mDialog.show();
        mDialog.setPositiveBtnText("确定");
        mDialog.setPositiveBtnListener(new OnClickDialogBtnListener() {
            @Override
            public void onClick() {
                mDialog.dismiss();
            }
        });
        mDialog.setNegativeBtnText("忽略");
        mDialog.setNegativeBtnListener(new OnClickDialogBtnListener() {
            @Override
            public void onClick() {
                mDialog.dismiss();
            }
        });
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                String userName=mDialog.getUserName();
                Message msg=handler.obtainMessage(GET_USERNAME);
                msg.obj=userName;
                handler.sendMessage(msg);
            }
        });
    }



    private GameSuccessDialog getGameSuccessDialog(){
        if(gameSuccessDialog==null){
            gameSuccessDialog=new GameSuccessDialog(context);
        }
        return gameSuccessDialog;
    }

    private void showGameFailedDialog(){
        final GameFailedDialog dialog=getGameFailedDialog();
        dialog.show();
        dialog.setMessageText("有错误哦！");
        dialog.setPositiveListener(new OnClickDialogBtnListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
            }
        });
        dialog.setNegativeListener(new OnClickDialogBtnListener() {
            @Override
            public void onClick() {
                dialog.dismiss();
                gameExit();
            }
        });
    }

    private GameFailedDialog getGameFailedDialog(){
        if(gameFailedDialog==null){
            gameFailedDialog=new GameFailedDialog(context);
        }
        return gameFailedDialog;
    }

    private void gameExit(){
        myTimer.stop();
        if(onGameExitListener!=null){
            onGameExitListener.onGameExit();
        }
    }

    private void saveToRankList(String username){
        SudokuOpenHelper openHelper=new SudokuOpenHelper(context,"Sudoku.db",null,1);
        SQLiteDatabase db=openHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("player_name",username);
        contentValues.put("level",level);
        contentValues.put("game_time",myTimer.getTime());
        contentValues.put("game_seconds_time",myTimer.getTimeSeconds());
        contentValues.put("play_time",Util.getSystemTime());
        long res=db.insert("Rank",null,contentValues);
        if(res!=-1){
            Toast.makeText(context, "加入排行榜成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "加入排行榜失败", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean checkAnswer(){
        SudokuNumber[][] userAnswer=sudokuView.getSudokuData();
        int[][] realAnswer=sudokuPuzzle.getAnswerArray();
        for(int i=0;i<9;++i)
            for(int j=0;j<9;++j){
                if(userAnswer[i][j].getValue()!=realAnswer[i][j]){
                    return false;
                }
            }
        return true;
    }

    public void gameArchive(){
        SudokuNumber[][] sudokuNumbers=sudokuView.getSudokuData();
        int[][] answer=sudokuPuzzle.getAnswerArray();
        GameDataDBModel gameDataDBModel=new GameDataDBModel(sudokuNumbers,answer,level,myTimer.getTimeSeconds());
        saveArchiveDataToDatabase(gameDataDBModel);
        gameExit();
    }

    private void saveArchiveDataToDatabase(GameDataDBModel gameData){
        byte[] gameByteData=null;
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos=new ObjectOutputStream(baos);
            oos.writeObject(gameData);
            oos.flush();
            gameByteData=baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SudokuOpenHelper openHelper=new SudokuOpenHelper(context,"Sudoku.db",null,1);
        SQLiteDatabase sdb=openHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("archive_time", Util.getSystemTime());
        contentValues.put("game_data",gameByteData);
        long res=sdb.insert("Archive",null,contentValues);

        if(res==-1){
            Toast.makeText(context, "save error", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "save success", Toast.LENGTH_SHORT).show();
        }

    }



    public boolean isIfNewGame() {
        return ifNewGame;
    }

    public int getLevel() {
        return level;
    }

    public void setOnGameFinishedListener(OnGameFinishedListener onGameFinishedListener) {
        this.onGameFinishedListener = onGameFinishedListener;
    }

    public interface OnGameFinishedListener{
        void OnGameFinished();
    }

    public void setOnGameExitListener(OnGameExitListener onGameExitListener) {
        this.onGameExitListener = onGameExitListener;
    }

    public interface OnGameExitListener{
        void onGameExit();
    }

    public void setOnGameStartListener(OnGameStartListener onGameStartListener) {
        this.onGameStartListener = onGameStartListener;
    }

    public interface OnGameStartListener{
        void onGameStart();
    }


}
