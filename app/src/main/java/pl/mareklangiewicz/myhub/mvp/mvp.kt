/** MVP Pattern related definitions - interfaces and base classes */

package pl.mareklangiewicz.myhub.mvp

import pl.mareklangiewicz.myhub.data.*
import pl.mareklangiewicz.myviews.*
import pl.mareklangiewicz.myutils.*

interface IModel

interface IPresenter<X : IXiew> {

    /**
     * One can attach/detach (nullify) a view to a presenter many times in its lifecycle
     * Usually concrete presenters will do some additional stuff on attach/detach.
     * On android the presenter usually lives as long as fragment (with setRetainInstance(true))
     * And view is attached on every onViewCreated, and detached on every onDestroyView.
     * So presenter survives device orientation changes etc...
     */
    var xiew: X?
}

interface IStatusTiew : ITiew {
    var highlight: Boolean
}

interface INoteDiew : IDiew<Note?>

interface IRepoDiew : IDiew<Repo?>

interface INoteLstDiew : ILstDiew<Note>
interface IRepoLstDiew : ILstDiew<Repo> {
    val clicksFromRepos : IPusher<IRepoDiew, Cancel>
}

interface IMyAccountDiew : IDiew<Account?> {
    val progress: IProgressDiew
    val status: IStatusTiew
    val login: ITiew
    val password: ITiew
    val otp: ITiew
    val loginButton: IButtonTiew
    val avatar: IUrlImageXiew
    val name: ITiew
    val description: ITiew
    val notes: INoteLstDiew
}

interface IMyReposDiew : IDiew<Account?> {
    val progress: IProgressDiew
    val status: IStatusTiew
    val repos: IRepoLstDiew
    val notes: INoteLstDiew
    fun showNotes()
}

