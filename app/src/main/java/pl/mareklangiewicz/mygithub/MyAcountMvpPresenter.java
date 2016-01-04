package pl.mareklangiewicz.mygithub;

import android.support.annotation.NonNull;

import com.noveogroup.android.log.MyLogger;

import javax.inject.Inject;

import pl.mareklangiewicz.mygithub.data.Account;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyAcountMvpPresenter extends MvpPresenter<MyAccountMvpView> {

    private MyLogger log = MyLogger.UIL;

    private @NonNull MGMvpModel mModel;

    @Inject MyAcountMvpPresenter(@NonNull MGMvpModel model) {
        mModel = model;
    }

    @Override public void attachView(@NonNull MyAccountMvpView mvpView) {
        super.attachView(mvpView);
        //noinspection ConstantConditions
        getMvpView().setProgress(ProgressMvpView.INDETERMINATE);
        mModel.getAccount("langara")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                        //noinspection ConstantConditions
                        getMvpView().setAvatar(account.avatar);
                        getMvpView().setName(account.name);
                        getMvpView().setDescription(account.description);
                        getMvpView().setNotes(account.notes);
                    }
                });
    }


}
