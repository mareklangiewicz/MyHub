package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.mareklangiewicz.myhub.R
import pl.mareklangiewicz.myhub.data.Repo
import pl.mareklangiewicz.myhub.mvp.IReposView


internal class ReposAdapter : RecyclerView.Adapter<ReposAdapter.ViewHolder>, IReposView {

    constructor() { }

    override var repos: List<Repo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override var onClick: (repo: Repo) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mh_item_repo, parent, false)
        val holder = ViewHolder(view)
        holder.content.setOnClickListener { onClick(holder.repo) }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = repos[position]
        val context = holder.name.context
        holder.repo = repo
        holder.name.text = repo.name
        holder.description.text = repo.description
        holder.watchers.text = context.resources.getString(R.string.mh_watchers, repo.watchers)
        holder.stars.text = context.resources.getString(R.string.mh_stars, repo.stars)
        holder.forks.text = context.resources.getString(R.string.mh_forks, repo.forks)
    }

    override fun getItemCount(): Int = repos.size

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content: View = itemView.findViewById(R.id.mh_ir_ll_content)
        val name: TextView = itemView.findViewById(R.id.mh_ir_tv_name) as TextView
        val description: TextView = itemView.findViewById(R.id.mh_ir_tv_description) as TextView
        val watchers: TextView = itemView.findViewById(R.id.mh_ir_tv_watchers) as TextView
        val stars: TextView = itemView.findViewById(R.id.mh_ir_tv_stars) as TextView
        val forks: TextView = itemView.findViewById(R.id.mh_ir_tv_forks) as TextView
        lateinit var repo: Repo
    }

}
