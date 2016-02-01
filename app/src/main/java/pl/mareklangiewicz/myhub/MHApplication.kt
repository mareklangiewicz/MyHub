package pl.mareklangiewicz.myhub

import android.app.Application

import com.noveogroup.android.log.MyLogger
import com.squareup.leakcanary.LeakCanary

import javax.inject.Inject
import javax.inject.Named

import io.realm.Realm
import io.realm.RealmConfiguration
import pl.mareklangiewicz.myhub.di.*

class MHApplication : Application() {

    val component: ApplicationComponent by lazy { ComponentBuilders.buildApplicationComponent(this) }

    override fun onCreate() {
        super.onCreate()
        val config = RealmConfiguration.Builder(this).build()
        Realm.setDefaultConfiguration(config)
        LeakCanary.install(this)
    }

}
