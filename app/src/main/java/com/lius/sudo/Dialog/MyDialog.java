package com.lius.sudo.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lius.sudo.R;

/**
 * Created by 刘有泽 on 2016/8/16.
 */
public class MyDialog extends Dialog{

    private EditText editText;
    private Button positiveBtn;
    private Button negativeBtn;
    private Button ignoreBtn;
    private ReturnDataListener returnDataListener;

    public MyDialog(Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydialog_layout);
        setCancelable(false);

        initView();
        initEvent();
    }

    private void initView(){
        positiveBtn=(Button)findViewById(R.id.positive_btn);
        negativeBtn=(Button)findViewById(R.id.negative_btn);
        ignoreBtn=(Button)findViewById(R.id.ignore_btn);
        editText=(EditText)findViewById(R.id.player_et);
    }
    private void initEvent(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText.getText().length()==5)
                    Toast.makeText(getContext(),"最大长度为5个字符",Toast.LENGTH_SHORT).show();

            }
        });
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(returnDataListener!=null)
                    returnDataListener.returnData(editText.getText().toString());
                dismiss();

            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(returnDataListener!=null)
                    returnDataListener.returnData("取消");
                dismiss();
            }
        });
        ignoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(returnDataListener!=null)
                    returnDataListener.returnData("蜜汁玩家");
                dismiss();
            }
        });
    }
    public void setReturnDataListener(ReturnDataListener r){
        this.returnDataListener=r;
    }
    public interface ReturnDataListener{
        public void returnData(String data);
    }
}
