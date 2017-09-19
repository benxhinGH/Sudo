package com.lius.sudo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lius.sudo.R;
import com.lius.sudo.controller.GameController;
import com.lius.sudo.utilities.Util;
import com.lius.sudo.view.SudokuView;

/**
 * Created by UsielLau on 2017/9/14 0014 22:45.
 */

public class GameActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private GameController gameController;

    private LinearLayout rootLayout;

    private SudokuView sudokuView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findViews();
        addViews();
        toolbar.setBackgroundColor(getResources().getColor(R.color.purple));
        setSupportActionBar(toolbar);
        handleIntent();
        checkFloatWindowPermission();
    }

    private void findViews(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        rootLayout=(LinearLayout)findViewById(R.id.root_layout);
    }

    private void addViews(){
        addSudokuView();
    }

    private void addSudokuView(){
        sudokuView=new SudokuView(this);
        int screenWidth= Util.getScreenSize(this).x;
        int leftMargin=Util.dp2px(this,10);
        int width=screenWidth-leftMargin*2;
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,width);
        params.leftMargin=leftMargin;
        params.rightMargin=leftMargin;
        params.topMargin=leftMargin;
        sudokuView.setLayoutParams(params);
        rootLayout.addView(sudokuView);
    }


    private void handleIntent(){
        Intent intent=getIntent();
        Bundle extra=intent.getBundleExtra("extra");
        String gameType=extra.getString("gameType");
        if(gameType.equals("newGame")){
            int gameLevel=extra.getInt("gameLevel");
            startGameAtLevel(gameLevel);
        }else {
            int archiveNumber=extra.getInt("archiveNumber");
        }
    }

    private void startGameAtLevel(int level){
        gameController=new GameController(this,sudokuView);
        gameController.setIfNewGame(true);
        gameController.setLevel(level);
        gameController.startGame();

    }

    private void checkFloatWindowPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 0);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_menu:
                Toast.makeText(this, "click menu", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}
