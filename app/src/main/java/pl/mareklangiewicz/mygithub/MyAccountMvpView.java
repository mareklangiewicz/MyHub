package pl.mareklangiewicz.mygithub;

import android.support.annotation.Nullable;

public interface MyAccountMvpView extends NotesMvpView, ProgressMvpView {

    @Nullable String getStatus();
    @Nullable String getLogin();
    @Nullable String getPassword();
    @Nullable String getOtp();
    @Nullable String getAvatar();
    @Nullable String getName();
    @Nullable String getDescription();

    void setStatus(@Nullable String status);
    void setLogin(@Nullable String login);
    void setPassword(@Nullable String password);
    void setOtp(@Nullable String otp);
    void setAvatar(@Nullable String avatar);
    void setName(@Nullable String name);
    void setDescription(@Nullable String description);

}
