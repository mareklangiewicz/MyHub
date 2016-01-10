package pl.mareklangiewicz.mygithub;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import pl.mareklangiewicz.mygithub.data.Account;
import pl.mareklangiewicz.mygithub.data.Note;
import pl.mareklangiewicz.mygithub.data.Repo;
import pl.mareklangiewicz.mygithub.io.GitHub;
import pl.mareklangiewicz.mygithub.mvp.IModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static pl.mareklangiewicz.myutils.MyTextUtils.str;

@Singleton
@MainThread
public class MGModel implements IModel {

    private @NonNull GitHub.Service mGitHubService;

    @Inject MGModel(@NonNull GitHub.Service github) {
        mGitHubService = github;
        // TODO NOW
    }


    /**
     * Important: it already does:
     * .subscribeOn(Schedulers.io())
     * .observeOn(AndroidSchedulers.mainThread());
     * Important2: it saves fetched data to realm db.
     *
     * @param user     user identifier
     * @param password special case: if password is empty - it just returns public data about given user
     * @param otp      if empty: it uses only basic authentication without Two Factor Authentication
     * @return account data from github.
     */
    public Observable<Account> fetchAccount(@NonNull String user, @NonNull String password, @NonNull String otp) {

        Observable<GitHub.User> userObservable =
                password.isEmpty()
                        ?
                        mGitHubService.getUserObservable(user)
                        :
                        otp.isEmpty()
                                ?
                                mGitHubService.getUserAuthObservable(encodeBasicAuthHeader(user, password))
                                :
                                mGitHubService.getUserTFAObservable(encodeBasicAuthHeader(user, password), otp);

        Observable<List<GitHub.Repository>> reposObservable =
                password.isEmpty()
                        ?
                        mGitHubService.getUserReposObservable(user)
                        :
                        otp.isEmpty()
                                ?
                                mGitHubService.getUserReposAuthObservable(encodeBasicAuthHeader(user, password))
                                :
                                mGitHubService.getUserReposTFAObservable(encodeBasicAuthHeader(user, password), otp);
        return userObservable
                .zipWith(reposObservable, new Func2<GitHub.User, List<GitHub.Repository>, Account>() {
                    @Override public Account call(GitHub.User user, List<GitHub.Repository> repositories) {
                        Account account = user2account(user, repositories);
                        saveAccount(account);
                        return account;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void saveAccount(final Account account) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(account);
            }
        });
        realm.close();
    }

    /**
     * Loads account data from realm db
     * Returns null if no data found for given login.
     * Important: it already does:
     * .subscribeOn(Schedulers.io())
     * .observeOn(AndroidSchedulers.mainThread());
     */
    public Observable<Account> loadAccount(@NonNull final String login) {
        return Observable.defer(new Func0<Observable<Account>>() {
            @Override public Observable<Account> call() {
                Realm realm = Realm.getDefaultInstance();
                Account account = realm.where(Account.class).equalTo("login", login).findFirst();
                if(account != null)
                    account = realm.copyFromRealm(account); // deep copy can be moved between threads (mutation is forbidden)
                realm.close();
                return Observable.just(account);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Loads latest account data from realm db
     * Returns null if no data found.
     * Important: it already does:
     * .subscribeOn(Schedulers.io())
     * .observeOn(AndroidSchedulers.mainThread());
     */
    public Observable<Account> loadLatestAccount() {
        return Observable.defer(new Func0<Observable<Account>>() {
            @Override public Observable<Account> call() {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Account> results = realm.where(Account.class).findAllSorted("time", Sort.DESCENDING);
                Account account = results.size() > 0 ? results.get(0) : null;
                if(account != null)
                    account = realm.copyFromRealm(account); // deep copy can be moved between threads (mutation is forbidden)
                realm.close();
                return Observable.just(account);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Important: it will set the "time" property to current time
     */
    private static Account user2account(@NonNull GitHub.User user, @Nullable List<GitHub.Repository> repositories) {
        String login = user.login == null ? "" : user.login;
        String name = user.name == null ? "" : user.name;
        String location = user.location == null ? "" : user.location;
        String email = user.email == null ? "" : user.email;
        String company = user.company == null ? "" : user.company;
        String description = String.format("%s\n%s\n%s", location, email, company);
        Account account = new Account(System.currentTimeMillis(), login, name, user.avatar_url, description);
        RealmList<Note> notes = account.getNotes();

        //TODO SOMEDAY: comment out not important notes
        notes.add(new Note("Name", user.name));
        notes.add(new Note("Location", user.location));
        notes.add(new Note("Login", user.login));
        notes.add(new Note("Type", user.type));
        notes.add(new Note("E-Mail", user.email));
        notes.add(new Note("GitHub Page", user.html_url));
        notes.add(new Note("Company", user.company));
        notes.add(new Note("Blog", user.blog));
        notes.add(new Note("Public Repos", str(user.public_repos)));
        notes.add(new Note("Public Gists", str(user.public_gists)));
        notes.add(new Note("Followers", str(user.followers)));
        notes.add(new Note("Following", str(user.following)));
        notes.add(new Note("Created At", user.created_at));
        notes.add(new Note("Updated At", user.updated_at));

        if(user.site_admin != null) notes.add(new Note("Site Admin", user.site_admin ? "YES" : "NO"));
        if(user.hireable != null) notes.add(new Note("Hireable", user.hireable ? "YES" : "NO"));
        if(user.bio != null) notes.add(new Note("Bio", user.bio));
        if(user.total_private_repos != null) notes.add(new Note("Total Private Repos", str(user.total_private_repos)));
        if(user.owned_private_repos != null) notes.add(new Note("Owned Private Repos", str(user.owned_private_repos)));
        if(user.private_gists != null) notes.add(new Note("Private Gists", str(user.private_gists)));
        if(user.disk_usage != null) notes.add(new Note("Disk Usage", str(user.disk_usage)));
        if(user.collaborators != null) notes.add(new Note("Collaborators", str(user.collaborators)));
//        if(user.plan != null) account.notes.add(new Note("Plan", str(user.plan))); // TODO SOMEDAY: better plan representation

        if(repositories != null) {
            RealmList<Repo> repos = account.getRepos();
            for(GitHub.Repository r : repositories) {
                repos.add(new Repo(r.name, r.description, r.forks_count, r.watchers_count, r.stargazers_count));
                // TODO LATER: add repo notes
            }
        }

        return account;
    }

    private static @NonNull String encodeBasicAuthHeader(@NonNull String user, @NonNull String password) {
        String text = user + ":" + password;
        byte[] data = text.getBytes();
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
    }
}
