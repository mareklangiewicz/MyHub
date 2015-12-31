package pl.mareklangiewicz.mygithub;

import android.support.annotation.NonNull;

import com.noveogroup.android.log.MyLogger;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyReposMvpPresenter extends MvpPresenter<MyReposMvpView> {

    private MyLogger log = MyLogger.UIL;

    private @NonNull MGMvpModel mModel;

    @Inject MyReposMvpPresenter(@NonNull MGMvpModel model) {
        mModel = model;
    }

    Observable<List<Repo>> getRepos(String user) {
        return mModel.getReposFromGitHub(user);
    }

    @Override public void attachView(@NonNull MyReposMvpView mvpView) {
        super.attachView(mvpView);
        //noinspection ConstantConditions
        getMvpView().setProgress(ProgressMvpView.INDETERMINATE);
        getRepos("JakeWharton")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Repo>>() {
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
                    @Override public void onNext(List<Repo> repos) {
                        //noinspection ConstantConditions
                        getMvpView().setRepos(repos);
                    }
                });
    }


}
