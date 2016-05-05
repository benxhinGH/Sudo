package com.lius.sudo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by lius on 16-5-5.
 */
public class LButton extends Button implements View.OnTouchListener{
    private int mButtonColor;
    private int mCornerRadius;
    private int mShadowColor;

    public LButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        customLButton();

    }

    public LButton(Context context) {
        super(context);
    }

    private void init(){
        Resources resources=getResources();
        mButtonColor=resources.getColor(R.color.lbutton_default_color);
        mCornerRadius=resources.getDimensionPixelSize(R.dimen.lbutton_default_cornerradius);
        mShadowColor=resources.getColor(R.color.lbutton_default_shadow_color);

    }
    private void customLButton(){
        StateListDrawable stateListDrawable=new StateListDrawable();
        stateListDrawable.addState(new int[]{-android.R.attr.state_pressed},createDrawable(mButtonColor,mCornerRadius));
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed},createDrawable(mShadowColor,mCornerRadius));
        this.setBackground(stateListDrawable);
    }


    private ShapeDrawable createDrawable(int color,int cornerRadius){
        float[] outerRadius=new float[]{cornerRadius,cornerRadius,cornerRadius,cornerRadius,
                cornerRadius,cornerRadius,cornerRadius,cornerRadius};
        RoundRectShape roundRectShape=new RoundRectShape(outerRadius,null,null);
        ShapeDrawable shapeDrawable=new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
