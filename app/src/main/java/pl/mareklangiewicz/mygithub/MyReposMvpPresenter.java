package pl.mareklangiewicz.mygithub;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.noveogroup.android.log.MyLogger;

import java.util.List;

import javax.inject.Inject;

import pl.mareklangiewicz.mygithub.data.Account;
import pl.mareklangiewicz.mygithub.data.Repo;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

@MainThread
public class MyReposMvpPresenter extends MvpPresenter<MyReposMvpView> {

    private MyLogger log = MyLogger.UIL;

    private @NonNull MGMvpModel mModel;
    private @Nullable Subscription subscription;

    @Inject MyReposMvpPresenter(@NonNull MGMvpModel model) {
        mModel = model;
    }

    Observable<List<Repo>> getRepos() {
        return mModel.loadLatestAccount().map(new Func1<Account, List<Repo>>() {
            @Override public List<Repo> call(Account account) {
                return account == null ? null : account.getRepos();
            }
        });
    }

    @Override public void attachView(@NonNull MyReposMvpView mvpView) {
        super.attachView(mvpView);
        //noinspection ConstantConditions
        subscription = getRepos()
                .subscribe(new Observer<List<Repo>>() {
                    @Override public void onCompleted() {
                        log.v("loading completed.");
                    }

                    @Override public void onError(Throwable e) {
                        log.e(e);
                    }

                    @Override public void onNext(List<Repo> repos) {
                        //noinspection ConstantConditions
                        getMvpView().setRepos(repos);
                    }
                });
    }

    @Override public void detachView() {
        if(subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
        subscription = null;
        super.detachView();
    }
}
