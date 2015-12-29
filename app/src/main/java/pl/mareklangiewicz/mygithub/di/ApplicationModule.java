package pl.mareklangiewicz.mygithub.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;

import dagger.*;

@Module
public class ApplicationModule {

    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Named("Application") Context provideContext() {
        return mApplication;
    }

}
