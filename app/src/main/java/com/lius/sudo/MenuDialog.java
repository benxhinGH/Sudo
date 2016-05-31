package com.lius.sudo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;

/**
 * Created by lius on 16-5-31.
 */
public class MenuDialog extends Dialog{

    private AnimationSet animIn,animOut;
    private View mDialogView;
    private Button completeButton,resetButton,saveButton,quitButton;
    public MenuDialog(Context context) {
        super(context,R.style.color_dialog);
        init();
    }
    private void init(){
        animIn=AnimationLoader.getInAnimation(getContext());
        animOut=AnimationLoader.getOutAnimation(getContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDialogView.startAnimation(animIn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogView=getWindow().getDecorView().findViewById(android.R.id.content);

        setContentView(R.layout.menu_layout);
        findViews();
        setListeners();

    }
    private void findViews(){
        completeButton=(Button)findViewById(R.id.complete_button);
        resetButton=(Button)findViewById(R.id.reset_button);
        saveButton=(Button)findViewById(R.id.save_button);
        quitButton=(Button)findViewById(R.id.quit_button);
    }
    private void setListeners(){
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.DIALOGFLAG=MainActivity.COMPLETE;
                mDialogView.startAnimation(animOut);

            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity.DIALOGFLAG=MainActivity.RESET;
                mDialogView.startAnimation(animOut);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.DIALOGFLAG=MainActivity.SAVE;
                mDialogView.startAnimation(animOut);
            }
        });
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.DIALOGFLAG=MainActivity.QUIT;
                mDialogView.startAnimation(animOut);
            }
        });
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                callDismiss();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void callDismiss(){
        dismiss();
    }
}
