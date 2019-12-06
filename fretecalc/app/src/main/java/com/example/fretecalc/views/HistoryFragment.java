package com.example.fretecalc.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fretecalc.R;
import com.example.fretecalc.models.routes.RouteSearch;
import com.example.fretecalc.models.routes.Search;
import com.example.fretecalc.viewmodels.HistoryFragmentViewModel;
import com.example.fretecalc.views.adapters.RecyclerHistoryAdapter;
import com.example.fretecalc.views.interfaces.HistoryListener;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class HistoryFragment extends Fragment implements HistoryListener {
    public static final String SEARCH_DB_KEY = "localsearch";
    private RecyclerView recyclerHistory;
    private RecyclerHistoryAdapter adapter;
    private List<RouteSearch> routeSearchList = new ArrayList<>();
    private HistoryFragmentViewModel viewModel;
    private TextView labelRecent;

    public HistoryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        initViews(view);

        viewModel.getList();

        viewModel.getRouteSearchList().observe(this, routeSearches -> {
            if (routeSearches != null && !routeSearches.isEmpty()) {
                adapter.update(routeSearches);
            } else {
                adapter.update(this.routeSearchList);
            }
        });

        return view;
    }

    private void initViews(View view) {
        recyclerHistory = view.findViewById(R.id.recycler_history_list);
        viewModel = ViewModelProviders.of(this).get(HistoryFragmentViewModel.class);
        adapter = new RecyclerHistoryAdapter(routeSearchList, this);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerHistory.setAdapter(adapter);
    }

    @Override
    public void onHistoryClick(Search search) {
        Intent intent = new Intent(getContext(), ResultActivity.class);
        intent.putExtra(SEARCH_DB_KEY, search.getId());
        startActivity(intent);
    }
}
