package pl.mareklangiewicz.myhub.di

import android.app.Application
import android.content.Context

import javax.inject.Named
import javax.inject.Singleton

import dagger.Component
import pl.mareklangiewicz.myhub.MHApplication
import pl.mareklangiewicz.myhub.ui.MyAccountFragment
import pl.mareklangiewicz.myhub.ui.MyReposFragment

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    @Named("Application") fun applicationContext(): Context

    fun application(): Application

    fun inject(a: MHApplication)
    fun inject(f: MyAccountFragment)
    fun inject(f: MyReposFragment)
}
