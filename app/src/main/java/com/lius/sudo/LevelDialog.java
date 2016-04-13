package com.lius.sudo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2016/4/13 0013.
 */
public class LevelDialog extends Dialog{

    private Button[] levelButton=new Button[5];


    public LevelDialog(Context context){
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("levelDialog");
        setContentView(R.layout.leveldialog);
        findViews();
        setListeners();
    }

    private void findViews(){
        levelButton[0]=(Button)findViewById(R.id.level_1);
        levelButton[1]=(Button)findViewById(R.id.level_2);
        levelButton[2]=(Button)findViewById(R.id.level_3);
        levelButton[3]=(Button)findViewById(R.id.level_4);
        levelButton[4]=(Button)findViewById(R.id.level_5);
    }
    private void setListeners(){
        for(int i=0;i<5;++i){
            final int t=i+1;
            levelButton[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StartActivity.level=t;
                    dismiss();
                }
            });
        }
    }
}
