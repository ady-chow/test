package com.ady.test.rx.omi;

import com.tantanapp.common.android.app.App;
import com.tantanapp.common.android.app.LifecycleEvent;

import java.util.concurrent.TimeUnit;

import rx.Notification;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Actions;
import rx.functions.Func0;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/** Created by molikto on 08/25/15. */
public class Rxu {

  public static Subscription delayedSubscription(
      Func0<Subscription> org, Scheduler scheduler, int time, TimeUnit unit) {
    return Subscriptions.create(
        new Action0() {
          Subscription subs;
          boolean cancelled = false;

          {
            scheduler
                .createWorker()
                .schedule(
                    () -> {
                      synchronized (this) {
                        if (!cancelled) {
                          subs = org.call();
                        }
                      }
                    },
                    time,
                    unit);
          }

          @Override
          public void call() {
            synchronized (this) {
              cancelled = true;
              if (subs != null) {
                subs.unsubscribe();
              }
            }
          }
        });
  }

  public static <T> Subscriber<T> ignore() {
    return Subscribers.from(
        new Observer<T>() {
          @Override
          public void onCompleted() {}

          @Override
          public void onError(Throwable e) {}

          @Override
          public void onNext(T t) {}
        });
  }

  public static <T> Observable.Transformer<T, T> itm() {
    return o -> o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
  }

  public static <T> Observable.Transformer<T, Notification<T>> reportThenMaterialize() {
    return o -> o.doOnError(e -> App.me.logError(e)).materialize();
  }

  public static <T> Observable.Transformer<T, Notification<T>> printThenMaterialize() {
    return o -> o.doOnError(e -> App.me.logError(e)).materialize();
  }

  /**
   * lazy: lazy subscribe, only the first time lifecycle shows true we subscribe to original lazy
   * onNext, only lifecycle is lifecycleBoolean onNext is called, it will cache the latest onNext
   * during the negative lifecycle when lifecycle completes, the how observable completes and it is
   * unsubscribed from original
   */
  public static <T> Observable<T> lazyWithLifecycle(
      final Func0<Observable<T>> source,
      final Observable<LifecycleEvent> lifecycle,
      final boolean delayOnNegative) {
    return throttle(
        source, delayOnNegative ? lifecycle.map(e -> e.positive) : lifecycle.map(e -> true));
  }

  public static <T> Observable<T> throttle(
      Func0<Observable<T>> source, Observable<Boolean> throttle) {
    return throttle(source, throttle, false);
  }

  /**
   * Returns an Observable that emits items only when throttle is open. Like {@link
   * Observable#sample} but allow to send two kind throttle events: throttle-open and
   * throttle-close, and allow to connect upstream lazily(with {@code initiallyOpen} set to {@code
   * false}).
   *
   * <p>When received an item from the {@code source} Observable:
   *
   * <ul>
   *   <li>if throttle is open, emit the item immediately
   *   <li>if throttle is closed, cache the item and drop the last cached item(if existing)
   * </ul>
   *
   * When the {@code throttle} Observable emits an ({@code} Boolean}):
   *
   * <ul>
   *   <li>if {@code true}: set throttle to open; if there is a cached item, emit it and clear the
   *       cache
   *   <li>if {@code false}: set throttle to closed
   * </ul>
   *
   * Emits and error when received error from the {@code source} Observable or the {@code throttle}
   * Observable. Complete when the {@code source} Observable or the {@code throttle} Observable is
   * completed.
   *
   * @param source the upstream Observable
   * @param throttle the throttle Observable
   * @param initiallyOpen set {@code} true to set throttle open when the returned Observable is
   *     subscribed
   * @param <T> the item class
   * @return an Observable that only emits items when throttle is open
   */
  public static <T> Observable<T> throttle(
      Func0<Observable<T>> source, Observable<Boolean> throttle, boolean initiallyOpen) {
    return Observable.create(new ThrottleOnSubscribe<>(source, throttle, initiallyOpen));
  }

  public static Subscriber<? super Object> print() {
    return Subscribers.create(Actions.empty(), e -> App.me.logError(e), Actions.empty());
  }

  public static Subscription computation(Action0 action) {
    return Observables.fromAction(action)
        .subscribeOn(Schedulers.computation())
        .compose(Rxu.printThenMaterialize())
        .subscribe();
  }

  public static Subscription io(Action0 action) {
    return Observables.fromAction(action)
        .subscribeOn(Schedulers.io())
        .compose(Rxu.printThenMaterialize())
        .subscribe();
  }

  public static Subscription ui(Action0 action) {
    return Observables.fromAction(action).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
  }

  public static Subscription ui(Action0 action, long delay) {
    return Observable.timer(delay, TimeUnit.MILLISECONDS)
        .flatMap(time -> Observables.fromAction(action).subscribeOn(AndroidSchedulers.mainThread()))
        .subscribe();
  }
}
