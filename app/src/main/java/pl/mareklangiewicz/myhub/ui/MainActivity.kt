package pl.mareklangiewicz.myhub.ui

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Build
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.view.View
import android.view.animation.LinearInterpolator
import com.afollestad.materialdialogs.MaterialDialog
import io.realm.Realm
import pl.mareklangiewicz.myactivities.MyActivity
import pl.mareklangiewicz.mydrawables.MyLivingDrawable
import pl.mareklangiewicz.mydrawables.MyMagicLinesDrawable
import pl.mareklangiewicz.myhub.BuildConfig
import pl.mareklangiewicz.myhub.R
import pl.mareklangiewicz.myhub.data.Account
import pl.mareklangiewicz.myviews.IMyNavigation
import java.util.*

class MainActivity : MyActivity() {

    // TODO LATER: fix automatic change of V and VV boolean flags in MyBlocks to stop unnecessary logging (or at least just set it all by hand to false)
    // TODO LATER: new MyFragment for online github search
    // TODO SOMEDAY: do we want to change title on ToolBar depending of context? (I guess material guidelines say so..)
    // TODO SOMEDAY: option to star/unstar repos - then MyFragment with starred repos

    private lateinit var myMagicLinesDrawable: MyLivingDrawable
    private lateinit var logoTextViewAnimator: ObjectAnimator
    private lateinit var homePageTextViewAnimator: ObjectAnimator
    private lateinit var magicLinesDrawableAnimator: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val nav = globalNavigation!!

        nav.inflateHeader(R.layout.mg_global_header)
        nav.inflateMenu(R.menu.mg_global_menu)

        val header = nav.header

        if (BuildConfig.DEBUG) {
            val menu = nav.menu
            menu.findItem(R.id.ds_mode).setTitle("build type: " + BuildConfig.BUILD_TYPE)
            menu.findItem(R.id.ds_flavor).setTitle("build flavor: " + BuildConfig.FLAVOR)
            menu.findItem(R.id.ds_version_code).setTitle("version code: " + BuildConfig.VERSION_CODE)
            menu.findItem(R.id.ds_version_name).setTitle("version name: " + BuildConfig.VERSION_NAME)
            menu.findItem(R.id.ds_time_stamp).setTitle("build time: %tF %tT".format(Locale.US, BuildConfig.TIME_STAMP, BuildConfig.TIME_STAMP))
        }


        myMagicLinesDrawable = MyMagicLinesDrawable().setColor(822083583).setStrokeWidth(dp2px(4f))
        header.findViewById(R.id.magic_underline_view).background = myMagicLinesDrawable

        logoTextViewAnimator = ObjectAnimator.ofPropertyValuesHolder(
                header.findViewById(R.id.text_logo),
                PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 0.2f, 1f),
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -130f, -30f, 0f))
        homePageTextViewAnimator = ObjectAnimator.ofPropertyValuesHolder(
                header.findViewById(R.id.text_home_page),
                PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 0f, 0.7f),
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -50f, -50f, 0f))

        logoTextViewAnimator.interpolator = LinearInterpolator()
        homePageTextViewAnimator.interpolator = LinearInterpolator()

        magicLinesDrawableAnimator = ObjectAnimator.ofInt(myMagicLinesDrawable, "level", 0, 10000)
        magicLinesDrawableAnimator.setDuration(1000).interpolator = LinearInterpolator()


        if (savedInstanceState == null) {
            selectGlobalItem(R.id.mg_fragment_my_account)
        }
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        super.onDrawerSlide(drawerView, slideOffset)
        if (drawerView !== mGlobalNavigationView)
            return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            logoTextViewAnimator.setCurrentFraction(slideOffset)
            homePageTextViewAnimator.setCurrentFraction(slideOffset)
        }
    }


    override fun onDrawerOpened(drawerView: View) {
        super.onDrawerOpened(drawerView)
        if (drawerView !== mGlobalNavigationView)
            return
        if (!magicLinesDrawableAnimator.isStarted)
            magicLinesDrawableAnimator.start()
    }

    override fun onDrawerClosed(drawerView: View) {
        super.onDrawerClosed(drawerView)
        if (drawerView !== mGlobalNavigationView)
            return
        magicLinesDrawableAnimator.cancel()
        myMagicLinesDrawable.setLevel(0)
    }

    override fun onItemSelected(nav: IMyNavigation, item: MenuItem): Boolean {
        if (super.onItemSelected(nav, item))
            return true
        if (item.itemId == R.id.reset_all) {
            resetAll()
            return true
        }
        return false
    }

    private fun resetAll() {
        MaterialDialog.Builder(this).title(R.string.mg_reset_all).content(R.string.mg_are_you_sure_reset).positiveText(R.string.mg_reset).negativeText(R.string.mg_cancel).onPositive { dialog, which ->
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            realm.clear(Account::class.java)
            realm.commitTransaction()
            realm.close()
            onCommand("fragment .ui.MyAccountFragment")
        }.show()
    }

    fun showLocalNavigation() {
        if (mLocalDrawerLayout != null)
            mLocalDrawerLayout.openDrawer(GravityCompat.END)
    }
}

