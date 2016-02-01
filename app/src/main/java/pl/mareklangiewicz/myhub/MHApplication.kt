package pl.mareklangiewicz.myhub

import android.app.Application
//import com.squareup.leakcanary.LeakCanary
import io.realm.Realm
import io.realm.RealmConfiguration
import pl.mareklangiewicz.myhub.di.ApplicationComponent
import pl.mareklangiewicz.myhub.di.ComponentBuilders

class MHApplication : Application() {

    val component: ApplicationComponent by lazy { ComponentBuilders.buildApplicationComponent(this) }

    override fun onCreate() {
        super.onCreate()
        val config = RealmConfiguration.Builder(this).build()
        Realm.setDefaultConfiguration(config)
//        LeakCanary.install(this)
    }

}
