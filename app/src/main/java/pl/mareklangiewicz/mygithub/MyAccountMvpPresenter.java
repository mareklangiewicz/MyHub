package pl.mareklangiewicz.mygithub;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.noveogroup.android.log.MyLogger;

import javax.inject.Inject;

import pl.mareklangiewicz.mygithub.data.Account;
import rx.Observer;

@MainThread
public class MyAccountMvpPresenter extends MvpPresenter<MyAccountMvpView> {

    private MyLogger log = MyLogger.UIL;

    private @NonNull MGMvpModel mModel;

    @Inject MyAccountMvpPresenter(@NonNull MGMvpModel model) {
        mModel = model;
    }

    @Override public void attachView(@NonNull MyAccountMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void onLoginButtonClick() {
        //noinspection ConstantConditions
        login(getMvpView().getLogin(), getMvpView().getPassword(), getMvpView().getOtp());
    }

    private void login(@Nullable String user, @Nullable String password, @Nullable String otp) {
        //noinspection ConstantConditions
        getMvpView().setProgress(ProgressMvpView.INDETERMINATE);
        mModel.getAccount(user == null ? "" : user, password == null ? "" : password, otp == null ? "" : otp)
                .subscribe(new Observer<Account>() {
                    @Override public void onCompleted() {
                        if(isViewAttached()) {
                            //noinspection ConstantConditions
                            getMvpView().setProgress(ProgressMvpView.HIDDEN);
                        }
                        log.v("loading completed.");
                    }
                    @Override public void onError(Throwable e) {
                        if(isViewAttached()) {
                            //noinspection ConstantConditions
                            getMvpView().setProgress(ProgressMvpView.HIDDEN);
                        }
                        log.e(e);
                    }
                    @Override public void onNext(Account account) {
                        if(isViewAttached()) {
                            //noinspection ConstantConditions
                            getMvpView().setAvatar(account.avatar);
                            getMvpView().setName(account.name);
                            getMvpView().setDescription(account.description);
                            getMvpView().setNotes(account.notes);
                        }
                    }
                });
    }

}
