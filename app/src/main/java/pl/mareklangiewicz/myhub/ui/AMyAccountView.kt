package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import pl.mareklangiewicz.myhub.mvp.*


/**
 * Created by Marek Langiewicz on 29.01.16.
 * Android implementation of IMyAccountView
 */
class AMyAccountView(
        override val progress: IProgressView,
        override val status: IStatusView,
        override val login: ITextView,
        override val password: ITextView,
        override val otp: ITextView,
        override val loginButton: IButtonView,
        override val avatar: IImageView,
        override val name: ITextView,
        override val description: ITextView,
        override val notes: INoteListView
) : IMyAccountView {
    constructor(
            progress: ProgressBar,
            status: TextView,
            login: TextView,
            password: TextView,
            otp: TextView,
            loginButton: Button,
            avatar: ImageView,
            name: TextView,
            description: TextView,
            notes: RecyclerView
    ) : this(
            AProgressView(progress),
            AStatusView(status),
            ATextView(login),
            ATextView(password),
            ATextView(otp),
            AButtonView(loginButton),
            AAvatarView(avatar),
            ATextView(name),
            ATextView(description),
            ANoteListView(notes)
    )
}
