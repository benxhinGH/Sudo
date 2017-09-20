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
import com.lius.sudo.view.SudokuView;

/**
 * Created by UsielLau on 2017/9/20 0020 17:40.
 */

public class GameFailedDialog extends ColorDialogBase {

    private Context context;

    private OnClickDialogBtnListener positiveListener;
    private OnClickDialogBtnListener negativeListener;

    private int defaultBtnMargin=5;


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
        Button positiveBtn=new Button(context);
        positiveBtn.setText("知道了");
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(positiveListener!=null){
                    positiveListener.onClick();
                }
            }
        });
        LinearLayout.LayoutParams positiveParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        positiveParams.weight=1;
        int margin=Util.dp2px(context,defaultBtnMargin);
        positiveParams.setMargins(margin,margin,margin,margin);
        positiveBtn.setLayoutParams(positiveParams);

        Button negativeBtn=new Button(context);
        negativeBtn.setText("不玩了");
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(negativeListener!=null){
                    negativeListener.onClick();
                }
            }
        });
        LinearLayout.LayoutParams negativeParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        negativeParams.weight=1;
        negativeParams.setMargins(margin,margin,margin,margin);
        negativeBtn.setLayoutParams(negativeParams);

        btnGroupLayout.addView(positiveBtn);
        btnGroupLayout.addView(negativeBtn);


    }

    @Override
    int getDialogColor() {
        return context.getResources().getColor(R.color.red);
    }

    public void setPositiveListener(OnClickDialogBtnListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    public void setNegativeListener(OnClickDialogBtnListener negativeListener) {
        this.negativeListener = negativeListener;
    }
}
