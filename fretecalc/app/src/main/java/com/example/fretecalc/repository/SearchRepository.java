package com.example.fretecalc.repository;

import android.content.Context;

import com.example.fretecalc.models.routes.Search;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface SearchRepository {

    Single<Long> saveSearch(Context context, Search search);

    Observable<List<Search>> getRecentSearches(Context context);

    Observable<Search> getSearchById(Context context, Long id);
}
