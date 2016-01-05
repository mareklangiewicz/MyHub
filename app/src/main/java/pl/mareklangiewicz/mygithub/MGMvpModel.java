package pl.mareklangiewicz.mygithub;

import android.net.Uri;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import pl.mareklangiewicz.mygithub.data.Account;
import pl.mareklangiewicz.mygithub.data.Note;
import pl.mareklangiewicz.mygithub.data.Repo;
import pl.mareklangiewicz.mygithub.io.GitHub;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static pl.mareklangiewicz.myutils.MyTextUtils.str;

@Singleton
@MainThread
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
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param user user identifier
     * @param password special case: if password is empty - it just returns public data about given user
     * @param otp if empty: it uses only basic authentication without Two Factor Authentication
     * @return Can return more than one item (first older from cache (mem or db) and later current from github service).
     */
    public Observable<Account> getAccount(@NonNull String user, @NonNull String password, @NonNull String otp) {
        Observable<GitHub.User> oe =
                password.isEmpty()
                        ?
                        mGitHubService.getUserObservable(user)
                        :
                        otp.isEmpty()
                                ?
                                mGitHubService.getUserAuthObservable(encodeBasicAuthHeader(user, password))
                                :
                                mGitHubService.getUserTFAObservable(encodeBasicAuthHeader(user, password), otp);

        return oe
                .map(new Func1<GitHub.User, Account>() {
                    @Override public Account call(GitHub.User user) {
                        return user2account(user);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static Account user2account(GitHub.User user) {
        String description = String.format("%s\n%s\n%s",
                user.location == null ? "" : user.location,
                user.email == null ? "" : user.email,
                user.company == null ? "" : user.company
        );
        //TODO SOMEDAY: comment out not important notes
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

        if(user.site_admin != null) account.notes.add(new Note("Site Admin", user.site_admin ? "YES" : "NO"));
        if(user.hireable != null) account.notes.add(new Note("Hireable", user.hireable ? "YES" : "NO"));
        if(user.bio != null) account.notes.add(new Note("Bio", user.bio));
        if(user.total_private_repos != null) account.notes.add(new Note("Total Private Repos", str(user.total_private_repos)));
        if(user.owned_private_repos != null) account.notes.add(new Note("Owned Private Repos", str(user.owned_private_repos)));
        if(user.private_gists != null) account.notes.add(new Note("Private Gists", str(user.private_gists)));
        if(user.disk_usage != null) account.notes.add(new Note("Disk Usage", str(user.disk_usage)));
        if(user.collaborators != null) account.notes.add(new Note("Collaborators", str(user.collaborators)));
//        if(user.plan != null) account.notes.add(new Note("Plan", str(user.plan))); // TODO SOMEDAY: better plan representation
        return account;
    }

    private static @NonNull String encodeBasicAuthHeader(@NonNull String user, @NonNull String password) {
        String text = user + ":" + password;
        byte[] data = text.getBytes();
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
    }
}
