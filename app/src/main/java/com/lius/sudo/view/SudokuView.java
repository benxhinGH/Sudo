package com.lius.sudo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.lius.sudo.R;
import com.lius.sudo.model.SudokuNumber;

/**
 * Created by UsielLau on 2017/9/14 0014 21:36.
 */

public class SudokuView extends View{

    private float viewWidth;
    private float viewHeight;

    private Paint backgroundPaint=new Paint();
    private Paint linePaint=new Paint();
    private Paint numberPaint=new Paint();


    private SudokuNumber[][] sudokuData;


    public SudokuView(Context context) {
        super(context);
    }

    public SudokuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SudokuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        viewWidth=w/9f;
        viewHeight=h/9f;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawNumbers(canvas);
        super.onDraw(canvas);
    }

    private void drawBackground(Canvas canvas){
        //绘制背景色
        backgroundPaint.setColor(getResources().getColor(R.color.white));
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);
        //绘制网格线
        linePaint.setColor(getResources().getColor(R.color.purple));
        linePaint.setStrokeWidth(4);
        //先绘制细网格线
        for(int i=0;i<9;++i){
            canvas.drawLine(0,i*viewHeight,getWidth(),i*viewHeight,linePaint);
            canvas.drawLine(i*viewWidth,0,i*viewWidth,getHeight(),linePaint);
        }
        //然后绘制粗网格线
        linePaint.setStrokeWidth(20);
        for(int i=1;i<9;++i){
            if(i%3!=0)continue;
            canvas.drawLine(0,i*viewHeight,getWidth(),i*viewHeight,linePaint);
            canvas.drawLine(i*viewWidth,0,i*viewWidth,getHeight(),linePaint);
        }
    }

    private void drawNumbers(Canvas canvas){
        if(sudokuData==null)return;
        numberPaint.setColor(getResources().getColor(R.color.light_purple));
        numberPaint.setStyle(Paint.Style.STROKE);
        numberPaint.setTextSize(viewHeight * 0.45f);
        numberPaint.setTextAlign(Paint.Align.CENTER);
        numberPaint.setAntiAlias(true);

        //使用Paint获取FontMetrics对象，下面计算y的公式是使字符在单元格中居中的算法
        Paint.FontMetrics fm=numberPaint.getFontMetrics();
        float x=viewWidth/2;
        float y=viewHeight/2-(fm.ascent+fm.descent)/2;

        for(int i=0;i<9;++i)
            for(int j=0;j<9;j++){
                //根据数字类型调整画笔颜色
                switch (sudokuData[i][j].getType()){
                    case DEFAULT:
                        numberPaint.setColor(Color.BLACK);
                        break;
                    case USER:
                        numberPaint.setColor(getResources().getColor(R.color.light_purple));
                        break;
                    default:
                        break;
                }
                //绘制数字
                canvas.drawText(String.valueOf(sudokuData[i][j].getValue()),i*viewWidth+x,j*viewHeight+y,numberPaint);
            }

    }

    public void setPuzzleData(SudokuNumber[][] sudokuData){
        this.sudokuData=sudokuData;
    }

    public void putNumber(int row, int column, int number){
        if(sudokuData==null)return;
        if(row<1||row>9)return;
        if(column<1||column>9)return;
        if(number<1||number>9)return;
        if(sudokuData[row][column]==null)sudokuData[row][column]=new SudokuNumber(0,null);
        sudokuData[row][column].setValue(number);
        sudokuData[row][column].setType(SudokuNumber.NumberType.USER);
        invalidate();
    }

    public void removeNumber(int row,int column){
        putNumber(row,column,0);
    }


}
