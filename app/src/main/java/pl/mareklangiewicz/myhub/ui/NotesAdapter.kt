package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.mareklangiewicz.myhub.R
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.mvp.INotesView

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder>, INotesView {

    constructor() { }

    override var notes: List<Note> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.mg_note, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.label.text = note.label
        holder.text.text = note.text
    }

    override fun getItemCount() = notes.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var label = itemView.findViewById(R.id.label) as TextView
        var text = itemView.findViewById(R.id.text) as TextView
    }
}
