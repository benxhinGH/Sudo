package com.lius.sudo;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class MainActivity extends Activity {

    private Button completeButton;

    private SudoView sudoView;
    private Button resetButton;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        level=StartActivity.level;
        sudoView=(SudoView)findViewById(R.id.first_sudoview);

        if(level!=1){
            sudoView.changeLevel(level);
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






    }


}
