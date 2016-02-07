package pl.mareklangiewicz.myhub.ui

import android.widget.ProgressBar
import pl.mareklangiewicz.myhub.mvp.IProgressView

/**
 * Created by Marek Langiewicz on 29.01.16.
 * Android implementation of IProgressView
 */
open class AProgressView(private val bar: ProgressBar) : AView(bar), IProgressView {

    override var max: Double = 100.0
        get() = field
        set(value) {
            field = value.coerceAtLeast(min)
            bar.max = field.toInt()
        }

    override var pos: Double = min
        get() = field
        set(value) {
            field = value.coerceIn(min, max)
            bar.progress = field.toInt()
        }

    override var indeterminate = false
    get() = field
    set(value) {
        field = value
        bar.isIndeterminate = value
    }


    init {
        max = max
        pos = pos
        indeterminate = indeterminate
    }
}
