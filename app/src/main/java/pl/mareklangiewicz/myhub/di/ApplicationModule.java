package pl.mareklangiewicz.myhub.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import pl.mareklangiewicz.myhub.io.GitHub;

@Module
public class ApplicationModule {

    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Named("Application") Context provideContext() {
        return mApplication;
    }

    @Provides GitHub.Service provideGitHubService() {
        return GitHub.create();
    }

}
