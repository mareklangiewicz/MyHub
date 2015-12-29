package pl.mareklangiewicz.mygithub;

import android.app.Application;

import pl.mareklangiewicz.mygithub.di.ApplicationComponent;
import pl.mareklangiewicz.mygithub.di.ApplicationModule;
import pl.mareklangiewicz.mygithub.di.DaggerApplicationComponent;

public class MGApplication extends Application {

    ApplicationComponent mComponent;

    public ApplicationComponent getComponent() {
        if (mComponent == null) {
            mComponent = DaggerApplicationComponent
                    .builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mComponent;
    }
}
