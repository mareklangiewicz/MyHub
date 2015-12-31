package pl.mareklangiewicz.mygithub;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MyReposMvpPresenter extends MvpPresenter<MyReposMvpView> {

    private @NonNull MGMvpModel mModel;

    @Inject
    MyReposMvpPresenter(@NonNull MGMvpModel model) {
        mModel = model;
    }

    Observable<List<Repo>> getRepos(String user) {
        return mModel.getReposFromGitHub(user);
    }

    @Override public void attachView(@NonNull MyReposMvpView mvpView) {
        super.attachView(mvpView);
        getRepos("JakeWharton")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Repo>>() {
                    @Override public void call(List<Repo> repos) {
                        //noinspection ConstantConditions
                        getMvpView().setRepos(repos);
                    }
                });
    }

    // TODO NOW

}
