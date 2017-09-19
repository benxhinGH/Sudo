package com.lius.sudo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lius.sudo.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lius on 16-6-5.
 */
public class WelcomeActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(WelcomeActivity.this,MainInterfaceActivity.class);
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(task,1000);

    }
}
