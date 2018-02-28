package com.ady.test.anim;

import android.support.v4.util.Pair;

import com.sunshine.engine.bone.StageView;

import java.util.HashMap;

/** Created by ady on 2018/2/28. */
public class AnimHelper {
  public static HashMap<String, String> ANIMATIONS = new HashMap<>();
  public static final String LIKE = "like";
  public static final String PASS = "pass";

  static {
    ANIMATIONS.put(LIKE, "animations/like/");
    ANIMATIONS.put(PASS, "animations/pass/");
  }

  public static void reset(StageView stageView, String folderPath) {
    stageView.play(folderPath);
    stageView.setPercent(0);
  }

  public static void play(StageView stageView, String folderPath) {
    stageView.play(folderPath);
    stageView.setPercent(1);
  }

  public static void play(StageView stageView, String folderPath, float percent) {
    stageView.play(folderPath);
    stageView.setPercent(percent);
  }
}
