package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import kotlinx.android.synthetic.main.mh_item_note.view.*
import pl.mareklangiewicz.myhub.R
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.mvp.INoteLstDiew
import pl.mareklangiewicz.myutils.inflate
import pl.mareklangiewicz.myviews.RecyclerDiew

class ANoteLstDiew(rview: RecyclerView) : INoteLstDiew, RecyclerDiew<Note, ANoteDiew>(rview) {

    override fun create(): ANoteDiew {
        val vg = view.inflate<ViewGroup>(R.layout.mh_item_note)!!
        return ANoteDiew(vg, vg.mh_in_tv_label, vg.mh_in_tv_text)
    }

    override fun bind(view: ANoteDiew, item: Note) { view.data = item }
}


