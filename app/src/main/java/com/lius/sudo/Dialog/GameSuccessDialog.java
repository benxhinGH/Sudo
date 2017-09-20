package com.lius.sudo.Dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lius.sudo.R;
import com.lius.sudo.utilities.Util;

/**
 * Created by UsielLau on 2017/9/20 0020 17:29.
 */

public class GameSuccessDialog extends ColorDialogBase {

    private Context context;

    private OnClickDialogBtnListener positiveBtnListener;
    private OnClickDialogBtnListener negativeBtnListener;

    private int defaultBtnMargin=5;


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
        Button positiveBtn=new Button(context);
        positiveBtn.setText("是");
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(positiveBtnListener!=null){
                    positiveBtnListener.onClick();
                }
            }
        });
        LinearLayout.LayoutParams positiveParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin= Util.dp2px(context,defaultBtnMargin);
        positiveParams.setMargins(margin,margin,margin,margin);
        positiveParams.weight=1;
        positiveBtn.setLayoutParams(positiveParams);


        Button negativeBtn=new Button(context);
        negativeBtn.setText("否");
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(negativeBtnListener!=null){
                    negativeBtnListener.onClick();
                }
            }
        });
        LinearLayout.LayoutParams negativeParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        negativeParams.setMargins(margin,margin,margin,margin);
        negativeParams.weight=1;
        negativeBtn.setLayoutParams(negativeParams);
        btnGroupLayout.addView(positiveBtn);
        btnGroupLayout.addView(negativeBtn);
    }

    @Override
    int getDialogColor() {
        return context.getResources().getColor(R.color.green);
    }

    public void setPositiveBtnListener(OnClickDialogBtnListener positiveBtnListener) {
        this.positiveBtnListener = positiveBtnListener;
    }

    public void setNegativeBtnListener(OnClickDialogBtnListener negativeBtnListener) {
        this.negativeBtnListener = negativeBtnListener;
    }
}
