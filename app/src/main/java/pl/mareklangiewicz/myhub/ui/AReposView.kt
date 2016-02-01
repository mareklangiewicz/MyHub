package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import pl.mareklangiewicz.myhub.mvp.IReposView

/**
 * Created by Marek Langiewicz on 31.01.16.
 * Android implementation of IReposView
 */
internal class AReposView(private val rv: RecyclerView, private val adapter: ReposAdapter = ReposAdapter()) : IReposView by adapter {
    init {
        rv.layoutManager = WCLinearLayoutManager(rv.context) // LLM that understands "wrap_content"
        rv.adapter = adapter
    }
}
