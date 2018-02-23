package com.ady.test.radar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;

import com.ady.test.R;
import com.ady.test.swipecard.Metrics;

import java.util.ArrayList;

/** User: molikto Date: 07/29/14 Time: 16:16 */
public class RadarRipple extends android.support.v7.widget.AppCompatImageView {

  private static final int DURATION = 3000;
  private static final int MAX_ALPHA = 225;

  private int startPosition;
  private ArrayList<Long> times = new ArrayList<Long>();

  private final AccelerateInterpolator accInterpolator = new AccelerateInterpolator();
  Paint paint = new Paint();
  int maxStrokeWidth;

  {
    paint.setColor(getContext().getResources().getColor(R.color.colorPrimary));
    paint.setAlpha(MAX_ALPHA);
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.STROKE);
  }

  private ValueAnimator anim;

  public RadarRipple(Context context) {
    this(context, null);
  }

  public RadarRipple(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RadarRipple(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    maxStrokeWidth = isInEditMode() ? 0 : Metrics.dp(3);
  }

  public void onStop() {
    if (anim != null) {
      anim.cancel();
    }
  }

  public void onStart() {
    if (anim != null) {
      anim.start();
    }
  }

  public void ripple() {
    if (times.size() == 0 || SystemClock.uptimeMillis() - times.get(times.size() - 1) > 100) {
      times.add(SystemClock.uptimeMillis());
      if (anim == null) {
        anim = ValueAnimator.ofFloat(0, 1).setDuration(1000);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.addUpdateListener(
            animation -> {
              if (times.size() == 0) {
                anim.cancel();
                anim = null;
              } else {
                invalidate();
              }
            });
        anim.start();
      }
    }
  }

  public void setRadarColor(int color) {
    Drawable d = getDrawable();
    if (d != null) {
      d.mutate();
      d.setColorFilter(new LightingColorFilter(Color.BLACK, color));
      paint.setColor(color);
    }
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int size = getMeasuredHeight() / 2;
    if (size > 5) {
      int profileImageSize =
          getContext().getResources().getDimensionPixelSize(R.dimen.radar_image_size);
      int maxSizeOfProfileImage = (int) (profileImageSize * 1.2f);
      // (size - start) / DURATION * 200 + start = maxSizeOfProfileImage
      //
      // solve from above equation
      startPosition =
          (int) (1f * (maxSizeOfProfileImage - size * 200 / DURATION) / (1 - 200 / DURATION));
      if (startPosition > profileImageSize) {
        startPosition = profileImageSize;
      }
      startPosition = (int) (startPosition * 0.95f);
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    boolean changed = false;
    for (Long t : times) {
      if (SystemClock.uptimeMillis() - t < DURATION) {
        changed = true;
        float ratio = 1.0f * (SystemClock.uptimeMillis() - t) / DURATION;
        float interpolated = accInterpolator.getInterpolation(ratio);
        paint.setAlpha((int) (10 + (MAX_ALPHA - 10) * (1 - interpolated)));
        paint.setStrokeWidth(maxStrokeWidth * (1 - interpolated));
        int center = getMeasuredWidth() / 2;
        int size = (int) (startPosition + (getMeasuredWidth() - startPosition) * ratio);
        size /= 2;
        canvas.drawCircle(center, center, size, paint);
      }
    }
    if (!changed && anim != null) {
      anim.cancel();
      anim = null;
    }
  }
}
