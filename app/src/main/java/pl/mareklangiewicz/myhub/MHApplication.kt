package pl.mareklangiewicz.myhub

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import io.realm.Realm
import io.realm.RealmConfiguration
import pl.mareklangiewicz.myhub.di.ApplicationComponent
import pl.mareklangiewicz.myhub.di.ApplicationModule
import pl.mareklangiewicz.myhub.di.DaggerApplicationComponent

val APP_START_TIME = System.currentTimeMillis()

class MHApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
    }

    lateinit var REF_WATCHER: RefWatcher

    override fun onCreate() {
        super.onCreate()
        val config = RealmConfiguration.Builder(this).build()
        Realm.setDefaultConfiguration(config)
        REF_WATCHER = LeakCanary.install(this)
    }

}
