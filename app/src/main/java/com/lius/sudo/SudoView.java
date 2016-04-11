package com.lius.sudo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class SudoView extends View{

    private float width;
    private float height;
    private int selectedX;
    private int selectedY;
    private Context mContext;
    Game game=new Game();
    public SudoView(Context context){
        super(context);
        mContext=context;

    }
    public SudoView(Context context,AttributeSet attrs){
        super(context,attrs);
        mContext=context;
    }
    @Override
    protected void onSizeChanged(int w,int h,int oldw,int oldh){
        this.width=w/9f;
        this.height=h/9f;
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onDraw(Canvas canvas){
        Paint backgroundPaint=new Paint();
        backgroundPaint.setColor(getResources().getColor(R.color.sudoBackground));
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);

        Paint darkPaint=new Paint();
        darkPaint.setColor(getResources().getColor(R.color.sudoDark));

        Paint whitePaint=new Paint();
        whitePaint.setColor(getResources().getColor(R.color.sudoWhite));

        Paint lightPaint=new Paint();
        lightPaint.setColor(getResources().getColor(R.color.sudoLight));
        for(int i=0;i<9;++i){
            canvas.drawLine(0,i*height,getWidth(),i*height,lightPaint);
            canvas.drawLine(0,i*height+1,getWidth(),i*height+1,whitePaint);
            canvas.drawLine(i*width,0,i*width,getHeight(),lightPaint);
            canvas.drawLine(i*width+1,0,i*width+1,getHeight(),whitePaint);
        }
        for(int i=0;i<9;++i){
            if(i%3!=0)continue;
            canvas.drawLine(0,i*height,getWidth(),i*height,darkPaint);
            canvas.drawLine(0,i*height+1,getWidth(),i*height+1,whitePaint);
            canvas.drawLine(i*width,0,i*width,getHeight(),darkPaint);
            canvas.drawLine(i*width+1,0,i*width+1,getHeight(),whitePaint);
        }
        Paint numberPaint=new Paint();
        numberPaint.setColor(Color.BLACK);
        numberPaint.setStyle(Paint.Style.STROKE);
        numberPaint.setTextSize(height * 0.75f);
        numberPaint.setTextAlign(Paint.Align.CENTER);

        //使用Paint获取FontMetrics对象，下面计算y的公式是使字符在单元格中居中的算法
        Paint.FontMetrics fm=numberPaint.getFontMetrics();
        float x=width/2;
        float y=height/2-(fm.ascent+fm.descent)/2;

        for(int i=0;i<9;++i)
            for(int j=0;j<9;++j)
                canvas.drawText(game.getNumberString(i,j),i*width+x,j*height+y,numberPaint);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()!=MotionEvent.ACTION_DOWN){
            return super.onTouchEvent(event);
        }
        selectedX=(int)(event.getX()/width);
        selectedY=(int)(event.getY()/height);

        if(!game.ifIsDefault(selectedX,selectedY)){
            KeyDialog keyDialog=new KeyDialog(getContext(),this);
            keyDialog.show();
        }


        return true;



    }

    public void setSelectedNum(int num){
        game.setNum(selectedX,selectedY,num);
        invalidate();
    }


}
