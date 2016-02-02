package pl.mareklangiewicz.myhub.di;

import android.app.Application;
import android.content.Context;

import com.noveogroup.android.log.MyLogger;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.mareklangiewicz.myhub.io.GitHub;

@Module
public class ApplicationModule {

    protected final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides Application provideApplication() {
        return application;
    }

    @Provides
    @Named("Application") Context provideApplicationContext(Application application) {
        return application;
    }

    @Singleton
    @Provides
    @Named("UI") MyLogger provideUILogger() { return MyLogger.UIL; }


    @Singleton
    @Provides GitHub.Service provideGitHubService() {
        return GitHub.create();
    }

}
