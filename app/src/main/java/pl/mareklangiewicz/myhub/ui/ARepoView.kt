package pl.mareklangiewicz.myhub.ui
import android.view.ViewGroup
import pl.mareklangiewicz.myhub.mvp.*
import pl.mareklangiewicz.myviews.*
import android.widget.*
import pl.mareklangiewicz.myhub.data.Repo
import pl.mareklangiewicz.myutils.*

/**
 * Created by Marek Langiewicz on 09.06.16.
 */
class ARepoView(
        view: ViewGroup,
        private val name: ITextView,
        private val description: ITextView,
        private val watchers: ITextView,
        private val stars: ITextView,
        private val forks: ITextView
) : AView<ViewGroup>(view), IRepoView {
    constructor(
            view: ViewGroup,
            tvname: TextView,
            tvdesc: TextView,
            tvwatchers: TextView,
            tvstars: TextView,
            tvforks: TextView

    ) : this(
            view,
            ATextView(tvname),
            ATextView(tvdesc),
            ATextView(tvwatchers),
            ATextView(tvstars),
            ATextView(tvforks))

    override var data: Repo? = null
        set(value) {
            field = value
            name.data = value?.name ?: ""
            description.data = value?.description ?: ""
            watchers.data = value?.description ?: ""
            watchers.data = value?.watchers?.str ?: ""
            stars.data = value?.stars?.str ?: ""
            forks.data = value?.forks?.str ?: ""
        }
}


