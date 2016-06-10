package pl.mareklangiewicz.myhub.ui

import android.graphics.Color
import android.widget.TextView
import pl.mareklangiewicz.myhub.mvp.IStatusView
import pl.mareklangiewicz.myviews.ATextView

/**
 * Created by Marek Langiewicz on 04.02.16.
 * Android implementation of IStatusView
 */
class AStatusView(tview: TextView) : ATextView<TextView>(tview), IStatusView {

    override var highlight: Boolean
        get() = view.currentTextColor == COLOR_HIGHLIGHT
        set(value) {
            view.setTextColor(if(value) COLOR_HIGHLIGHT else COLOR_NORMAL)
        }

    companion object {
        const val COLOR_NORMAL = Color.BLACK
        const val COLOR_HIGHLIGHT = Color.RED
    }
}
