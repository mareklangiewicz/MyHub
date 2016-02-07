package pl.mareklangiewicz.myhub.ui

import kotlinx.android.synthetic.main.mh_item_repo.view.*
import pl.mareklangiewicz.myhub.R
import pl.mareklangiewicz.myhub.data.Repo


internal class ARepoListAdapter : AItemListAdapter<Repo>(
        R.layout.mh_item_repo,
        {
            mh_ir_tv_name.text = it.name
            mh_ir_tv_description.text = it.description
            mh_ir_tv_watchers.text = context.resources.getString(R.string.mh_watchers, it.watchers)
            mh_ir_tv_stars.text = context.resources.getString(R.string.mh_stars, it.stars)
            mh_ir_tv_forks.text = context.resources.getString(R.string.mh_forks, it.forks)

        }
)
