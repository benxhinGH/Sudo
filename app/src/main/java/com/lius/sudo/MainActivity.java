package com.lius.sudo;

import android.app.Activity;
import android.os.Bundle;


/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SudoView(this));
    }
}
