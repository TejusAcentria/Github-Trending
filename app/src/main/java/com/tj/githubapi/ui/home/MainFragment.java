package com.tj.githubapi.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tj.githubapi.App;
import com.tj.githubapi.R;
import com.tj.githubapi.data.DataManager;
import com.tj.githubapi.ui.base.BaseFragment;
import com.tj.githubapi.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends BaseFragment<MainViewModel> implements SwipeRefreshLayout.OnRefreshListener {
    private View mView;
    private MainAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    public static MainFragment getInstance() {
        return new MainFragment();
    }

    @Override
    public MainViewModel getViewModel() {
        MainViewModelFactory factory = new MainViewModelFactory(DataManager.getInstance(App.getInstance()));
        return new ViewModelProvider(getActivity(), factory).get(MainViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, mView);
        initView();
        setupRecycler();

        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.getRepos().observe(this, itemModels -> {
            mAdapter.addData(itemModels);
            updateRefreshLayout(false);
        });
        viewModel.getError().observe(this, isError -> {
            if (isError) {
                displaySnackbar(true, "Can't load more github repos");
                updateRefreshLayout(false);
            }
        });

        if (DataManager.getInstance(App.getInstance()).getDate() == null)
            DataManager.getInstance(App.getInstance()).setDate(Util.getDefaultDate());

        updateRefreshLayout(true);
        displaySnackbar(false, "Loading...");
        viewModel.loadRepos(DataManager.getInstance(App.getInstance()).getDate());

    }

    private void initView() {
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setupRecycler() {
        mLayoutManager = new LinearLayoutManager(App.getInstance());
        mAdapter = new MainAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onRefresh() {
        mAdapter.clearData();

        if (Util.isNetworkAvailable(App.getInstance())) {
            showError(View.GONE);
            displaySnackbar(false, "Loading...");
            viewModel.loadRepos(DataManager.getInstance(App.getInstance()).getDate());
        } else {
            updateRefreshLayout(false);
            showError(View.VISIBLE);
            displaySnackbar(true, "No Internet Connection :(");
        }
    }

    private void updateRefreshLayout(boolean refresh) {
        mSwipeRefreshLayout.setRefreshing(refresh);
    }

    private void showError(int Visibility) {
        getActivity().findViewById(R.id.sample_main_layout).findViewById(R.id.imgview).setVisibility(Visibility);
    }

    private void displaySnackbar(boolean isError, String message) {
        Util.showSnack(mView, isError, message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onClear();
    }

}



