package pl.mareklangiewicz.myhub.ui

import android.view.View
import com.jakewharton.rxbinding.view.clicks
import pl.mareklangiewicz.myhub.mvp.IView
import rx.Observable

/**
 * Created by Marek Langiewicz on 04.02.16.
 * Simple abstraction of android view
 */
open class AView(val view: View) : IView {
    override var visible: Boolean
        get() = view.visibility == View.VISIBLE
        set(value) {
            view.visibility = if(value) View.VISIBLE else View.INVISIBLE
        }

    override var enabled: Boolean
        get() = view.isEnabled
        set(value) {
            view.isEnabled = value
        }

    override val clicks: Observable<out AView>
        get() = view.clicks().map { this }

}