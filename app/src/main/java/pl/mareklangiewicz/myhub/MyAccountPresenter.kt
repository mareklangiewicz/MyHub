package pl.mareklangiewicz.myhub

import android.support.annotation.MainThread
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import pl.mareklangiewicz.myhub.data.Account
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.mvp.IMyAccountDiew
import pl.mareklangiewicz.myhub.mvp.IPresenter
import pl.mareklangiewicz.myloggers.MyAndroLogger
import pl.mareklangiewicz.myutils.*
import javax.inject.Inject
import javax.inject.Named


@MainThread
class MyAccountPresenter @Inject constructor(private val model: MHModel, @Named("UI") private val log: MyAndroLogger)
    : IPresenter<IMyAccountDiew> {

    private var loginSub: (Cancel) -> Unit = { }
    private var loadLatestAccountSubscription: Disposable = Disposables.disposed()
    private var getAccountSubscription: Disposable = Disposables.disposed()

    override var xiew: IMyAccountDiew? = null
        get() = field
        set(value) {
            loginSub(Cancel)
            loadLatestAccountSubscription.dispose()

            field = value
            if (value == null) return

            logging = logging // sync ui..

            loginSub = value.loginButton.clicks { login() }

            if (logging)
                return

            loadLatestAccountSubscription = model.loadLatestAccount().lsubscribe(log, logOnCompleted = "load latest account completed") {
                if (it === null)
                    clearAccount()
                else
                    showAccount(it)
            }

        }

    private var logging: Boolean = false // true if we are running long operation (fetching account from internet)
            // it updates ui accordingly (just displays some moving progress bar)
        set(value) {
            field = value
            val v = xiew ?: return
            v.progress.fuzzy = value
            v.progress.visible = value
            v.loginButton.enabled = !value
        }

    fun login() {
        val v = xiew
        if (v == null) {
            log.e("Can not login. View is detached.")
            return
        }
        login(v.login.data, v.password.data, v.otp.data)
    }

    private fun login(name: String, password: String, otp: String) {

        if (logging) {
            log.w("[SNACK]I'm trying...")
            return
        }

        getAccountSubscription.dispose()

        logging = true

        getAccountSubscription = getAccount(name, password, otp).subscribe(
                { account: Account ->
                    showAccount(account)
                },

                { e: Throwable? ->
                    logging = false
                    log.e("[SNACK]Error ${e?.message ?: ""}", throwable = e)
                    clearAccount(clearLoginInfo = false)
                },

                {
                    logging = false
                    log.v("Account loading completed.")
                }
        )
    }

    /**
     * Tries to load account for given user from local db (realm) and from internet (github) too.
     * Data from local db will come first (or not at all), and data from internet will come later (if any).
     * So we should override displayed account data when new data comes.
     * Password and/or otp can be empty. See MHModel.fetchAccount for details
     */
    private fun getAccount(user: String, password: String, otp: String): Observable<Account> {
        return model.loadAccount(user).toObservable().concatWith(model.fetchAccount(user, password, otp))
    }

    /**
     * Displays given account on attached IView.
     */
    private fun showAccount(account: Account) {
        val v = xiew ?: return
        v.data = account
    }

    private fun clearAccount(clearLoginInfo: Boolean = true) {
        val v = xiew ?: return
        if (clearLoginInfo) {
            v.data = null
            v.password.data = ""
            v.otp.data = ""
            return
        }
        v.status.highlight = true
        v.status.data = "not loaded."
        v.avatar.url = ""
        v.name.data = ""
        v.description.data = ""
        v.notes.data = Lst.of(Note("No info", "Log in to get info"))
    }
}
