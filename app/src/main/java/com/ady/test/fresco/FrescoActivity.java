package com.ady.test.fresco;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ady.test.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

/** Created by ady on 2018/2/7. */
public class FrescoActivity extends Activity {

  private SimpleDraweeView simpleDraweeView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fresco);
    simpleDraweeView = findViewById(R.id.simple_draw);
    testSimpleDraweeView();
  }

  private void testSimpleDraweeView() {
    Uri uri =
        Uri.fromFile(
            new File(Environment.getExternalStorageDirectory() + File.separator + "test.jpg"));
    uri = Uri.parse("res://drawable-xhdpi/" + R.drawable.drawer_banner);
    simpleDraweeView.setImageURI(uri);
    Log.d("ady", "testSimpleDraweeView: " + uri);
  }
}
