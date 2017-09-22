package com.lius.sudo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lius.sudo.R;
import com.lius.sudo.utilities.Util;

/**
 * Created by UsielLau on 2017/9/22 0022 11:41.
 */

public class FinishedActivity extends AppCompatActivity {

    private TextView levelTv;
    private TextView timeTv;
    private Button finishBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);
        findViews();
        initViews();
        setListeners();
    }

    private void findViews(){
        levelTv=(TextView)findViewById(R.id.level_tv);
        timeTv=(TextView)findViewById(R.id.time_tv);
        finishBtn=(Button)findViewById(R.id.finish_btn);
    }

    private void initViews(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        int level=bundle.getInt("level");
        String time=bundle.getString("time");
        levelTv.setText("难度等级："+ Util.getGameLevelText(level));
        timeTv.setText("游戏用时："+time);
    }

    private void setListeners(){
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
