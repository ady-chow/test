package com.ady.test.customedview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ady.test.R;

/**
 * Created by zhouxinyuan on 2018/1/21.
 */

public class CustomActivity extends Activity {

  LinearLayout mLL;
  ImageView mIV;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_custom);
    mLL = findViewById(R.id.custom_area);
    mIV = findViewById(R.id.custom_drawable);
    testDrawable();
    mLL.addView(new MyBitmap(this, getBitmap(this, R.drawable.ic_profile_tag_delete)));
  }

  private void testDrawable() {
    Bitmap bitmap = getBitmap(this, R.drawable.ic_profile_tag_delete);
    Log.d("ady", "testDrawable: bm = " + bitmap);
    if (bitmap != null) {
      MyDrawable drawable = new MyDrawable(bitmap);
      mIV.setImageDrawable(drawable);
    }
  }

  private static Bitmap getBitmap(Context context, int vectorDrawableId) {
    Bitmap bitmap = null;
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
      Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
      if (vectorDrawable != null) {
        bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
            vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        Log.d("ady", "getBitmap: w = " + vectorDrawable.getIntrinsicWidth() + ", h = " + canvas.getHeight());
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
      }
    } else {
      bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
    }
    return bitmap;
  }

}
