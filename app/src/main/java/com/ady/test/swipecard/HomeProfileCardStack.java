package com.ady.test.swipecard;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.OvershootInterpolator;


import java.util.ArrayList;
import java.util.List;


public class HomeProfileCardStack extends VSwipeStack<HomeProfileCard> {

  public HomeProfileCardStack(Context context) {
    super(context);
  }

  public HomeProfileCardStack(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public HomeProfileCardStack(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void animateInCards(int baseDelay) {
    float scaleSize = 1 - (scaleDown * 2);
    long duration = 800;
    List<Animator> animators = new ArrayList<>();
    for (int i = 0; i < nonAnimatingCardCounts(); i++) {
      // i is the count for home_profile_card from the top
      HomeProfileCard card = cardFromTop(i);

      // the top home_profile_card will overshoot more
      OvershootInterpolator overshootInterp =
          new OvershootInterpolator(nonAnimatingCardCounts() - i - 1);

      long delay = (long) (i * duration * 0.11f) + baseDelay;
      long alphaDelay = (long) (i * duration * 0.15f) + baseDelay;
      card.setBasedScale(scaleSize);
      animators.add(Anu.prop(card, "basedScale", delay, duration, overshootInterp, 1));
      if (card.getBasedTranslationY() != 0) {
        animators.add(Anu.prop(card, "basedTranslationY", baseDelay, duration, null, 0));
      }
      card.setAlpha(0);
      if (i == 0) {
        animators.add(
            Anu.end(
                Anu.prop(card, "alpha", alphaDelay, (long) (duration * 0.45f), null, 1f),
                () -> {
                  for (int j = 0; j < getChildCount(); j++) {
                    HomeProfileCard k = cardFromTop(j);
                    if (k != null) k.setAlpha(1);
                  }
                }));
      }
    }
    AnimatorSet set = new AnimatorSet();
    set.playTogether(animators);
    Anu.a(this, set);
  }
}
