package pl.mareklangiewicz.myhub.mvp;

import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Base class for presenters in MVP pattern.
 */
@MainThread
public class Presenter<T extends IView> {

    private @Nullable T mIView;

    @CallSuper
    public void attachIView(@NonNull T iview) {
        mIView = iview;
    }

    @CallSuper
    public void detachIView() {
        mIView = null;
    }

    /**
     * @return attached view. null if no view is attached
     */
    public final @Nullable T getIView() {
        return mIView;
    }

}
