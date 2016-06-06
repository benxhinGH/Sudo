package com.lius.sudo.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lius.sudo.tools.AnimationLoader;
import com.lius.sudo.tools.DisplayUtil;
import com.lius.sudo.R;


/**
 * Created by lius on 16-5-29.
 */
public class LDialog extends Dialog{

    final int SHOW_LOADING_DIALOG=0;
    final int CLOSE_LOADING_DIALOG=1;


    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int DIALOG_TYPE_SUCCESS=0;
    private static final int DIALOG_TYPE_WRONG=1;
    private static final int LEFT=0;
    private static final int RIGHT=1;
    private int DISMISSFLAG=0;
    private int dialogType=0;
    private static final int DEFAULT_RADIUS     = 6;
    private AnimationSet animIn,animOut;
    private View mDialogView;
    private TextView mContentTv,leftButton,rightButton;
    private Context mContext;
    private LocalBroadcastManager localBroadcastManager;

    public LDialog(Context context,int dialogType) {
        //this(context,0);
        super(context, R.style.color_dialog);
        this.dialogType=dialogType;
        init();
        mContext=context;
    }
    //public LDialog(Context context, int theme){
    //    super(context,R.style.color_dialog);
     //   init();

    //}

    @Override
    protected void onStart() {
        super.onStart();
        mDialogView.startAnimation(animIn);
    }

