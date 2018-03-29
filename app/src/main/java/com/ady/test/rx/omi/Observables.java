package com.ady.test.rx.omi;


import rx.Observable;
import rx.functions.Action0;

/** Created by san on 08/02/2018. */
public final class Observables {
  private Observables() {}

  public static Observable<Unit> fromAction(Action0 action) {
    return Observable.create(
        subscriber -> {
          action.call();
          subscriber.onNext(Unit.UNIT);
          subscriber.onCompleted();
        });
  }

  public static <T> Observable<T> fromProvider(Provider<T> provider) {
    return Observable.create(
        subscriber ->
            provider.call(
                t -> {
                  subscriber.onNext(t);
                  subscriber.onCompleted();
                }));
  }
}
