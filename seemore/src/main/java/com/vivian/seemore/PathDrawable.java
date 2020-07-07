package com.vivian.seemore;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 思路：写recyclerview加载更多的时候，想到虎嗅那个有一个贝塞尔曲线的动画，于是自己写一个
 * 因为尽量不要写到recyclerview里边，保持一个低耦合的状态
 * 所以这边是给see more的layout，加了一个background，通过在adapter中监听recyclerview的滑动状态，来更新path值
 */
public class PathDrawable extends Drawable {
    Path mPath;
    Paint mPaint;

    public PathDrawable() {
        mPaint = new Paint();
        mPaint.setColor(0xffe8e9e9);
        mPaint.setAntiAlias(true);
        mPath = new Path();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    int centerY;
    int x;

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        centerY = bounds.height() / 2;
        x = bounds.width();

        setPath(bounds, 0,bounds.right);
    }


    public void setPath(Rect bounds, int scrollX,int right) {
        mPath.reset();
        mPath.moveTo(right, 0);
        mPath.quadTo(-right, centerY, right , bounds.bottom);
        mPath.close();
    }

    public void setScrollX(int scrollX,int right) {
        setPath(getBounds(), scrollX,right);
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
