package pl.mareklangiewicz.myhub.ui
import android.view.ViewGroup
import pl.mareklangiewicz.myhub.mvp.*
import pl.mareklangiewicz.myviews.*
import android.widget.*
import pl.mareklangiewicz.myhub.data.Repo
import pl.mareklangiewicz.myutils.*

/** Created by Marek Langiewicz on 09.06.16. */
class ARepoDiew(
        view: ViewGroup,
        private val name: ITiew,
        private val description: ITiew,
        private val watchers: ITiew,
        private val stars: ITiew,
        private val forks: ITiew
) : ADiew<ViewGroup, Repo?>(view), IRepoDiew {
    constructor(
            view: ViewGroup,
            tvname: TextView,
            tvdesc: TextView,
            tvwatchers: TextView,
            tvstars: TextView,
            tvforks: TextView

    ) : this(
            view,
            ATiew(tvname),
            ATiew(tvdesc),
            ATiew(tvwatchers),
            ATiew(tvstars),
            ATiew(tvforks))

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


