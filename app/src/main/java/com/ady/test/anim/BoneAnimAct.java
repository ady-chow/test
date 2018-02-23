package com.ady.test.anim;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.ady.test.R;
import com.sunshine.engine.particle.SceneView;

/** Created by ady on 2018/2/22. */
public class BoneAnimAct extends Activity {
  SceneView pass;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_bone_anim);
    pass = findViewById(R.id.dislike);
    pass.setOnClickListener(
        v -> {
          Log.d("ady", "onCreate: bone click");
        });
  }

  @Override
  protected void onResume() {
    super.onResume();
    pass.setVisibility(View.VISIBLE);
    pass.playByAsset("anim/pass/config.xml", "anim/pass/pic");
  }

  @Override
  protected void onPause() {
    pass.stop();
    super.onPause();
  }
}
