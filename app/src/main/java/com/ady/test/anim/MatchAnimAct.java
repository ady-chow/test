package com.ady.test.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ady.test.R;

import rx.functions.Action2;

/** Created by ady on 2018/3/1. */
public class MatchAnimAct extends Activity {

  private TextView _title;

  private Button _start_btn;

  private TextView _skip_btn;

  private float screenWidth;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_match_anim);
    screenWidth = getResources().getDisplayMetrics().widthPixels;
    _title = findViewById(R.id.title);
    _start_btn = findViewById(R.id.start_chat);
    _skip_btn = findViewById(R.id.keep_play);
    findViewById(R.id.scale_title).setOnClickListener(c -> animTitle());
    findViewById(R.id.translate_start_btn).setOnClickListener(c -> animStartBtn());
    findViewById(R.id.translate_skip_btn).setOnClickListener(c -> animSkipBtn());
  }

  @Override
  protected void onResume() {
    super.onResume();
    animTitle();
    animStartBtn();
    animSkipBtn();
  }

  private void animTitle() {
    ObjectAnimator animatorX = ObjectAnimator.ofFloat(_title, "scaleX", 0f, 1f);
    ObjectAnimator animatorY = ObjectAnimator.ofFloat(_title, "scaleY", 0f, 1f);
    AnimatorSet set = new AnimatorSet();
    set.setDuration(1000);
    set.setStartDelay(100);
    set.setInterpolator(new SpringScaleInterpolator(0.4f));
    set.playTogether(animatorX, animatorY);
    set.start();
  }

  private void animStartBtn() {
    transButton(_start_btn, _skip_btn.getX() - screenWidth, 0);
  }

  private void animSkipBtn() {
    withMeasureNonZeroSize(
        _skip_btn,
        (w, h) ->
            transButton(_skip_btn, screenWidth + _skip_btn.getWidth(), screenWidth / 2 - w / 2));
  }

  private void transButton(View v, float offset, float origin) {
    ObjectAnimator animatorX = ObjectAnimator.ofFloat(v, "translationX", offset, origin);
    animatorX.setDuration(1000);
    animatorX.setStartDelay(100);
    animatorX.setInterpolator(new SpringScaleInterpolator(0.8f));
    animatorX.addListener(
        new Animator.AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animation) {}

          @Override
          public void onAnimationEnd(Animator animation) {
            v.setTranslationX(origin);
          }

          @Override
          public void onAnimationCancel(Animator animation) {}

          @Override
          public void onAnimationRepeat(Animator animation) {}
        });
    animatorX.start();
  }

  //  private void animStartBtn() {
  //    float offset = screenWidth - _skip_btn.getX();
  //    ObjectAnimator animatorX = ObjectAnimator.ofFloat(_start_btn, "translationX", -offset, 0);
  //    animatorX.setDuration(1000);
  //    animatorX.setStartDelay(100);
  //    animatorX.setInterpolator(new SpringScaleInterpolator(0.8f));
  //    animatorX.addListener(
  //        new Animator.AnimatorListener() {
  //          @Override
  //          public void onAnimationStart(Animator animation) {
  //            Log.d("ady", "onAnimationStart: " + _start_btn.getTranslationX());
  //          }
  //
  //          @Override
  //          public void onAnimationEnd(Animator animation) {
  //            Log.d("ady", "onAnimationEnd: " + _start_btn.getTranslationX());
  //            _start_btn.setTranslationX(0);
  //          }
  //
  //          @Override
  //          public void onAnimationCancel(Animator animation) {}
  //
  //          @Override
  //          public void onAnimationRepeat(Animator animation) {}
  //        });
  //    animatorX.start();
  //  }
  //
  //  private void animSkipBtn() {
  //    withMeasureNonZeroSize(_skip_btn, (w, h) -> doAnimSkipBtn(w));
  //  }
  //
  //  private void doAnimSkipBtn(int width) {
  //    Log.d("ady", "animSkipBtn: w = " + width);
  //    float origin = screenWidth / 2 - width / 2;
  //    float offset = screenWidth + _skip_btn.getWidth();
  //    ObjectAnimator animatorX = ObjectAnimator.ofFloat(_skip_btn, "translationX", offset,
  // origin);
  //    animatorX.setDuration(1000);
  //    animatorX.setStartDelay(100);
  //    animatorX.setInterpolator(new SpringScaleInterpolator(0.8f));
  //    animatorX.addListener(
  //        new Animator.AnimatorListener() {
  //          @Override
  //          public void onAnimationStart(Animator animation) {
  //            Log.d("ady", "onAnimationStart: " + _skip_btn.getTranslationX());
  //          }
  //
  //          @Override
  //          public void onAnimationEnd(Animator animation) {
  //            Log.d("ady", "onAnimationEnd: " + _skip_btn.getTranslationX());
  //            _skip_btn.setTranslationX(origin);
  //          }
  //
  //          @Override
  //          public void onAnimationCancel(Animator animation) {}
  //
  //          @Override
  //          public void onAnimationRepeat(Animator animation) {}
  //        });
  //    animatorX.start();
  //  }

  public static void withMeasureNonZeroSize(
      final View view, final Action2<Integer, Integer> action) {
    if (view.getHeight() == 0 || view.getWidth() == 0) {
      view.addOnLayoutChangeListener(
          new View.OnLayoutChangeListener() {
            boolean called = false;

            @Override
            public void onLayoutChange(
                View v,
                int left,
                int top,
                int right,
                int bottom,
                int oldLeft,
                int oldTop,
                int oldRight,
                int oldBottom) {
              int oldWidth = oldRight - oldLeft;
              int oldHeight = oldBottom - oldTop;
              int width = right - left;
              int height = bottom - top;
              boolean changed = oldWidth != width || oldHeight != height;
              if (changed && !called && width != 0 && height != 0) {
                called = true;
                action.call(width, height);
                view.removeOnLayoutChangeListener(this);
              }
            }
          });
    } else {
      view.post(() -> action.call(view.getWidth(), view.getHeight()));
    }
  }
}
