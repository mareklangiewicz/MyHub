package pl.mareklangiewicz.mygithub;

import android.app.Application;

import com.noveogroup.android.log.MyLogger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import pl.mareklangiewicz.mygithub.di.ApplicationComponent;
import pl.mareklangiewicz.mygithub.di.ApplicationModule;
import pl.mareklangiewicz.mygithub.di.DaggerApplicationComponent;

public class MGApplication extends Application {

    // TODO SOMEDAY: add Leak Canary? (just copy from MyIntent)

    private MyLogger log = MyLogger.UIL;

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

    @Override public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }

}
