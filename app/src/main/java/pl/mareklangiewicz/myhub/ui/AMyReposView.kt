package pl.mareklangiewicz.myhub.ui

import android.widget.TextView
import getValue
import pl.mareklangiewicz.myhub.mvp.IMyReposView
import pl.mareklangiewicz.myhub.mvp.INotesView
import pl.mareklangiewicz.myhub.mvp.IProgressView
import pl.mareklangiewicz.myhub.mvp.IReposView
import setValue

/**
 * Created by Marek Langiewicz on 31.01.16.
 * Android implementation of IMyReposView
 */
class AMyReposView(
        private val statusTextView: TextView,
        private val rv: IReposView,
        private val nv: INotesView,
        private val pv: IProgressView
) : IMyReposView, IReposView by rv, INotesView by nv, IProgressView by pv {
    override var status by statusTextView
}

