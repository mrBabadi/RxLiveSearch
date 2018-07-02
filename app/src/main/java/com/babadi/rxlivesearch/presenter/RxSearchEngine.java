package com.babadi.rxlivesearch.presenter;

import android.support.v7.widget.SearchView;

import com.babadi.rxlivesearch.remote.ApiClient;
import com.babadi.rxlivesearch.remote.model.TownModel;
import com.babadi.rxlivesearch.view.SearchResults;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RxSearchEngine {

    private Disposable dispose;
    private SearchResults searchListener;

    public RxSearchEngine(SearchResults searchListener) {
        this.searchListener = searchListener;
    }

    public void search(Observable<String> stringObservable) {
        stringObservable.observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<CharSequence>() {
                    @Override
                    public void accept(final CharSequence s){
                        if (searchListener == null) {
                            return;
                        }
                        if (s.toString().length() < 3) {
                            return;
                        }
                        searchListener.onShow();
                        dispose = ApiClient.getClient().searchTown(s.toString())
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<TownModel>>() {
                                    @Override
                                    public void accept(List<TownModel> townList) throws Exception {
                                        if (searchListener == null) {
                                            return;
                                        }
                                        searchListener.onHide();
                                        searchListener.onResult(townList,s);
                                    }
                                });
                    }

                }).subscribe();
    }

    public static Observable<String> fromView(SearchView searchView) {

        final PublishSubject<String> subject = PublishSubject.create();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                subject.onComplete();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                subject.onNext(text);
                return true;
            }
        });

        return subject;
    }

    public void disposeSearchObservable() {
        if (dispose != null) {
            if (!dispose.isDisposed()) {
                dispose.dispose();
                dispose = null;
            }
        }
        searchListener = null;
    }
}
