package pl.mareklangiewicz.myhub.ui

import android.widget.TextView
import getValue
import pl.mareklangiewicz.myhub.mvp.ITextView
import setValue

/**
 * Created by Marek Langiewicz on 04.02.16.
 * Simple abstraction of android TextView
 */
open class ATextView(private val tv: TextView) : AView(tv), ITextView {
    override var text by tv
}