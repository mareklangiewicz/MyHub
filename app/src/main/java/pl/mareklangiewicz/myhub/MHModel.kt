package pl.mareklangiewicz.myhub

import android.support.annotation.MainThread
import android.util.Base64
import io.realm.Realm
import io.realm.Sort
import pl.mareklangiewicz.myhub.data.Account
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.data.Repo
import pl.mareklangiewicz.myhub.io.GitHub
import pl.mareklangiewicz.myhub.mvp.IModel
import pl.mareklangiewicz.myutils.MyTextUtils.str
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

//TODO SOMEDAY: Use RXJava Single instead of Observable if we return exactly one item?

@Singleton
@MainThread
class MHModel @Inject constructor(private val gitHubService: GitHub.Service) : IModel {

    /**
     * Fetches account data (repos too) from github.

     * Important1:
     * Returned Account is a deep copy that can be moved between threads
     * and should not be modified (rxjava style - not realm style)
     * Important2:
     * Use it on UI thread - it already does:
     * .subscribeOn(Schedulers.io())
     * .observeOn(AndroidSchedulers.mainThread());
     * Important3:
     * it saves fetched data to realm db.
     * @param user     user identifier
     * @param password special case: if password is empty - it just returns public data about given user
     * @param otp      if empty: it uses only basic authentication without Two Factor Authentication
     * @return account data from github.
     */
    fun fetchAccount(user: String, password: String, otp: String): Observable<Account> {

        val userObservable = when {
            password.isEmpty() -> gitHubService.getUserObservable(user)
            otp.isEmpty() -> gitHubService.getUserAuthObservable(encodeBasicAuthHeader(user, password))
            else -> gitHubService.getUserTFAObservable(encodeBasicAuthHeader(user, password), otp)
        }

        // TODO LATER: github pagination..
        val reposObservable = when {
            password.isEmpty() -> gitHubService.getUserReposObservable(user)
            otp.isEmpty() -> gitHubService.getUserReposAuthObservable(encodeBasicAuthHeader(user, password))
            else -> gitHubService.getUserReposTFAObservable(encodeBasicAuthHeader(user, password), otp)
        }

        return userObservable.zipWith(reposObservable) { user, repositories ->
            val account = user2account(user, repositories)
            saveAccount(account)
            account
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Saves or updates given account data in realm db.
     */
    fun saveAccount(account: Account) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { it.copyToRealmOrUpdate(account) }
        realm.close()
    }

    /**
     * Loads account data from realm db
     * Emits exactly one item (unless some error happens)
     * Emits null if no data found for given login.
     * Important1:
     * Emitted Account is a deep copy that can be moved between threads
     * and should not be modified (rxjava style - not realm style)
     * Important2:
     * Use it on UI thread - it already does:
     * .subscribeOn(Schedulers.io())
     * .observeOn(AndroidSchedulers.mainThread());
     */
    fun loadAccount(login: String): Observable<Account?> {
        return Observable.defer {
            val realm = Realm.getDefaultInstance()
            var account: Account? = realm.where(Account::class.java).equalTo("login", login).findFirst()
            if (account != null)
                account = realm.copyFromRealm<Account>(account) // deep copy can be moved between threads (mutation is forbidden)
            realm.close()
            Observable.just(account)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Loads latest account data from realm db
     * Emits exactly one item (unless some error happens)
     * Emits null if no data found.
     * Important1:
     * Emitted Account is a deep copy that can be moved between threads
     * and should not be modified (rxjava style - not realm style)
     * Important2:
     * Use it on UI thread - it already does:
     * .subscribeOn(Schedulers.io())
     * .observeOn(AndroidSchedulers.mainThread());
     */
    fun loadLatestAccount(): Observable<Account?> {
        return Observable.defer {
            val realm = Realm.getDefaultInstance()
            val results = realm.where(Account::class.java).findAllSorted("time", Sort.DESCENDING)
            var account: Account? = if (results.size > 0) results[0] else null
            if (account != null)
                account = realm.copyFromRealm<Account>(account) // deep copy can be moved between threads (mutation is forbidden)
            realm.close()
            Observable.just(account)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Converts GitHub.User to Account
     * Important: it will set the "time" property to current time
     */
    private fun user2account(user: GitHub.User, repositories: List<GitHub.Repository>?): Account {
        val login = user.login ?: ""
        val name = user.name ?: ""
        val location = user.location ?: ""
        val email = user.email ?: ""
        val company = user.company ?: ""
        val description = "%s\n%s\n%s".format(location, email, company)
        val account = Account(System.currentTimeMillis(), login, name, user.avatar_url, description)
        val notes = account.notes

        //TODO SOMEDAY: comment out not important notes
        notes.add(Note("Name", user.name))
        notes.add(Note("Location", user.location))
        notes.add(Note("Login", user.login))
        notes.add(Note("Type", user.type))
        notes.add(Note("E-Mail", user.email))
        notes.add(Note("GitHub Page", user.html_url))
        notes.add(Note("Company", user.company))
        notes.add(Note("Blog", user.blog))
        notes.add(Note("Public Repos", str(user.public_repos)))
        notes.add(Note("Public Gists", str(user.public_gists)))
        notes.add(Note("Followers", str(user.followers)))
        notes.add(Note("Following", str(user.following)))
        notes.add(Note("Created At", user.created_at))
        notes.add(Note("Updated At", user.updated_at))

        if (user.site_admin != null) notes.add(Note("Site Admin", if (user.site_admin) "YES" else "NO"))
        if (user.hireable != null) notes.add(Note("Hireable", if (user.hireable) "YES" else "NO"))
        if (user.bio != null) notes.add(Note("Bio", user.bio))
        if (user.total_private_repos != null) notes.add(Note("Total Private Repos", str(user.total_private_repos)))
        if (user.owned_private_repos != null) notes.add(Note("Owned Private Repos", str(user.owned_private_repos)))
        if (user.private_gists != null) notes.add(Note("Private Gists", str(user.private_gists)))
        if (user.disk_usage != null) notes.add(Note("Disk Usage", str(user.disk_usage)))
        if (user.collaborators != null) notes.add(Note("Collaborators", str(user.collaborators)))
        //        if(user.plan != null) account.notes.add(new Note("Plan", str(user.plan))); // TODO SOMEDAY: better plan representation

        if (repositories != null) {
            val repos = account.repos
            for (r in repositories) {
                val repo = Repo(r.name, r.description, r.forks_count ?: 0, r.watchers_count ?: 0, r.stargazers_count ?: 0)
                val rnotes = repo.notes

                //TODO SOMEDAY: comment out not important repo notes
                rnotes.add(Note("Name", r.name))

                if (r.full_name != null) rnotes.add(Note("Full Name", r.full_name))
                if (r.description != null) rnotes.add(Note("Description", r.description))
                if (r.html_url != null) rnotes.add(Note("GitHub Page", r.html_url))
                if (r.id != null) rnotes.add(Note("Id", str(r.id)))
                if (r.size != null) rnotes.add(Note("Size", str(r.size)))
                if (r.language != null) rnotes.add(Note("Language", r.language))
                if (r.fork != null) rnotes.add(Note("Fork", if (r.fork) "YES" else "NO"))
                if (r.forks_count != null) rnotes.add(Note("Forks", str(r.forks_count)))
                if (r.watchers_count != null) rnotes.add(Note("Watchers", str(r.watchers_count)))
                if (r.stargazers_count != null) rnotes.add(Note("Stargazers", str(r.stargazers_count)))
                if (r.open_issues_count != null) rnotes.add(Note("Open Issues", str(r.open_issues_count)))
                if (r.default_branch != null) rnotes.add(Note("Default Branch", r.default_branch))
                if (r.has_issues != null) rnotes.add(Note("Has Issues", if (r.has_issues) "YES" else "NO"))
                if (r.has_wiki != null) rnotes.add(Note("Has Wiki", if (r.has_wiki) "YES" else "NO"))
                if (r.has_pages != null) rnotes.add(Note("Has Pages", if (r.has_pages) "YES" else "NO"))
                if (r.has_downloads != null) rnotes.add(Note("Has Downloads", if (r.has_downloads) "YES" else "NO"))
                if (r.pushed_at != null) rnotes.add(Note("Pushed At", r.pushed_at))
                if (r.created_at != null) rnotes.add(Note("Created At", r.created_at))
                if (r.updated_at != null) rnotes.add(Note("Updated At", r.updated_at))

                repos.add(repo)
            }
        }

        return account
    }

    private fun encodeBasicAuthHeader(user: String, password: String): String {
        val text = user + ":" + password
        val data = text.toByteArray()
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP)
    }
}

