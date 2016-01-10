package pl.mareklangiewicz.mygithub;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
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

    @Override public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        if(BuildConfig.DEBUG)
            Realm.deleteRealm(config); // TODO someday comment this
        Realm.setDefaultConfiguration(config);

    }

}
