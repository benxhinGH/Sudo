package com.lius.sudo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.lius.sudo.R;

/**
 * Created by UsielLau on 2017/9/14 0014 22:45.
 */

public class GameActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findViews();
        setSupportActionBar(toolbar);
    }

    private void findViews(){
        toolbar=(Toolbar)findViewById(R.id.toolbar);
    }
}
