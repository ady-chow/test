package com.ady.test.customedview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhouxinyuan on 2018/1/23.
 */

public class MyBitmap extends View {

  Bitmap mBitmap;
  Paint mPaint;

  public MyBitmap(Context context, Bitmap bitmap) {
    this(context, null, bitmap);
  }

  public MyBitmap(Context context, @Nullable AttributeSet attrs, Bitmap bitmap) {
    super(context, attrs);
    this.mBitmap = bitmap;
    init();
  }

  private void init() {
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setFilterBitmap(true);
    mPaint.setDither(true);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawBitmap(mBitmap, 100, 100, mPaint);
  }
}
