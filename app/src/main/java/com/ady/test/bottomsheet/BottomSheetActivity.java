package com.ady.test.bottomsheet;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.MenuBuilder;

import com.ady.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ady on 2018/1/29.
 */

public class BottomSheetActivity extends FragmentActivity implements ItemListDialogFragment.Listener {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_bottom_sheet);
    init();
  }

  private void init() {
//    BottomNavigationMenuView menuView = findViewById(R.id.bottom_sheet);
//    BottomNavigationItemView itemView = findViewById(R.id.item);
//    ItemListDialogFragment.Data testData = new ItemListDialogFragment.Data(
//        BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher),
//        "test",
//        BitmapFactory.decodeResource(getResources(), R.drawable.self_abc_btn_radio_to_on_mtrl_000)
//    );
//    ArrayList<ItemListDialogFragment.Data> list = new ArrayList<ItemListDialogFragment.Data>() {
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

  @Override
  public void onItemClicked(int position) {

  }
}
