package com.ady.test.rx.omi;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.observers.Subscribers;

/** See {@link Rxu#throttle(Func0, Observable, boolean)} */
class ThrottleOnSubscribe<T> implements Observable.OnSubscribe<T> {
  private final AtomicBoolean open = new AtomicBoolean();
  private final Func0<Observable<T>> source;
  private final Observable<Boolean> throttle;
  private final boolean initiallyOpen;

  ThrottleOnSubscribe(
      Func0<Observable<T>> source, Observable<Boolean> throttle, boolean initiallyOpen) {
    this.source = source;
    this.throttle = throttle;
    this.initiallyOpen = initiallyOpen;
  }

  @Override
  public void call(final Subscriber<? super T> subscriber) {
    final AtomicReference<T> last = new AtomicReference<>();

    final Subscriber connector = Subscribers.empty();

    final Subscriber<T> delegate =
        Subscribers.create(
            t -> {
              if (open.get()) {
                subscriber.onNext(t);
              } else {
                last.set(t);
              }
            },
            err -> {
              last.set(null);
              subscriber.onError(err);
            },
            () -> {
              last.set(null);
              subscriber.onCompleted();
            });

    final boolean[] connected = {false};

    if (initiallyOpen) {
      connector.add(source.call().subscribe(delegate));
      connected[0] = true;
    }

    connector.add(
        throttle.subscribe(
            open -> {
              this.open.set(open);
              if (open) {
                if (!connected[0]) {
                  connector.add(source.call().subscribe(delegate));
                  connected[0] = true;
                }
                final T cached = last.get();
                if (cached != null) {
                  subscriber.onNext(cached);
                  last.set(null);
                }
              }
            },
            err -> {
              last.set(null);
              subscriber.onError(err);
            },
            () -> {
              last.set(null);
              subscriber.onCompleted();
            }));

    subscriber.add(connector);
  }
}
