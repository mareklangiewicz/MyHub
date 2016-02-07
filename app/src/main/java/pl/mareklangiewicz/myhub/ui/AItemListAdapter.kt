package pl.mareklangiewicz.myhub.ui

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxrelay.PublishRelay

open class AItemListAdapter<Item>(
        @LayoutRes private val rLayoutItemView: Int,
        private val bind: View.(Item) -> Unit
) : RecyclerView.Adapter<AItemListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

    var items: List<Item> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(onCreateView(parent, viewType))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.itemView
        val item = items[position]
        view.setOnClickListener { itemClicks.call(item) }
        onBindView(view, item)
    }

    open protected fun onCreateView(parent: ViewGroup, position: Int): View {
        return LayoutInflater.from(parent.context).inflate(rLayoutItemView, parent, false)
    }

    open protected fun onBindView(view: View, item: Item) {
        view.bind(item)
    }

    val itemClicks: PublishRelay<Item> = PublishRelay.create()
        get() = field


}
