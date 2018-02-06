package com.ady.test.swipecard;

import android.animation.Animator;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

import rx.functions.Action1;

public class VSwipeStack<T extends VSwipeCard> extends AdapterView<BAdapter> {

  private static final String TAG = "VSwipeStack";
  private BAdapter adapter;
  private Stack<T> recycled = new Stack<>();
  private Stack<T> recycledEmpty = new Stack<>();
  private DataSetObserver observer =
      new DataSetObserver() {
        @Override
        public void onChanged() {
          renderByAdapter();
        }

        @Override
        public void onInvalidated() {
          renderByAdapter();
        }
      };

  // setttings
  protected int screw = Metrics.dp(6);
  protected float scaleDown = 0.0225f;
  protected int displayedCount = 4;

  // states
  protected float bgAlpha = 0;
  protected Point childSize;
  protected boolean callRenderInOnLayout;
  protected ArrayList<View> cardsAnimatingOut = new ArrayList<>();

  public boolean isAllowUpSwipe() {
    return allowUpSwipe;
  }

  public void setAllowUpSwipe(boolean allowUpSwipe) {
    this.allowUpSwipe = allowUpSwipe;
  }

  protected boolean allowUpSwipe = false;

  public enum OnCardSwipeResult {
    pass,
    stay,
    back
  }

  public interface Listener<T> {
    public OnCardSwipeResult onCardSwiped(
        T card, VSwipeCard.SwipeDirection direction, boolean fromButton);
  }

  public interface SwipePassedListener {
    public void swipPassed(boolean isPassed);
  }

  public interface SwipeRatioListener {
    public void swipeRatio(boolean horizontal, float factor);
  }

  public void onTopCardSwipe(SwipeRatioListener swipeRatioListener) {
    this.swipeRatioListener = swipeRatioListener;
  }

  public void onSwipePassed(SwipePassedListener swipePassedListener) {
    this.swipePassedListener = swipePassedListener;
  }

  protected Listener<T> listener;
  protected SwipeRatioListener swipeRatioListener;
  protected SwipePassedListener swipePassedListener; // listener for passed by
  protected int baseUpwards;
  protected int topMostChildShouldBe;

  public static final int VIEW_TYPE_NORMAL = 0;
  public static final int VIEW_TYPE_EMPTY = 1;


  private int emptyViewInStackCount = 0;

  public void onCardSwiped(Listener<T> listener) {
    this.listener = listener;
  }

  public VSwipeStack(Context context) {
    super(context);
  }

