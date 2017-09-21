package com.lius.sudo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lius.sudo.R;
import com.lius.sudo.business.MyTimer;
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
    private LinearLayout bottomLayout;

    private TextView gameLevelTv;
    private TextView gameTimeTv;
    private TextView gamePatternTv;

    private int defaultSudokuViewMargin=10;

    private SudokuView sudokuView;

    private AlertDialog gameMenuDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findViews();
        addViews();
        toolbar.setBackgroundColor(getResources().getColor(R.color.purple));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        handleIntent();
        checkFloatWindowPermission();
    }

    private void findViews(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        rootLayout=(LinearLayout)findViewById(R.id.root_layout);
    }

    private void addViews(){
        addSudokuView();
        addBottomLayout();
    }

    private void addSudokuView(){
        sudokuView=new SudokuView(this);
        int screenWidth= Util.getScreenSize(this).x;
        int leftMargin=Util.dp2px(this,defaultSudokuViewMargin);
        int width=screenWidth-leftMargin*2;
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width,width);
        params.leftMargin=leftMargin;
        params.rightMargin=leftMargin;
        params.topMargin=leftMargin;
        sudokuView.setLayoutParams(params);
        rootLayout.addView(sudokuView);
    }

    private void addBottomLayout(){
        bottomLayout=new LinearLayout(this);
        LinearLayout.LayoutParams bottomLayoutParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        bottomLayout.setOrientation(LinearLayout.VERTICAL);
        int margin=Util.dp2px(this,defaultSudokuViewMargin);
        bottomLayoutParams.setMargins(margin,margin,margin,margin);
        bottomLayout.setLayoutParams(bottomLayoutParams);
        inflateBottomLayout();
        rootLayout.addView(bottomLayout);
    }

    /**
     * 填充底部布局
     */
    private void inflateBottomLayout(){
        bottomLayout.addView(getGameLevelLayout());
        bottomLayout.addView(getGameTimeLayout());
        bottomLayout.addView(getGamePatternLayout());
    }

    private LinearLayout getGameLevelLayout(){
        LinearLayout layout=(LinearLayout)getLayoutInflater().inflate(R.layout.my_prompt_layout,null);
        TextView prompt_tv=(TextView)layout.findViewById(R.id.prompt_tv);
        prompt_tv.setText("等级：");
        gameLevelTv=(TextView)layout.findViewById(R.id.content_tv);
        return layout;
    }

    private LinearLayout getGameTimeLayout(){
        LinearLayout layout=(LinearLayout)getLayoutInflater().inflate(R.layout.my_prompt_layout,null);
        TextView prompt_tv=(TextView)layout.findViewById(R.id.prompt_tv);
        prompt_tv.setText("时间：");
        gameTimeTv=(TextView)layout.findViewById(R.id.content_tv);
        return layout;
    }

    private LinearLayout getGamePatternLayout(){
        LinearLayout layout=(LinearLayout)getLayoutInflater().inflate(R.layout.my_prompt_layout,null);
        TextView prompt_tv=(TextView)layout.findViewById(R.id.prompt_tv);
        prompt_tv.setText("模式：");
        gamePatternTv=(TextView)layout.findViewById(R.id.content_tv);
        return layout;
    }


    private void handleIntent(){
        Intent intent=getIntent();
        Bundle extra=intent.getBundleExtra("extra");
        String gameType=extra.getString("gameType");
        if(gameType.equals("newGame")){
            int gameLevel=extra.getInt("gameLevel");
            startGameAtLevel(gameLevel);
        }else {
            int archiveId=extra.getInt("archiveId");
            startGameFromArchive(archiveId);
        }
    }

    private void startGameFromArchive(int archiveId){
        gameController=new GameController(this,sudokuView);
        gameController.setIfNewGame(false);
        gameController.setArchiveNumber(archiveId);
        setGameControllerListener();
        gameController.startGame();
    }

    private void startGameAtLevel(int level){
        gameController=new GameController(this,sudokuView);
        gameController.setIfNewGame(true);
        gameController.setLevel(level);
        setGameControllerListener();
        gameController.startGame();
    }

    private void setGameControllerListener(){
        //将时间textview传入
        gameController.setGameTimeTv(gameTimeTv);
        gameController.setOnGameExitListener(new GameController.OnGameExitListener() {
            @Override
            public void onGameExit() {
                finish();
            }
        });

        gameController.setOnGameStartListener(new GameController.OnGameStartListener() {
            @Override
            public void onGameStart() {
                gameLevelTv.setText(Util.getGameLevelText(gameController.getLevel()));
                if(gameController.isIfNewGame()){
                    gamePatternTv.setText("新游戏");
                }else{
                    gamePatternTv.setText("存档");
                }
            }
        });
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
                showDialogGameMenu();
                break;
            default:
                break;
        }
        return true;
    }

    private void showDialogGameMenu(){
        if(gameMenuDialog==null){
            gameMenuDialog=createGameMenuDialog();
        }
        gameMenuDialog.show();
    }

    private AlertDialog createGameMenuDialog(){
        final AlertDialog dialog=new AlertDialog.Builder(this).create();
        dialog.show();
        View dialogView= LayoutInflater.from(this).inflate(R.layout.dialog_game_menu,null);
        dialog.getWindow().setContentView(dialogView);
        //消除白色背景
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //获取控件
        Button gameFinishBtn=(Button)dialogView.findViewById(R.id.game_finish_btn);
        Button gameSaveBtn=(Button)dialogView.findViewById(R.id.game_save_btn);
        Button gameResetBtn=(Button)dialogView.findViewById(R.id.game_reset_btn);
        gameFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameController.gameFinished();
                dialog.dismiss();
            }
        });
        gameSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameController.gameArchive();
                dialog.dismiss();
            }
        });
        gameResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GameActivity.this, "reset", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        return dialog;
    }


}
