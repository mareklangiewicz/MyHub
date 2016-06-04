package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import pl.mareklangiewicz.myhub.data.Repo
import pl.mareklangiewicz.myhub.mvp.IRepoListView

/**
 * Created by Marek Langiewicz on 31.01.16.
 * Android implementation of IRepoListView
 */
internal class ARepoListView(private val recyclerView: RecyclerView)
: AItemListView<Repo>(recyclerView, ARepoListAdapter()), IRepoListView

