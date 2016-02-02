package pl.mareklangiewicz.myhub.ui

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.mh_fragment_my_repos.*
import pl.mareklangiewicz.myfragments.MyFragment
import pl.mareklangiewicz.myhub.*
import pl.mareklangiewicz.myhub.data.Note
import javax.inject.Inject

class MyReposFragment : MyFragment() {

    // TODO LATER: local search on ToolBar
    // TODO SOMEDAY: local menu with sorting options?

    @Inject lateinit var presenter: MyReposPresenter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as MHApplication).component.inject(this)
    }

    public override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflateHeader(R.layout.mh_notes)
        return inflater.inflate(R.layout.mh_fragment_my_repos, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val v = AMyReposView(
                mh_fmr_tv_status,
                AReposView(mh_fmr_rv_repos),
                ANotesView(header.findViewById(R.id.mh_n_rv_notes) as RecyclerView),
                AProgressView(mh_fmr_pb_progress, log)
        )
        v.notes = listOf(Note("No details", "Select repository to get details"))
        v.onClick = {
            v.notes = it.notes
            (activity as MHActivity).showLocalNavigation()
        }
        presenter.view = v
    }

    override fun onDestroyView() {
        presenter.view = null
        super.onDestroyView()
    }
}
