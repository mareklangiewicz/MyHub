package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.mareklangiewicz.myhub.R
import pl.mareklangiewicz.myhub.data.Repo
import javax.inject.Inject


internal class ReposAdapter : RecyclerView.Adapter<ReposAdapter.ViewHolder> {

    var repos: List<Repo> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var callback: Callback? = null

    @Inject
    constructor() {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mg_item_repo, parent, false)
        val holder = ViewHolder(view)
        holder.contentTextView.setOnClickListener {
            if (callback != null) {
                callback!!.onItemClick(holder.repo)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //noinspection ConstantConditions
        val repo = repos[position]
        val context = holder.nameTextView.context
        holder.repo = repo
        holder.nameTextView.text = repo.name
        holder.descriptionTextView.text = repo.description
        holder.watchersTextView.text = context.resources.getString(R.string.mg_watchers, repo.watchers)
        holder.starsTextView.text = context.resources.getString(R.string.mg_stars, repo.stars)
        holder.forksTextView.text = context.resources.getString(R.string.mg_forks, repo.forks)
    }

    override fun getItemCount(): Int = repos.size

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentTextView: View = itemView.findViewById(R.id.content)
        val nameTextView: TextView = itemView.findViewById(R.id.name) as TextView
        val descriptionTextView: TextView = itemView.findViewById(R.id.description) as TextView
        val watchersTextView: TextView = itemView.findViewById(R.id.watchers) as TextView
        val starsTextView: TextView = itemView.findViewById(R.id.stars) as TextView
        val forksTextView: TextView = itemView.findViewById(R.id.forks) as TextView
        var repo: Repo? = null

    }

    interface Callback {
        fun onItemClick(repo: Repo?)
    }
}
