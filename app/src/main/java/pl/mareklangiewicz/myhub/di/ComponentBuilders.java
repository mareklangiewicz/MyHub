package pl.mareklangiewicz.myhub.di;

import android.app.Application;

/**
 * Created by Marek Langiewicz on 01.02.16.
 * Separated cause Dagger needs it to be in Java (not Kotlin)
 * TODO LATER: check jetbrains:kotlin-examples/gradle/android-dagger
 * TODO LATER: when they update it to work with new android gradle plugin etc..
 * TODO LATER: (so I can get rid of java code)
 */
public class ComponentBuilders {
    public static ApplicationComponent buildApplicationComponent(Application application) {
        return DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(application))
                .build();
    }
}
