package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.mh_item_note.view.*
import pl.mareklangiewicz.myhub.R
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.mvp.INotesView

internal class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder>, INotesView {

    constructor() { }

    override var notes: List<Note> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.mh_item_note, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.label.text = note.label ?: ""
        holder.text.text = note.text ?: ""
    }

    override fun getItemCount() = notes.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var label = itemView.mh_in_tv_label
        var text = itemView.mh_in_tv_text
    }
}
