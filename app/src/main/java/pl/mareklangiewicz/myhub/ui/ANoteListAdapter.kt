package pl.mareklangiewicz.myhub.ui

import kotlinx.android.synthetic.main.mh_item_note.view.*
import pl.mareklangiewicz.myhub.R
import pl.mareklangiewicz.myhub.data.Note

internal class ANoteListAdapter : AItemListAdapter<Note>(
        R.layout.mh_item_note,
        {
            mh_in_tv_label.text = it.label ?: ""
            mh_in_tv_text.text = it.text ?: ""
        }
)
