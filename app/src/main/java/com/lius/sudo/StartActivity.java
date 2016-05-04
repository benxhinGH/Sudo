package com.lius.sudo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;


/**
 * Created by Administrator on 2016/4/12 0012.
 */
public class StartActivity extends Activity{

    public static int level=1;
    private Button startGameButton;
    private Button levelButton;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.startlayout);
        startGameButton=(Button)findViewById(R.id.startgame_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GenerateSudoku generateSudoku=new GenerateSudoku(level);
                        String sudokuData=generateSudoku.getStringData();
                        Intent intent=new Intent(StartActivity.this,MainActivity.class);
                        intent.putExtra("data",sudokuData);
                        closeProgressDialog();
                        startActivity(intent);

                    }
                }).start();

            }
        });
        levelButton=(Button)findViewById(R.id.level_button);
        levelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LevelDialog levelDialog=new LevelDialog(StartActivity.this);
                levelDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        switch (level) {
                            case 1:
                                levelButton.setText("当前等级：入门级");
                                break;
                            case 2:
                                levelButton.setText("当前等级：初级");
                                break;
                            case 3:
                                levelButton.setText("当前等级：普通");
                                break;
                            case 4:
                                levelButton.setText("当前等级：高级");
                                break;
                            case 5:
                                levelButton.setText("当前等级：骨灰级");
                                break;
                            default:
                                break;
                        }
                    }
                });
                levelDialog.show();


            }
        });
    }
    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("waiting...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }


}
