package com.lius.sudo.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.lius.sudo.Dialog.LastDialog;
import com.lius.sudo.Dialog.LevelDialog;
import com.lius.sudo.GenerateSudoku;
import com.lius.sudo.MainActivity;
import com.lius.sudo.R;


/**
 * Created by Administrator on 2016/4/12 0012.
 */
public class StartActivity extends Activity {

    final int SHOW_LOADING_DIALOG=0;
    final int CLOSE_LOADING_DIALOG=1;

    public static int level = -1;
    private Button startGameButton;
    private Button elseButton;
    private Button readArchiveButton;
    private Button quitButton;
    private Button rankButton;
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
    protected void onResume() {
        super.onResume();
        level=-1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.startlayout);
        startGameButton = (Button) findViewById(R.id.startgame_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog ld=new LevelDialog(StartActivity.this);
                ld.show();
                ld.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if(level!=-1){
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

                    }
                });



            }
        });

        elseButton=(Button)findViewById(R.id.else_button);
        elseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(StartActivity.this,"我是菜牛，哈哈",Toast.LENGTH_SHORT).show();
                new LastDialog(StartActivity.this).show();
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
        rankButton=(Button)findViewById(R.id.rank_button);
        rankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LevelDialog ld=new LevelDialog(StartActivity.this);
                ld.show();
                ld.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(level!=-1){
                            Intent intent=new Intent(StartActivity.this,RankActivity.class);
                            startActivity(intent);

                        }
                    }
                });
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
