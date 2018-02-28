package com.ady.test.anim;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ady.test.R;
import com.sunshine.engine.bone.StageView;
import com.sunshine.engine.particle.SceneView;

import static com.ady.test.anim.AnimHelper.ANIMATIONS;
import static com.ady.test.anim.AnimHelper.LIKE;
import static com.ady.test.anim.AnimHelper.PASS;

/** Created by ady on 2018/2/22. */
public class BoneAnimAct extends Activity {
  StageView pass;
  EditText percent;
  Button play;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_bone_anim);
    percent = findViewById(R.id.percent);
    play = findViewById(R.id.play);
    pass = findViewById(R.id.dislike);
    play.setOnClickListener(
        v -> {
          try {
            pass.play(ANIMATIONS.get(PASS));
            pass.setPercent(Float.valueOf(percent.getText().toString()), 500);
          } catch (Exception e) {
            Log.e("ady", "onCreate: ", e);
          }
        });
  }

  @Override
  protected void onResume() {
    pass.play(ANIMATIONS.get(PASS));
    pass.setPercent(0);
    super.onResume();
  }

  @Override
  protected void onPause() {
    pass.stop();
    super.onPause();
  }
}
