package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import pl.mareklangiewicz.myhub.mvp.INotesView

/**
 * Created by Marek Langiewicz on 29.01.16.
 * Android implementation of INotesView
 */
internal class ANotesView(private val rv: RecyclerView, private val adapter: NotesAdapter = NotesAdapter()) : INotesView by adapter {
    init {
        rv.layoutManager = WCLinearLayoutManager(rv.context) // LLM that understands "wrap_content"
        rv.adapter = adapter
    }
}
