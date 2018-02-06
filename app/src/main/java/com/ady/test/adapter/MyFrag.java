package com.ady.test.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ady.test.R;

import java.util.ArrayList;

/**
 * Created by ady on 2018/2/6.
 */

public class MyFrag extends Fragment {

  private ListView listView;
  private MyAdapter adapter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.frag, container, false);
    listView = root.findViewById(R.id.list);
    return root;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    adapter = new MyAdapter(getContext());
    listView.setAdapter(adapter);
    adapter.render(new ArrayList<String>() {{
      add("test1");
      add("test222222222");
    }});
  }
}
