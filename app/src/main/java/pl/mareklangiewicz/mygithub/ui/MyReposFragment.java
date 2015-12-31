package pl.mareklangiewicz.mygithub.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import pl.mareklangiewicz.myfragments.MyFragment;
import pl.mareklangiewicz.mygithub.MGApplication;
import pl.mareklangiewicz.mygithub.MyReposMvpView;
import pl.mareklangiewicz.mygithub.MyReposMvpPresenter;
import pl.mareklangiewicz.mygithub.R;
import pl.mareklangiewicz.mygithub.Repo;

public class MyReposFragment extends MyFragment implements MyReposMvpView {

    private @Nullable RecyclerView mRecyclerView;

    @Inject ReposAdapter mAdapter;
    @Inject MyReposMvpPresenter mMvpPresenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MGApplication)getActivity().getApplication()).getComponent().inject(this);
        mMvpPresenter.attachView(this);
    }

    @Override
    public @Nullable View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState); //just for logging
        View rootView = inflater.inflate(R.layout.mg_fragment_my_repos, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.repos_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        //noinspection ConstantConditions
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        //noinspection ConstantConditions
        mRecyclerView.setAdapter(null);
        mRecyclerView = null;
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        mMvpPresenter.detachView();
        super.onDestroy();
    }

    @Override public void setProgress(int progress) {
        // TODO NOW
    }

    @Override public int getProgress() {
        // TODO NOW
        return UNKNOWN;
    }

    @Override public void setRepos(@Nullable List<Repo> repos) {
        mAdapter.setRepos(repos);
    }

    @Override public @Nullable List<Repo> getRepos() {
        return mAdapter.getRepos();
    }
}
