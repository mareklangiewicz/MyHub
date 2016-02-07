package pl.mareklangiewicz.myhub

import android.support.annotation.MainThread
import com.noveogroup.android.log.MyLogger
import lsubscribe
import pl.mareklangiewicz.myhub.data.Account
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.mvp.IMyReposView
import pl.mareklangiewicz.myhub.mvp.Presenter
import rx.Subscription
import rx.subscriptions.Subscriptions
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@MainThread
class MyReposPresenter @Inject constructor(private val model: MHModel, @Named("UI") private val log: MyLogger) : Presenter<IMyReposView>() {

    private var itemClicksSubscription: Subscription = Subscriptions.unsubscribed()
    private var loadLatestAccountSubscription: Subscription = Subscriptions.unsubscribed()

    override var view: IMyReposView?

        get() = super.view

        set(value) {

            if (!itemClicksSubscription.isUnsubscribed) itemClicksSubscription.unsubscribe()
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
        if(account == null) {
            v.status.highlight = true
            v.status.text = "no repos loaded."
            v.notes.items = listOf(Note("No details", "Log in first to get some repository list"))
        }
        else {
            v.status.highlight = false
            v.status.text = "%s: %d repos (%tF %tT):".format(Locale.US, account.login, account.repos.size, account.time, account.time)
            v.notes.items = listOf(Note("No details", "Select repository to get details"))
        }
        v.repos.items = account?.repos ?: emptyList()

        if (!itemClicksSubscription.isUnsubscribed) itemClicksSubscription.unsubscribe()

        itemClicksSubscription = v.repos.itemClicks.lsubscribe(log) { repo ->
            v.notes.items = repo.notes
            v.showNotes()
        }
    }
}
