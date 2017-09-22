package com.lius.sudo.Dialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lius.sudo.R;

/**
 * Created by UsielLau on 2017/9/22 0022 12:00.
 */

public class GetUserNameDialog extends InquiryDialog {

    private Context context;

    private EditText userNameEt;

    private String userName;


    public GetUserNameDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    boolean setContentView(RelativeLayout contentLayout) {
        userNameEt=new EditText(context);
        userNameEt.setHint("请输入玩家昵称");
        userNameEt.setTextColor(Color.BLACK);
        userNameEt.setHintTextColor(Color.GRAY);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        userNameEt.setLayoutParams(params);
        userNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userName=s.toString();
            }
        });
        contentLayout.addView(userNameEt);
        return true;
    }

    public String getUserName(){
        return userName;
    }


}
