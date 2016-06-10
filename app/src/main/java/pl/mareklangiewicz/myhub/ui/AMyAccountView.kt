package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.*
import pl.mareklangiewicz.myhub.data.*
import pl.mareklangiewicz.myhub.mvp.*
import pl.mareklangiewicz.myviews.*
import pl.mareklangiewicz.myutils.*
import java.util.*


/**
 * Created by Marek Langiewicz on 29.01.16.
 * Android implementation of IMyAccountView
 */
class AMyAccountView(
        view: ViewGroup,
        override val progress: IProgressView,
        override val status: IStatusView,
        override val login: ITextView,
        override val password: ITextView,
        override val otp: ITextView,
        override val loginButton: IButtonView,
        override val avatar: IUrlImageView,
        override val name: ITextView,
        override val description: ITextView,
        override val notes: INoteLstView
) : AView<ViewGroup>(view),IMyAccountView {
    constructor(
            view: ViewGroup,
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
            view,
            AProgressView(progress),
            AStatusView(status),
            ATextView(login),
            ATextView(password),
            ATextView(otp),
            AButtonView(loginButton),
            AAvatarView(avatar),
            ATextView(name),
            ATextView(description),
            ANoteLstView(notes)
    )

    override var data: Account?
        get() = super.data // unsupported
        set(value) {
            status.highlight = value === null
            status.data = if(value === null) "not loaded." else "loaded: %tF %tT.".format(Locale.US, value.time, value.time)
            login.data = value?.login ?: ""
            avatar.url = value?.avatar ?: ""
            name.data = value?.name ?: ""
            description.data = value?.description ?: ""
            notes.data = value?.notes?.asLst() ?: Lst.of(Note("No info", "Log in to get info"))
        }
}
