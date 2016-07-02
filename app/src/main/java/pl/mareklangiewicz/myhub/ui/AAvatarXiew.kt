package pl.mareklangiewicz.myhub.ui

import android.widget.ImageView
import pl.mareklangiewicz.myhub.R
import pl.mareklangiewicz.myutils.loadUrl
import pl.mareklangiewicz.myviews.AUrlImageXiew

/**
 * Created by Marek Langiewicz on 04.02.16.
 * Android implementation of view displaying avatar with given url
 */
class AAvatarXiew(private val imageView: ImageView) : AUrlImageXiew(imageView) {
    override var url = ""
        set(value) { //TODO LATER: handle invalid urls
            field = value
            if (value.isNotBlank()) imageView.loadUrl(value) else imageView.setImageResource(R.drawable.mh_avatar)
        }
}