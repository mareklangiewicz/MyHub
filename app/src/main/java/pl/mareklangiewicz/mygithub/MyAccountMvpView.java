package pl.mareklangiewicz.mygithub;

import android.net.Uri;
import android.support.annotation.Nullable;

public interface MyAccountMvpView extends NotesMvpView, ProgressMvpView {

    @Nullable Uri getAvatar();
    void setAvatar(@Nullable Uri avatar);

    @Nullable String getName();
    void setName(@Nullable String name);

    @Nullable String getDescription();
    void setDescription(@Nullable String description);
}
