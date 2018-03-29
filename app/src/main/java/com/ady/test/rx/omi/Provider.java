package com.ady.test.rx.omi;

import rx.functions.Action1;

public interface Provider<T> extends Action1<Receiver<T>> {}
