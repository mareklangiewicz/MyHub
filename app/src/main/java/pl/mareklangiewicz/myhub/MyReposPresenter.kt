package pl.mareklangiewicz.myhub

import android.support.annotation.MainThread
import com.noveogroup.android.log.MyLogger
import pl.mareklangiewicz.myhub.data.Account
import pl.mareklangiewicz.myhub.mvp.IMyReposView
import pl.mareklangiewicz.myhub.mvp.Presenter
import rx.Observer
import rx.Subscription
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@MainThread
class MyReposPresenter @Inject constructor(private val model: MHModel, @Named("UI") private val log: MyLogger) : Presenter<IMyReposView>() {

    private var subscription: Subscription? = null
        set(value) {
            val old = field
            if (old != null && !old.isUnsubscribed)
                old.unsubscribe()
            field = value
        }

    override var view: IMyReposView?
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
                                log.e(e, "[SNACK]Error %s", e?.message ?: "")
                            }

                            override fun onNext(account: Account?) {
                                showAccount(account)
                            }
                        })
        }

    /** Displays given account on attached IView. Clears IView if account is null. */
    private fun showAccount(account: Account?) {
        val v = view ?: return
        v.status = if (account != null)
            "%s: %d repos (%tF %tT):".format(Locale.US, account.login, account.repos.size, account.time, account.time)
        else
            "no repos loaded"
        v.repos = account?.repos ?: emptyList()
    }
}
