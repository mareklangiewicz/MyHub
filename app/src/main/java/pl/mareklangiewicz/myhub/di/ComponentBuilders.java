package pl.mareklangiewicz.myhub.di;

import android.app.Application;

/**
 * Created by Marek Langiewicz on 01.02.16.
 * Separated cause Dagger needs it to be in Java (not Kotlin)
 */
public class ComponentBuilders {
    public static ApplicationComponent buildApplicationComponent(Application application) {
        return DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(application))
                .build();
    }
}