  public VSwipeStack(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public VSwipeStack(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public BAdapter getAdapter() {
    return adapter;
  }

  @Override
  public void setAdapter(BAdapter a) {
    if (adapter != null) adapter.unregisterDataSetObserver(observer);
    this.adapter = a;
    if (adapter != null) {
      adapter.registerDataSetObserver(observer);
    }
    renderByAdapter();
  }

  @Override
  protected void onMeasure(int wm, int hm) {
    View v;
    if (adapter == null) {
      throw new IllegalStateException(
          "it is expected to have adapter set no to simplify measuring");
    } else if (getChildCount() == 0) {
      v = adapter.inflate(this, VIEW_TYPE_NORMAL);
      recycled.add((T) v);
    } else {
      v = getChildAt(emptyViewInStackCount);
    }
    childSize = new Point();
    v.measure(
        Measure.exactly(Measure.size(wm)),
        Measure.exactly(Measure.size(hm)) - (displayedCount - 2) * screw);
    childSize.x = v.getMeasuredWidth();
    childSize.y = v.getMeasuredHeight();
    int height = childSize.y + (displayedCount - 2) * screw;
    super.onMeasure(Measure.exactly(Measure.size(wm)), Measure.exactly(height));
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    if (callRenderInOnLayout) renderByAdapter();
  }

  private void recycleAndChangeToDefaultProperties(T v) {
    if (v.getParent() != null) throw new IllegalStateException("!!");

    if (v.isEmpty) {
      recycledEmpty.add(v);
      emptyViewInStackCount--;
    } else {
      v.resetCardStateOnRecycle();
      recycled.add(v);
    }
  }

  private void renderByAdapter() {
    if (childSize == null) {
      callRenderInOnLayout = true;
      return;
    } else {
      callRenderInOnLayout = false;
    }
    if (adapter != null) {
      topMostChildShouldBe = Math.min(displayedCount, adapter.getCount()) - 1;
      baseUpwards = displayedCount - 1 - topMostChildShouldBe;
      int limit = Math.min(adapter.getCount(), displayedCount);
      int animatingoutSize = cardsAnimatingOut.size();
      int childSizeDiff = getChildCount() - animatingoutSize - limit;
      int nonIdleNotEmptyChildCount = getChildCount() - animatingoutSize - emptyViewInStackCount;
      // NOTE on some devices it is bugy to remove add views...
      if (childSizeDiff > 0) {
        for (int i = 0; i < childSizeDiff; i++) {
          View c = getChildAt(0);
          removeViewsInLayout(0, 1);
          recycleAndChangeToDefaultProperties((T) c);
        }
      } else if (childSizeDiff < 0) {
        for (int i = nonIdleNotEmptyChildCount;
             i < Math.abs(childSizeDiff) + nonIdleNotEmptyChildCount;
             i++) {
          T convert = null;
          if (adapter.getItemViewType(i) == VIEW_TYPE_NORMAL) {
            convert = recycled.isEmpty() ? null : recycled.pop();
          } else if (adapter.getItemViewType(i) == VIEW_TYPE_EMPTY) {
            convert = recycledEmpty.isEmpty() ? null : recycledEmpty.pop();
          }
          if (convert == null) {
            convert = (T) adapter.inflate(this, adapter.getItemViewType(i));
          }
          if (convert.isEmpty) {
            addViewInLayout(convert, 0, generateDefaultLayoutParams(), true);
            emptyViewInStackCount++;
          } else {
            addViewInLayout(convert, emptyViewInStackCount, generateDefaultLayoutParams(), true);
          }
        }
      } else if (childSizeDiff == 0
          && emptyViewInStackCount > 0
          && getChildCount() - cardsAnimatingOut.size() != 4) {
        T convert = recycled.isEmpty() ? null : recycled.pop();
        if (convert == null) {
          convert = (T) adapter.inflate(this, adapter.getItemViewType(emptyViewInStackCount - 1));
        }
        addViewInLayout(convert, emptyViewInStackCount, generateDefaultLayoutParams(), true);

        View c = getChildAt(0);
        removeViewsInLayout(0, 1);
        recycleAndChangeToDefaultProperties((T) c);
      }

      for (int i = limit - 1; i >= 0; i--) {
        T c = cardFromTop(i);
        c.acceptTouchEvents(i == 0);
        if (c.getRight() == 0) {
          c.measure(Measure.exactly(childSize.x), Measure.exactly(childSize.y));
          int widthDiff = getMeasuredWidth() - c.getMeasuredWidth();
          if (c.isEmpty) {
            c.layout(
                widthDiff / 2,
                childSize.y - c.getMeasuredHeight(),
                widthDiff / 2 + childSize.x,
                childSize.y);
          } else {
            c.layout(widthDiff / 2, 0, widthDiff / 2 + c.getMeasuredWidth(), c.getMeasuredHeight());
          }
        }
        adapter.adapt(c, adapter.getItem(i), adapter.getItemViewType(i), i);
        if (c.getBackground() != null) c.getBackground().setAlpha(255);
      }

      renderBgAlpha();
      invalidate();
    }
  }

  private void renderBgAlpha() {
    int nonIdleChildCount = getChildCount() - cardsAnimatingOut.size();
    float maxDownwards = displayedCount - 2;
    for (int i = 0; i < nonIdleChildCount; i++) {
      float goingDownwards = Math.min((topMostChildShouldBe - i) - bgAlpha, maxDownwards);
      T v = cardFromBottom(i);
      // not the first non idle card user is viewing
      if (i < nonIdleChildCount - 1) {
        float scale = 1 - goingDownwards * scaleDown;
        float inter = scale * scale * scale;
        float a = (float) Math.sqrt(Math.sqrt(goingDownwards));
        float b = (float) (a * a * a * Math.sqrt(a));
        v.baseTransform(inter, b * screw + v.getHeight() * (1 - inter) / 2);
      }

      if (nonIdleChildCount == displayedCount) {
        if (getChildAt(0).getBackground() != null) {
          getChildAt(0).getBackground().setAlpha((int) (bgAlpha * 255f));
        }
      }
    }
  }

  public int nonAnimatingCardCounts() {
    return getChildCount() - cardsAnimatingOut.size();
  }

  public T cardFromBottom(int i) {
    return (T) getChildAt(i);
  }

  public void bgAlpha(float i) {
    bgAlpha = Math.max(i, 0);
    renderBgAlpha();
  }

  public void topTranslateFactor(boolean horizontal, float factor) {

    if (swipeRatioListener != null) {
      swipeRatioListener.swipeRatio(horizontal, factor);
    }
  }

  @Override
  public View getSelectedView() {
    return null;
  }

  @Override
  public void setSelection(int position) {
  }

  public T topCard() {
    return cardFromTop(0);
  }

  public T cardFromTop(int i) {
    return cardFromBottom(getChildCount() - cardsAnimatingOut.size() - 1 - i);
  }

  public Queue<VSwipeCard.SwipeDirection> swipeQueue = new ArrayDeque<>();

  public void animatedOut(
      final T card, VSwipeCard.SwipeDirection direction, float factor, boolean autoSwipe) {
    bgAlpha = factor - 1;
    cardsAnimatingOut.add(card);
    // big enough so no z order change is made

    if (card.getBackground() != null) {
      card.getBackground().setAlpha(255);
    }

    // note: always call listeners after update state finish!
    switch (listener.onCardSwiped(card, direction, autoSwipe)) {
      case pass:
        // we added it to the animating list, so the top card is the card bellow now. so we can
        // swipe it now!
        // hopefully the listener will fix the elevation!
        Animator anim = Anu.a(card);
        Animator.AnimatorListener listener =
            new Animator.AnimatorListener() {
              @Override
              public void onAnimationStart(Animator animation) {
              }

              @Override
              public void onAnimationEnd(Animator animation) {
                cardsAnimatingOut.remove(card);
                int index = indexOfChild(card);
                if (index < 0) {
                  throw new IllegalStateException("");
                } else {
                  removeViewsInLayout(index, 1);
                  invalidate();
                }
                recycleAndChangeToDefaultProperties(card);
              }

              @Override
              public void onAnimationCancel(Animator animation) {
              }

              @Override
              public void onAnimationRepeat(Animator animation) {
              }
            };
        if (anim == null) {
          listener.onAnimationEnd(null);
        } else {
          anim.addListener(listener);
        }
        if (!swipeQueue.isEmpty()) {
          // Log.d(TAG, "animating out delayed card");
          swipe(swipeQueue.poll());
        }
        if (swipePassedListener != null) {
          swipePassedListener.swipPassed(true);
        }
        break;
      case back:
        post(
            () -> {
              cardsAnimatingOut.remove(card);
              card.cancelAnim();
              card.animateBack();
            });
        break;
      case stay:
        post(
            () -> {
              cardsAnimatingOut.remove(card);
              card.cancelAnim();
            });
        break;
    }
  }

  public void swipe(VSwipeCard.SwipeDirection which) {
    if (!isAllowUpSwipe() && which == VSwipeCard.SwipeDirection.UP) {
      return;
    }
    T card = topCard();
    if (card != null) {
      if (!card.autoSwipe(which)) {
        // yes.. we do not use it now. but it won't harm to keep it
        if (swipeQueue.size() < 0) {
          swipeQueue.add(which);
        }
      }
    }
  }

  Action1<T> clickListener;

  public void itemClick(Action1<T> listener) {
    this.clickListener = listener;
  }
}
