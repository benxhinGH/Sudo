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
 * Created by UsielLau on 2017/9/20 0020 17:29.
 */

public class GameSuccessDialog extends ColorDialogBase {

    private Context context;

    private OnClickDialogBtnListener positiveBtnListener;
    private OnClickDialogBtnListener negativeBtnListener;


    public GameSuccessDialog(@NonNull Context context) {
        super(context);
        this.context=context;

    }

    @Override
    public void setMessageText(String msg) {
        super.setMessageText(msg);
        msgTv.setText("解题成功！是否加入排行榜？");
    }

    @Override
    void setLogoIv(ImageView logoIv) {
        logoIv.setImageResource(R.drawable.ic_success);
    }


    @Override
    void addButtons(LinearLayout btnGroupLayout) {
        Button positiveBtn=getDefaultButton();
        positiveBtn.setText("是");
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(positiveBtnListener!=null){
                    positiveBtnListener.onClick();
                }
            }
        });

        Button negativeBtn=getDefaultButton();
        negativeBtn.setText("否");
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(negativeBtnListener!=null){
                    negativeBtnListener.onClick();
                }
            }
        });

        btnGroupLayout.addView(positiveBtn);
        btnGroupLayout.addView(negativeBtn);
    }

    @Override
    int getDialogColor() {
        return context.getResources().getColor(R.color.green);
    }

    @Override
    boolean setContentView(RelativeLayout contentLayout) {
        return false;
    }

    public void setPositiveBtnListener(OnClickDialogBtnListener positiveBtnListener) {
        this.positiveBtnListener = positiveBtnListener;
    }

    public void setNegativeBtnListener(OnClickDialogBtnListener negativeBtnListener) {
        this.negativeBtnListener = negativeBtnListener;
    }
}
