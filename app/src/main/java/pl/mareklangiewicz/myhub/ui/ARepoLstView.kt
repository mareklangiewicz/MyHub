package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import pl.mareklangiewicz.myhub.mvp.*
import kotlinx.android.synthetic.main.mh_item_repo.view.*
import pl.mareklangiewicz.myhub.data.Repo
import pl.mareklangiewicz.myutils.*
import pl.mareklangiewicz.myviews.*
import pl.mareklangiewicz.myhub.R

/**
 * Created by Marek Langiewicz on 10.06.16.
 */
class ARepoLstView(rview: RecyclerView) : IRepoLstView, MyRecyclerView<Repo, ARepoView>(rview) {

    override val clicksFromRepos = Relay<ARepoView>()

    override fun create(): ARepoView {
        val vgroup = view.inflate<ViewGroup>(R.layout.mh_item_repo)!!
        return ARepoView(
                vgroup,
                vgroup.mh_ir_tv_name,
                vgroup.mh_ir_tv_description,
                vgroup.mh_ir_tv_watchers,
                vgroup.mh_ir_tv_stars,
                vgroup.mh_ir_tv_forks
        ).apply { clicks { clicksFromRepos.push(it as ARepoView) } }
    }

    override fun bind(view: ARepoView, item: Repo) { view.data = item }
}
