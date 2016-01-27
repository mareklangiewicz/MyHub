package pl.mareklangiewicz.myhub.ui;

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
import pl.mareklangiewicz.myhub.MGApplication;
import pl.mareklangiewicz.myhub.MyAccountPresenter;
import pl.mareklangiewicz.myhub.mvp.IMyAccountView;
import pl.mareklangiewicz.myhub.R;
import pl.mareklangiewicz.myhub.data.Note;

public class MyAccountFragment extends MyFragment implements IMyAccountView {

    // TODO LATER: implement AutoCompleteTextView instead of EditText for login field (propose users already saved in local db)

    private @Nullable String status;
    private @Nullable String login;
    private @Nullable String password;
    private @Nullable String otp;
    private @Nullable String avatar;
    private @Nullable String name;
    private @Nullable String description;

    private int progress = HIDDEN;

    @Bind(R.id.progress_bar) ProgressBar progressBar;
    @Bind(R.id.status) TextView statusTextView;
    @Bind(R.id.edit_text_login) EditText loginEditText;
    @Bind(R.id.edit_text_password) EditText passwordEditText;
    @Bind(R.id.edit_text_otp) EditText otpEditText;
    @Bind(R.id.login_button) Button loginButton;
    @Bind(R.id.avatar) ImageView avatarImageView;
    @Bind(R.id.name) TextView nameTextView;
    @Bind(R.id.description) TextView descriptionTextView;
    @Nullable RecyclerView recyclerView;

    @Inject NotesAdapter adapter;
    @Inject MyAccountPresenter presenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MGApplication) getActivity().getApplication()).getComponent().inject(this);
        adapter.setNotes(Collections.singletonList(new Note("No info", "Log in to get info")));
        presenter.attachIView(this);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState); //just for logging

        View rootView = inflater.inflate(R.layout.mg_fragment_my_account, container, false);

        ButterKnife.bind(this, rootView);

        setProgress(progress);

        loginEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) { login = s.toString(); }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) { password = s.toString(); }
        });

        otpEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override public void afterTextChanged(Editable s) { otp = s.toString(); }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                presenter.onLoginButtonClick();
            }
        });

        inflateHeader(R.layout.mg_notes);
        //noinspection ConstantConditions
        recyclerView = ButterKnife.findById(getHeader(), R.id.notes_recycler_view);
        //noinspection ConstantConditions
        recyclerView.setLayoutManager(new WCLinearLayoutManager(getActivity())); // LLM that understands "wrap_content"
        recyclerView.setAdapter(adapter);

        if(savedInstanceState != null) {
            setStatus(status);
            setLogin(login);
            setPassword(password);
            setOtp(otp);
            setAvatar(avatar);
            setName(name);
            setDescription(description);
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(recyclerView != null)
            recyclerView.setAdapter(null);
        recyclerView = null;
        ButterKnife.unbind(this);
    }

    @Override public void onDestroy() {
        presenter.detachIView();
        super.onDestroy();
    }

    @Override public int getProgress() {
        return progress;
    }

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

    @Nullable @Override public List<Note> getNotes() {
        return adapter.getNotes();
    }

    @Override public void setNotes(@Nullable List<Note> notes) {
        adapter.setNotes(notes);
    }


    @Nullable @Override public String getStatus() {
        return status;
    }

    @Nullable @Override public String getLogin() {
        return login;
    }

    @Nullable @Override public String getPassword() {
        return password;
    }

    @Nullable @Override public String getOtp() {
        return otp;
    }

    @Nullable @Override public String getAvatar() {
        return avatar;
    }

    @Nullable @Override public String getName() {
        return name;
    }

    @Nullable @Override public String getDescription() {
        return description;
    }


    @Override public void setStatus(@Nullable String status) {
        this.status = status;
        if(statusTextView != null)
            statusTextView.setText(status);
    }

    @Override public void setLogin(@Nullable String login) {
        this.login = login;
        if(loginEditText != null)
            loginEditText.setText(login);
    }

    @Override public void setPassword(@Nullable String password) {
        this.password = password;
        if(passwordEditText != null)
            passwordEditText.setText(password);
    }

    @Override public void setOtp(@Nullable String otp) {
        this.otp = otp;
        if(otpEditText != null)
            otpEditText.setText(otp);
    }


    @Override public void setAvatar(@Nullable String avatar) {
        this.avatar = avatar;
        if(avatarImageView != null) {
            if(this.avatar == null || this.avatar.isEmpty())
                avatarImageView.setImageResource(R.drawable.mg_avatar);
            else
                Picasso.with(getActivity()).load(this.avatar).into(avatarImageView);
            //TODO LATER: handle invalid urls
        }

    }

    @Override public void setName(@Nullable String name) {
        this.name = name;
        if(nameTextView != null)
            nameTextView.setText(this.name == null ? "" : this.name);
    }


    @Override public void setDescription(@Nullable String description) {
        this.description = description;
        if(descriptionTextView != null)
            descriptionTextView.setText(this.description == null ? "" : this.description);
    }

}