    //获取加载动画
    private void init(){
        animIn= AnimationLoader.getInAnimation(getContext());
        animOut=AnimationLoader.getOutAnimation(getContext());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DISMISSFLAG=0;
        localBroadcastManager=LocalBroadcastManager.getInstance(mContext);
        initView();
    }
    private void initView(){
        View contentView=View.inflate(getContext(),R.layout.ldialog_layout,null);
        setContentView(contentView);
        resizeDialog();

        //获取各控件实例
        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mContentTv = (TextView) contentView.findViewById(R.id.tvContent);
        leftButton=(TextView)findViewById(R.id.leftbutton);
        rightButton=(TextView)findViewById(R.id.rightbutton);
        View llBtnGroup = findViewById(R.id.llBtnGroup);
        ImageView logoIv = (ImageView) contentView.findViewById(R.id.logoIv);
        logoIv.setBackgroundResource(getIconByType(dialogType));

        LinearLayout topLayout = (LinearLayout) contentView.findViewById(R.id.topLayout);
        ImageView triangleIv = new ImageView(getContext());
        triangleIv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(getContext(), 10)));
        triangleIv.setImageBitmap(createTriangel((int) (DisplayUtil.getScreenSize(getContext()).x * 0.7), DisplayUtil.dp2px(getContext(), 10)));
        topLayout.addView(triangleIv);


        setBtnBackground(leftButton);
        setBtnBackground(rightButton);
        setBottomCorners(llBtnGroup);



        int radius = DisplayUtil.dp2px(getContext(), DEFAULT_RADIUS);
        float[] outerRadii = new float[] { radius, radius, radius, radius, 0, 0, 0, 0 };
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setColor(getContext().getResources().getColor(R.color.light_purple));
        LinearLayout llTop = (LinearLayout) findViewById(R.id.llTop);
        llTop.setBackgroundDrawable(shapeDrawable);

        //mTitleTv.setText("this's title");
        mContentTv.setText(getContentTextByType(dialogType));
        //mPositiveBtn.setText("ok");
        //mPositiveBtn.setOnClickListener(new View.OnClickListener() {
        //    @Override
         //   public void onClick(View view) {
         //       mDialogView.startAnimation(animOut);
         //   }
       // });
        leftButton.setText(getButtonTextByType(dialogType,LEFT));
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogView.startAnimation(animOut);
                DISMISSFLAG=1;

            }
        });
        rightButton.setText(getButtonTextByType(dialogType,RIGHT));
        setRightButtonListener();
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                callDismiss();
                Log.d("LDialog","打电话callDismiss打电话callDismiss打电话callDismiss打电话callDismiss");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void resizeDialog() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int)(DisplayUtil.getScreenSize(getContext()).x * 0.7);
        getWindow().setAttributes(params);
        //大概意思应该是说把当前窗口的宽度设置为设备屏幕宽度的0.7倍吧...
    }
    private Bitmap createTriangel(int width, int height) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        return getBitmap(width, height, R.color.light_purple);
    }

    private Bitmap getBitmap(int width, int height, int backgroundColor) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getContext().getResources().getColor(R.color.light_purple));
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(width / 2, height);
        path.lineTo(width, 0);


        path.close();

        canvas.drawPath(path, paint);
        return bitmap;

    }
    //设置TextView按钮的边框
    private void setBtnBackground(final TextView btnOk) {
        btnOk.setTextColor(createColorStateList(R.color.light_purple, R.color.color_dialog_gray));
        btnOk.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.sel_btn));
    }
    private ColorStateList createColorStateList(int normal, int pressed) {
        return createColorStateList(normal, pressed, Color.BLACK, Color.BLACK);
    }

    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[] { pressed, focused, normal, focused, unable, normal };
        int[][] states = new int[6][];
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        states[2] = new int[] { android.R.attr.state_enabled };
        states[3] = new int[] { android.R.attr.state_focused };
        states[4] = new int[] { android.R.attr.state_window_focused };
        states[5] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }
    //设置对话框下面两个角的弧度
    private void setBottomCorners(View llBtnGroup) {
        int radius = DisplayUtil.dp2px(getContext(), DEFAULT_RADIUS);
        float[] outerRadii = new float[] { 0, 0, 0, 0, radius, radius, radius, radius };
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.WHITE);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        llBtnGroup.setBackgroundDrawable(shapeDrawable);
    }
    private void callDismiss(){
        super.dismiss();
        switch(DISMISSFLAG){
            case 1:
                Intent intent=new Intent("com.lius.sudo.FINISH");
                localBroadcastManager.sendBroadcast(intent);
                Log.d("LDialog","发出FINISH广播发出FINISH广播发出FINISH广播发出FINISH广播发出FINISH广播");
                break;
            case 2:
                Intent intent2=new Intent("com.lius.sudo.GENERATESUDOKU");
                localBroadcastManager.sendBroadcast(intent2);
                Log.d("LDialog","发出GENERATESUDOKU广播发出GENERATESUDOKU广播发出GENERATESUDOKU广播");
                break;
            default:
                break;

        }
    }
    /*private int getColorByType(int type){
        switch (type){
            case DIALOG_TYPE_SUCCESS:
                return R.color.color_type_success;
            case DIALOG_TYPE_WRONG:
                return R.color.color_type_wrong;
            default:
                break;
        }
        return R.color.color_type_success;

    }*/
    private int getIconByType(int type){
        switch (type){
            case DIALOG_TYPE_SUCCESS:
                return R.drawable.ic_success;
            case DIALOG_TYPE_WRONG:
                return R.drawable.ic_wrong;
            default:
                break;
        }
        return R.drawable.ic_success;
    }
    private String getContentTextByType(int type){
        switch (type){
            case DIALOG_TYPE_SUCCESS:
                return "解题成功！";
            case DIALOG_TYPE_WRONG:
                return "存在错误！";
            default:
                break;
        }
        return "????";
    }
    private String getButtonTextByType(int type,int leftOrRight){
        switch (type){
            case DIALOG_TYPE_SUCCESS:
                if(leftOrRight==LEFT)return "返回主界面";
                else if(leftOrRight==RIGHT)return "继续游戏";
                break;
            case DIALOG_TYPE_WRONG:
                if(leftOrRight==LEFT)return "返回主界面";
                else if(leftOrRight==RIGHT)return "返回游戏";
                break;
            default:
                break;
        }
        return "????";
    }
    private void setRightButtonListener(){
        if(dialogType==DIALOG_TYPE_SUCCESS){
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialogView.startAnimation(animOut);
                    DISMISSFLAG=2;

                }
            });
        }else if(dialogType==DIALOG_TYPE_WRONG){
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialogView.startAnimation(animOut);

                }
            });
        }
    }



    /*private void initAnimListener() {
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDialogView.post(new Runnable() {
                    @Override
                    public void run() {
                        callDismiss();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void initListener() {
        mPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnPositiveListener != null) {
                    mOnPositiveListener.onClick(PromptDialog.this);
                }
            }
        });

        initAnimListener();
    }*/
}
