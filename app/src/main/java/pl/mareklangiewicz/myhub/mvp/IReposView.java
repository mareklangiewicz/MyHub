package pl.mareklangiewicz.myhub.mvp;

import android.support.annotation.Nullable;

import java.util.List;

import pl.mareklangiewicz.myhub.data.Repo;

public interface IReposView extends IView {

    @Nullable String getStatus();
    @Nullable List<Repo> getRepos();

    void setStatus(@Nullable String status);
    void setRepos(@Nullable List<Repo> repos);
}
