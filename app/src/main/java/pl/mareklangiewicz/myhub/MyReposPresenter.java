package pl.mareklangiewicz.myhub;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.noveogroup.android.log.MyLogger;

import java.util.Locale;

import javax.inject.Inject;

import pl.mareklangiewicz.myhub.data.Account;
import pl.mareklangiewicz.myhub.mvp.IMyReposView;
import pl.mareklangiewicz.myhub.mvp.Presenter;
import rx.Observer;
import rx.Subscription;

@MainThread
public class MyReposPresenter extends Presenter<IMyReposView> {

    private MyLogger log = MyLogger.UIL;

    private @NonNull MGModel mModel;
    private @Nullable Subscription subscription;

    @Inject MyReposPresenter(@NonNull MGModel model) {
        mModel = model;
    }

    @Override public void attachIView(@NonNull IMyReposView iview) {
        super.attachIView(iview);
        subscription = mModel.loadLatestAccount()
                .subscribe(new Observer<Account>() {
                    @Override public void onCompleted() {
                        log.v("loading completed.");
                    }

                    @Override public void onError(Throwable e) {
                        log.e(e, "[SNACK]Error %s", e.getLocalizedMessage());
                    }

                    @Override public void onNext(Account account) {
                        showAccount(account);
                    }
                });
    }

    @Override public void detachIView() {
        if(subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
        subscription = null;
        super.detachIView();
    }

    /**
     * Displays given account on attached IView. Clears IView if account is null.
     */
    private void showAccount(@Nullable Account account) {
        IMyReposView view = getIView();
        if(view != null) {
            view.setStatus(
                    account != null
                            ?
                            String.format(Locale.US, "%s: %d repos (%tF %tT):",
                                    account.getLogin(),
                                    account.getRepos().size(),
                                    account.getTime(),
                                    account.getTime()
                            )
                            :
                            "no repos loaded"
            );
            view.setRepos(account != null ? account.getRepos() : null);
        }

    }
}
