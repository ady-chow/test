package com.ady.test.rx;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;

/** Created by ady on 2018/3/7. */
public class RxJavaTester01 {

  static int i = 10;

  public static void main(String[] args) {

    Observable<Integer> observable =
        Observable.create(
            new Observable.OnSubscribe<Integer>() {
              @Override
              public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(i);
                subscriber.onCompleted();
              }
            });

    i = 15;

    observable.subscribe(
        new Observer<Integer>() {

          @Override
          public void onCompleted() {
            System.out.println("对complete事件作出响应");
          }

          @Override
          public void onError(Throwable e) {
            System.out.println("对error事件作出响应");
          }

          @Override
          public void onNext(Integer integer) {
            System.out.println("接收到的整数是：" + integer);
          }
        });
  }
}
