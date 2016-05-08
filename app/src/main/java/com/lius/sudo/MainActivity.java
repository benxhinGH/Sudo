package com.lius.sudo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;
import java.text.SimpleDateFormat;


/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class MainActivity extends Activity {

    private Button completeButton;

    private SudoView sudoView;
    private Button resetButton;
    private Button saveButton;


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




        completeButton=(Button)findViewById(R.id.complete_button);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sudoView.game.judgeResult()){
                    Toast.makeText(MainActivity.this,"恭喜过关！",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(MainActivity.this,"存在错误！",Toast.LENGTH_SHORT).show();

                }
            }
        });
        resetButton=(Button)findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudoView.resetGame();
            }
        });

        saveButton=(Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor=getSharedPreferences("sudoarchdata",MODE_PRIVATE).edit();
                SimpleDateFormat formatter=new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
                Date curDate=new Date(System.currentTimeMillis());//获取当前时间
                String str=formatter.format(curDate);
                Log.d("MainActivity","格式化后的时间为"+str);
                String data=sudoView.getSudokuArch();
                editor.putString(str,data);
                editor.commit();
                Toast.makeText(MainActivity.this,"已存档",Toast.LENGTH_SHORT).show();


                SharedPreferences sharedPreferences=getSharedPreferences("sudoarchdata",MODE_PRIVATE);
                String testStr=sharedPreferences.getString(str,"");
                Log.d("MainActivity","测试保存数据"+testStr);

            }
        });






    }


}
