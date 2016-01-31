package pl.mareklangiewicz.myhub.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import pl.mareklangiewicz.myfragments.MyFragment
import pl.mareklangiewicz.myhub.MGApplication
import pl.mareklangiewicz.myhub.MyReposPresenter
import pl.mareklangiewicz.myhub.R
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.data.Repo
import pl.mareklangiewicz.myhub.mvp.IMyReposView
import pl.mareklangiewicz.myhub.mvp.IProgressView
import javax.inject.Inject

class MyReposFragment : MyFragment(), IMyReposView, ReposAdapter.Callback {

    // TODO LATER: local search on ToolBar
    // TODO SOMEDAY: local menu with sorting options?

    public override var progress = IProgressView.HIDDEN
        set(value) {
            var newvalue = value
            if (newvalue == IProgressView.INDETERMINATE) {
                if (progressBar != null) {
                    progressBar!!.visibility = View.VISIBLE
                    progressBar!!.progress = IProgressView.MIN
                    progressBar!!.isIndeterminate = true
                }
            } else if (newvalue == IProgressView.HIDDEN) {
                if (progressBar != null) {
                    progressBar!!.visibility = View.INVISIBLE
                    progressBar!!.progress = IProgressView.MIN
                    progressBar!!.isIndeterminate = false
                }
            } else {
                if (newvalue < IProgressView.MIN) {
                    log.w("correcting progress value from %d to %d", newvalue, IProgressView.MIN)
                    newvalue = IProgressView.MIN
                } else if (newvalue > IProgressView.MAX) {
                    log.w("correcting progress value from %d to %d", newvalue, IProgressView.MAX)
                    newvalue = IProgressView.MAX
                }
                if (progressBar != null) {
                    progressBar!!.visibility = View.VISIBLE
                    progressBar!!.progress = newvalue
                    progressBar!!.isIndeterminate = false
                }
            }
            field = newvalue
        }
    public override var status: String = ""
        set(value) {
            field = value
            if (statusTextView != null)
                statusTextView!!.text = value
        }

    @Bind(R.id.progress_bar) internal var progressBar: ProgressBar? = null
    @Bind(R.id.text_view_status) internal var statusTextView: TextView? = null
    @Bind(R.id.repos_recycler_view) internal var reposRecyclerView: RecyclerView? = null

    internal var notesRecyclerView: RecyclerView? = null

    private val reposAdapter = ReposAdapter()
    private val notesAdapter = NotesAdapter()

    @Inject lateinit var presenter: MyReposPresenter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity.application as MGApplication).component.inject(this)
        reposAdapter.callback = this
        notesAdapter.notes = listOf(Note("No details", "Select repository to get details"))
        presenter.view = this
    }

    public override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState) //just for logging

        val rootView = inflater!!.inflate(R.layout.mg_fragment_my_repos, container, false)

        ButterKnife.bind(this, rootView)

        progress = progress

        val manager = LinearLayoutManager(activity)
        //noinspection ConstantConditions
        reposRecyclerView!!.layoutManager = manager
        reposRecyclerView!!.adapter = reposAdapter

        inflateHeader(R.layout.mg_notes)
        //noinspection ConstantConditions
        notesRecyclerView = ButterKnife.findById<RecyclerView>(header, R.id.notes_recycler_view)
        //noinspection ConstantConditions
        notesRecyclerView!!.layoutManager = WCLinearLayoutManager(activity) // LLM that understands "wrap_content"
        notesRecyclerView!!.adapter = notesAdapter

        return rootView
    }

    public override fun onDestroyView() {
        reposRecyclerView!!.adapter = null
        super.onDestroyView()
        ButterKnife.unbind(this)
    }

    public override fun onDestroy() {
        presenter.view = null
        super.onDestroy()
    }

    public override var repos: List<Repo>
        get() {
            return reposAdapter.repos
        }
        set(value) {
            reposAdapter.repos = value
        }

    public override fun onItemClick(repo: Repo?) {
        if (repo == null) {
            log.d("Clicked repo is null.")
            return
        }
        notesAdapter.notes = repo.notes
        (activity as MainActivity).showLocalNavigation()

    }
}
