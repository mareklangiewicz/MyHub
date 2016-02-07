package pl.mareklangiewicz.myhub.ui

import android.widget.Button
import pl.mareklangiewicz.myhub.mvp.IButtonView

/**
 * Created by Marek Langiewicz on 06.02.16.
 * Simple abstraction of android Button
 */
open class AButtonView(private val button: Button) : ATextView(button), IButtonView
