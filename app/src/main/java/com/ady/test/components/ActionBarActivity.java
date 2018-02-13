package com.ady.test.components;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;

import com.ady.test.R;

/** Created by ady on 2018/2/13. */
public class ActionBarActivity extends Activity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_action_bar);
    final ActionBar actionBar = getActionBar();
    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_profile_other_top));
    actionBar.setCustomView(R.layout.myactionbar); // 自定义ActionBar布局
    // TODO: 2018/2/13 immersive status bar
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add("hello");
    return true;
  }
}
