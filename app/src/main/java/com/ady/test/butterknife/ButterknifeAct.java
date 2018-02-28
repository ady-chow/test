package com.ady.test.butterknife;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.Toast;

import com.ady.test.R;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** Created by ady on 2018/2/27. */
public class ButterknifeAct extends Activity {

  @BindView(R.id.btn1)
  Button btn1;

  @BindView(R.id.btn2)
  Button btn2;

  @BindView(R.id.btn3)
  Button btn3;

  @BindString(R.string.app_name)
  String appName;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.act_butterknife);
    ButterKnife.bind(this);
    btn1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
  }

  @OnClick(R.id.btn1)
  void clickBtn1() {
    Toast.makeText(this, "test btn1 " + appName, Toast.LENGTH_SHORT).show();
  }
}
