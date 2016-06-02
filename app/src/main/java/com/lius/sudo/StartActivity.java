package com.lius.sudo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;


/**
 * Created by Administrator on 2016/4/12 0012.
 */
public class StartActivity extends Activity {

    final int SHOW_LOADING_DIALOG=0;
    final int CLOSE_LOADING_DIALOG=1;

    public static int level = 1;
    private Button startGameButton;
    private Button levelButton;
    private Button readArchiveButton;
    private Button quitButton;
    private Dialog mLoadingDialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_LOADING_DIALOG:
                    showProgressDialog();
                    break;
                case CLOSE_LOADING_DIALOG:
                    closeProgressDialog();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.startlayout);
        startGameButton = (Button) findViewById(R.id.startgame_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg=new Message();
                        msg.what=SHOW_LOADING_DIALOG;
                        handler.sendMessage(msg);
                        GenerateSudoku generateSudoku = new GenerateSudoku(level);
                        String sudokuData = generateSudoku.getStringData();
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        intent.putExtra("flag","0");
                        intent.putExtra("data", sudokuData);
                        Message msg1=new Message();
                        msg1.what=CLOSE_LOADING_DIALOG;
                        handler.sendMessage(msg1);
                        startActivity(intent);

                    }
                }).start();

            }
        });
        levelButton = (Button) findViewById(R.id.level_button);
        levelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LevelDialog levelDialog = new LevelDialog(StartActivity.this);
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
        readArchiveButton=(Button)findViewById(R.id.read_archive_button);
        readArchiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StartActivity.this,ArchiveActivity.class);
                startActivity(intent);
            }
        });
        quitButton=(Button)findViewById(R.id.quit_btn);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void showProgressDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = createLoadingDialog(this, "生成初盘中...");
            mLoadingDialog.setCanceledOnTouchOutside(false);
        }
        mLoadingDialog.show();
    }

    private void closeProgressDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog=null;

    }

    private Dialog createLoadingDialog(Context context, String msg) {
        Log.d("StartActivity","创建LoadingDialog创建LoadingDialog创建LoadingDialog创建LoadingDialog");
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loadingdialog_layout, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.loadingdialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loadingdialog_anim);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);// 可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;
    }

}
