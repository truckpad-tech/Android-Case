package com.example.fretecalc.utils;

import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public abstract class SingleClickListener implements View.OnClickListener {
    private static final long THRESHOLD_MILLIS = 1000L;
    private final PublishSubject<View> viewPublishSubject = PublishSubject.<View>create();

    public SingleClickListener() {
        viewPublishSubject.throttleFirst(THRESHOLD_MILLIS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view -> onClicked(view));
    }

    @Override
    public void onClick(View v) {
        viewPublishSubject.onNext(v);
    }

    public abstract void onClicked(View v);
}