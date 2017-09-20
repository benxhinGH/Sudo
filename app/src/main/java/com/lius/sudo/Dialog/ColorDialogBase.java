package com.lius.sudo.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lius.sudo.R;
import com.lius.sudo.animation.AnimationLoader;
import com.lius.sudo.utilities.Util;

/**
 * Created by UsielLau on 2017/9/20 0020 15:46.
 */

public abstract class ColorDialogBase extends Dialog {

    private Context context;

    private View dialogView;

    private LinearLayout topLayout;
    private ImageView logoIv;
    private LinearLayout triangleLayout;
    protected TextView msgTv;
    private LinearLayout btnGroupLayout;

    /**
     * 对话框主题颜色,默认为蓝色
     */
    protected int dialogColor= R.color.red;

    /**
     * 对话框默认圆角弧度，单位dp
     */
    private int defaultRadius=6;

    private int defaultTriangleLayoutHeight=10;

    private Animation animIn;
    private Animation animOut;



    public ColorDialogBase(@NonNull Context context) {
        super(context,R.style.my_color_dialog);
        this.context=context;
        getAnimations();
    }

    public ColorDialogBase(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected ColorDialogBase(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateAndFindView();
        setContentView(dialogView);
        resizeDialog();
        setViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
        dialogView.startAnimation(animIn);
    }

    private void getAnimations(){
        animIn= AnimationLoader.getInAnimation(context);
        animOut=AnimationLoader.getOutAnimation(context);
    }

    private void inflateAndFindView(){
        dialogView= LayoutInflater.from(context).inflate(R.layout.dialog_color_base,null);
        topLayout=(LinearLayout)dialogView.findViewById(R.id.top_layout);
        logoIv=(ImageView)dialogView.findViewById(R.id.logo_iv);
        triangleLayout=(LinearLayout)dialogView.findViewById(R.id.triangle_layout);
        msgTv=(TextView)dialogView.findViewById(R.id.msg_tv);
        btnGroupLayout=(LinearLayout)dialogView.findViewById(R.id.btn_group_layout);
    }

    /**
     * 将对话框宽度设为屏幕的0.7倍
     */
    private void resizeDialog(){
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.width=(int)(Util.getScreenSize(context).x*0.7);
        getWindow().setAttributes(params);
    }

    private void setViews(){
        setLogoIv(logoIv);
        setTopLayoutBackground();
        setTriangleLayout();
        addButtons(btnGroupLayout);
        setBtnsBackground();
        setBtnGroupLayoutBackground();
    }

    private void setTopLayoutBackground(){
        GradientDrawable bg=(GradientDrawable)context.getResources().getDrawable(R.drawable.color_dialog_top_layout_bg);
        bg.setColor(getDialogColor());
        topLayout.setBackgroundResource(R.drawable.color_dialog_top_layout_bg);
    }

    abstract void setLogoIv(ImageView logoIv);


    private void setTriangleLayout(){
        BitmapDrawable bg=new BitmapDrawable(context.getResources(),createTriangleBitmapLayout());
        triangleLayout.setBackground(bg);
    }

    private Bitmap createTriangleBitmapLayout(){
        int width=getWindow().getAttributes().width;
        int height=Util.dp2px(context,defaultTriangleLayoutHeight);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getDialogColor());
        //画一个三角形
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(width / 2, height);
        path.lineTo(width, 0);
        path.close();

        canvas.drawPath(path, paint);
        return bitmap;
    }

    private void setBtnsBackground(){
        int btnNum=btnGroupLayout.getChildCount();
        Button tempBtn;
        for(int i=0;i<btnNum;++i){
            tempBtn=(Button)btnGroupLayout.getChildAt(i);
            tempBtn.setTextColor(createBtnTextColorStateList());
            tempBtn.setBackground(createBtnBg());
        }
    }

    private StateListDrawable createBtnBg(){
        StateListDrawable bg=new StateListDrawable();
        GradientDrawable pressedShape=(GradientDrawable)context.getResources().getDrawable(R.drawable.dialog_color_btn_pressed_bg);
        bg.addState(new int[]{android.R.attr.state_pressed},pressedShape);
        ShapeDrawable normalShape=getBtnBg();
        bg.addState(new int[]{},normalShape);
        return bg;
    }

    private ShapeDrawable getBtnBg(){
        int radius = Util.dp2px(context,defaultRadius);
        float[] outerRadii = new float[] { radius, radius, radius, radius, radius, radius, radius, radius };
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setAntiAlias(true);
        shapeDrawable.getPaint().setStyle(Paint.Style.STROKE);
        shapeDrawable.getPaint().setColor(getDialogColor());
        return shapeDrawable;
    }

    private ColorStateList createBtnTextColorStateList(){
        int[] colors=new int[]{R.color.gray,getDialogColor()};
        int[][] states=new int[2][];
        states[0]=new int[]{android.R.attr.state_pressed};
        states[1]=new int[]{};
        ColorStateList colorStateList=new ColorStateList(states,colors);
        return colorStateList;
    }

    private void setBtnGroupLayoutBackground(){
        GradientDrawable bg=(GradientDrawable)context.getResources().getDrawable(R.drawable.dialog_color_btn_group_bg);
        btnGroupLayout.setBackground(bg);
    }

    public void setMessageText(String msg){
        msgTv.setText(msg);
    }

    /**
     * 由用户实现，向dialog下方的线性布局中添加按钮
     * @param btnGroupLayout
     */
    abstract void addButtons(LinearLayout btnGroupLayout);

    abstract int getDialogColor();


    @Override
    public void dismiss() {
        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mDismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        dialogView.startAnimation(animOut);
    }
    private void mDismiss(){
        super.dismiss();
    }
}
