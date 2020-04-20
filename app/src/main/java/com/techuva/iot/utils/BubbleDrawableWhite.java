package com.techuva.iot.utils;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class BubbleDrawableWhite extends Drawable {

    public static final int LEFT = 0;
    public static final int CENTER = 1;
    public static final int RIGHT = 2;

    private Paint mPaint;
    private int mColor;

    private RectF mBoxRect;
    private int mBoxWidth;
    private int mBoxHeight;
    private float mCornerRad;
    private Rect mBoxPadding = new Rect();

    private Path mPointer;
    private int mPointerWidth;
    private int mPointerHeight;
    private int mPointerAlignment;


    public BubbleDrawableWhite(int pointerAlignment) {
        setPointerAlignment(pointerAlignment);
        initBubble();
    }

    public void setPadding(int left, int top, int right, int bottom) {
        mBoxPadding.left = left;
        mBoxPadding.top = top;
        mBoxPadding.right = right;
        mBoxPadding.bottom = bottom;
    }

    public void setCornerRadius(float cornerRad) {
        mCornerRad = cornerRad;
    }

    public void setPointerAlignment(int pointerAlignment) {
        if (pointerAlignment < 0 || pointerAlignment > 3) {
            Log.e("BubbleDrawable", "Invalid pointerAlignment argument");
        } else {
            mPointerAlignment = pointerAlignment;
        }
    }

    public void setPointerWidth(int pointerWidth) {
        mPointerWidth = pointerWidth;
    }

    public void setPointerHeight(int pointerHeight) {
        mPointerHeight = pointerHeight;
    }

    // Private Methods
    ////////////////////////////////////////////////////////////

    private void initBubble() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mColor = Color.WHITE;
        mPaint.setColor(mColor);
        mCornerRad = 0;
        setPointerWidth(30);
        setPointerHeight(30);
    }

    private void updatePointerPath() {
        mPointer = new Path();
        mPointer.setFillType(Path.FillType.EVEN_ODD);

        // Set the starting point
        mPointer.moveTo(-(mBoxWidth+mPointerWidth), pointerHorizontalStart()-mPointerHeight);

        // Define the lines
        mPointer.rLineTo(-50, mPointerWidth);

        /*mPointer.rLineTo(-(mPointerWidth / 2), mPointerHeight);
        mPointer.rLineTo(-(mPointerWidth / 2), -mPointerHeight);*/

        mPointer.rLineTo(-(mBoxWidth+mPointerWidth+20) , (mPointerHeight/2));
        mPointer.rLineTo(-(mBoxWidth+mPointerWidth+20), -(mPointerHeight/2));
        mPointer.close();
    }

    private float pointerHorizontalStart() {
        float x = 0;
        switch (mPointerAlignment) {
            case LEFT:
                x = mCornerRad;
                break;
            case CENTER:
                x = (mBoxHeight / 2);
                break;
            case RIGHT:
                x = mBoxWidth - mCornerRad - mPointerWidth;
        }
        return x;
    }

    // Superclass Override Methods
    ////////////////////////////////////////////////////////////

    @Override
    public void draw(Canvas canvas) {
        mBoxRect = new RectF(0.0f, 0.0f, mBoxWidth, mBoxHeight);
        canvas.drawRoundRect(mBoxRect, mCornerRad, mCornerRad, mPaint);
        updatePointerPath();
        canvas.drawPath(mPointer, mPaint);
    }

    @SuppressLint("WrongConstant")
    @Override
    public int getOpacity() {
        return 255;
    }

    @Override
    public void setAlpha(int alpha) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean getPadding(Rect padding) {
        padding.set(mBoxPadding);
        return true;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        mBoxWidth = bounds.width() + mPointerWidth;
        mBoxHeight = getBounds().height();
        super.onBoundsChange(bounds);
    }
}