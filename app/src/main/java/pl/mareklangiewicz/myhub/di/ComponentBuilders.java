package pl.mareklangiewicz.myhub.di;

import android.app.Application;

import pl.mareklangiewicz.myhub.MHApplication;

/**
 * Created by Marek Langiewicz on 01.02.16.
 */
public class ComponentBuilders {
    public static ApplicationComponent buildApplicationComponent(Application application) {
        return DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(application))
                .build();
    }
}
