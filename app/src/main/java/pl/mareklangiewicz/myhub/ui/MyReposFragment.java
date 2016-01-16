package pl.mareklangiewicz.myhub.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.mareklangiewicz.myfragments.MyFragment;
import pl.mareklangiewicz.myhub.MGApplication;
import pl.mareklangiewicz.myhub.data.Note;
import pl.mareklangiewicz.myhub.mvp.IMyReposView;
import pl.mareklangiewicz.myhub.MyReposPresenter;
import pl.mareklangiewicz.myhub.R;
import pl.mareklangiewicz.myhub.data.Repo;

public class MyReposFragment extends MyFragment implements IMyReposView, ReposAdapter.Callback {

    // TODO LATER: local search on ToolBar
    // TODO SOMEDAY: local menu with sorting options?

    private int mProgress = HIDDEN;
    private @Nullable String mStatus;

    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.status) TextView mStatusTextView;
    @Bind(R.id.repos_recycler_view) RecyclerView mReposRecyclerView;

    @Nullable RecyclerView mNotesRecyclerView;

    @Inject ReposAdapter mReposAdapter;
    @Inject NotesAdapter mNotesAdapter;

    @Inject MyReposPresenter mPresenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MGApplication)getActivity().getApplication()).getComponent().inject(this);
        mReposAdapter.setCallback(this);
        mNotesAdapter.setNotes(Collections.singletonList(new Note("No details", "Select repository to get details")));
        mPresenter.attachIView(this);
    }

    @Override
    public @Nullable View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState); //just for logging

        View rootView = inflater.inflate(R.layout.mg_fragment_my_repos, container, false);

        ButterKnife.bind(this, rootView);

        setProgress(mProgress);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        //noinspection ConstantConditions
        mReposRecyclerView.setLayoutManager(manager);
        mReposRecyclerView.setAdapter(mReposAdapter);

        inflateHeader(R.layout.mg_notes);
        //noinspection ConstantConditions
        mNotesRecyclerView = ButterKnife.findById(getHeader(), R.id.notes_recycler_view);
        //noinspection ConstantConditions
        mNotesRecyclerView.setLayoutManager(new WCLinearLayoutManager(getActivity())); // LLM that understands "wrap_content"
        mNotesRecyclerView.setAdapter(mNotesAdapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        mReposRecyclerView.setAdapter(null);
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override public void onDestroy() {
        mPresenter.detachIView();
        super.onDestroy();
    }

    @Override public int getProgress() {
        return mProgress;
    }

    // TODO LATER: move "progress" stuff to some base fragment class? or to custom view?
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

    @Nullable @Override public String getStatus() {
        return mStatus;
    }

    @Override public void setStatus(@Nullable String status) {
        mStatus = status;
        if(mStatusTextView != null)
            mStatusTextView.setText(status);
    }

    @Override public void setRepos(@Nullable List<Repo> repos) {
        mReposAdapter.setRepos(repos);
    }

    @Override public @Nullable List<Repo> getRepos() {
        return mReposAdapter.getRepos();
    }

    @Override public void onItemClick(Repo repo) {
        if(repo == null) {
            log.d("Clicked repo is null.");
            return;
        }
        mNotesAdapter.setNotes(repo.getNotes());
        ((MainActivity)getActivity()).showLocalNavigation();

    }
}
