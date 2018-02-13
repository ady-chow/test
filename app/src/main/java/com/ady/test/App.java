package com.ady.test;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/** Created by ady on 2018/2/1. */
public class App extends Application {

  private static Context me;

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    me = base;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Fresco.initialize(this);
  }

  public static Context me() {
    return me;
  }
}
