package pl.mareklangiewicz.myhub.di

import android.app.Application
import android.content.Context

import com.noveogroup.android.log.MyLogger

import javax.inject.Named
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import pl.mareklangiewicz.myhub.io.GithubService
import pl.mareklangiewicz.myhub.io.createGithubService

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
        return MyLogger.UIL
    }


    @Singleton
    @Provides
    internal fun provideGitHubService(): GithubService {
        return createGithubService()
    }

}
