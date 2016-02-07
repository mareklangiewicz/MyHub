package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import android.widget.ProgressBar
import android.widget.TextView
import pl.mareklangiewicz.myhub.mvp.*

/**
 * Created by Marek Langiewicz on 31.01.16.
 * Android implementation of IMyReposView
 */
class AMyReposView(
        val activity: MHActivity, // we need that just to show a drawer with repo notes...
        override val progress: IProgressView,
        override val status: IStatusView,
        override val repos: IRepoListView,
        override val notes: INoteListView
) : IMyReposView {
    constructor(
            activity: MHActivity,
            progress: ProgressBar,
            status: TextView,
            repos: RecyclerView,
            notes: RecyclerView
    )
    : this(
            activity,
            AProgressView(progress),
            AStatusView(status),
            ARepoListView(repos),
            ANoteListView(notes)
    )

    override fun showNotes() {
        activity.showLocalNavigation()
    }

}

