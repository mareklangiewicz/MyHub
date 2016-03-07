package pl.mareklangiewicz.myhub.ui

import android.widget.ImageView
import loadUrl
import pl.mareklangiewicz.myhub.R
import pl.mareklangiewicz.myhub.mvp.IImageView

/**
 * Created by Marek Langiewicz on 04.02.16.
 * Android implementation of view displaying avatar with given url
 */
class AAvatarView(private val imageView: ImageView) : AView(imageView), IImageView {

    //TODO LATER: handle invalid urls
    override var url = ""  // "" is written directly to backing field.
        set(value) {
            field = value
            if (value.isEmpty())
                imageView.setImageResource(R.drawable.mh_avatar)
            else
                imageView.loadUrl(value)
        }
}