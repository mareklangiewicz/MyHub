/** MVP Pattern related definitions - interfaces and base classes */

package pl.mareklangiewicz.myhub.mvp

import pl.mareklangiewicz.myhub.data.*
import pl.mareklangiewicz.myviews.*
import pl.mareklangiewicz.myutils.*

interface IModel

open class Presenter<V : IView> {

    /**
     * One can attach/detach (nullify) a view of presenter many times in its lifecycle
     * Usually concrete presenters will override setter and do some additional stuff on attach/detach.
     * On android the presenter usually lives as long as fragment (with setRetainInstance(true))
     * And view is attached on every onViewCreated, and detached on every onDestroyView.
     * So presenter survives device orientation changes etc...
     */
    open var view: V? = null
}

interface IStatusView : ITextView {
    var highlight: Boolean
}

interface INoteView : IDataView<Note?>

interface IRepoView : IDataView<Repo?>

interface INoteLstView : ILstView<Note>
interface IRepoLstView : ILstView<Repo> {
    val clicksFromRepos : IPusher<IRepoView, Cancel>
}

interface IMyAccountView : IDataView<Account?> {
    val progress: IProgressView
    val status: IStatusView
    val login: ITextView
    val password: ITextView
    val otp: ITextView
    val loginButton: IButtonView
    val avatar: IUrlImageView
    val name: ITextView
    val description: ITextView
    val notes: INoteLstView
}

interface IMyReposView : IDataView<Account?> {
    val progress: IProgressView
    val status: IStatusView
    val repos: IRepoLstView
    val notes: INoteLstView
    fun showNotes()
}

