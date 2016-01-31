package pl.mareklangiewicz.myhub;

import android.app.Application;

import com.noveogroup.android.log.MyLogger;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;
import javax.inject.Named;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import pl.mareklangiewicz.myhub.di.*;

public class MGApplication extends Application {

    @Inject @Named("UI") MyLogger log;

    ApplicationComponent component;

    public ApplicationComponent getComponent() {
        if (component == null) {
            component = DaggerApplicationComponent
                    .builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return component;
    }

    @Override public void onCreate() {
        super.onCreate();
        getComponent().inject(this);
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
        LeakCanary.install(this);
    }

}
