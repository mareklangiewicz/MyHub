package pl.mareklangiewicz.myhub.di

import android.app.Application
import android.content.Context

import com.noveogroup.android.log.Logger
import com.noveogroup.android.log.MyLogger

import javax.inject.Named
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import pl.mareklangiewicz.myhub.io.GitHub

@Module
class ApplicationModule(protected val application: Application) {

    @Provides internal fun provideApplication(): Application {
        return application
    }

    @Provides
    @Named("Application")
    internal fun provideApplicationContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    @Named("UI")
    internal fun provideUILogger(): MyLogger {
        return MyLogger("MyHub", Logger.Level.ERROR, "%logger", "%s")
    }


    @Singleton
    @Provides
    internal fun provideGitHubService(): GitHub.Service {
        return GitHub.create()
    }

}