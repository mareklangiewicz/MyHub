package pl.mareklangiewicz.myhub

import android.support.annotation.MainThread
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import pl.mareklangiewicz.myhub.data.Account
import pl.mareklangiewicz.myhub.mvp.IMyReposDiew
import pl.mareklangiewicz.myhub.mvp.IPresenter
import pl.mareklangiewicz.myloggers.MyAndroLogger
import pl.mareklangiewicz.myutils.Cancel
import pl.mareklangiewicz.myutils.Lst
import pl.mareklangiewicz.myutils.asLst
import pl.mareklangiewicz.myutils.lsubscribe
import javax.inject.Inject
import javax.inject.Named

@MainThread
class MyReposPresenter @Inject constructor(private val model: MHModel, @Named("UI") private val log: MyAndroLogger)
: IPresenter<IMyReposDiew> {

    private var clicksSub: (Cancel) -> Unit = { }
    private var loadLatestAccountSubscription: Disposable = Disposables.disposed()

    override var xiew: IMyReposDiew? = null

        get() = field

        set(value) {

            clicksSub(Cancel)
            loadLatestAccountSubscription.dispose()

            field = value
            if (value == null) return

            loadLatestAccountSubscription = model.loadLatestAccount().lsubscribe(log, logOnCompleted = "load latest account repos completed") {
                showAccount(it)
            }

            value.progress.visible = false // we don't use it at all.. (currently)
        }

    /** Displays given account on attached IView. Clears IView if account is null. */
    private fun showAccount(account: Account?) {

        val v = xiew ?: return

        v.data = account

        clicksSub(Cancel)

        clicksSub = v.repos.clicksFromRepos {
            v.notes.data = it.data?.notes?.asLst() ?: Lst()
            v.showNotes()
        }
    }
}
