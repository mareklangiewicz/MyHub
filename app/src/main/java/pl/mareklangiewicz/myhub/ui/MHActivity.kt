package pl.mareklangiewicz.myhub.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import io.realm.Realm
import kotlinx.android.synthetic.main.mh_global_header.view.*
import pl.mareklangiewicz.myactivities.MyActivity
import pl.mareklangiewicz.myhub.*
import pl.mareklangiewicz.myutils.MyCommand

class MHActivity : MyActivity() {

    // TODO LATER: fix automatic change of V and VV boolean flags in MyBlocks to stop unnecessary logging (or at least just set it all by hand to false)
    // TODO LATER: new MyFragment for online github search
    // TODO SOMEDAY: option to star/unstar repos - then MyFragment with starred repos

    private lateinit var animations: MHActivityAnimations

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val nav = gnav!!

        nav.headerId = R.layout.mh_global_header
        nav.menuId = R.menu.mh_global_menu

        val header = nav.headerObj!!

        animations = MHActivityAnimations(
                header.mh_gh_tv_logo,
                header.mh_gh_tv_home_page,
                header.mh_gh_v_magic_underline,
                lcolor = 822083583,
                lwidth = dp2px(4f)
        )

        nav.items { // we ignore returned subscription - navigation will live as long as activity
            when(it) {
                R.id.mh_gm_i_clear_data -> clearAll()
            }
        }

        if (savedInstanceState == null) nav.setCheckedItem(R.id.mh_gm_i_f_my_account, true)
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        super.onDrawerSlide(drawerView, slideOffset)
        if (drawerView !== gnav)
            return
        animations.onGlobalDrawerSlide(slideOffset)
    }


    override fun onDrawerOpened(drawerView: View) {
        super.onDrawerOpened(drawerView)
        if (drawerView !== gnav)
            return
        animations.onGlobalDrawerOpened()
    }

    override fun onDrawerClosed(drawerView: View) {
        super.onDrawerClosed(drawerView)
        if (drawerView !== gnav)
            return
        animations.onGlobalDrawerClosed()
    }

    private fun clearAll() {
        MaterialDialog.Builder(this)
                .title(R.string.mh_clear_data)
                .content(R.string.mh_are_you_sure_clear)
                .positiveText(R.string.mh_clear)
                .negativeText(R.string.mh_cancel)
                .onPositive { _, _ ->
                    val realm = Realm.getDefaultInstance()
                    realm.beginTransaction()
                    realm.deleteAll()
                    realm.commitTransaction()
                    realm.close()
                    execute("fragment .ui.MyAccountFragment")
                }
                .show()
    }

    fun showLNav() {
        val drawer = findViewById(R.id.ma_local_drawer_layout) as? DrawerLayout
        if(drawer !== null) {
            drawer.openDrawer(GravityCompat.END)
            return
        }
    }

    override fun onCommandCustom(command: MyCommand) {
        when(command["action"]) {
            "orientation" -> when(command["data"]) {
                "portrait" -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                "landscape" -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                "unspecified" -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
            else -> super.onCommandCustom(command)
        }
    }

    override fun onCommandStartFragment(command: MyCommand) {
        val f = fgmt
        if(f !== null)
            (application as? MHApplication)?.REF_WATCHER?.watch(f)
        super.onCommandStartFragment(command)
    }
}

