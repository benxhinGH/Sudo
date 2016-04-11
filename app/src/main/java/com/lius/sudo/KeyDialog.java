package com.lius.sudo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Administrator on 2016/4/7 0007.
 */
public class KeyDialog extends Dialog {

    private final View keys[]=new View[10];

    private SudoView sudoView;

    public KeyDialog(Context context,SudoView sudoView){
        super(context);
        this.sudoView=sudoView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("KeyDialog");
        setContentView(R.layout.keypad);
        findViews();
        setListeners();
    }


    private void findViews(){
        keys[0]=findViewById(R.id.keypad_1);
        keys[1]=findViewById(R.id.keypad_2);
        keys[2]=findViewById(R.id.keypad_3);
        keys[3]=findViewById(R.id.keypad_4);
        keys[4]=findViewById(R.id.keypad_5);
        keys[5]=findViewById(R.id.keypad_6);
        keys[6]=findViewById(R.id.keypad_7);
        keys[7]=findViewById(R.id.keypad_8);
        keys[8]=findViewById(R.id.keypad_9);
        keys[9]=findViewById(R.id.clear);


    }

    private void returnResult(int num){
        sudoView.setSelectedNum(num);
        dismiss();
    }

    private void setListeners(){
        for(int i=0;i<keys.length;++i){
            final int t=i+1;
            keys[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnResult(t);
                }
            });
        }
    }
}
