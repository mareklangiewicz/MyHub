package pl.mareklangiewicz.myhub

import android.support.annotation.MainThread
import android.util.Base64
import io.realm.Realm
import io.realm.Sort
import pl.mareklangiewicz.myhub.data.Account
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.data.Repo
import pl.mareklangiewicz.myhub.io.Repository
import pl.mareklangiewicz.myhub.io.GithubService
import pl.mareklangiewicz.myhub.io.User
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
class MHModel @Inject constructor(private val gitHubService: GithubService) : IModel {


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

    private val Boolean.yesno: String get() = if(this) "YES" else "NO"
    /**
     * Converts User to Account
     * Important: it will set the "time" property to current time
     */
    private fun user2account(user: User, repositories: List<Repository>?): Account {
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

        user.site_admin?.let { notes.add(Note("Site Admin", it.yesno)) }
        user.hireable?.let { notes.add(Note("Hireable", it.yesno)) }
        user.bio?.let { notes.add(Note("Bio", it)) }
        user.total_private_repos?.let { notes.add(Note("Total Private Repos", str(it))) }
        user.owned_private_repos?.let { notes.add(Note("Owned Private Repos", str(it))) }
        user.private_gists?.let { notes.add(Note("Private Gists", str(it))) }
        user.disk_usage?.let { notes.add(Note("Disk Usage", str(it))) }
        user.collaborators?.let { notes.add(Note("Collaborators", str(it))) }
//        user.plan?.let { account.notes.add(Note("Plan", str(it))); }// TODO SOMEDAY: better plan representation

        if (repositories != null) {
            val repos = account.repos
            for (r in repositories) {
                val repo = Repo(r.name!!, r.description, r.forks_count ?: 0, r.watchers_count ?: 0, r.stargazers_count ?: 0)
                val rnotes = repo.notes

                //TODO SOMEDAY: comment out not important repo notes
                rnotes.add(Note("Name", r.name))

                r.full_name?.let { rnotes.add(Note("Full Name", it)) }
                r.description?.let { rnotes.add(Note("Description", it)) }
                r.html_url?.let { rnotes.add(Note("GitHub Page", it)) }
                r.id?.let { rnotes.add(Note("Id", str(it))) }
                r.size?.let { rnotes.add(Note("Size", str(it))) }
                r.language?.let { rnotes.add(Note("Language", it)) }
                r.fork?.let { rnotes.add(Note("Fork", it.yesno)) }
                r.forks_count?.let { rnotes.add(Note("Forks", str(it))) }
                r.watchers_count?.let { rnotes.add(Note("Watchers", str(it))) }
                r.stargazers_count?.let { rnotes.add(Note("Stargazers", str(it))) }
                r.open_issues_count?.let { rnotes.add(Note("Open Issues", str(it))) }
                r.default_branch?.let { rnotes.add(Note("Default Branch", it)) }
                r.has_issues?.let { rnotes.add(Note("Has Issues", it.yesno)) }
                r.has_wiki?.let { rnotes.add(Note("Has Wiki", it.yesno)) }
                r.has_pages?.let { rnotes.add(Note("Has Pages", it.yesno)) }
                r.has_downloads?.let { rnotes.add(Note("Has Downloads", it.yesno)) }
                r.pushed_at?.let { rnotes.add(Note("Pushed At", it)) }
                r.created_at?.let { rnotes.add(Note("Created At", it)) }
                r.updated_at?.let { rnotes.add(Note("Updated At", it)) }

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

