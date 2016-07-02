package pl.mareklangiewicz.myhub.ui

import android.view.ViewGroup
import android.widget.TextView
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.mvp.INoteDiew
import pl.mareklangiewicz.myviews.*

/** Created by Marek Langiewicz on 10.06.16. */
class ANoteDiew(
        view: ViewGroup,
        private val label: ITiew,
        private val text: ITiew
) : ADiew<ViewGroup, Note?>(view), INoteDiew {
    constructor(
            view: ViewGroup,
            tvlabel: TextView,
            tvtext: TextView
    ) : this(
            view,
            ATiew(tvlabel),
            ATiew(tvtext) )

    override var data: Note? = null
        set(value) {
            field = value
            label.data = value?.label ?: ""
            text.data = value?.text ?: ""
        }
}
