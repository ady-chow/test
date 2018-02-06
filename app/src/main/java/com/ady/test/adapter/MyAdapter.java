package com.ady.test.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ady.test.App;
import com.ady.test.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by ady on 2018/2/1.
 */

public class MyAdapter extends BaseAdapter {

  private Context mContext;

  public MyAdapter(Context context) {
    this.mContext = context;
  }

  private List<String> data = new ArrayList<>();

  @Override
  public int getCount() {
    return data.size();
  }

  @Override
  public Object getItem(int position) {
    return data.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
    }
    ((MyItem) convertView).render(data.get(position));
    return convertView;
  }

  public void render(List<String> list) {
    data = list;
    notifyDataSetChanged();
  }
}
