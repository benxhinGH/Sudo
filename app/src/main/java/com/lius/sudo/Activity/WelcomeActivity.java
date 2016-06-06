package com.lius.sudo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.lius.sudo.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lius on 16-6-5.
 */
public class WelcomeActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_layout);
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(WelcomeActivity.this,StartActivity.class);
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(task,1000*2);

    }
}
