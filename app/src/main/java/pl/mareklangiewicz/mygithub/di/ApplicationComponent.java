package pl.mareklangiewicz.mygithub.di;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;
import pl.mareklangiewicz.mygithub.ui.MyReposFragment;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @Named("Application") Context context();

    Application application();

    void inject(MyReposFragment f);
}
