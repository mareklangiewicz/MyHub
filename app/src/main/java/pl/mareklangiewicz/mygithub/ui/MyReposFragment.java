package pl.mareklangiewicz.mygithub.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;

import pl.mareklangiewicz.myfragments.MyFragment;
import pl.mareklangiewicz.mygithub.MGApplication;
import pl.mareklangiewicz.mygithub.MyReposMvpView;
import pl.mareklangiewicz.mygithub.MyReposMvpPresenter;
import pl.mareklangiewicz.mygithub.R;
import pl.mareklangiewicz.mygithub.Repo;

public class MyReposFragment extends MyFragment implements MyReposMvpView {

    private int mProgress = MIN;

    private @Nullable ProgressBar mProgressBar;
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

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        setProgress(mProgress);

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
        mProgressBar = null;
        super.onDestroyView();
    }

    @Override public void onDestroy() {
        mMvpPresenter.detachView();
        super.onDestroy();
    }

    @Override public int getProgress() {
        return mProgress;
    }

    @Override public void setProgress(int progress) {
        if(progress == INDETERMINATE) {
            if(mProgressBar != null) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(MIN);
                mProgressBar.setIndeterminate(true);
            }
        }
        else if(progress == HIDDEN) {
            if(mProgressBar != null) {
                mProgressBar.setVisibility(View.INVISIBLE);
                mProgressBar.setProgress(MIN);
                mProgressBar.setIndeterminate(false);
            }
        }
        else {
            if(progress < MIN) {
                log.w("correcting progress value from %d to %d", progress, MIN);
                progress = MIN;
            }
            else if(progress > MAX) {
                log.w("correcting progress value from %d to %d", progress, MAX);
                progress = MAX;
            }
            if(mProgressBar != null) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(progress);
                mProgressBar.setIndeterminate(false);
            }
        }
        mProgress = progress;
    }

    @Override public void setRepos(@Nullable List<Repo> repos) {
        mAdapter.setRepos(repos);
    }

    @Override public @Nullable List<Repo> getRepos() {
        return mAdapter.getRepos();
    }
}
