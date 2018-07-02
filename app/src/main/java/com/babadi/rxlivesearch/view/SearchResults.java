package com.babadi.rxlivesearch.view;

import com.babadi.rxlivesearch.remote.model.TownModel;

import java.util.List;

public interface SearchResults extends ShowAndDismiss {

    void onResult(List<TownModel> townsList,CharSequence chara);

}
