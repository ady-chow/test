package com.ady.test.swipecard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ady.test.R;

import java.util.List;

/** Created by ady on 2018/1/31. */
public class HomeProfileCard extends VSwipeCard {

  /* GENERATED CODE START home_profile_card */

  public LinearLayout _user_info;
  public VForceRelayoutText _user_summary;
  //  public VForceRelayoutText _user_desc;

  /* GENERATED CODE END home_profile_card */

  private static final String TAG = "ady";

  {
    setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.card_shadow));
  }

  public HomeProfileCard(Context context) {
    super(context);
    isEmpty = true;
    addView(new View(getContext()));
  }

  public HomeProfileCard(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    _user_summary = findViewById(R.id.user_summary);
    //    _user_desc = findViewById(R.id.user_desc);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (isEmpty) {
      heightMeasureSpec = MeasureSpec.makeMeasureSpec(Metrics.dp(30), MeasureSpec.EXACTLY);
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  public void render(User user) {
    Drawable d = getResources().getDrawable(R.drawable.ic_main_female);

    /* user_summary includes: name, age, gender */
    _user_summary.setText(
        String.format(getContext().getString(R.string.USER_PROFILE_SUMMARY), user.name, user.age));
    _user_summary.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, d, null);
    Log.d(TAG, "render: " + _user_summary.getText() + ", " + _user_summary.getMeasuredWidth());
    Vu.withMeasureNonZeroSize(
        _user_summary,
        (w, h) -> {
          Log.d(TAG, "render: w = " + w + ", h = " + h);
//          _user_summary.
        });

    //    List<String> languages = user.profile.languages;
    //    String religion = user.profile.religion;
    //    String work = user.profile.job;
    //    String school = user.profile.school;
    //
    //    /* user_desc includes: languages, religion, work or school */
    //    _user_desc.setText(
    //        TagHelper.profileHomeCardTags(
    //            languages, religion, TextUtils.isEmpty(work) ? school : work));
    //
    //    if (user.pictures != null && user.pictures.size() > 0) {
    //      String url = user.pictures.get(0).url;
    //      App.image.localFile(_image, url);
    //    }
  }
}
