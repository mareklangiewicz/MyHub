package pl.mareklangiewicz.myhub;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.noveogroup.android.log.MyLogger;

import java.util.Locale;

import javax.inject.Inject;

import pl.mareklangiewicz.myhub.data.Account;
import pl.mareklangiewicz.myhub.mvp.IMyAccountView;
import pl.mareklangiewicz.myhub.mvp.Presenter;
import pl.mareklangiewicz.myhub.mvp.IProgressView;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

@MainThread
public class MyAccountPresenter extends Presenter<IMyAccountView> {

    private MyLogger log = MyLogger.UIL;

    private @NonNull MGModel mModel;
    private @Nullable Subscription subscription;

    @Inject MyAccountPresenter(@NonNull MGModel model) {
        mModel = model;
    }

    @Override public void attachIView(@NonNull IMyAccountView iview) {
        super.attachIView(iview);
        subscription = mModel.loadLatestAccount()
                .subscribe(new Observer<Account>() {
                    @Override public void onCompleted() {
                        log.v("loading completed.");
                    }

                    @Override public void onError(Throwable e) {
                        log.e(e, "[SNACK] Error %s", e.getLocalizedMessage());
                    }

                    @Override public void onNext(Account account) {
                        showAccount(account);
                    }
                });
    }

    @Override public void detachIView() {
        if(subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
        subscription = null;
        super.detachIView();
    }

    public void onLoginButtonClick() {
        IMyAccountView view = getIView();
        if(view == null) {
            log.e("Can not login. View is detached.");
            return;
        }
        login(view.getLogin(), view.getPassword(), view.getOtp());
    }

    private void login(@Nullable final String user, @Nullable String password, @Nullable String otp) {
        IMyAccountView view = getIView();
        if(view == null) {
            log.e("Can not login. View is detached.");
            return;
        }
        view.setProgress(IProgressView.INDETERMINATE);
        if(subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
        subscription = getAccount(user, password, otp)
                .subscribe(new Observer<Account>() {
                    @Override public void onCompleted() {
                        IMyAccountView view = getIView();
                        if(view != null) view.setProgress(IProgressView.HIDDEN);
                        log.v("loading completed.");
                    }

                    @Override public void onError(Throwable e) {
                        log.e(e, "[SNACK]Error %s", e.getLocalizedMessage());
                        IMyAccountView view = getIView();
                        if(view == null)
                            return;
                        view.setProgress(IProgressView.HIDDEN);
                        view.setLogin(user);
                    }

                    @Override public void onNext(Account account) {
                        showAccount(account);
                    }
                });
    }

    /**
     * Tries to load account for given user from local db (realm) and from internet (github) too.
     * Data from local db will come first (can be null), and data from internet will come later (if any).
     * So we should override displayed account data when new data comes.
     */
    private Observable<Account> getAccount(@Nullable String user, @Nullable String password, @Nullable String otp) {
        if(user == null) user = "";
        if(password == null) password = "";
        if(otp == null) otp = "";
        return mModel.loadAccount(user)
                .concatWith(mModel.fetchAccount(user, password, otp));
    }

    /**
     * Displays given account on attached IView. Clears IView if account is null.
     */
    private void showAccount(@Nullable Account account) {
        IMyAccountView view = getIView();
        if(view != null) {
            view.setStatus(account != null ? String.format(Locale.US, "loaded: %tF %tT.", account.getTime(), account.getTime()) : "not loaded.");
            view.setLogin(account != null ? account.getLogin() : "");
            view.setAvatar(account != null ? account.getAvatar() : "");
            view.setName(account != null ? account.getName() : "");
            view.setDescription(account != null ? account.getDescription() : "");
            view.setNotes(account != null ? account.getNotes() : null);
        }

    }

}
