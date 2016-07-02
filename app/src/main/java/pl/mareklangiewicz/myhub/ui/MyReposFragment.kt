package pl.mareklangiewicz.myhub.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.mh_fragment_my_repos.*
import kotlinx.android.synthetic.main.mh_notes.view.*
import pl.mareklangiewicz.myfragments.MyFragment
import pl.mareklangiewicz.myhub.*
import javax.inject.Inject

class MyReposFragment : MyFragment() {

    // TODO LATER: local search on ToolBar
    // TODO SOMEDAY: local menu with sorting options?

    @Inject lateinit var presenter: MyReposPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as MHApplication).component.inject(this)
        manager?.name = BuildConfig.NAME_PREFIX + getString(R.string.mh_my_repositories)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        manager!!.lnav!!.headerId = R.layout.mh_notes
        return inflater.inflate(R.layout.mh_fragment_my_repos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val v = AMyReposDiew(
                view as ViewGroup,
                mh_fmr_pb_progress,
                mh_fmr_tv_status,
                mh_fmr_rv_repos,
                manager!!.lnav!!.headerObj!!.mh_n_rv_notes
        )
        presenter.xiew = v
    }

    override fun onDestroyView() {
        manager?.lnav?.headerId = -1
        presenter.xiew = null
        super.onDestroyView()
    }
}

