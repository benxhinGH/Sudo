package com.lius.sudo.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;

import com.lius.sudo.tools.AnimationLoader;
import com.lius.sudo.R;
import com.lius.sudo.Activity.StartActivity;

/**
 * Created by Administrator on 2016/4/13 0013.
 */
public class LevelDialog extends Dialog{

    private AnimationSet animIn,animOut;
    private View mDialogView;

    private Button[] levelButton=new Button[5];


    public LevelDialog(Context context){
        this(context,0);
    }
    public LevelDialog(Context context,int theme){
        super(context, R.style.color_dialog);
        init();
    }
    //获取加载动画
    private void init(){
        animIn= AnimationLoader.getInAnimation(getContext());
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

        //initView();

        //setTitle("levelDialog");
        setContentView(R.layout.leveldialog);
        findViews();
        setListeners();

    }
    //private void initView(){
     //   View contentView=View.inflate()
    //}

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
                    mDialogView.startAnimation(animOut);
                }
            });
        }
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
