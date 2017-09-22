package com.lius.sudo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lius.sudo.R;
import com.lius.sudo.model.SudokuNumber;
import com.lius.sudo.utilities.Util;

/**
 * Created by UsielLau on 2017/9/14 0014 21:36.
 */

public class SudokuView extends View{

    private Canvas canvas;

    //是否绘制选中格子提示
    private boolean drawCellPrompt=false;

    private float cellWidth;
    private float cellHeight;

    private float viewWidth;
    private float viewHeight;

    //view在当前屏幕中的绝对坐标
    private int[] viewAbsCoordinate;

    private int cellX,cellY;

    private Paint backgroundPaint=new Paint();
    private Paint linePaint=new Paint();
    private Paint numberPaint=new Paint();
    private Paint promptPaint=new Paint();

    private Context context;

    private OnClickCellListener mOnClickCellListener;

    private NumberSelecter numberSelecter;

    private WindowManager windowManager;


    private SudokuNumber[][] sudokuData;


    public SudokuView(Context context) {
        super(context);
        this.context=context;
    }

    public SudokuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public SudokuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }





    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        cellWidth=w/9f;
        cellHeight=h/9f;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas=canvas;
        drawBackground(canvas);
        drawNumbers(canvas);
        if(drawCellPrompt){
            drawSelectedCellPrompt(cellX,cellY);
        }
        super.onDraw(canvas);
        viewWidth=getWidth();
        viewHeight=getHeight();
    }

    private void drawBackground(Canvas canvas){
        //绘制背景色
        backgroundPaint.setColor(getResources().getColor(R.color.white));
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);
        //绘制网格线
        linePaint.setColor(getResources().getColor(R.color.purple));
        linePaint.setStrokeWidth(4);
        //先绘制细网格线
        for(int i=0;i<10;++i){
            canvas.drawLine(0,i*cellHeight,getWidth(),i*cellHeight,linePaint);
            canvas.drawLine(i*cellWidth,0,i*cellWidth,getHeight(),linePaint);
        }
        //然后绘制粗网格线
        linePaint.setStrokeWidth(10);
        for(int i=1;i<9;++i){
            if(i%3!=0)continue;
            canvas.drawLine(0,i*cellHeight,getWidth(),i*cellHeight,linePaint);
            canvas.drawLine(i*cellWidth,0,i*cellWidth,getHeight(),linePaint);
        }
    }

    private void drawNumbers(Canvas canvas){
        if(sudokuData==null)return;
        numberPaint.setColor(getResources().getColor(R.color.purple));
        numberPaint.setStyle(Paint.Style.STROKE);
        numberPaint.setTextSize(cellHeight * 0.45f);
        numberPaint.setTextAlign(Paint.Align.CENTER);
        numberPaint.setAntiAlias(true);

        //使用Paint获取FontMetrics对象，下面计算y的公式是使字符在单元格中居中的算法
        Paint.FontMetrics fm=numberPaint.getFontMetrics();
        float x=cellWidth/2;
        float y=cellHeight/2-(fm.ascent+fm.descent)/2;

        for(int i=0;i<9;++i)
            for(int j=0;j<9;j++){
                if(sudokuData[i][j].getValue()<1||sudokuData[i][j].getValue()>9)continue;
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
                canvas.drawText(String.valueOf(sudokuData[i][j].getValue()),j*cellWidth+x,i*cellHeight+y,numberPaint);
            }

    }

    private void drawSelectedCellPrompt(int cellX,int cellY){
        promptPaint.setColor(getResources().getColor(R.color.purple));
        promptPaint.setAlpha(100);
        float left=cellX*cellWidth;
        float top=cellY*cellHeight;
        float right=left+cellWidth;
        float bottom=top+cellHeight;
        canvas.drawRect(left,top,right,bottom,promptPaint);
    }

    public void setPuzzleData(SudokuNumber[][] sudokuData){
        this.sudokuData=sudokuData;
        invalidate();
    }

    public void putNumber(int row, int column, int number){
        if(sudokuData==null)return;
        if(row<0||row>8)return;
        if(column<0||column>8)return;
        if(number<0||number>9)return;
        if(sudokuData[row][column]==null)sudokuData[row][column]=new SudokuNumber(0,null);
        sudokuData[row][column].setValue(number);
        sudokuData[row][column].setType(SudokuNumber.NumberType.USER);

        invalidate();
    }

    public void removeNumber(int row,int column){
        putNumber(row,column,0);
    }

    private WindowManager getWindowManager(){
        if(windowManager==null){
            windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }

    private void showNumberSelecter(float startX,float startY,float endX,float endY){
        if(numberSelecter==null){
            numberSelecter=new NumberSelecter(context);
        }
        numberSelecter.setStartX(startX);
        numberSelecter.setStartY(startY);
        numberSelecter.setEndX(endX);
        numberSelecter.setEndY(endY);
        WindowManager windowManager=getWindowManager();
        int screenWidth=windowManager.getDefaultDisplay().getWidth();
        int screenHeight=windowManager.getDefaultDisplay().getHeight();
        WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();
        layoutParams.x=0;
        layoutParams.y=0;
        layoutParams.type= WindowManager.LayoutParams.TYPE_PHONE;
        layoutParams.format= PixelFormat.RGBA_8888;
        layoutParams.gravity= Gravity.LEFT|Gravity.TOP;
        layoutParams.width=screenWidth;
        layoutParams.height=screenHeight;
        windowManager.addView(numberSelecter,layoutParams);
    }

    private void removeNumberSelecter(){
        getWindowManager().removeView(numberSelecter);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                cellX=(int)(event.getX()/cellWidth);
                cellY=(int)(event.getY()/cellHeight);
                //当前view的绝对坐标
                int[] abs=getSudokuViewAbsCoordinate();
                if(sudokuData[cellY][cellX].getType()!= SudokuNumber.NumberType.DEFAULT){
                    drawCellPrompt=true;
                    invalidate();
                    showNumberSelecter(viewWidth/2+abs[0],viewHeight/2+abs[1],event.getRawX(),event.getRawY());
                }else {
                    //如果点击的是默认数字的格子，则返回false，不再接受事件序列剩下的事件
                    return false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                numberSelecter.endChanged(event.getRawX(),event.getRawY());
                break;
            case MotionEvent.ACTION_UP:
                drawCellPrompt=false;
                removeNumberSelecter();
                int number=numberSelecter.getCurrentSelectedNumber();
                putNumber(cellY,cellX,number);

                break;
        }

        return true;
    }

    private int[] getSudokuViewAbsCoordinate(){
        if(viewAbsCoordinate==null){
            viewAbsCoordinate=new int[2];
            getLocationOnScreen(viewAbsCoordinate);
        }
        return viewAbsCoordinate;
    }

    public SudokuNumber[][] getSudokuData(){
        return sudokuData;
    }

    public void setOnClickCellListener(OnClickCellListener onClickCellListener){
        mOnClickCellListener=onClickCellListener;
    }

    public interface OnClickCellListener{
        void onClick(int cellX,int cellY);
    }


}

