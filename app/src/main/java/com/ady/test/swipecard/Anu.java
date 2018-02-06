package com.ady.test.swipecard;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Property;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.ady.test.R;


/** animation utils User: molikto Date: 01/05/15 Time: 02:51 */
public final class Anu {
  private Anu() {}

  private static final String TAG = "Anim";

  public static Animator.AnimatorListener invisibleWhenEnd(final View v) {
    return new Listener() {
      @Override
      public void onAnimationEnd(Animator animation) {
        v.setVisibility(View.INVISIBLE);
      }
    };
  }

  // standard curve
  public static final Interpolator FAST_OUT_SLOW_IN = new FastOutSlowInInterpolator();
  public static final Interpolator FAST_IN_SLOW_OUT = new FastInSlowOutInterpolator();
  public static final Interpolator SLOW_OUT = new DecelerateInterpolator();
  public static final Interpolator OVER_SHOOT = new OvershootInterpolator(1);
  public static final Interpolator ANTI_OVER_SHOOT = new AnticipateOvershootInterpolator(1);

  public static void cancel(View v) {
    Animator a = Anu.a(v);
    if (a != null) {
      a.cancel();
      v.setTag(R.id.key_anim, null);
    }
  }

  public static class Listener implements Animator.AnimatorListener {

    @Override
    public void onAnimationStart(Animator animation) {}

    @Override
    public void onAnimationEnd(Animator animation) {}

    @Override
    public void onAnimationCancel(Animator animation) {}

    @Override
    public void onAnimationRepeat(Animator animation) {}
  }

  public static Animator a(final View v) {
    Object prev = v.getTag(R.id.key_anim);
    if (prev instanceof Animator) {
      return (Animator) prev;
    }
    return null;
  }

  /**
   * this is a method such that associate the animator with the view, so you can cancel or ...
   * whatever
   */
  public static Animator a(final View v, Animator animator) {
    Object prev = v.getTag(R.id.key_anim);
    if (prev != null) {
      ((Animator) prev).cancel();
      // Log.d(TAG, "previous animation ongoing");
    }
    v.setTag(R.id.key_anim, animator);
    animator.addListener(
        new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {}

          @Override
          public void onAnimationEnd(Animator animation) {
            v.setTag(R.id.key_anim, null);
          }

          @Override
          public void onAnimationCancel(Animator animation) {
            v.setTag(R.id.key_anim, null);
          }

          @Override
          public void onAnimationRepeat(Animator animation) {}
        });
    animator.start();
    return animator;
  }

  public static Animator together(Interpolator interp, long duration, Animator... animators) {
    return set(true, interp, duration, animators);
  }

  public static Animator set(
      boolean together, Interpolator interp, long duration, Animator... animators) {
    return set(together, interp, duration, null, animators);
  }

  public static Animator set(
      boolean together,
      Interpolator interpolator,
      long duration,
      Animator.AnimatorListener listener,
      Animator... animators) {
    AnimatorSet a = new AnimatorSet();
    if (together) {
      a.playTogether(animators);
    } else {
      a.playSequentially(animators);
    }
    if (interpolator != null) a.setInterpolator(interpolator);
    if (duration != -1) a.setDuration(duration);
    if (listener != null) a.addListener(listener);
    return a;
  }

  public static Animator together(long duration, Animator... animators) {
    return set(true, null, duration, animators);
  }

  public static Animator sequentially(Animator... animators) {
    return set(false, null, -1, animators);
  }

  public static Animator nothing(int duration) {
    return ValueAnimator.ofFloat(0, 1).setDuration(duration);
  }

  public static Animator together(Animator... animators) {
    return set(true, null, -1, animators);
  }

  public static Animator prop(View v, Property<View, Integer> prop, int... vs) {
    return prop(v, prop, 0, -1, null, vs);
  }

  public static Animator prop(View v, Property<View, Float> prop, float... vs) {
    return prop(v, prop, 0, -1, null, vs);
  }

  public static Animator prop(
      View v,
      Property<View, Integer> prop,
      long delay,
      long duration,
      Interpolator interp,
      int... vs) {
    return prop(ObjectAnimator.ofInt(v, prop, vs), interp, delay, duration);
  }

  public static Animator prop(
      View v,
      Property<View, Float> prop,
      long delay,
      long duration,
      Interpolator interp,
      float... vs) {
    return prop(ObjectAnimator.ofFloat(v, prop, vs), interp, delay, duration);
  }

  public static Animator prop(View v, String prop, float... vs) {
    return prop(v, prop, 0, -1, null, vs);
  }

  public static Animator prop(
      View v, String prop, long delay, long duration, Interpolator interp, float... vs) {
    return prop(ObjectAnimator.ofFloat(v, prop, vs), interp, delay, duration);
  }

  public static Animator prop(Animator a, Interpolator interp, long delay, long duration) {
    if (duration != -1) a.setDuration(duration);
    if (interp != null) a.setInterpolator(interp);
    if (delay != 0) a.setStartDelay(delay);
    return a;
  }

  public static Animator start(Animator a, final Runnable listener) {
    a.addListener(
        new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {
            listener.run();
          }

          @Override
          public void onAnimationEnd(Animator animation) {}

          @Override
          public void onAnimationCancel(Animator animation) {}

          @Override
          public void onAnimationRepeat(Animator animation) {}
        });
    return a;
  }

  public static Animator end(Animator a, final Runnable listener) {
    a.addListener(
        new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {}

          @Override
          public void onAnimationEnd(Animator animation) {
            listener.run();
          }

          @Override
          public void onAnimationCancel(Animator animation) {}

          @Override
          public void onAnimationRepeat(Animator animation) {}
        });
    return a;
  }

  public static Animator endAndCancel(Animator a, final Runnable onEnd, final Runnable onCancel) {
    a.addListener(
        new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {}

          @Override
          public void onAnimationEnd(Animator animation) {
            onEnd.run();
          }

          @Override
          public void onAnimationCancel(Animator animation) {
            onCancel.run();
          }

          @Override
          public void onAnimationRepeat(Animator animation) {}
        });
    return a;
  }

  public static final Property<View, Float> SCALE =
      new Property<View, Float>(Float.TYPE, "scale") {
        @Override
        public Float get(View object) {
          return object.getScaleX();
        }

        @Override
        public void set(View object, Float value) {
          object.setScaleX(value);
          object.setScaleY(value);
        }
      };

  public static final Property<View, Integer> BACKGROUND_DRAWABLE_ALPHA =
      new Property<View, Integer>(Integer.TYPE, "backgroundDrawableAlpha") {
        @Override
        public Integer get(View object) {
          return object.getBackground() == null || Build.VERSION.SDK_INT < 19
              ? 0
              : object.getBackground().getAlpha();
        }

        @Override
        public void set(View object, Integer value) {
          if (object.getBackground() != null) {
            object.getBackground().setAlpha(value);
          }
        }
      };

  public static final Property<TextView, Integer> TEXT_GREYSCALE =
      new Property<TextView, Integer>(Integer.TYPE, "textGreyScale") {
        @Override
        public Integer get(TextView object) {
          return Color.red(object.getCurrentTextColor());
        }

        @Override
        public void set(TextView object, Integer value) {
          object.setTextColor(Color.rgb(value, value, value));
        }
      };
}
