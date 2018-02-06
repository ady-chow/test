package com.ady.test.swipecard;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;


/** Created by molikto on 01/05/15. */
public class VSwipeCard extends FrameLayout implements View.OnClickListener {

  private static final String TAG = "VSwipeCard";

  private boolean swipable;
  private long autoSwipeStart;

  protected boolean isEmpty = false;

  public VSwipeCard(Context context) {
    super(context);
  }

  public VSwipeCard(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public VSwipeCard(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public enum SwipeDirection {
    RIGHT(1),
    UP(2),
    LEFT(-1);
    private int value;

    SwipeDirection(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    public static SwipeDirection fromValue(int value) {
      switch (value) {
        case 1:
          return RIGHT;
        case 2:
          return UP;
        case -1:
          return LEFT;
      }
      return null;
    }
  }

  {
    setOnClickListener(this);
  }

  private VSwipeStack stack() {
    return (VSwipeStack) getParent();
  }

  public static int CARD_MAX_ROTATION = 30;
  public static int CARD_ANIMATE_DURATION = 450;
  public static int CARD_MIN_SWIPE_X = Metrics.dp(42);
  public static int CARD_MIN_SWIPE_Y = Metrics.dp(42 * 1.5f);

  public static float SWIPE_WIDTH_FACTOR = 1.5f;
  public static float SWIPE_HEIGHT_FACTOR = 1.5f;

  public static float SWIPE_HEIGHT_RATIO = 2f;

  protected int animatingOutState;
  private static int NOT_ANIMATING_OUT = 0;
  protected static int BEFORE_CALL_STACK_UP = 2;
  protected static int BEFORE_CALL_STACK_RIGHT = 1;
  protected static int BEFORE_CALL_STACK_LEFT = -1;
  private static int AFTER_CALL_STACK = 3;

  // add for non-animation state
  private boolean isAnimateOut = false;

  protected float maxXSwipe() {
    return getWidth() / 2.4f;
  }

  protected float maxYSwipe() {
    return getHeight() / 3f;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (!swipable) return false;
    if (!gd.onTouchEvent(event)) {
      switch (event.getActionMasked()) {
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
          if (Math.abs(getTranslationX()) / SWIPE_HEIGHT_RATIO > -getTranslationY()
              || !stack().isAllowUpSwipe()) {
            if (Math.abs(getTranslationX()) > maxXSwipe()) {
              animateOutWithoutInitialVelocity(event.getY());
            } else {
              animateBack();
            }
          } else if (stack().isAllowUpSwipe()) {
            if (-getTranslationY() > maxYSwipe()) {
              animateOutWithoutInitialVelocity(event.getY());
            } else {
              animateBack();
            }
          }
      }
    }
    return true;
  }

  // modify by Jack: Support custom animation time
  public void animateBack() {
    animateBack(CARD_ANIMATE_DURATION);
  }

  boolean isAnimateBacking = false;

  public void animateBack(int t) {
    Animator animator =
        Anu.together(
            new OvershootInterpolator(),
            t,
            Anu.prop(this, "translationX", 0),
            Anu.prop(this, "translationY", 0),
            Anu.prop(this, "rotation", 0));
    animator.addListener(
        new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {
            isAnimateBacking = true;
          }

          @Override
          public void onAnimationEnd(Animator animation) {
            isAnimateBacking = false;
            swipable = true;
            autoSwipeStart = 0;
            animatingOutState = NOT_ANIMATING_OUT;
          }

          @Override
          public void onAnimationCancel(Animator animation) {
            isAnimateBacking = false;
            swipable = true;
            autoSwipeStart = 0;
            animatingOutState = NOT_ANIMATING_OUT;
          }

          @Override
          public void onAnimationRepeat(Animator animation) {}
        });
    Anu.a(this, animator);
  }
  // end

  public boolean autoSwipe(SwipeDirection which) {
    if (isAnimateBacking) return false;
    autoSwipeStart = System.currentTimeMillis();
    swipable = false;
    return animateOutWithoutInitialVelocity(which, getHeight() / 4);
  }

  public void cancelAnim() {
    Anu.cancel(this);
    animatingOutState = NOT_ANIMATING_OUT;
  }

  public boolean isAutoSwipe() {
    return autoSwipeStart != 0;
  }

  private void animateOutWithoutInitialVelocity(float y) {
    SwipeDirection which =
        (-getTranslationY() > Math.abs(getTranslationX()) * SWIPE_HEIGHT_RATIO
                    && stack().isAllowUpSwipe())
                && Math.abs(getTranslationX()) < maxXSwipe()
            ? SwipeDirection.UP
            : (getTranslationX() > 0 ? SwipeDirection.RIGHT : SwipeDirection.LEFT);
    animateOutWithoutInitialVelocity(which, y);
  }

  private boolean animateOutWithoutInitialVelocity(SwipeDirection which, float y) {

    if (animatingOutState == NOT_ANIMATING_OUT) {
      swipable = false;
      float transX = 0;
      float transY = 0;

      boolean hasInitialPosition =
          Math.abs(getTranslationX()) > Metrics.dp(96)
              || Math.abs(getTranslationY()) > Metrics.dp(86);
      Interpolator interp =
          !hasInitialPosition ? new AnticipateInterpolator(0.6f) : new LinearInterpolator();
      Animator animator = null;
      if (which == SwipeDirection.LEFT || which == SwipeDirection.RIGHT) {
        transX = which.getValue() * Vu.screenWidth() * 1.5f;

        transY =
            (!hasInitialPosition || getTranslationY() == 0)
                ? getHeight() / 10
                : getTranslationX() == 0
                    ? Vu.screenHeight() * which.getValue()
                    : getTranslationY() * transX / getTranslationX();

        animator =
            Anu.a(
                this,
                Anu.together(
                    interp,
                    CARD_ANIMATE_DURATION,
                    Anu.prop(this, "translationX", transX),
                    Anu.prop(this, "translationY", transY),
                    Anu.prop(this, "rotation", which.getValue() * maxRotation(y))));
        animatingOutState = BEFORE_CALL_STACK_RIGHT * which.getValue();
      } else if (which == SwipeDirection.UP && stack().isAllowUpSwipe()) {
        transY = Vu.screenHeight();
        transX = 0;
        animator =
            Anu.a(
                this,
                Anu.together(
                    interp,
                    CARD_ANIMATE_DURATION,
                    Anu.prop(this, "translationX", transX),
                    Anu.prop(this, "translationY", -transY),
                    Anu.prop(this, "rotation", 0)));
        animatingOutState = BEFORE_CALL_STACK_UP;
      }

      checkIfAnimatorNotWork(animator, which);
      return true;
    } else {
      return false;
    }
  }

  private void checkIfAnimatorNotWork(Animator animator, SwipeDirection which) {
    Anu.end(
        animator,
        () -> {
          if (!isAnimateOut) {

            isAnimateOut = true;
            stack().animatedOut(VSwipeCard.this, which, 1, autoSwipeStart != 0);
          }
        });
  }

  private void animateOutWithInitialVelocity(float vx, float vy, float y) {
    if (animatingOutState == NOT_ANIMATING_OUT) {
      swipable = false;

      SwipeDirection which =
          (-vy > Math.abs(vx) * SWIPE_HEIGHT_RATIO && stack().isAllowUpSwipe())
              ? SwipeDirection.UP
              : (vx > 0 ? SwipeDirection.RIGHT : SwipeDirection.LEFT);
      Animator animator = null;
      if (which == SwipeDirection.LEFT || which == SwipeDirection.RIGHT) {
        int liked = vx > 0 ? SwipeDirection.RIGHT.getValue() : SwipeDirection.LEFT.getValue();
        float transX = liked * Vu.screenWidth() * 2.0f;
        double sqrtVx = vx > 0 ? Math.sqrt(vx) : -Math.sqrt(Math.abs(vx));
        double sqrtVy = vy > 0 ? Math.sqrt(vy) : -Math.sqrt(Math.abs(vy));
        float transY =
            Math.abs((transX - getTranslationX()) / vx * (float) sqrtVy) * (vy > 0 ? 1 : -1)
                + getTranslationY();
        long duration =
            (long)
                Math.min(
                    (transX - getTranslationX()) / ((1.433 + (Math.abs(vx) * 0.029)) * sqrtVx),
                    675);
        if (duration <= 10) duration = 10;
        animator =
            Anu.a(
                this,
                Anu.together(
                    new LinearInterpolator(),
                    duration,
                    Anu.prop(this, "translationX", transX),
                    Anu.prop(this, "translationY", transY),
                    Anu.prop(this, "rotation", liked * maxRotation(y))));
        animatingOutState = BEFORE_CALL_STACK_RIGHT * liked;
      } else if (which == SwipeDirection.UP && stack().isAllowUpSwipe()) {
        float transY = -Vu.screenHeight();
        double sqrtVx = vx > 0 ? Math.sqrt(vx) : -Math.sqrt(Math.abs(vx));
        double sqrtVy = vy > 0 ? Math.sqrt(vy) : -Math.sqrt(Math.abs(vy));
        float transX =
            Math.abs((transY - getTranslationY()) / vy * (float) sqrtVx) * (vx > 0 ? 1 : -1)
                + getTranslationX();
        long duration =
            (long)
                Math.min(
                    (transY - getTranslationY()) / ((1.433 + (Math.abs(vy) * 0.029)) * sqrtVy),
                    675);
        if (duration <= 10) duration = 10;
        animator =
            Anu.a(
                this,
                Anu.together(
                    new LinearInterpolator(),
                    duration,
                    Anu.prop(this, "translationX", transX),
                    Anu.prop(this, "translationY", transY),
                    Anu.prop(this, "rotation", 0)));
        animatingOutState = BEFORE_CALL_STACK_UP;
      }
      checkIfAnimatorNotWork(animator, which);
    }
  }

  private float baseTransformY;
  private float baseScale = 1;
  private float additionalTranslationY;
  private float additionalScale = 1;

  public void baseTransform(float scale, float transformY) {
    this.baseScale = scale;
    this.baseTransformY = transformY;
    setBasedTranslationY(additionalTranslationY);
    setBasedScale(additionalScale);
  }

  public void setBasedTranslationY(float translationY) {
    additionalTranslationY = translationY;
    super.setTranslationY(baseTransformY + translationY);
  }

  public float getBasedTranslationY() {
    return additionalTranslationY;
  }

  public void setBasedScale(float scale) {
    additionalScale = scale;
    super.setScaleX(baseScale * scale);
    super.setScaleY(baseScale * scale);
  }

  public float getBasedScale() {
    return additionalScale;
  }

  @Override
  public void setScaleX(float scaleX) {
    if (animatingOutState == NOT_ANIMATING_OUT) {
    } else {
      super.setScaleX(scaleX);
    }
  }

  @Override
  public void setScaleY(float scaleY) {
    if (animatingOutState == NOT_ANIMATING_OUT) {
    } else {
      super.setScaleY(scaleY);
    }
  }

  float factor;

  @Override
  public void setTranslationX(float translationX) {
    super.setTranslationX(translationX);
    if (stack() == null) {
      return;
    }
    if (animatingOutState != AFTER_CALL_STACK) {

      float timeBonus = autoSwipeStart != 0 ? -200 : 0;
      factor =
          normalizedDistanceX(
              timeBonus
                  + SWIPE_WIDTH_FACTOR * Math.abs(translationX)
                  + Math.abs(getTranslationY()));
      if (Math.abs(animatingOutState) == BEFORE_CALL_STACK_RIGHT
          && factor >= 1
          && Math.abs(translationX) > 0) {
        int oldState = animatingOutState;
        animatingOutState = AFTER_CALL_STACK;
        stack().bgAlpha(1);
        isAnimateOut = true;
        stack()
            .animatedOut(
                this,
                oldState >= 0 ? SwipeDirection.RIGHT : SwipeDirection.LEFT,
                factor,
                autoSwipeStart != 0);
      } else {
        stack().bgAlpha(factor);
        if (Math.abs(translationX) > -getTranslationY() / SWIPE_HEIGHT_RATIO
            || -getTranslationY() <= maxYSwipe() / 2) {
          float r = normalizedDistanceX(2 * Math.abs(translationX));
          if (translationX > 0) {
            doTranslateFactor(true, r);
          } else {
            doTranslateFactor(true, -r);
          }
        }
      }
    }
  }

  @Override
  public void setTranslationY(float translationY) {
    super.setTranslationY(translationY);
    if (stack() == null) {
      return;
    }
    if (animatingOutState != AFTER_CALL_STACK) {
      if (animatingOutState == BEFORE_CALL_STACK_UP && factor >= 1 && Math.abs(translationY) > 0) {
        animatingOutState = AFTER_CALL_STACK;
        stack().bgAlpha(1);
        isAnimateOut = true;
        stack().animatedOut(this, SwipeDirection.UP, factor, autoSwipeStart != 0);
      } else {
        if (Math.abs(getTranslationX()) <= -translationY / SWIPE_HEIGHT_RATIO
            && -translationY > maxYSwipe() / 2) {
          float r;
          if (isAutoSwipe()) {
            r = normalizedDistanceY(2 * Math.abs(translationY));
          } else {
            r = normalizedDistanceY(2 * Math.abs(translationY + maxYSwipe() / 2));
          }
          if (translationY < 0) {
            doTranslateFactor(false, r * 2);
          }
        }
      }
    }
  }

  protected void doTranslateFactor(boolean horizontal, float factor) {
    if (autoSwipeStart == 0) stack().topTranslateFactor(horizontal, factor);
  }

  static float MAX_POINT = 7f;

  protected void resetCardStateOnRecycle() {
    setTranslationX(0);
    setTranslationY(0);
    setScaleX(1);
    setScaleY(1);
    setRotation(0);
    setPivotX(getWidth() / 2);
    setPivotY(getHeight() / 2);
    animatingOutState = NOT_ANIMATING_OUT;
    autoSwipeStart = 0;
    isAnimateOut = false;
  }

  private GestureDetectorAlt.OnGestureListener gdListener =
      new GestureDetectorAlt.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
          Animator anim = Anu.a(VSwipeCard.this);
          if (anim != null && animatingOutState == NOT_ANIMATING_OUT) {
            anim.cancel();
          }
          return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
          return performClick();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

          if (Math.abs(e2.getRawX() - e1.getRawX()) > CARD_MIN_SWIPE_X
              || (e1.getRawY() - e2.getRawY() > CARD_MIN_SWIPE_Y && stack().isAllowUpSwipe())) {
            if (getTranslationX() * velocityX < 0 || getTranslationY() * velocityY < 0) {
              onRevertedFling(e2, velocityX, velocityY);
            } else {
              animateOutWithInitialVelocity(velocityX / 1000, velocityY / 1000, e2.getY());
            }
          } else {
            animateBack();
          }
          return true;
        }

        private void onRevertedFling(MotionEvent e2, float velocityX, float velocityY) {
          if (Math.abs(velocityX / 100) < Metrics.dp(5)) {
            animateOutWithoutInitialVelocity(e2.getY());
          } else if (Math.abs(velocityX / 100) < Metrics.dp(12)
              && Math.abs(velocityX / 100) >= Metrics.dp(5)) {
            animateBack();
          } else {
            animateOutWithInitialVelocity(velocityX / 1000, velocityY / 1000, e2.getY());
          }
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2) {
          setPivotX(e1.getRawX());
          setPivotY(e1.getRawY());
          float disX = e2.getRawX() - e1.getRawX();
          float disY = e2.getRawY() - e1.getRawY();
          render(disX, disY, e2.getX(), e2.getY());
          return true;
        }
      };

