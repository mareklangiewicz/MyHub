package pl.mareklangiewicz.mygithub;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.mareklangiewicz.mygithub.data.Account;
import pl.mareklangiewicz.mygithub.data.Note;
import pl.mareklangiewicz.mygithub.data.Repo;
import pl.mareklangiewicz.mygithub.io.GitHub;
import rx.Observable;
import rx.functions.Func1;

import static pl.mareklangiewicz.myutils.MyTextUtils.str;

@Singleton
public class MGMvpModel implements MvpModel {

    private @NonNull GitHub.Service mGitHubService;

    @Inject MGMvpModel(@NonNull GitHub.Service github) {
        mGitHubService = github;
        // TODO NOW
    }


    Observable<List<Repo>> getRepos(String user) {
        return mGitHubService.getUserReposObservable(user)
                .map(new Func1<List<GitHub.Repository>, List<Repo>>() {
                    @Override public List<Repo> call(List<GitHub.Repository> repositories) {
                        ArrayList<Repo> result = new ArrayList<>(repositories.size());
                        for(GitHub.Repository r : repositories)
                            result.add(new Repo(r.name, r.description, r.forks_count, r.watchers_count, r.stargazers_count));
                        return result;
                    }
                });
    }

    public Observable<Account> getAccount(String user) {
        return mGitHubService.getUserObservable(user)
                .map(new Func1<GitHub.User, Account>() {
                    @Override public Account call(GitHub.User user) {
                        String description = String.format("%s\n%s\n%s",
                                user.location == null ? "" : user.location,
                                user.email == null ? "" : user.email,
                                user.company == null ? "" : user.company
                        );
                        //TODO NOW: comment not important notes
                        Account account = new Account(Uri.parse(user.avatar_url), user.name, description,
                                new Note("Name", user.name),
                                new Note("Location", user.location),
                                new Note("Login", user.login),
                                new Note("Type", user.type),
                                new Note("E-Mail", user.email),
                                new Note("GitHub Page", user.html_url),
                                new Note("Company", user.company),
                                new Note("Blog", user.blog),
                                new Note("Public Repos", str(user.public_repos)),
                                new Note("Public Gists", str(user.public_gists)),
                                new Note("Followers", str(user.followers)),
                                new Note("Following", str(user.following)),
                                new Note("Created At", user.created_at),
                                new Note("Updated At", user.updated_at)
                        );

                        if(user.site_admin          != null) account.notes.add(new Note("Site Admin", user.site_admin ? "YES" : "NO"));
                        if(user.hireable            != null) account.notes.add(new Note("Hireable"  , user.hireable   ? "YES" : "NO"));
                        if(user.bio                 != null) account.notes.add(new Note("Bio"       , user.bio));
                        if(user.total_private_repos != null) account.notes.add(new Note("Total Private Repos", str(user.total_private_repos)));
                        if(user.owned_private_repos != null) account.notes.add(new Note("Owned Private Repos", str(user.owned_private_repos)));
                        if(user.private_gists       != null) account.notes.add(new Note("Private Gists", str(user.private_gists)));
                        if(user.disk_usage          != null) account.notes.add(new Note("Disk Usage", str(user.disk_usage)));
                        if(user.collaborators       != null) account.notes.add(new Note("Collaborators", str(user.collaborators)));
//                        if(user.plan                != null) account.notes.add(new Note("Plan", str(user.plan))); // TODO SOMEDAY: better plan representation
                        return account;
                    }
                });
    }

}
