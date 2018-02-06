package com.ady.test.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by ady on 2018/2/6.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {
  public MyTextView(Context context) {
    super(context);
  }

  public MyTextView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
//    Log.d("ady", "onLayout: l = " + left + ", t = " + top + ", r = " + right + ", b = " + bottom);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    Log.d("ady", "onMeasure: w = " + getMeasuredWidth() + ", h = " + getMeasuredHeight());
  }

  @Override
  public void setText(CharSequence text, BufferType type) {
//    Log.d("ady", "setText: text = " + text);
    super.setText(text, type);
  }
}
