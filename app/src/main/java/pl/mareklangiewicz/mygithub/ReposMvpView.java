package pl.mareklangiewicz.mygithub;

import android.support.annotation.Nullable;

import java.util.List;

import pl.mareklangiewicz.mygithub.data.Repo;

public interface ReposMvpView extends MvpView {
    void setRepos(@Nullable List<Repo> repos);
    @Nullable List<Repo> getRepos();
}
