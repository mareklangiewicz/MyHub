package pl.mareklangiewicz.mygithub.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import pl.mareklangiewicz.mygithub.MyAccountPresenter;
import pl.mareklangiewicz.mygithub.mvp.IMyAccountView;
import pl.mareklangiewicz.mygithub.R;
import pl.mareklangiewicz.mygithub.data.Note;

public class MyAccountFragment extends MyFragment implements IMyAccountView {

    // TODO LATER: implement AutoCompleteTextView instead of EditText for login field (propose users already saved in local db)

    private @Nullable String mStatus;
    private @Nullable String mLogin;
    private @Nullable String mPassword;
    private @Nullable String mOtp;
    private @Nullable String mAvatar;
    private @Nullable String mName;
    private @Nullable String mDescription;

    private int mProgress = HIDDEN;

    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.status) TextView mStatusTextView;
    @Bind(R.id.edit_text_login) EditText mLoginEditText;
    @Bind(R.id.edit_text_password) EditText mPasswordEditText;
    @Bind(R.id.edit_text_otp) EditText mOtpEditText;
    @Bind(R.id.login_button) Button mLoginButton;
    @Bind(R.id.avatar) ImageView mAvatarImageView;
    @Bind(R.id.name) TextView mNameTextView;
    @Bind(R.id.description) TextView mDescriptionTextView;
    @Nullable RecyclerView mRecyclerView;

    @Inject NotesAdapter mAdapter;
    @Inject MyAccountPresenter mPresenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MGApplication) getActivity().getApplication()).getComponent().inject(this);
        mAdapter.setNotes(Collections.singletonList(new Note("No info", "Log in to get info")));
        mPresenter.attachIView(this);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState); //just for logging

        View rootView = inflater.inflate(R.layout.mg_fragment_my_account, container, false);

        ButterKnife.bind(this, rootView);

        setProgress(mProgress);

        mLoginEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) { mLogin = s.toString(); }
        });

        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) { mPassword = s.toString(); }
        });

        mOtpEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) { mOtp = s.toString(); }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mPresenter.onLoginButtonClick();
            }
        });

        inflateHeader(R.layout.mg_myaccount_local_header);
        //noinspection ConstantConditions
        mRecyclerView = ButterKnife.findById(getHeader(), R.id.notes_recycler_view);
        //noinspection ConstantConditions
        mRecyclerView.setLayoutManager(new WCLinearLayoutManager(getActivity())); // LLM that understands "wrap_content"
        mRecyclerView.setAdapter(mAdapter);

        if(savedInstanceState != null) {
            setStatus(mStatus);
            setLogin(mLogin);
            setPassword(mPassword);
            setOtp(mOtp);
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
        mPresenter.detachIView();
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


    @Nullable @Override public String getStatus() {
        return mStatus;
    }

    @Nullable @Override public String getLogin() {
        return mLogin;
    }

    @Nullable @Override public String getPassword() {
        return mPassword;
    }

    @Nullable @Override public String getOtp() {
        return mOtp;
    }

    @Nullable @Override public String getAvatar() {
        return mAvatar;
    }

    @Nullable @Override public String getName() {
        return mName;
    }

    @Nullable @Override public String getDescription() {
        return mDescription;
    }


    @Override public void setStatus(@Nullable String status) {
        mStatus = status;
        if(mStatusTextView != null)
            mStatusTextView.setText(status);
    }

    @Override public void setLogin(@Nullable String login) {
        mLogin = login;
        if(mLoginEditText != null)
            mLoginEditText.setText(login);
    }

    @Override public void setPassword(@Nullable String password) {
        mPassword = password;
        if(mPasswordEditText != null)
            mPasswordEditText.setText(password);
    }

    @Override public void setOtp(@Nullable String otp) {
        mOtp = otp;
        if(mOtpEditText != null)
            mOtpEditText.setText(otp);
    }


    @Override public void setAvatar(@Nullable String avatar) {
        mAvatar = avatar;
        if(mAvatarImageView != null) {
            if(mAvatar == null || mAvatar.isEmpty())
                mAvatarImageView.setImageResource(R.drawable.mg_avatar);
            else
                Picasso.with(getActivity()).load(mAvatar).into(mAvatarImageView);
            //TODO LATER: handle invalid urls
        }

    }

    @Override public void setName(@Nullable String name) {
        mName = name;
        if(mNameTextView != null)
            mNameTextView.setText(mName == null ? "" : mName);
    }


    @Override public void setDescription(@Nullable String description) {
        mDescription = description;
        if(mDescriptionTextView != null)
            mDescriptionTextView.setText(mDescription == null ? "" : mDescription);
    }

}
