package pl.mareklangiewicz.mygithub;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.noveogroup.android.log.MyLogger;

import javax.inject.Inject;

import pl.mareklangiewicz.mygithub.data.Account;
import pl.mareklangiewicz.mygithub.mvp.IMyAccountView;
import pl.mareklangiewicz.mygithub.mvp.Presenter;
import pl.mareklangiewicz.mygithub.mvp.IProgressView;
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
                        log.e(e);
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
        //noinspection ConstantConditions
        login(getIView().getLogin(), getIView().getPassword(), getIView().getOtp());
    }

    private void login(@Nullable final String user, @Nullable String password, @Nullable String otp) {
        //noinspection ConstantConditions
        getIView().setProgress(IProgressView.INDETERMINATE);
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
                        log.d(e);
                        IMyAccountView view = getIView();
                        if(view == null)
                            return;
                        view.setProgress(IProgressView.HIDDEN);
                        String msg = e.getLocalizedMessage();
                        if(msg == null || msg.isEmpty()) msg = "load error.";
                        view.setStatus(msg);
                        view.setLogin(user);
                    }

                    @Override public void onNext(Account account) {
                        showAccount(account);
                    }
                });
    }

    private Observable<Account> getAccount(@Nullable String user, @Nullable String password, @Nullable String otp) {
        if(user == null) user = "";
        if(password == null) password = "";
        if(otp == null) otp = "";
        return mModel.loadAccount(user)
                .concatWith(mModel.fetchAccount(user, password, otp));
    }

    private void showAccount(@Nullable Account account) {
        IMyAccountView view = getIView();
        if(view != null) {
            view.setStatus(account != null ? String.format("loaded: %tF %tT.", account.getTime(), account.getTime()) : "not loaded.");
            view.setLogin(account != null ? account.getLogin() : "");
            view.setAvatar(account != null ? account.getAvatar() : "");
            view.setName(account != null ? account.getName() : "");
            view.setDescription(account != null ? account.getDescription() : "");
            view.setNotes(account != null ? account.getNotes() : null);
        }

    }

}
