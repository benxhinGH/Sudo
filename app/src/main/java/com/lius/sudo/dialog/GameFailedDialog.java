package com.lius.sudo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lius.sudo.R;

/**
 * Created by UsielLau on 2017/9/20 0020 17:40.
 */

public class GameFailedDialog extends ColorDialogBase {

    private Context context;

    private OnClickDialogBtnListener positiveListener;
    private OnClickDialogBtnListener negativeListener;



    public GameFailedDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    void setLogoIv(ImageView logoIv) {
        logoIv.setImageResource(R.drawable.ic_wrong);
    }


    @Override
    void addButtons(LinearLayout btnGroupLayout) {
        Button positiveBtn=getDefaultButton();
        positiveBtn.setText("知道了");
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(positiveListener!=null){
                    positiveListener.onClick();
                }
            }
        });


        Button negativeBtn=getDefaultButton();
        negativeBtn.setText("不玩了");
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(negativeListener!=null){
                    negativeListener.onClick();
                }
            }
        });


        btnGroupLayout.addView(positiveBtn);
        btnGroupLayout.addView(negativeBtn);


    }

    @Override
    int getDialogColor() {
        return context.getResources().getColor(R.color.red);
    }

    @Override
    boolean setContentView(RelativeLayout contentLayout) {
        return false;
    }

    public void setPositiveListener(OnClickDialogBtnListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    public void setNegativeListener(OnClickDialogBtnListener negativeListener) {
        this.negativeListener = negativeListener;
    }
}
