package com.ady.test.anim;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ady.test.R;
import com.airbnb.lottie.LottieAnimationView;

/** Created by ady on 2018/2/12. */
public class AnimActivity extends Activity {

  LottieAnimationView like;
  LottieAnimationView pass;
  LottieAnimationView match;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_anim);
    like = findViewById(R.id.like_anim);
    like.setAnimation(R.raw.like);
    like.loop(true);
    like.playAnimation();

    pass = findViewById(R.id.pass_anim);
    pass.setAnimation(R.raw.pass);
    pass.loop(true);
    pass.playAnimation();

    match = findViewById(R.id.match_anim);
    match.setAnimation(R.raw.match);
    match.loop(true);
    match.playAnimation();
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (!like.isAnimating()) {
      like.resumeAnimation();
    }
    if (!pass.isAnimating()) {
      pass.resumeAnimation();
    }
    if (!match.isAnimating()) {
      match.resumeAnimation();
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (like.isAnimating()) {
      like.pauseAnimation();
    }
    if (pass.isAnimating()) {
      pass.pauseAnimation();
    }
    if (match.isAnimating()) {
      match.pauseAnimation();
    }
  }
}
