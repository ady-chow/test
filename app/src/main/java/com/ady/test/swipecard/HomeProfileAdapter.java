package com.ady.test.swipecard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ady.test.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ady on 2018/2/6.
 */
public class HomeProfileAdapter extends AAdapter<String> {

  private Context context;
  private final List<String> userIds = new ArrayList<>();
  private final Map<String, User> userEntities = new HashMap<>();

  public HomeProfileAdapter(Context context) {
    this.context = context;
  }

  @Override
  public List<String> list() {
    return userIds;
  }

  @Override
  public View inflate(ViewGroup parent, int itemViewType) {
    return LayoutInflater.from(context).inflate(R.layout.home_profile_card, parent, false);
  }

  @Override
  public int getItemViewType(int position) {
    return VSwipeStack.VIEW_TYPE_NORMAL;
  }

  @Override
  public void adapt(View convertView, String userId, int itemViewType, int position) {
    HomeProfileCard c = (HomeProfileCard) convertView;
    User u = userEntities.get(userId);
    if (u != null) {
      c.render(u);
    }
  }

  public void renderUsers(List<String> userIds) {
    userEntities.clear();
    this.userIds.clear();
    Cu.foreach(
        userIds,
        (userId) -> {
          User u = new User(userId);
          userEntities.put(userId, u);
          this.userIds.add(userId);
        });
    notifyDataSetChanged();
  }
}
