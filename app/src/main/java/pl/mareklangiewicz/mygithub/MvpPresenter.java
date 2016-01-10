package pl.mareklangiewicz.mygithub;

import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Base class for presenters in MVP pattern.
 */
@MainThread
public class MvpPresenter<T extends MvpView> {

    private @Nullable T mMvpView;

    @CallSuper
    public void attachView(@NonNull T mvpView) {
        mMvpView = mvpView;
    }

    @CallSuper
    public void detachView() {
        mMvpView = null;
    }

    /**
     * @return attached view. null if no view is attached
     */
    public final @Nullable T getMvpView() {
        return mMvpView;
    }

}
