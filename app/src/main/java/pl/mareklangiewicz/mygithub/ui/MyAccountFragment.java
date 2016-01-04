package pl.mareklangiewicz.mygithub.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.mareklangiewicz.myfragments.MyFragment;
import pl.mareklangiewicz.mygithub.MGApplication;
import pl.mareklangiewicz.mygithub.MyAccountMvpView;
import pl.mareklangiewicz.mygithub.MyAcountMvpPresenter;
import pl.mareklangiewicz.mygithub.R;
import pl.mareklangiewicz.mygithub.data.Note;

public class MyAccountFragment extends MyFragment implements MyAccountMvpView {

    private @Nullable Uri mAvatar;
    private @Nullable String mName;
    private @Nullable String mDescription;

    private int mProgress = MIN;

    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.avatar) ImageView mAvatarImageView;
    @Bind(R.id.name) TextView mNameTextView;
    @Bind(R.id.description) TextView mDescriptionTextView;
    @Nullable RecyclerView mRecyclerView;

    @Inject NotesAdapter mAdapter;
    @Inject MyAcountMvpPresenter mMvpPresenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MGApplication) getActivity().getApplication()).getComponent().inject(this);
        mAdapter.setNotes(Collections.singletonList(new Note("No info", "Log in to get info")));
        mMvpPresenter.attachView(this);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState); //just for logging

        View rootView = inflater.inflate(R.layout.mg_fragment_my_account, container, false);

        ButterKnife.bind(this, rootView);

        setProgress(mProgress);

        inflateHeader(R.layout.mg_myaccount_local_header);
        //noinspection ConstantConditions
        mRecyclerView = ButterKnife.findById(getHeader(), R.id.notes_recycler_view);
        //noinspection ConstantConditions
        mRecyclerView.setLayoutManager(new WCLinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        if(savedInstanceState != null) {
            setAvatar(mAvatar);
            setName(mName);
            setDescription(mDescription);
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mRecyclerView != null)
            mRecyclerView.setAdapter(null);
        mRecyclerView = null;
        ButterKnife.unbind(this);
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

    @Nullable @Override public List<Note> getNotes() {
        return mAdapter.getNotes();
    }

    @Override public void setNotes(@Nullable List<Note> notes) {
        mAdapter.setNotes(notes);
    }


    @Nullable @Override public Uri getAvatar() {
        return mAvatar;
    }

    @Override public void setAvatar(@Nullable Uri avatar) {
        mAvatar = avatar;
        if(mAvatar == null)
            mAvatarImageView.setImageResource(R.drawable.mg_avatar);
        else
            Picasso.with(getActivity()).load(avatar).into(mAvatarImageView);
        //TODO LATER: handle invalid urls

    }

    @Nullable @Override public String getName() {
        return mName;
    }

    @Override public void setName(@Nullable String name) {
        mName = name;
        mNameTextView.setText(mName == null ? "" : mName);
    }

    @Nullable @Override public String getDescription() {
        return mDescription;
    }

    @Override public void setDescription(@Nullable String description) {
        mDescription = description;
        mDescriptionTextView.setText(mDescription == null ? "" : mDescription);
    }
}
