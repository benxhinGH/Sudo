package com.lius.sudo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lius.sudo.R;

import java.util.zip.Inflater;

/**
 * Created by UsielLau on 2017/9/15 0015 13:48.
 */

public class MainInterfaceActivity extends AppCompatActivity {

    private Button btnStartGame;
    private Button btnRank;
    private Button btnAbout;
    private Button btnExit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListeners();
    }

    private void findViews(){
        btnStartGame=(Button)findViewById(R.id.start_game_btn);
        btnRank=(Button)findViewById(R.id.rank_btn);
        btnAbout=(Button)findViewById(R.id.about_btn);
        btnExit=(Button)findViewById(R.id.exit_btn);
    }

    private void setListeners(){
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogStartGame();
            }
        });
        btnRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void showDialogStartGame(){
        final AlertDialog dialog=new AlertDialog.Builder(this).create();
        dialog.show();
        View dialogView= LayoutInflater.from(this).inflate(R.layout.dialog_choose_game_type,null);
        dialog.getWindow().setContentView(dialogView);
        //消除白色背景
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //获取控件
        Button newGameBtn=(Button)dialogView.findViewById(R.id.new_game_btn);
        Button archiveBtn=(Button)dialogView.findViewById(R.id.archive_btn);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showDialogNewGame();
            }
        });
        archiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainInterfaceActivity.this,ArchiveActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    private void showDialogNewGame(){
        final AlertDialog dialog=new AlertDialog.Builder(this).create();
        dialog.show();
        View dialogView= LayoutInflater.from(this).inflate(R.layout.dialog_choose_game_level,null);
        dialog.getWindow().setContentView(dialogView);
        //消除白色背景
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //获取控件
        Button btnLevel1=(Button)dialogView.findViewById(R.id.level_1_btn);
        Button btnLevel2=(Button)dialogView.findViewById(R.id.level_2_btn);
        Button btnLevel3=(Button)dialogView.findViewById(R.id.level_3_btn);
        Button btnLevel4=(Button)dialogView.findViewById(R.id.level_4_btn);
        Button btnLevel5=(Button)dialogView.findViewById(R.id.level_5_btn);
        btnLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGameAtLevel(1);
                dialog.dismiss();
            }
        });
        btnLevel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGameAtLevel(2);
                dialog.dismiss();
            }
        });
        btnLevel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGameAtLevel(3);
                dialog.dismiss();
            }
        });
        btnLevel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGameAtLevel(4);
                dialog.dismiss();
            }
        });
        btnLevel5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGameAtLevel(5);
                dialog.dismiss();
            }
        });

    }

    private void startNewGameAtLevel(int level){
        Intent intent=new Intent(this,GameActivity.class);
        Bundle extra=new Bundle();
        extra.putString("gameType","newGame");
        extra.putInt("gameLevel",level);
        intent.putExtra("extra",extra);
        startActivity(intent);
    }

}
