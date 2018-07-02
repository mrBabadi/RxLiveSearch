package com.babadi.rxlivesearch;

import android.annotation.SuppressLint;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.babadi.rxlivesearch.adapter.TownCursorAdapter;
import com.babadi.rxlivesearch.presenter.RxSearchEngine;
import com.babadi.rxlivesearch.remote.ApiClient;
import com.babadi.rxlivesearch.remote.model.TownModel;
import com.babadi.rxlivesearch.view.SearchResults;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements SearchResults {

    private String TAG = "MainActivity";
    private SearchView searchView;
    private ProgressBar progressBar;
    Disposable searchDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.searching_progress);
        searchView = findViewById(R.id.searchVieww);

        searchDisposable = RxSearchEngine.fromView(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String text) throws Exception {
                        if (text.isEmpty() || text.length() < 2) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                })
                .distinctUntilChanged()
                .subscribeOn(AndroidSchedulers.mainThread())
                .switchMap(new Function<String, Observable<List<TownModel>>>() {
                    @Override
                    public Observable<List<TownModel>> apply(String query) throws Exception {
                        return dataFromNetwork(query);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TownModel>>() {
                    @Override
                    public void accept(List<TownModel> result) throws Exception {
                        Log.e("MAIN_MAIN", result.size() + " /");
                        queryResults(result);
                    }
                });
    }

    private Observable<List<TownModel>> dataFromNetwork(final String query) {
        return ApiClient.getClient().searchTown(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void queryResults(List<TownModel> result) {
        // Cursor
        String[] columns = new String[]{"_id", "townID", "townName"};
        Object[] temp = new Object[]{0, "default", "default"};

        MatrixCursor cursor = new MatrixCursor(columns);

        for (int i = 0; i < result.size(); i++) {

            temp[0] = i;
            temp[1] = result.get(i).getTownId();
            temp[2] = result.get(i).getTownNameFa();

            cursor.addRow(temp);
        }

        searchView.setSuggestionsAdapter(new TownCursorAdapter(this, cursor, searchView));
    }


    @Override
    public void onShow() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHide() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailedToFind() {
    }

    @Override
    public void onResult(List<TownModel> townsList, CharSequence chara) {
        /*townAdapter.clear();*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (searchDisposable != null) {
            if (!searchDisposable.isDisposed()) {
                searchDisposable.dispose();
                searchDisposable = null;
            }
        }
    }
}
