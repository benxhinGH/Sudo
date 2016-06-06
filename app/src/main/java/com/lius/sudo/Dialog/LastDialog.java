package com.lius.sudo.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
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
 * Created by lius on 16-6-5.
 */
public class LastDialog extends Dialog{


    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int DEFAULT_RADIUS     = 6;
    private Context context;
    private AnimationSet animIn,animOut;
    private View mDialogView;
    private TextView t1,t2,t3,t4,mPositiveBtn;
    public LastDialog(Context context) {
        super(context, R.style.color_dialog);
        this.context=context;
        init();
    }
    private void init(){
        animIn= AnimationLoader.getInAnimation(getContext());
        animOut=AnimationLoader.getOutAnimation(getContext());

    }
    @Override
    protected void onStart() {
        super.onStart();
        mDialogView.startAnimation(animIn);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    private void initView(){
        View contentView=View.inflate(getContext(),R.layout.lastdialog_layout,null);
        setContentView(contentView);
        resizeDialog();

        mDialogView = getWindow().getDecorView().findViewById(android.R.id.content);
        mPositiveBtn=(TextView)findViewById(R.id.btnPositive);
        t1 = (TextView) contentView.findViewById(R.id.t1);
        t2 = (TextView) contentView.findViewById(R.id.t2);
        t3 = (TextView) contentView.findViewById(R.id.t3);
        t4 = (TextView) contentView.findViewById(R.id.t4);
        View llBtnGroup = findViewById(R.id.llBtnGroup);
        ImageView logoIv = (ImageView) contentView.findViewById(R.id.logoIv);
        logoIv.setBackgroundResource(R.drawable.ic_help);

        LinearLayout topLayout = (LinearLayout) contentView.findViewById(R.id.topLayout);
        ImageView triangleIv = new ImageView(getContext());
        triangleIv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(getContext(), 10)));
        triangleIv.setImageBitmap(createTriangel((int) (DisplayUtil.getScreenSize(getContext()).x * 0.7), DisplayUtil.dp2px(getContext(), 10)));
        topLayout.addView(triangleIv);


        setBtnBackground(mPositiveBtn);
        setBottomCorners(llBtnGroup);



        int radius = DisplayUtil.dp2px(getContext(), DEFAULT_RADIUS);
        float[] outerRadii = new float[] { radius, radius, radius, radius, 0, 0, 0, 0 };
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setColor(getContext().getResources().getColor(R.color.light_purple));
        LinearLayout llTop = (LinearLayout) findViewById(R.id.llTop);
        llTop.setBackgroundDrawable(shapeDrawable);


        t1.setText("版本：1.0");
        t2.setText("制作：风雨夜未央");
        t3.setText("icon设计：会跑路的提百万" );
        t4.setText("QQ:824797605");

        mPositiveBtn.setText("好的");
        mPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
               mDialogView.startAnimation(animOut);
           }
        });

        animOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                callDismiss();

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
    private void setBottomCorners(View llBtnGroup) {
        int radius = DisplayUtil.dp2px(getContext(), DEFAULT_RADIUS);
        float[] outerRadii = new float[] { 0, 0, 0, 0, radius, radius, radius, radius };
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.WHITE);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        llBtnGroup.setBackgroundDrawable(shapeDrawable);
    }
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
    private void callDismiss(){
        dismiss();
    }

}
