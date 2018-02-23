package com.ady.test.anim;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.JsonReader;

import com.ady.test.R;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.airbnb.lottie.parser.LottieCompositionParser;

/** Created by ady on 2018/2/12. */
public class LottieAnimActivity extends Activity {

  LottieAnimationView like;
  LottieAnimationView pass;
  LottieAnimationView match;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_anim);
    like = findViewById(R.id.like_anim);
    LottieComposition composition =
        LottieComposition.Factory.fromInputStreamSync(getResources().openRawResource(R.raw.like));
    like.setComposition(composition);
    //    like.playAnimation();
    like.setProgress(0.1f);
    like.postDelayed(() -> like.setProgress(0.2f), 1000);
    like.postDelayed(() -> like.setProgress(0.3f), 2000);
    like.postDelayed(() -> like.setProgress(0.4f), 3000);
    like.postDelayed(() -> like.setProgress(0.5f), 4000);
    like.postDelayed(() -> like.setProgress(0.6f), 5000);
    like.postDelayed(() -> like.setProgress(0.7f), 6000);
    like.postDelayed(() -> like.setProgress(0.8f), 7000);
    like.postDelayed(() -> like.setProgress(0.9f), 8000);

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
    //    if (!like.isAnimating()) {
    //      like.resumeAnimation();
    //    }
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
    //    if (like.isAnimating()) {
    //      like.pauseAnimation();
    //    }
    if (pass.isAnimating()) {
      pass.pauseAnimation();
    }
    if (match.isAnimating()) {
      match.pauseAnimation();
    }
  }
}
