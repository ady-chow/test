package com.ady.test.swipecard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ady.test.R;

import java.util.ArrayList;

/** Created by san on 08/01/2018. */
public class HomeFrag extends Fragment {

  /* GENERATED CODE START home */

  public HomeProfileCardStack _stack;
  public LinearLayout _buttons;
  public ImageView _dislike;
  public ImageView _like;

  /* GENERATED CODE END home */

  private HomeProfileAdapter adapter;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.home, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    _stack = view.findViewById(R.id.stack);
    adapter = new HomeProfileAdapter(getContext());
    _stack.setAdapter(adapter);
    adapter.renderUsers(
        new ArrayList<String>() {
          {
            add("1");
            add("2");
            add("3");
          }
        });
  }
}
