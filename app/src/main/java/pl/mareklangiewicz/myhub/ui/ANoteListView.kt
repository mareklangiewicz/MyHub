package pl.mareklangiewicz.myhub.ui

import android.support.v7.widget.RecyclerView
import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.mvp.INoteListView

/**
 * Created by Marek Langiewicz on 29.01.16.
 * Android implementation of INoteListView
 */
internal open class ANoteListView(val recyclerView: RecyclerView)
: AItemListView<Note>(recyclerView, WCLinearLayoutManager(recyclerView.context), ANoteListAdapter()), INoteListView