  protected float normalizedDistanceX(float distance) {
    float maxVariation = this.getWidth() / SWIPE_WIDTH_FACTOR;
    if (maxVariation == 0) {
      return 0;
    }
    return Math.max(-1, Math.min(distance / maxVariation, 1));
  }

  protected float normalizedDistanceY(float distance) {
    float maxVariation = this.getHeight() / SWIPE_HEIGHT_FACTOR;
    if (maxVariation == 0) {
      return 0;
    }
    return Math.max(-1, Math.min(distance / maxVariation, 1));
  }

  private void render(float disX, float disY, float x, float y) {
    setTranslationX(disX);
    setTranslationY(disY);
    setRotation(normalizedDistanceX(disX) * maxRotation(y));
  }

  private float maxRotation(float y) {
    float factor =
        y > getHeight() / 3 * 2
            ? getHeight() / 3 * 2 - y
            : Math.min(getHeight() / 3 * 2 - y, getHeight() / 3);
    return factor / getHeight() * CARD_MAX_ROTATION * 1.4f;
  }

  GestureDetectorAlt gd = new GestureDetectorAlt(getContext(), gdListener);

  {
    gd.setIsLongpressEnabled(false);
  }

  public void acceptTouchEvents(boolean b) {
    swipable = b;
  }

  @Override
  public void onClick(View v) {
    if (stack().clickListener != null) {
      stack().clickListener.call(this);
    }
  }
}
