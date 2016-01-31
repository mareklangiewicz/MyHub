/** MVP Pattern related definitions - interfaces and base classes */

package pl.mareklangiewicz.myhub.mvp

import pl.mareklangiewicz.myhub.data.Note
import pl.mareklangiewicz.myhub.data.Repo

interface IModel
interface IView

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
    var progress: Int

    companion object {
        const val HIDDEN = -1
        const val INDETERMINATE = -2 // some moving state indicating that something is happening
        const val MIN = 0
        const val MAX = 10000
    }
}

interface INotesView : IView {
    var notes: List<Note>
    // TODO SOMEDAY: change to mutable list.. but we don't need that now (we just change it to whole new list when we have new data)
}

interface IReposView : IView {
    var status: String
    var repos: List<Repo>
    // TODO SOMEDAY: change to mutable list.. but we don't need that now (we just change it to whole new list when we have new data)
}

interface IMyReposView : IReposView, IProgressView

interface IMyAccountView : INotesView, IProgressView {
    var status: String
    var login: String
    var password: String
    var otp: String
    var avatar: String
    var name: String
    var description: String
}
