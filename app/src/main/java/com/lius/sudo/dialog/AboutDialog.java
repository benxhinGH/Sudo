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
 * Created by UsielLau on 2017/9/22 0022 15:19.
 */

public class AboutDialog extends ColorDialogBase {

    private Context context;

    private OnClickDialogBtnListener onClickDialogBtnListener;


    public AboutDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    void setLogoIv(ImageView logoIv) {
        logoIv.setImageResource(R.drawable.ic_help);
    }

    @Override
    void addButtons(LinearLayout btnGroupLayout) {
        Button button=getDefaultButton();
        button.setText("ok");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickDialogBtnListener!=null){
                    onClickDialogBtnListener.onClick();
                }
            }
        });
        btnGroupLayout.addView(button);
    }

    public void setOnClickDialogBtnListener(OnClickDialogBtnListener onClickDialogBtnListener) {
        this.onClickDialogBtnListener = onClickDialogBtnListener;
    }

    @Override
    int getDialogColor() {
        return context.getResources().getColor(R.color.color_type_success);
    }

    @Override
    boolean setContentView(RelativeLayout contentLayout) {
        return false;
    }
}
