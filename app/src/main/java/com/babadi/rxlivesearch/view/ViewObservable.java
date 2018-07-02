package com.babadi.rxlivesearch.view;

import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Predicate;

public final class ViewObservable {

    public static Observable<String> createTextChangeObservable(final AppCompatAutoCompleteTextView inputSearch) {
        Observable<String> stringObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                final TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (charSequence.toString().length() < 2) {
                            return;
                        }
                        emitter.onNext(charSequence.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                };

                inputSearch.addTextChangedListener(watcher);

                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        inputSearch.removeTextChangedListener(watcher);
                    }
                });
            }
        });

        return stringObservable
                .filter(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return s.length() > 2;
            }
        }).debounce(1000, TimeUnit.MILLISECONDS);
    }
}
