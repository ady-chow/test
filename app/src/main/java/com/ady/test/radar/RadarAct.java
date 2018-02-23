package com.ady.test.radar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ady.test.R;

/** Created by ady on 2018/2/23. */
public class RadarAct extends Activity {

  //  RadarRipple ripple;
  Radar radar;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_radar);
    //    ripple = findViewById(R.id.ripple);
    //    ripple.ripple();
    radar = findViewById(R.id.radar);
    radar.start();
  }

  @Override
  protected void onStart() {
    super.onStart();
    //    if (ripple != null) {
    //      ripple.onStart();
    //    }
    if (radar != null) {
      radar.onStart();
    }
  }

  @Override
  protected void onStop() {
    //    if (ripple != null) {
    //      ripple.onStop();
    //    }
    if (radar != null) {
      radar.onStop();
    }
    super.onStop();
  }
}
