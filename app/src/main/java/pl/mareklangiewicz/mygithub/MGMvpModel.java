package pl.mareklangiewicz.mygithub;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.mareklangiewicz.mygithub.io.GitHub;
import rx.Observable;
import rx.functions.Func1;

@Singleton
public class MGMvpModel implements MvpModel {

    private @NonNull GitHub.Service mGitHubService;

    @Inject
    MGMvpModel(@NonNull GitHub.Service github) {
        mGitHubService = github;
        // TODO NOW
    }


    Observable<List<Repo>> getReposFromGitHub(String user) {
        return mGitHubService.getUserReposObservable(user)
                .map(new Func1<List<GitHub.Repository>, List<Repo>>() {
                    @Override public List<Repo> call(List<GitHub.Repository> repositories) {
                        ArrayList<Repo> result = new ArrayList<>(repositories.size());
                        for(GitHub.Repository r: repositories)
                            result.add(new Repo(r.name, r.description, r.forks_count, r.watchers_count, r.stargazers_count));
                        return result;
                    }
                });
    }


    // TODO NOW
}
