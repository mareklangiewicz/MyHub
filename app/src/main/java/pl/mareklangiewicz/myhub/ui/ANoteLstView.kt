package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import kotlinx.android.synthetic.main.mh_item_note.view.*
import pl.mareklangiewicz.myhub.R
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.mvp.INoteLstView
import pl.mareklangiewicz.myutils.inflate
import pl.mareklangiewicz.myviews.ALabTextView
import pl.mareklangiewicz.myviews.MyRecyclerView

class ANoteLstView(rview: RecyclerView) : INoteLstView, MyRecyclerView<Note, ANoteView>(rview) {

    override fun create(): ANoteView {
        val vg = view.inflate<ViewGroup>(R.layout.mh_item_note)!!
        return ANoteView(vg, vg.mh_in_tv_label, vg.mh_in_tv_text)
    }

    override fun bind(view: ANoteView, item: Note) { view.data = item }
}


