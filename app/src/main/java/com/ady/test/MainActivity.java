package com.ady.test;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.ady.test.anim.BoneAnimAct;
import com.ady.test.anim.LottieAnimActivity;
import com.ady.test.bottomsheet.BottomSheetActivity;
import com.ady.test.bottomsheet.ItemListDialogFragment;
import com.ady.test.components.ActionBarActivity;
import com.ady.test.customedview.CustomActivity;
import com.ady.test.fresco.FrescoActivity;
import com.ady.test.radar.RadarAct;
import com.ady.test.rx.RxJavaTester;
import com.ady.test.span.SpanActivity;

/** Created by zhouxinyuan on 2018/1/15. */
public class MainActivity extends FragmentActivity implements ItemListDialogFragment.Listener {

  @SuppressLint("StaticFieldLeak")
  public static Activity act;

  public static final String TAG = "ady";

  // 调用系统相册-选择图片
  private static final int IMAGE = 1;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    act = this;
    setContentView(R.layout.activity_main);
    final RxJavaTester rxJavaTester = new RxJavaTester();
    findViewById(R.id.test_string).setOnClickListener(v -> rxJavaTester.testString2());
    findViewById(R.id.test_drawable).setOnClickListener(v -> rxJavaTester.testDrawable());
    findViewById(R.id.test_scheduler).setOnClickListener(v -> rxJavaTester.testScheduler());
    findViewById(R.id.test_transform).setOnClickListener(v -> rxJavaTester.testTransform2());
    findViewById(R.id.test_radio_btn).setOnClickListener(v -> testRadioButton());
    findViewById(R.id.test_spannable_string).setOnClickListener(v -> testSpannableString());
    findViewById(R.id.test_custom_view).setOnClickListener(v -> testCustomView());
    findViewById(R.id.test_date_picker)
        .setOnClickListener(
            v -> {
              testDatePicker();
            });
    findViewById(R.id.test_gallery)
        .setOnClickListener(
            v -> {
              testGallery();
            });
    findViewById(R.id.test_bottom_sheet).setOnClickListener(v -> testBottomSheet());
    findViewById(R.id.test_fresco).setOnClickListener(v -> testFresco());
    findViewById(R.id.test_anim_lottie).setOnClickListener(v -> testAnim());
    findViewById(R.id.test_actionbar).setOnClickListener(v -> testActionBar());
    //    testAdapter();
    findViewById(R.id.test_anim_bone)
        .setOnClickListener(v -> startActivity(new Intent(this, BoneAnimAct.class)));
    start(R.id.test_radar, RadarAct.class);
  }

  private void start(int id, Class<?> cls) {
    findViewById(id).setOnClickListener(v -> startActivity(new Intent(this, cls)));
  }

  private void testActionBar() {
    startActivity(new Intent(this, ActionBarActivity.class));
  }

  private void testAnim() {
    startActivity(new Intent(this, LottieAnimActivity.class));
  }

  private void testFresco() {
    startActivity(new Intent(this, FrescoActivity.class));
  }

  void testRadioButton() {
    Log.d(TAG, "testRadioButton: ");
  }

  void testSpannableString() {
    startActivity(new Intent(this, SpanActivity.class));
  }

  void testCustomView() {
    startActivity(new Intent(this, CustomActivity.class));
  }

  void testDatePicker() {
    Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.date_picker_dialog);
    DatePicker datePicker = dialog.findViewById(R.id.date_picker);
    dialog
        .findViewById(R.id.cancel)
        .setOnClickListener(
            c -> {
              dialog.dismiss();
            });
    dialog
        .findViewById(R.id.ok)
        .setOnClickListener(
            c -> {
              Toast.makeText(
                      act,
                      datePicker.getYear()
                          + "-"
                          + datePicker.getMonth()
                          + "-"
                          + datePicker.getDayOfMonth(),
                      Toast.LENGTH_SHORT)
                  .show();
            });
    dialog.show();
  }

  void testGallery() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
          == PackageManager.PERMISSION_GRANTED) {
        openGallery();
      } else {
        //        requestPermissions();
      }
    }
  }

  void openGallery() {
    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(intent, IMAGE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    // 获取图片路径
    if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
      Uri selectedImage = data.getData();
      Log.d(TAG, "onActivityResult: selectedImage = " + selectedImage);
      String[] filePathColumns = {MediaStore.Images.Media.DATA};
      Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
      c.moveToFirst();
      int columnIndex = c.getColumnIndex(filePathColumns[0]);
      String imagePath = c.getString(columnIndex);
      Log.d(TAG, "onActivityResult: imagePath = " + imagePath);
      showImage(imagePath);
      c.close();
    }
  }

  // 加载图片
  private void showImage(String imaePath) {
    Bitmap bm = BitmapFactory.decodeFile(imaePath);
    Log.d(TAG, "showImage: width = " + bm.getWidth() + ", h = " + bm.getHeight());
    ImageView iv = findViewById(R.id.test_gallery_image);
    iv.setImageBitmap(bm);
  }

  private void testBottomSheet() {
    startActivity(new Intent(this, BottomSheetActivity.class));
    //    ItemListDialogFragment.Data testData = new ItemListDialogFragment.Data(
    //        BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher),
    //        "test",
    //        BitmapFactory.decodeResource(getResources(),
    // R.drawable.self_abc_btn_radio_to_on_mtrl_000)
    //    );
    //    ArrayList<ItemListDialogFragment.Data> list = new ArrayList<ItemListDialogFragment.Data>()
    // {
    //      {
    //        add(testData);
    //        add(testData);
    //        add(testData);
    //      }
    //    };
    //    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    //    ItemListDialogFragment f = ItemListDialogFragment.newInstance(list);
    //    ft.add(f, "bottom_sheet").commit();
  }

  private void testAdapter() {
    //    MyAdapter adapter = new MyAdapter(this);
    //    ListView listView = findViewById(R.id.list);
    //    listView.setAdapter(adapter);
    //    adapter.render(new ArrayList<String>() {{
    //      add("test1");
    //      add("test2");
    //    }});
  }

  @Override
  public void onItemClicked(int position) {
    Log.d(TAG, "onItemClicked: position = " + position);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add("test");
    return true;
  }
}
