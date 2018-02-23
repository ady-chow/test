package com.ady.test.radar;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.ady.test.R;

/** Created by ady on 2018/2/23. */
public class Radar extends View {

  private static final int DURATION = 3000;
  private static long START;
  Paint paint = new Paint();
  Bitmap oval = BitmapFactory.decodeResource(getResources(), R.drawable.ic_main_radar_avatar_oval);
  AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
  ValueAnimator animator;
  float centerX;
  float centerY;
  Rect src;
  RectF dst;

  public Radar(Context context) {
    super(context);
  }

  public Radar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    paint.setAntiAlias(true);
    paint.setDither(false);
    src = new Rect();
    dst = new RectF();
  }

  public void start() {
    START = SystemClock.uptimeMillis();
    if (animator == null) {
      animator = ObjectAnimator.ofFloat(0f, 1f).setDuration(DURATION);
      animator.setRepeatCount(ValueAnimator.INFINITE);
      animator.addUpdateListener(
          animation -> {
            invalidate();
          });
      animator.start();
    }
  }

  public void onStart() {
    if (animator != null) {
      animator.start();
    }
  }

  public void onStop() {
    if (animator != null) {
      animator.cancel();
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if (SystemClock.uptimeMillis() - START < DURATION) {
      float ratio = 1.0f * (SystemClock.uptimeMillis() - START) / DURATION;
      float speed = interpolator.getInterpolation(ratio);
      int w = getMeasuredWidth();
      int h = getMeasuredHeight();
      if (centerX == 0) {
        centerX = getX() + w / 2;
        centerY = getY() + h / 2;
      }
      float radius = w * (1 + speed) / 2;
      Log.d(
          "ady",
          "onDraw: ratio = "
              + ratio
              + " speed = "
              + speed
              + ", w = "
              + w
              + ", radius = "
              + radius
              + ", centerX = "
              + centerX
              + ", centerY = "
              + centerY
              + ", elapse = "
              + (SystemClock.uptimeMillis() - START));
      src.left = getLeft();
      src.top = getTop();
      src.bottom = getBottom();
      src.right = getRight();
      dst.left = centerX - radius;
      dst.top = centerY - radius;
      dst.right = centerX + radius;
      dst.bottom = centerY + radius;
      canvas.drawBitmap(oval, src, dst, paint);
    } else {
      if (animator != null) {
        animator.cancel();
        animator = null;
      }
    }
  }
}
