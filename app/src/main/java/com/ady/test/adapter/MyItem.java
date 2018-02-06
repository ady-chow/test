package com.ady.test.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ady.test.R;

/**
 * Created by ady on 2018/2/6.
 */

public class MyItem extends LinearLayout {

  private MyTextView mTextView;

  public MyItem(@NonNull Context context) {
    super(context);
  }

  public MyItem(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    mTextView = findViewById(R.id.text);
  }

  public void render(String text) {
    mTextView.setText(text, TextView.BufferType.NORMAL);
  }
}
