package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import pl.mareklangiewicz.myhub.mvp.*
import pl.mareklangiewicz.myviews.*
import pl.mareklangiewicz.myhub.data.Account
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myutils.Lst
import pl.mareklangiewicz.myutils.asLst
import java.util.*

/**
 * Created by Marek Langiewicz on 31.01.16.
 * Android implementation of IMyReposDiew
 */
class AMyReposDiew(
        view: ViewGroup,
        override val progress: IMovDiew,
        override val status: IStatusTiew,
        override val repos: IRepoLstDiew,
        override val notes: INoteLstDiew
) : ADiew<ViewGroup, Account?>(view), IMyReposDiew {
    constructor(
            view: ViewGroup,
            progress: ProgressBar,
            status: TextView,
            repos: RecyclerView,
            notes: RecyclerView
    )
    : this(
            view,
            AMovDiew(progress),
            AStatusTiew(status),
            ARepoLstDiew(repos),
            ANoteLstDiew(notes)
    )

    override var data: Account?
        get() = throw UnsupportedOperationException()
        set(value) {
            if(value == null) {
                status.highlight = true
                status.data = "no repos loaded."
                notes.data = Lst.of(Note("No details", "Log in first to get some repository list"))
            }
            else {
                status.highlight = false
                status.data = "%s: %d repos (%tF %tT):".format(Locale.US, value.login, value.repos.size, value.time, value.time)
                notes.data = Lst.of(Note("No details", "Select repository to get details"))
            }
            repos.data = value?.repos?.asLst() ?: Lst()

        }

    override fun showNotes() {
        (view.context as MHActivity).showLNav()
    }

}

