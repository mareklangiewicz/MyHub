package pl.mareklangiewicz.myhub.ui

import android.os.Bundle
import android.view.View
import hu.supercluster.paperwork.Paperwork
import pl.mareklangiewicz.myfragments.MyAboutFragment
import pl.mareklangiewicz.myhub.*
import pl.mareklangiewicz.myutils.str
import java.util.*

/**
 *
 * Created by Marek Langiewicz on 06.04.16.
 */
@Suppress("unused")
class MHAboutFragment : MyAboutFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manager?.name = BuildConfig.NAME_PREFIX + getString(R.string.mh_about)

        val paperwork = Paperwork(activity)

        title = "My Hub"
        description = "This sample app shows usage of some modern android libraries together. See: https://github.com/langara/MyHub"

        details = listOf(
                "build type" to BuildConfig.BUILD_TYPE,
                "build flavor" to BuildConfig.FLAVOR,
                "version code" to BuildConfig.VERSION_CODE.str,
                "version name" to BuildConfig.VERSION_NAME,
                "build time" to paperwork.get("buildTime"),
                "app start time" to "%tF %tT".format(Locale.getDefault(), APP_START_TIME, APP_START_TIME),
                "git sha" to paperwork.get("gitSha"),
                "git tag" to paperwork.get("gitTag"),
                "git info" to paperwork.get("gitInfo")
        )
    }
}



