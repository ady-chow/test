package com.ady.test.customedview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;


import com.ady.test.R;

import java.util.Arrays;

public class RoundView extends ImageView {

  private static final int SHAPE_MODE_ROUND_RECT = 1;
  private static final int SHAPE_MODE_CIRCLE = 2;

  private int mShapeMode = 0;
  private float mRadius = 0;
  private Shape mShape;
  private Paint mPaint;

  public RoundView(Context context) {
    super(context);
    init(null);
  }

  public RoundView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public RoundView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      setLayerType(LAYER_TYPE_HARDWARE, null);
    }
    mShapeMode = SHAPE_MODE_ROUND_RECT;
    mRadius = 100;
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setFilterBitmap(true);
    mPaint.setColor(Color.BLACK);
    mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    if (changed) {
      switch (mShapeMode) {
        case SHAPE_MODE_ROUND_RECT:
          break;
        case SHAPE_MODE_CIRCLE:
          int min = Math.min(getWidth(), getHeight());
          mRadius = (float) min / 2;
          break;
      }
      if (mShape == null) {
        float[] radius = new float[8];
        Arrays.fill(radius, mRadius);
        mShape = new RoundRectShape(radius, null, null);
      }
      mShape.resize(getWidth(), getHeight());
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    int saveCount = canvas.getSaveCount();
    canvas.save();
    super.onDraw(canvas);
    switch (mShapeMode) {
      case SHAPE_MODE_ROUND_RECT:
      case SHAPE_MODE_CIRCLE:
        if (mShape != null) {
          mShape.draw(canvas, mPaint);
        }
        break;
    }
    canvas.restoreToCount(saveCount);
  }
}