package com.ady.test.anim;

import android.view.animation.Interpolator;

import static java.lang.Math.PI;

/** Created by ady on 2018/3/1. */
public class SpringScaleInterpolator implements Interpolator {

  private float factor;

  public SpringScaleInterpolator(float factor) {
    this.factor = factor;
  }

  @Override
  public float getInterpolation(float input) {
    return (float)
        (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * PI) / factor) + 1);
  }
}
