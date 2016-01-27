package pl.mareklangiewicz.myhub.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import pl.mareklangiewicz.myhub.MGApplication;
import pl.mareklangiewicz.myhub.ui.MyAccountFragment;
import pl.mareklangiewicz.myhub.ui.MyReposFragment;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @Named("Application") Context applicationContext();

    Application application();

    void inject(MGApplication a);
    void inject(MyAccountFragment f);
    void inject(MyReposFragment f);
}
