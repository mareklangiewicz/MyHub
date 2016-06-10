package pl.mareklangiewicz.myhub.ui

import android.view.ViewGroup
import android.widget.TextView
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.mvp.INoteView
import pl.mareklangiewicz.myviews.*

/**
 * Created by Marek Langiewicz on 10.06.16.
 */
class ANoteView(
        view: ViewGroup,
        private val label: ITextView,
        private val text: ITextView
) : AView<ViewGroup>(view), INoteView {
    constructor(
            view: ViewGroup,
            tvlabel: TextView,
            tvtext: TextView
    ) : this(
            view,
            ATextView(tvlabel),
            ATextView(tvtext) )

    override var data: Note? = null
        set(value) {
            field = value
            label.data = value?.label ?: ""
            text.data = value?.text ?: ""
        }
}
