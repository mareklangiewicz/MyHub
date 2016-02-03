package pl.mareklangiewicz.myhub

import android.support.annotation.MainThread
import com.noveogroup.android.log.MyLogger
import pl.mareklangiewicz.myhub.data.Account
import pl.mareklangiewicz.myhub.mvp.IMyAccountView
import pl.mareklangiewicz.myhub.mvp.IProgressView
import pl.mareklangiewicz.myhub.mvp.Presenter
import rx.Observable
import rx.Observer
import rx.Subscription
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@MainThread
class MyAccountPresenter @Inject constructor(private val model: MHModel, @Named("UI") private val log: MyLogger) : Presenter<IMyAccountView>() {

    private var subscription: Subscription? = null
        set(value) {
            val old = field
            if (old != null && !old.isUnsubscribed)
                old.unsubscribe()
            field = value
        }

    override var view: IMyAccountView?
        get() = super.view
        set(value) {
            super.view = value
            subscription = // setter will unsubscribe any old subscription
                    if (value == null)
                        null
                    else
                        model.loadLatestAccount().subscribe(object : Observer<Account?> {
                            override fun onCompleted() {
                                log.v("loading completed.")
                            }

                            override fun onError(e: Throwable?) {
                                log.e(e, "[SNACK] Error %s", e?.message ?: "")
                            }

                            override fun onNext(account: Account?) {
                                showAccount(account)
                            }
                        })
        }

    fun login() {
        val v = view
        if (v == null) {
            log.e("Can not login. View is detached.")
            return
        }

        v.progress = IProgressView.INDETERMINATE

        subscription = getAccount(v.login, v.password, v.otp).subscribe(object : Observer<Account?> {
            override fun onCompleted() {
                val av = view
                if (av != null) av.progress = IProgressView.HIDDEN
                log.v("loading completed.")
            }

            override fun onError(e: Throwable?) {
                log.e(e, "[SNACK]Error %s", e?.message ?: "")
                val av = view ?: return
                av.progress = IProgressView.HIDDEN
                av.login = v.login
            }

            override fun onNext(account: Account?) {
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
     * Displays given account on attached IView. Clears IView if account is null.
     */
    private fun showAccount(account: Account?) {
        val v = view ?: return
        v.status = if (account != null) "loaded: %tF %tT.".format(Locale.US, account.time, account.time) else "not loaded."
        v.login = account?.login ?: ""
        v.avatar = account?.avatar ?: ""
        v.name = account?.name ?: ""
        v.description = account?.description ?: ""
        v.notes = account?.notes ?: emptyList()

    }

}
