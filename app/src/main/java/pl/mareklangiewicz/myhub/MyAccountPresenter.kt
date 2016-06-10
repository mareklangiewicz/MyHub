package pl.mareklangiewicz.myhub

import android.support.annotation.MainThread
import pl.mareklangiewicz.myhub.data.*
import pl.mareklangiewicz.myhub.mvp.IMyAccountView
import pl.mareklangiewicz.myhub.mvp.Presenter
import pl.mareklangiewicz.myloggers.MyAndroLogger
import pl.mareklangiewicz.myutils.*
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.subscriptions.Subscriptions
import javax.inject.Inject
import javax.inject.Named


@MainThread
class MyAccountPresenter @Inject constructor(private val model: MHModel, @Named("UI") private val log: MyAndroLogger)
: Presenter<IMyAccountView>() {

    private var loginClicksUnsub: IToDo = ToDo()
    private var loadLatestAccountSubscription: Subscription = Subscriptions.unsubscribed()
    private var getAccountSubscription: Subscription = Subscriptions.unsubscribed()

    override var view: IMyAccountView?

        get() = super.view

        set(value) {
            loginClicksUnsub.doItAll()
            if (!loadLatestAccountSubscription.isUnsubscribed) loadLatestAccountSubscription.unsubscribe()

            super.view = value
            if (value == null) return

            logging = logging // sync ui..

            val ctl = value.loginButton.clicks { login() }
            loginClicksUnsub.push { ctl(Cancel) }

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
            val v = view ?: return
            v.progress.indeterminate = value
            v.progress.visible = value
            v.loginButton.enabled = !value
        }

    fun login() {
        val v = view
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

        if (!getAccountSubscription.isUnsubscribed) getAccountSubscription.unsubscribe()

        logging = true

        getAccountSubscription = getAccount(name, password, otp).subscribe(object : Observer<Account?> {

            override fun onCompleted() {
                logging = false
                log.v("Account loading completed.")
            }

            override fun onError(e: Throwable?) {
                logging = false
                log.e("[SNACK]Error ${e?.message ?: ""}", throwable = e)
                clearAccount(clearLoginInfo = false)
            }

            override fun onNext(account: Account?) {
                if (account != null)
                    showAccount(account)
            }
        })
    }

    /**
     * Tries to load account for given user from local db (realm) and from internet (github) too.
     * Data from local db will come first (can be null), and data from internet will come later (if any).
     * So we should override displayed account data when new data comes.
     * Password and/or otp can be empty. See MHModel.fetchAccount for details
     */
    private fun getAccount(user: String, password: String, otp: String): Observable<Account?> {
        return model.loadAccount(user).concatWith(model.fetchAccount(user, password, otp))
    }

    /**
     * Displays given account on attached IView.
     */
    private fun showAccount(account: Account) {
        val v = view ?: return
        v.data = account
    }

    private fun clearAccount(clearLoginInfo: Boolean = true) {
        val v = view ?: return
        if(clearLoginInfo) {
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
