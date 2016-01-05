package pl.mareklangiewicz.mygithub;

import android.net.Uri;
import android.support.annotation.Nullable;

public interface MyAccountMvpView extends NotesMvpView, ProgressMvpView {

    @Nullable String getLogin();
    @Nullable String getPassword();
    @Nullable String getOtp();
    @Nullable Uri getAvatar();
    @Nullable String getName();
    @Nullable String getDescription();

    void setLogin(@Nullable String login);
    void setPassword(@Nullable String password);
    void setOtp(@Nullable String otp);
    void setAvatar(@Nullable Uri avatar);
    void setName(@Nullable String name);
    void setDescription(@Nullable String description);

}
