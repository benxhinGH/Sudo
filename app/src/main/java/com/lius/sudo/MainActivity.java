package com.lius.sudo;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import com.lius.sudo.DB.SudoOpenHelper;
import com.lius.sudo.Dialog.LDialog;
import com.lius.sudo.Dialog.LevelDialog;
import com.lius.sudo.Dialog.MenuDialog;
import com.lius.sudo.Dialog.MyDialog;
import com.lius.sudo.tools.DBUtil;

import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class MainActivity extends Activity {

    final int SHOW_LOADING_DIALOG=0;
    final int CLOSE_LOADING_DIALOG=1;
    final int UPDATE_TIME_TEXTVIEW=2;

    private String playerName;
    private boolean isCanceled;
    private boolean isFromArchive=false;
    private int archiveId;

    private boolean timerThreadStop=false;

    public static int DIALOGFLAG=0;
    public static final int COMPLETE=1;
    public static final int SAVE=2;
    public static final int RESET=3;
    public static final int QUIT=4;
    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;
    private IntentFilter intentFilter;
    private Dialog mLoadingDialog;
    private TextView timeTV;
    private TextView levelTV;
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
                case UPDATE_TIME_TEXTVIEW:
                    timeTV.setText(myTimer.getStringTime());
                default:
                    break;
            }
        }
    };



    private Button menuButton;

    private SudoView sudoView;

    private MyTimer myTimer;

    private Thread timerThread;

    private SudoOpenHelper sudoOpenHelper;





    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentdata",sudoView.getSudokuArch());
        outState.putInt("currentconsumetime",myTimer.getIntegerTime());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        myTimer=new MyTimer();
        sudoView=(SudoView)findViewById(R.id.first_sudoview);
        timeTV=(TextView)findViewById(R.id.time_textview);
        levelTV=(TextView)findViewById(R.id.gamelevel_textview);
        //获取数据库帮助类对象
        sudoOpenHelper=new SudoOpenHelper(this,"Sudoku.db",null,1);
        if(savedInstanceState==null){
            Intent intent=getIntent();
            String flag=intent.getStringExtra("flag");
            String data=intent.getStringExtra("data");
            if(flag.equals("0")){

                sudoView.setSudoku(data);
            }else if(flag.equals("1")){
                isFromArchive=true;
                archiveId=intent.getIntExtra("id",-1);
                sudoView.setSudokuFromArch(data);
                myTimer.setCurrentTime(intent.getIntExtra("consumetime",0));


            }
        }else{
            sudoView.setSudokuFromArch(savedInstanceState.getString("currentdata"));
            myTimer.setCurrentTime(savedInstanceState.getInt("currentconsumetime"));
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
                                if(isFromArchive==true){
                                    SQLiteDatabase db= DBUtil.getDatabase(MainActivity.this);
                                    //获取存档时间
                                    SimpleDateFormat formatter=new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
                                    Date curDate=new Date(System.currentTimeMillis());//获取当前时间
                                    String archiveTime=formatter.format(curDate);
                                    //获取当前耗时
                                    int consumeTime=myTimer.getIntegerTime();
                                    //获取数独数据和颜色数据
                                    String data=sudoView.getSudokuArch();
                                    String number=data.substring(0,81);
                                    String color=data.substring(81);
                                    ContentValues contentValues=new ContentValues();
                                    contentValues.put("archivetime",archiveTime);
                                    contentValues.put("consumetime",consumeTime);
                                    contentValues.put("number",number);
                                    contentValues.put("color",color);
                                    db.update("Sudoku",contentValues,"id=?",new String[]{Integer.toString(archiveId)});
                                    Toast.makeText(MainActivity.this,"存档已更新",Toast.LENGTH_SHORT).show();
                                    break;
                                }

                                MyDialog myDialog=new MyDialog(MainActivity.this);
                                myDialog.setReturnDataListener(new MyDialog.ReturnDataListener() {
                                    @Override
                                    public void returnData(String data) {
                                        if(data.equals("取消"))isCanceled=true;
                                        else if(data.equals(""))playerName="蜜汁玩家";
                                        else playerName=data;
                                    }
                                });
                                myDialog.show();
                                myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        if(isCanceled!=true){
                                            //获取SQLiteDataBase实例，若数据库不存在则创建数据库
                                            SQLiteDatabase db=sudoOpenHelper.getWritableDatabase();
                                            //获取要存入的数据
                                            //获取游戏级别
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
                                            //获取存档时间
                                            SimpleDateFormat formatter=new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
                                            Date curDate=new Date(System.currentTimeMillis());//获取当前时间
                                            String archiveTime=formatter.format(curDate);
                                            //获取当前耗时
                                            int consumeTime=myTimer.getIntegerTime();
                                            Log.i("MainActivity","当前耗时为"+consumeTime);
                                            //获取数独数据和颜色数据
                                            String data=sudoView.getSudokuArch();
                                            String number=data.substring(0,81);
                                            Log.i("MainActivity","number数据为"+number);
                                            String color=data.substring(81);
                                            Log.d("MainActivity","color数据为"+color);
                                            //组装数据
                                            ContentValues contentValues=new ContentValues();
                                            contentValues.put("player",playerName);
                                            contentValues.put("level",level);
                                            contentValues.put("archivetime",archiveTime);
                                            contentValues.put("consumetime",consumeTime);
                                            contentValues.put("number",number);
                                            contentValues.put("color",color);
                                            //插入数据
                                            db.insert("Sudoku",null,contentValues);

                                            Toast.makeText(MainActivity.this,"已存档",Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });


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

        //设置级别文本框的内容
        switch (StartActivity.level){
            case 1:
                levelTV.setText("入门级");
                break;
            case 2:
                levelTV.setText("初级");
                break;
            case 3:
                levelTV.setText("普通");
                break;
            case 4:
                levelTV.setText("高级");
                break;
            case 5:
                levelTV.setText("骨灰级");
                break;
            default:
                break;
        }
        localBroadcastManager=LocalBroadcastManager.getInstance(this);
        localReceiver=new LocalReceiver();
        intentFilter=new IntentFilter();
        intentFilter.addAction("com.lius.sudo.GENERATESUDOKU");
        intentFilter.addAction("com.lius.sudo.FINISH");
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);


        //启动计时线程
        timerThread=new Thread(new myTimerThread());
        timerThread.start();
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
        //使用共享变量的方法终止线程
        if(timerThread.isAlive())timerThreadStop=true;
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
    class myTimerThread implements Runnable{

        @Override
        public void run() {
            while (!timerThreadStop){
                myTimer.timePlusOne();
                Message msg=new Message();
                msg.what=UPDATE_TIME_TEXTVIEW;
                handler.sendMessage(msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
