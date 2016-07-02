package pl.mareklangiewicz.myhub.ui

import android.graphics.Color
import android.widget.TextView
import pl.mareklangiewicz.myhub.mvp.IStatusTiew
import pl.mareklangiewicz.myviews.ATiew

/**
 * Created by Marek Langiewicz on 04.02.16.
 * Android implementation of IStatusView
 */
class AStatusTiew(tview: TextView) : ATiew<TextView>(tview), IStatusTiew {

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
