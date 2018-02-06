package com.ady.test.rx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.ady.test.R;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.ady.test.MainActivity.TAG;
import static com.ady.test.MainActivity.act;

/**
 * Created by zhouxinyuan on 2018/1/18.
 */

public class RxJavaTester {

  /**
   * 变换一：map 事件对象的直接变换，一对一变换
   */
  public void testTransform1() {
    Log.d(TAG, "testTransform1: ");
    Observable.just(R.drawable.ic_launcher) // 输入类型 Integer
        .map(new Func1<Integer, Bitmap>() {
          @Override
          public Bitmap call(Integer drawableRes) { // 参数类型 Integer
            return BitmapFactory.decodeResource(act.getResources(), drawableRes); // 返回类型 Bitmap
          }
        })
        .subscribe(new Action1<Bitmap>() {
          @Override
          public void call(Bitmap bitmap) { // 参数类型 Bitmap
            ((ImageView) act.findViewById(R.id.test_img_2)).setImageBitmap(bitmap);
          }
        });
  }

  /**
   * 变换二：flatMap, 把事件拆成了两级，通过一组新创建的 Observable 将初始的对象『铺平』之后通过统一路径分发了下去
   */
  public void testTransform2() {
    Log.d(TAG, "testTransform2: ");
    Student[] students = new Student[]{
        new Student("ady", new Course("math"), new Course("english")),
        new Student("bob", new Course("chinese"), new Course("music"))
    };
    Subscriber<Course> subscriber = new Subscriber<Course>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onNext(Course course) {
        Log.d(TAG, course.name);
      }
    };
    Observable.from(students)
        .flatMap(new Func1<Student, Observable<Course>>() {
          @Override
          public Observable<Course> call(Student student) {
            return Observable.from(student.courses);
          }
        })
        .subscribe(subscriber);
  }

  /**
   * 后台线程取数据，主线程显示, subscribeOn: 事件产生的线程 observeOn: 事件消费的线程
   */
  public void testScheduler() {
    Log.d(TAG, "testScheduler: ");
    Observable.just(1, 2, 3, 4)
        .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
        .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
        .subscribe(new Action1<Integer>() {
          @Override
          public void call(Integer number) {
            Log.d(TAG, "number:" + number);
          }
        });
  }

  public void testDrawable() {
    Log.d(TAG, "testDrawable: ");
    final int drawableRes = R.drawable.ic_launcher;
    final ImageView imageView = act.findViewById(R.id.test_img_1);
    Observable.create(new Observable.OnSubscribe<Drawable>() {
      @Override
      public void call(Subscriber<? super Drawable> subscriber) {
        Drawable drawable = act.getResources().getDrawable(drawableRes);
        subscriber.onNext(drawable);
        subscriber.onCompleted();
      }
    }).subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
        .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
        .subscribe(new Observer<Drawable>() {
          @Override
          public void onNext(Drawable drawable) {
            imageView.setImageDrawable(drawable);
          }

          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            Toast.makeText(act, "Error!", Toast.LENGTH_SHORT).show();
          }
        });
  }

  /**
   * subscribe 完整定义的回调
   */
  public void testString1() {
    Log.d(TAG, "testString1: ");
    observable3.subscribe(subscriber);
  }

  /**
   * subscribe 还支持不完整定义的回调
   */
  public void testString2() {
    Log.d(TAG, "testString2: ");
    Action1<String> onNextAction = new Action1<String>() {
      @Override
      public void call(String s) {
        Log.d(TAG, "next: " + s);
      }
    };
    Action1<Throwable> onErrorAction = new Action1<Throwable>() {
      @Override
      public void call(Throwable throwable) {
        Log.d(TAG, "error: " + throwable);
      }
    };
    Action0 onCompletedAction = new Action0() {
      @Override
      public void call() {
        Log.d(TAG, "completed: ");
      }
    };
    Log.d(TAG, "test2: 自动创建 subscriber，并使用 onNextAction 来定义 onNext");
    observable2.subscribe(onNextAction);
    Log.d(TAG, "test2: 自动创建 subscriber，并使用 onNextAction, onErrorAction 来定义 onNext, onError");
    observable2.subscribe(onNextAction, onErrorAction);
    Log.d(TAG, "test2: 自动创建 subscriber，并使用 onNextAction, onErrorAction, onCompletedAction 来定义 onNext, onError, onCompleted");
    observable2.subscribe(onNextAction, onErrorAction, onCompletedAction);
  }

  /**
   * 被观察者一, create：创建事件队列的方法
   */
  Observable<String> observable1 = Observable.create(new Observable.OnSubscribe<String>() {
    @Override
    public void call(Subscriber<? super String> subscriber) {
      subscriber.onNext("Hello");
      subscriber.onNext("Hi");
      subscriber.onNext("Aloha");
      subscriber.onCompleted();
    }
  });

  /**
   * 被观察者二，just: 快捷创建事件队列，将传入的参数依次传递出来
   */
  Observable<String> observable2 = Observable.just("hi", "ady", "~");

  /**
   * 被观察者三，from: 快捷创建事件队列，将传入的数组或 Iterable 对象拆分成具体对象后，依次发送出来
   */
  String[] words = new String[]{"hello", "ady", "~"};
  Observable<String> observable3 = Observable.from(words);

  /**
   * 观察者一
   */
  Observer<String> observer = new Observer<String>() {
    @Override
    public void onNext(String s) {
      Log.d(TAG, "Item: " + s);
    }

    @Override
    public void onCompleted() {
      Log.d(TAG, "Completed!");
    }

    @Override
    public void onError(Throwable e) {
      Log.d(TAG, "Error!");
    }
  };

  /**
   * 观察者二
   */
  Subscriber<String> subscriber = new Subscriber<String>() {
    @Override
    public void onCompleted() {
      Log.d(TAG, "onCompleted: ");
    }

    @Override
    public void onError(Throwable e) {
      Log.d(TAG, "onError: ");
    }

    @Override
    public void onNext(String s) {
      Log.d(TAG, "onNext: " + s);
    }
  };
}
