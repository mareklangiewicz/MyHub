package pl.mareklangiewicz.myhub

import android.support.annotation.MainThread
import pl.mareklangiewicz.myhub.data.Account
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.mvp.*
import pl.mareklangiewicz.myloggers.MyAndroLogger
import pl.mareklangiewicz.myutils.*
import rx.Subscription
import rx.subscriptions.Subscriptions
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@MainThread
class MyReposPresenter @Inject constructor(private val model: MHModel, @Named("UI") private val log: MyAndroLogger)
: Presenter<IMyReposView>() {

    private val clicksSubscription = ToDo()
    private var loadLatestAccountSubscription: Subscription = Subscriptions.unsubscribed()

    override var view: IMyReposView?

        get() = super.view

        set(value) {

            clicksSubscription.doItAll()
            if (!loadLatestAccountSubscription.isUnsubscribed) loadLatestAccountSubscription.unsubscribe()

            super.view = value
            if (value == null) return

            loadLatestAccountSubscription = model.loadLatestAccount().lsubscribe(log, logOnCompleted = "load latest account repos completed") {
                showAccount(it)
            }

            value.progress.visible = false // we don't use it at all.. (currently)
        }

    /** Displays given account on attached IView. Clears IView if account is null. */
    private fun showAccount(account: Account?) {

        val v = view ?: return

        v.data = account

        clicksSubscription.doItAll()

        val ctl = v.repos.clicksFromRepos {
            v.notes.data = it.data?.notes?.asLst() ?: Lst()
            v.showNotes()
        }

        clicksSubscription.push { ctl(Cancel) }
    }
}
