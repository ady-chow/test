package com.ady.test.swipecard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/** Created by ady on 2018/2/1. */
public class VForceRelayoutText extends android.support.v7.widget.AppCompatTextView {

  private static final String TAG = "ady";

  public VForceRelayoutText(Context context) {
    super(context);
  }

  public VForceRelayoutText(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    Log.d(TAG, "onLayout: l = " + left + ", t = " + top + ", r = " + right + ", b = " + bottom);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    Log.d(TAG, "onMeasure: w = " + getMeasuredWidth() + ", h = " + getMeasuredHeight());
    //    if (getMeasuredWidth() > getRight()) {
    //      layout(getLeft(), getTop(), getLeft() + getMeasuredWidth(), getBottom());
    //    }
  }

  void setText(String text) {
    super.setText(text);
  }

}
