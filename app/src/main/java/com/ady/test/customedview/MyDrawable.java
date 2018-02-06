package com.ady.test.customedview;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 使用 BitmapShader 实现自定义圆角
 * Created by zhouxinyuan on 2018/1/20.
 */

public class MyDrawable extends Drawable {

  Bitmap mBitmap;
  Paint mPaint;
  RectF mRectF;

  MyDrawable(Bitmap bitmap) {
    this.mBitmap = bitmap;
    BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setShader(shader);
  }


  @Override
  public void draw(@NonNull Canvas canvas) {
    Rect rect = getBounds();
    canvas.drawRoundRect(mRectF, 100, 100, mPaint);
  }

  @Override
  public void setAlpha(int alpha) {
    mPaint.setAlpha(alpha);
  }

  @Override
  public void setColorFilter(@Nullable ColorFilter colorFilter) {
    mPaint.setColorFilter(colorFilter);
  }

  @Override
  public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  @Override
  public int getIntrinsicHeight() {
    return mBitmap.getHeight();
  }

  @Override
  public int getIntrinsicWidth() {
    return mBitmap.getWidth();
  }

  @Override
  public void setBounds(int left, int top, int right, int bottom) {
    super.setBounds(left, top, right, bottom);
    mRectF = new RectF(left, top, right, bottom);
  }
}
