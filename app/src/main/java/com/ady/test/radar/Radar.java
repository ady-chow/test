package com.ady.test.radar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.ady.test.R;
import com.ady.test.swipecard.Metrics;

/** Created by ady on 2018/2/23. */
public class Radar extends android.support.v7.widget.AppCompatImageView {

  static final long DURATION = 1500;
  private static long START;
  private Paint fillPaint;
  private Paint borderPaint;
  private Interpolator interpolator;
  private ValueAnimator animator;
  private float centerX;
  private float centerY;
  private float startPosition;

  public Radar(Context context) {
    super(context);
  }

  public Radar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    int radarBg = getResources().getColor(R.color.radar_bg);
    fillPaint = new Paint();
    fillPaint.setAntiAlias(true);
    fillPaint.setDither(false);
    fillPaint.setColor(radarBg);
    fillPaint.setAlpha(0x19);
    borderPaint = new Paint();
    borderPaint.setAntiAlias(true);
    borderPaint.setDither(false);
    borderPaint.setStrokeWidth(Metrics.dp(1));
    borderPaint.setStyle(Paint.Style.STROKE);
    borderPaint.setColor(radarBg);
    borderPaint.setAlpha(0xa6);
    interpolator = new AccelerateDecelerateInterpolator();
  }

  public void start(long delay) {
    START = SystemClock.uptimeMillis();
    if (animator == null) {
      animator = ValueAnimator.ofFloat(0f, 1f).setDuration(DURATION);
      animator.setInterpolator(interpolator);
      animator.setRepeatCount(ValueAnimator.INFINITE);
      animator.addUpdateListener(
          animation -> {
            invalidate();
          });
      animator.setStartDelay(delay);
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
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int profileImageSize =
        getContext().getResources().getDimensionPixelSize(R.dimen.radar_image_size);
    startPosition = profileImageSize * 0.95f;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (SystemClock.uptimeMillis() - START < DURATION) {
      float ratio = 1.0f * (SystemClock.uptimeMillis() - START) / DURATION;
      float speed = interpolator.getInterpolation(ratio);
      if (centerX == 0) {
        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;
      }
      float radius = (startPosition + (getMeasuredWidth() * 0.95f - startPosition) * speed) / 2;
      if (ratio >= 0.65f) {
        fillPaint.setAlpha((int) (fillPaint.getAlpha() * (1 - speed)));
        borderPaint.setAlpha((int) (borderPaint.getAlpha() * (1 - speed)));
      }
      canvas.drawCircle(centerX, centerY, radius, fillPaint);
      canvas.drawCircle(centerX, centerY, radius, borderPaint);
    } else {
      if (animator != null) {
        animator.cancel();
        animator = null;
      }
    }
  }
}
