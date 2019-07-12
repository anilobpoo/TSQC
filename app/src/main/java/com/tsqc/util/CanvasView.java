package com.tsqc.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.tsqc.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CanvasView extends View {

    private Paint mPaint;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private android.graphics.Path mPath;
    private Paint mBitmapPaint;
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>();
    public static int selectedcolor;
    private Map<Path, Integer> colorsMap = new HashMap<Path, Integer>();

    public CanvasView(Context c, int width, int height) {
        super(c);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        mCanvas = new Canvas();
        mPath = new Path();
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        selectedcolor = getResources().getColor(R.color.QRCodeBlackColor);

    }

    public CanvasView(Context context, AttributeSet arr) {
        super(context, arr);

    }

    // ////////////////////////all color for brush/////////////////
    public void setPaintMode() {
        mPaint.setColor(0xFF000000);
        mPaint.setStrokeWidth(10);
    }

    public void set_PaintModetrans() {
        mPaint.setColor(0x00000000);
        mPaint.setStrokeWidth(10);
    }

    public void setPaintMode_violet() {
        mPaint.setColor(0xFF8B00FF);
        mPaint.setStrokeWidth(10);
        selectedcolor = getResources().getColor(R.color.colorPrimary);
    }

    public void setPaintMode_indigo() {
        mPaint.setColor(0xFF000066);
        mPaint.setStrokeWidth(10);
        selectedcolor = getResources().getColor(R.color.holo_blue_dark);
    }

    public void setPaintMode_blue() {
        mPaint.setColor(0xFF0000FF);
        mPaint.setStrokeWidth(10);
        selectedcolor = getResources().getColor(R.color.dot_dark_screen2);
    }

    public void setPaintMode_green() {
        mPaint.setColor(0xFF00FF00);
        mPaint.setStrokeWidth(10);
        selectedcolor = getResources().getColor(R.color.dot_dark_screen1);
    }

    public void setPaintMode_yellow() {
        mPaint.setColor(0xFFFFFF00);
        mPaint.setStrokeWidth(10);
        selectedcolor = getResources().getColor(R.color.dot_light_screen1);
    }



    // /////////////////////// all background color set code////////////
    public void setPaintMode_blackbg() {
        mCanvas.drawColor(0xFF000000);
    }

    public void setPaintMode_whitebg() {
        mCanvas.drawColor(0xFFFFFFFF);
    }

    public void setPaintMode_pinkbg() {
        mCanvas.drawColor(0xFFFF33CC);
    }

    public void setPaintMode_orangebg() {
        mCanvas.drawColor(0xFFFF7F00);
    }

    public void setPaintMode_yellowbg() {
        mCanvas.drawColor(0xFFFFFF00);
    }

    public void setPaintMode_greenbg() {
        mCanvas.drawColor(0xFF00FF00);
    }

    public void setPaintMode_bluebg() {
        mCanvas.drawColor(0xFF0000FF);

    }

    public void setPaintMode_indigobg() {
        mCanvas.drawColor(0xFF000066);

    }

    public void setPaintMode_violetbg() {
        mCanvas.drawColor(0xFF8B00FF);

    }

    // ////////////////////////////////////////////////////
    public void setEraseMode() {
        selectedcolor = getResources().getColor(R.color.QRCodeWhiteColor);
        mPaint.setColor(0xFFFFFFFF);
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        for (Path p : paths) {
            mPaint.setColor(colorsMap.get(p));
            canvas.drawPath(p, mPaint);
        }

        mPaint.setColor(selectedcolor);
        canvas.drawPath(mPath, mPaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 8;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;

    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        paths.add(mPath);
        colorsMap.put(mPath, selectedcolor);
        mPath = new Path();
        mPath.reset();
        invalidate();

    }

    public void eraseAll() {

        if (mPath != null) {
            paths.clear();
        }
        invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                // currentMoveList.add(mPath);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();

                invalidate();
                break;
        }
        return true;
    }

    public void resetcanvas() {
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    public void onClickUndo() {
        if (paths.size() > 0) {
            undonePaths.add(paths.remove(paths.size() - 1));
            invalidate();
        } else {

        }

    }

    public Bitmap getBitmap()
    {
        //this.measure(100, 100);
        //this.layout(0, 0, 100, 100);
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);


        return bmp;
    }

}