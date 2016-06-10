package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import pl.mareklangiewicz.myhub.mvp.*
import pl.mareklangiewicz.myviews.AProgressView
import pl.mareklangiewicz.myviews.IProgressView
import pl.mareklangiewicz.myhub.data.Account
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myutils.Lst
import pl.mareklangiewicz.myutils.asLst
import pl.mareklangiewicz.myviews.AView
import java.util.*

/**
 * Created by Marek Langiewicz on 31.01.16.
 * Android implementation of IMyReposView
 */
class AMyReposView(
        view: ViewGroup,
        override val progress: IProgressView,
        override val status: IStatusView,
        override val repos: IRepoLstView,
        override val notes: INoteLstView
) : AView<ViewGroup>(view), IMyReposView {
    constructor(
            view: ViewGroup,
            progress: ProgressBar,
            status: TextView,
            repos: RecyclerView,
            notes: RecyclerView
    )
    : this(
            view,
            AProgressView(progress),
            AStatusView(status),
            ARepoLstView(repos),
            ANoteLstView(notes)
    )

    override var data: Account?
        get() = super.data // unsupported
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

