package com.lius.sudo;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.Toast;

import com.lius.sudo.Activity.StartActivity;
import com.lius.sudo.Dialog.LDialog;
import com.lius.sudo.Dialog.LevelDialog;
import com.lius.sudo.Dialog.MenuDialog;

import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class MainActivity extends Activity {

    final int SHOW_LOADING_DIALOG=0;
    final int CLOSE_LOADING_DIALOG=1;

    public static int DIALOGFLAG=0;
    public static final int COMPLETE=1;
    public static final int SAVE=2;
    public static final int RESET=3;
    public static final int QUIT=4;
    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;
    private IntentFilter intentFilter;
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



    private Button menuButton;

    private SudoView sudoView;


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        sudoView=(SudoView)findViewById(R.id.first_sudoview);
        Intent intent=getIntent();
        String flag=intent.getStringExtra("flag");
        String data=intent.getStringExtra("data");
        if(flag.equals("0")){

            sudoView.setSudoku(data);
        }else if(flag.equals("1")){

            sudoView.setSudokuFromArch(data);
        }
        menuButton=(Button)findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuDialog md=new MenuDialog(MainActivity.this);
                md.show();
                md.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        switch (DIALOGFLAG){
                            case COMPLETE:
                                if(sudoView.game.judgeResult()){
                                    new LDialog(MainActivity.this,0).show();


                                }else{
                                    new LDialog(MainActivity.this,1).show();
                                }
                                break;
                            case SAVE:
                                SharedPreferences.Editor editor=getSharedPreferences("sudoarchdata",MODE_PRIVATE).edit();
                                SimpleDateFormat formatter=new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
                                Date curDate=new Date(System.currentTimeMillis());//获取当前时间
                                String time=formatter.format(curDate);
                                String level="";
                                switch (StartActivity.level){
                                    case 1:
                                        level="入门级";
                                        break;
                                    case 2:
                                        level="初级";
                                        break;
                                    case 3:
                                        level="普通";
                                        break;
                                    case 4:
                                        level="高级";
                                        break;
                                    case 5:
                                        level="骨灰级";
                                        break;
                                    default:
                                        break;
                                }
                                String str=time+level;
                                Log.d("MainActivity","格式化后的时间为"+str);
                                String data=sudoView.getSudokuArch();
                                editor.putString(str,data);
                                editor.commit();
                                Toast.makeText(MainActivity.this,"已存档",Toast.LENGTH_SHORT).show();


                                SharedPreferences sharedPreferences=getSharedPreferences("sudoarchdata",MODE_PRIVATE);
                                String testStr=sharedPreferences.getString(str,"");
                                Log.d("MainActivity","测试保存数据"+testStr);
                                break;
                            case RESET:
                                sudoView.resetGame();
                                break;
                            case QUIT:
                                finish();
                                break;
                            default:
                                break;
                        }
                        DIALOGFLAG=0;

                    }
                });

            }
        });
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        localReceiver=new LocalReceiver();
        intentFilter=new IntentFilter();
        intentFilter.addAction("com.lius.sudo.GENERATESUDOKU");
        intentFilter.addAction("com.lius.sudo.FINISH");
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
    }
    class LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals("com.lius.sudo.GENERATESUDOKU")){
               // Log.d("MainActivity","收到GENERATESUDOKU广播收到GENERATESUDOKU广播收到GENERATESUDOKU广播");
                Dialog ld=new LevelDialog(MainActivity.this);
                ld.show();
                ld.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message msg=new Message();
                                msg.what=SHOW_LOADING_DIALOG;
                                handler.sendMessage(msg);
                                sudoView.regenerateSudoku();
                                Message msg1=new Message();
                                msg1.what=CLOSE_LOADING_DIALOG;
                                handler.sendMessage(msg1);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sudoView.invalidate();
                                    }
                                });
                            }
                        }).start();
                    }
                });

            }else if(action.equals("com.lius.sudo.FINISH")){
               // Log.d("Mainactivity","收到FINISH广播收到FINISH广播收到FINISH广播收到FINISH广播收到FINISH广播");
                finish();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);
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
        //Log.d("StartActivity","创建LoadingDialog创建LoadingDialog创建LoadingDialog创建LoadingDialog");
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
