package pl.mareklangiewicz.myhub.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import pl.mareklangiewicz.myloggers.*
import pl.mareklangiewicz.myutils.myhttp.GitHub
import javax.inject.Named
import javax.inject.Singleton

@Module class ApplicationModule(private val application: Application) {
    @Provides internal fun provideApplication(): Application = application
    @Provides @Named("Application") internal fun provideApplicationContext(application: Application): Context = application
    @Singleton @Provides @Named("UI") internal fun provideUILogger(): MyAndroLogger = MY_DEFAULT_ANDRO_LOGGER
    @Singleton @Provides internal fun provideGitHubService(): GitHub.Service = GitHub.service
}

