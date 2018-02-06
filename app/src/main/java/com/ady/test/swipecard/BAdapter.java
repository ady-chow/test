package com.ady.test.swipecard;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;


import rx.Observable;
import rx.subjects.PublishSubject;

/** User: molikto Date: 01/04/15 Time: 18:47 */
public abstract class BAdapter<T> extends BaseAdapter implements AbsListView.RecyclerListener {

  View adaptingView = null;

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    int viewType = getItemViewType(position);
    if (convertView == null) {
      convertView = inflate(parent, viewType);
    }
    adaptingView = convertView;
    adapt(convertView, (T) getItem(position), viewType, position);
    onViewRequested(position);
    return convertView;
  }

  public abstract View inflate(ViewGroup parent, int itemViewType);

  public abstract void
  adapt(View convertView, T item, int itemViewType, int position);

  public void onViewRequested(int position) {}

  PublishSubject<View> scraps = PublishSubject.create();

  @Override
  public void onMovedToScrapHeap(View view) {
    if (view != null) scraps.onNext(view);
  }

//  public <V> Observable<V> duringCreated(Observable<V> source) {
//    View adapting = adaptingView;
//    Activity act = (Activity) adapting.getContext();
//    Observable<Boolean> throttle =
//        act.lifecycleBoolean().takeUntil(scraps.filter(a -> a == adapting));
//    return Rxu.throttle(() -> source, throttle);
//  }
}
