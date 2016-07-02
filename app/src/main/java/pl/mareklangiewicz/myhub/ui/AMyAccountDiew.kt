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
class AMyAccountDiew(
        view: ViewGroup,
        override val progress: IProgressDiew,
        override val status: IStatusTiew,
        override val login: ITiew,
        override val password: ITiew,
        override val otp: ITiew,
        override val loginButton: IButtonTiew,
        override val avatar: IUrlImageXiew,
        override val name: ITiew,
        override val description: ITiew,
        override val notes: INoteLstDiew
) : ADiew<ViewGroup, Account?>(view), IMyAccountDiew {
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
            AProgressDiew(progress),
            AStatusTiew(status),
            ATiew(login),
            ATiew(password),
            ATiew(otp),
            AButtonTiew(loginButton),
            AAvatarXiew(avatar),
            ATiew(name),
            ATiew(description),
            ANoteLstDiew(notes)
    )

    override var data: Account?
        get() = throw UnsupportedOperationException()
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
