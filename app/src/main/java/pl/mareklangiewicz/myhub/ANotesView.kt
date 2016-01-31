package pl.mareklangiewicz.myhub

import android.support.v7.widget.RecyclerView
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.mvp.INotesView
import pl.mareklangiewicz.myhub.ui.NotesAdapter
import pl.mareklangiewicz.myhub.ui.WCLinearLayoutManager

/**
 * Created by Marek Langiewicz on 29.01.16.
 * Android implementation of INotesView
 */
class ANotesView(private val rv: RecyclerView) : INotesView {

    private val adapter = NotesAdapter()

    override var notes: List<Note>
        get() = adapter.notes
        set(value) {
            adapter.notes = value
        }

    init {
        rv.layoutManager = WCLinearLayoutManager(rv.context) // LLM that understands "wrap_content"
        rv.adapter = adapter
    }
}
