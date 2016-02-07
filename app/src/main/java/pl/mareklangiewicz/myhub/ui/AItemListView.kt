package pl.mareklangiewicz.myhub.ui

import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import pl.mareklangiewicz.myhub.mvp.IItemListView
import rx.Observable

/**
 * Created by Marek Langiewicz on 06.02.16.
 * Android implementation of IItemListView
 */
open class AItemListView<Item>(
        private val recyclerView: RecyclerView,
        private val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(recyclerView.context),
        private val adapter: AItemListAdapter<Item>
) : AView(recyclerView), IItemListView<Item> {

    constructor(
            recyclerView: RecyclerView,
            layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(recyclerView.context),
            @LayoutRes rLayoutItemView: Int,
            bind: View.(Item) -> Unit
    ) : this(
            recyclerView,
            layoutManager,
            AItemListAdapter(rLayoutItemView, bind)
    )

    override var items: List<Item>
        get() = adapter.items
        set(value) {
            adapter.items = value
        }

    override val itemClicks: Observable<Item>
        get() = adapter.itemClicks

    init {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }
}
