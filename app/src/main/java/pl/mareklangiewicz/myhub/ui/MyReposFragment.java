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

    private int progress = HIDDEN;
    private @Nullable String status;

    @Bind(R.id.progress_bar) ProgressBar progressBar;
    @Bind(R.id.status) TextView statusTextView;
    @Bind(R.id.repos_recycler_view) RecyclerView reposRecyclerView;

    @Nullable RecyclerView notesRecyclerView;

    @Inject ReposAdapter reposAdapter;
    @Inject NotesAdapter notesAdapter;

    @Inject MyReposPresenter presenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MGApplication)getActivity().getApplication()).getComponent().inject(this);
        reposAdapter.setCallback(this);
        notesAdapter.setNotes(Collections.singletonList(new Note("No details", "Select repository to get details")));
        presenter.attachIView(this);
    }

    @Override
    public @Nullable View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState); //just for logging

        View rootView = inflater.inflate(R.layout.mg_fragment_my_repos, container, false);

        ButterKnife.bind(this, rootView);

        setProgress(progress);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        //noinspection ConstantConditions
        reposRecyclerView.setLayoutManager(manager);
        reposRecyclerView.setAdapter(reposAdapter);

        inflateHeader(R.layout.mg_notes);
        //noinspection ConstantConditions
        notesRecyclerView = ButterKnife.findById(getHeader(), R.id.notes_recycler_view);
        //noinspection ConstantConditions
        notesRecyclerView.setLayoutManager(new WCLinearLayoutManager(getActivity())); // LLM that understands "wrap_content"
        notesRecyclerView.setAdapter(notesAdapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        reposRecyclerView.setAdapter(null);
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override public void onDestroy() {
        presenter.detachIView();
        super.onDestroy();
    }

    @Override public int getProgress() {
        return progress;
    }

    // TODO LATER: move "progress" stuff to some base fragment class? or to custom view?
    @Override public void setProgress(int progress) {
        if(progress == INDETERMINATE) {
            if(progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(MIN);
                progressBar.setIndeterminate(true);
            }
        }
        else if(progress == HIDDEN) {
            if(progressBar != null) {
                progressBar.setVisibility(View.INVISIBLE);
                progressBar.setProgress(MIN);
                progressBar.setIndeterminate(false);
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
            if(progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);
                progressBar.setIndeterminate(false);
            }
        }
        this.progress = progress;
    }

    @Nullable @Override public String getStatus() {
        return status;
    }

    @Override public void setStatus(@Nullable String status) {
        this.status = status;
        if(statusTextView != null)
            statusTextView.setText(status);
    }

    @Override public void setRepos(@Nullable List<Repo> repos) {
        reposAdapter.setRepos(repos);
    }

    @Override public @Nullable List<Repo> getRepos() {
        return reposAdapter.getRepos();
    }

    @Override public void onItemClick(Repo repo) {
        if(repo == null) {
            log.d("Clicked repo is null.");
            return;
        }
        notesAdapter.setNotes(repo.getNotes());
        ((MainActivity)getActivity()).showLocalNavigation();

    }
}
