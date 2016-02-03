package pl.mareklangiewicz.myhub.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxbinding.view.clicks
import com.squareup.picasso.Picasso
import pl.mareklangiewicz.myhub.mvp.IMyAccountView
import pl.mareklangiewicz.myhub.mvp.INotesView
import pl.mareklangiewicz.myhub.mvp.IProgressView
import getValue
import pl.mareklangiewicz.myhub.R
import rx.Observable
import setValue

/**
 * Created by Marek Langiewicz on 29.01.16.
 * Android implementation of IMyAccountView
 */
class AMyAccountView(
        private val statusTextView: TextView,
        private val loginTextView: TextView,
        private val passwordTextView: TextView,
        private val otpTextView: TextView,
        private val avatarImageView: ImageView,
        private val nameTextView: TextView,
        private val descriptionTextView: TextView,
        private val loginButton: View,
        private val nv: INotesView,
        private val pv: IProgressView
) : IMyAccountView, INotesView by nv, IProgressView by pv {
    override var status by statusTextView
    override var login by loginTextView
    override var password by passwordTextView
    override var otp by otpTextView

    //TODO LATER: handle invalid urls
    override var avatar: String = "" // "" is written directly to backing field.
        set(value) {
            field = value
            if (value.isEmpty())
                avatarImageView.setImageResource(R.drawable.mh_avatar)
            else
                Picasso.with(avatarImageView.context).load(value).into(avatarImageView)
        }

    override var name by nameTextView
    override var description by descriptionTextView

    override val loginButtonClicks: Observable<Unit>
        get() = loginButton.clicks()

}

