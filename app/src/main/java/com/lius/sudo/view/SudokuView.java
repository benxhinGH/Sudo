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

    private float cellWidth;
    private float cellHeight;

    //view在当前屏幕中的绝对坐标
    private int[] viewAbsCoordinate;

    private int cellX,cellY;

    private Paint backgroundPaint=new Paint();
    private Paint linePaint=new Paint();
    private Paint numberPaint=new Paint();

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
        numberPaint.setColor(getResources().getColor(R.color.light_purple));
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

    private void showNumberSelecter(float startX,float startY){
        if(numberSelecter==null){
            numberSelecter=new NumberSelecter(context);
        }
        numberSelecter.setStartX(startX);
        numberSelecter.setStartY(startY);
        numberSelecter.setEndX(startX);
        numberSelecter.setEndY(startY);
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
                float cellCenterX=cellX*cellWidth+cellWidth/2;
                float cellCenterY=cellY*cellWidth+cellHeight/2;
                if(sudokuData[cellY][cellX].getType()!= SudokuNumber.NumberType.DEFAULT){
                    showNumberSelecter(cellCenterX+abs[0],cellCenterY+abs[1]);
                }else {
                    //如果点击的是默认数字的格子，则返回false，不再接受事件序列剩下的事件
                    return false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                numberSelecter.endChanged(event.getRawX(),event.getRawY());
                break;
            case MotionEvent.ACTION_UP:
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
class NumberSelecter extends View {

    private Context context;

    private float startX;
    private float startY;

    private float endX;
    private float endY;
    //系统状态栏高度
    private int statusBarHeight;

    //默认文字所在圆的半径,单位dp
    private int defaultTextPathRadius=20;
    //默认文字所在圆的半径,单位px
    private int defaultTextPathRadiusPixel=-1;
    //默认圆环宽度，单位dp
    private int defaultRingWidth=15;
    //默认文字大小,单位sp
    private int defaultTextSize=10;


    private int currentSelectedNumber;

    public NumberSelecter(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public NumberSelecter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberSelecter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){
        statusBarHeight= Util.getStatusBarHeight(context);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint=new Paint();
        drawRing(canvas);
        drawNumber(canvas);
        //绘制中心圆点
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(startX,startY,Util.dp2px(context,2),paint);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(startX,startY,Util.dp2px(context,5),paint);
        //绘制线
        canvas.drawLine(startX,startY,endX,endY,paint);

        super.draw(canvas);
    }

    private void drawRing(Canvas canvas){
        //圆矩形轮廓
        int textPathRadius=dp2px(defaultTextPathRadius);
        RectF rectF=new RectF(startX-textPathRadius,startY-textPathRadius,
                startX+textPathRadius, startY+textPathRadius);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dp2px(defaultRingWidth));
        paint.setAlpha(100);
        canvas.drawArc(rectF,0,360,false,paint);

        //绘制选中部分扇形
        if(!fingerInSmallCircle()){
            paint.setAlpha(200);
            canvas.drawArc(rectF,-getSelectedArcAngle(),36,false,paint);
        }else{
            currentSelectedNumber=0;
        }

    }

    private boolean fingerInSmallCircle(){
        float tempX=endX-startX;
        float tempY=endY-startY;
        double dis=Math.sqrt(Math.pow(tempX,2)+Math.pow(tempY,2));
        if(dis<getDefaultTextPathRadiusInPixel())return true;
        return false;
    }

    private int getDefaultTextPathRadiusInPixel(){
        if(defaultTextPathRadiusPixel==-1){
            defaultTextPathRadiusPixel=Util.dp2px(context,defaultTextPathRadius);
        }
        return defaultTextPathRadiusPixel;
    }

    private void drawNumber(Canvas canvas){
        //绘制数字
        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(sp2px(defaultTextSize));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        float tempX,tempY;
        int currentNumber=4;
        int textPathRadius=dp2px(defaultTextPathRadius);
        for(int i=0;i<9;++i,currentNumber++){
            if(currentNumber==10)currentNumber=1;
            tempX=(float)(startX+textPathRadius*Math.cos(Math.toRadians(40*i+18)));
            tempY=(float)(startY+textPathRadius*Math.sin(Math.toRadians(40*i+18)));
            tempY+=10;
            canvas.drawText(String.valueOf(currentNumber),tempX,tempY,paint);
        }
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public void setStartY(float startY) {
        //减去状态栏的高度
        this.startY = startY-statusBarHeight;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public void setEndY(float endY) {
        //减去状态栏的高度
        this.endY = endY-statusBarHeight;
    }

    public void endChanged(float newEndX, float newEndY){
        endX=newEndX;
        //减去状态栏的高度
        endY=newEndY-statusBarHeight;
        invalidate();
    }

    public int getCurrentSelectedNumber() {
        return currentSelectedNumber;
    }

    private int getSelectedArcAngle(){
        //以起始点为原点，以平面直角坐标系为标准，计算拉线角度
        float tempY=endY-startY;
        float tempX=endX-startX;
        double tempRadian=Math.atan(Math.abs(tempY)/Math.abs(tempX));
        double tempAngle=Math.toDegrees(tempRadian);
        double angle=0.0;
        if(tempX>=0&&tempY>=0){
            //第四象限
            angle=360-tempAngle;
        }else if(tempX>=0&&tempY<=0){
            //第一象限
            angle=tempAngle;
        }else if(tempX<=0&&tempY>=0){
            //第三象限
            angle=tempAngle+180;
        }else if(tempX<=0&&tempY<=0){
            //第二象限
            angle=180-tempAngle;
        }
        int res=(int)angle/40;
        currentSelectedNumber=3-res;
        if(currentSelectedNumber<=0){
            currentSelectedNumber+=9;
        }
        return (res+1)*40;
    }

    private int dp2px(int dp){
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    private int sp2px(int sp){
        return (int)TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                getResources().getDisplayMetrics());
    }

}
