package pl.mareklangiewicz.myhub.ui

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import hu.supercluster.paperwork.Paperwork
import io.realm.Realm
import kotlinx.android.synthetic.main.mh_global_header.view.*
import pl.mareklangiewicz.myactivities.MyActivity
import pl.mareklangiewicz.myhub.BuildConfig
import pl.mareklangiewicz.myhub.R
import pl.mareklangiewicz.myhub.data.Account
import pl.mareklangiewicz.myviews.IMyNavigation

class MHActivity : MyActivity() {

    // TODO LATER: fix automatic change of V and VV boolean flags in MyBlocks to stop unnecessary logging (or at least just set it all by hand to false)
    // TODO LATER: new MyFragment for online github search
    // TODO SOMEDAY: do we want to change title on ToolBar depending of context? (I guess material guidelines say so..)
    // TODO SOMEDAY: option to star/unstar repos - then MyFragment with starred repos

    private lateinit var animations: MHAnimations

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val nav = globalNavigation!!

        nav.inflateHeader(R.layout.mh_global_header)
        nav.inflateMenu(R.menu.mh_global_menu)

        val header = nav.header

        if (BuildConfig.DEBUG) {
            val paperwork = Paperwork(this)
            val menu = nav.menu
            menu.findItem(R.id.mh_gm_i_ds_mode).setTitle("build type: ${BuildConfig.BUILD_TYPE}")
            menu.findItem(R.id.mh_gm_i_ds_flavor).setTitle("build flavor: ${BuildConfig.FLAVOR}")
            menu.findItem(R.id.mh_gm_i_ds_version_code).setTitle("version code: ${BuildConfig.VERSION_CODE}")
            menu.findItem(R.id.mh_gm_i_ds_version_name).setTitle("version name: ${BuildConfig.VERSION_NAME}")
            menu.findItem(R.id.mh_gm_i_ds_build_time).setTitle("build time: ${paperwork.get("buildTime")}")
            menu.findItem(R.id.mh_gm_i_ds_git_sha).setTitle("git sha: ${paperwork.get("gitSha")}")
            menu.findItem(R.id.mh_gm_i_ds_git_tag).setTitle("git tag: ${paperwork.get("gitTag")}")
            menu.findItem(R.id.mh_gm_i_ds_git_info).setTitle("git info: ${paperwork.get("gitInfo")}")
        }


        animations = MHAnimations(
                header.mh_gh_tv_logo,
                header.mh_gh_tv_home_page,
                header.mh_gh_v_magic_underline,
                lcolor = 822083583,
                lwidth = dp2px(4f)
        )

        if (savedInstanceState == null) {
            selectGlobalItem(R.id.mh_gm_i_f_my_account)
        }
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        super.onDrawerSlide(drawerView, slideOffset)
        if (drawerView !== mGlobalNavigationView)
            return
        animations.onGlobalDrawerSlide(slideOffset)
    }


    override fun onDrawerOpened(drawerView: View) {
        super.onDrawerOpened(drawerView)
        if (drawerView !== mGlobalNavigationView)
            return
        animations.onGlobalDrawerOpened()
    }

    override fun onDrawerClosed(drawerView: View) {
        super.onDrawerClosed(drawerView)
        if (drawerView !== mGlobalNavigationView)
            return
        animations.onGlobalDrawerClosed()
    }

    override fun onItemSelected(nav: IMyNavigation, item: MenuItem): Boolean {
        if (super.onItemSelected(nav, item))
            return true
        if (item.itemId == R.id.mh_gm_i_clear_data) {
            clearAll()
            return true
        }
        return false
    }

    private fun clearAll() {
        MaterialDialog.Builder(this)
                .title(R.string.mh_clear_data)
                .content(R.string.mh_are_you_sure_clear)
                .positiveText(R.string.mh_clear)
                .negativeText(R.string.mh_cancel)
                .onPositive { dialog, which ->
                    val realm = Realm.getDefaultInstance()
                    realm.beginTransaction()
                    realm.clear(Account::class.java)
                    realm.commitTransaction()
                    realm.close()
                    onCommand("fragment .ui.MyAccountFragment")
                }
                .show()
    }

    fun showLocalNavigation() {
        if (mLocalDrawerLayout != null)
            mLocalDrawerLayout.openDrawer(GravityCompat.END)
    }
}

