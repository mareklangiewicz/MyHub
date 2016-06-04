/** MVP Pattern related definitions - interfaces and base classes */

package pl.mareklangiewicz.myhub.mvp

import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.data.Repo
import rx.Observable

interface IModel

interface IView {

    var visible: Boolean
        get() = true
        set(value) = throw UnsupportedOperationException()

    var enabled: Boolean
        get() = true
        set(value) = throw UnsupportedOperationException()

    val clicks: Observable<out IView>
        get() = throw UnsupportedOperationException()

}


open class Presenter<T : IView> {

    /**
     * One can attach/detach (nullify) a view of presenter many times in its lifecycle
     * Usually concrete presenters will override setter and do some additional stuff on attach/detach.
     * On android the presenter usually lives as long as fragment (with setRetainInstance(true))
     * And view is attached on every onViewCreated, and detached on every onDestroyView.
     * So presenter survives device orientation changes etc...
     */
    open var view: T? = null
}


interface IProgressView : IView {

    var indeterminate: Boolean // true means it should display something indicating that it is working and we don't know how far are we
        get() = false
        set(value) = throw UnsupportedOperationException()

    var min: Double
        get() = 0.0
        set(value) = throw UnsupportedOperationException()

    var max: Double
        get() = 100.0
        set(value) = throw UnsupportedOperationException()

    var pos: Double // presents the current progress. should be always between min and max
}

interface ITextView : IView {
    var text: String
    val textChanges: Observable<String>
        get() = throw UnsupportedOperationException()
}

interface IButtonView : ITextView

interface IStatusView : ITextView {
    var highlight: Boolean
}

interface IImageView : IView {
    var url: String // displays image available at specified url
}

interface IItemListView<Item> : IView {
    var items: List<Item>
    val itemClicks: Observable<Item>
        get() = throw UnsupportedOperationException()
}

interface INoteListView : IItemListView<Note>
interface IRepoListView : IItemListView<Repo>

interface IMyAccountView : IView {
    val progress: IProgressView
    val status: IStatusView
    val login: ITextView
    val password: ITextView
    val otp: ITextView
    val loginButton: IButtonView
    val avatar: IImageView
    val name: ITextView
    val description: ITextView
    val notes: INoteListView
}

interface IMyReposView : IView {
    val progress: IProgressView
    val status: IStatusView
    val repos: IRepoListView
    val notes: INoteListView
    fun showNotes()
}

