package pl.mareklangiewicz.mygithub.ui;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import io.realm.Realm;
import pl.mareklangiewicz.myactivities.MyActivity;
import pl.mareklangiewicz.mydrawables.MyLivingDrawable;
import pl.mareklangiewicz.mydrawables.MyMagicLinesDrawable;
import pl.mareklangiewicz.mygithub.BuildConfig;
import pl.mareklangiewicz.mygithub.R;
import pl.mareklangiewicz.mygithub.data.Account;
import pl.mareklangiewicz.myviews.IMyNavigation;

public class MainActivity extends MyActivity {

    // TODO LATER: fix automatic change of V and VV boolean flags in MyBlocks to stop unnecessary logging (or at least just set it all by hand to false)
    // TODO LATER: new MyFragment for online github search
    // TODO SOMEDAY: do we want to change title on ToolBar depending of context? (I guess material guidelines say so..)
    // TODO SOMEDAY: option to star/unstar repos - then MyFragment with starred repos

    private @Nullable MyLivingDrawable mMyMagicLinesDrawable;
    private @Nullable ObjectAnimator mLogoTextViewAnimator;
    private @Nullable ObjectAnimator mHomePageTextViewAnimator;
    private @Nullable ObjectAnimator mMagicLinesDrawableAnimator;

    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //noinspection ConstantConditions
        getGlobalNavigation().inflateHeader(R.layout.mg_global_header);
        getGlobalNavigation().inflateMenu(R.menu.mg_global_menu);

        View header = getGlobalNavigation().getHeader();

        if(BuildConfig.DEBUG) {
            Menu menu = getGlobalNavigation().getMenu();
            //noinspection ConstantConditions
            menu.findItem(R.id.ds_mode).setTitle("build type: " + BuildConfig.BUILD_TYPE);
            menu.findItem(R.id.ds_flavor).setTitle("build flavor: " + BuildConfig.FLAVOR);
            menu.findItem(R.id.ds_version_code).setTitle("version code: " + BuildConfig.VERSION_CODE);
            menu.findItem(R.id.ds_version_name).setTitle("version name: " + BuildConfig.VERSION_NAME);
            menu.findItem(R.id.ds_time_stamp).setTitle(String.format("build time: %tF %tT", BuildConfig.TIME_STAMP, BuildConfig.TIME_STAMP));
        }


        mMyMagicLinesDrawable = new MyMagicLinesDrawable();
        mMyMagicLinesDrawable.setColor(0x30ffffff).setStrokeWidth(dp2px(4));
        //noinspection ConstantConditions
        header.findViewById(R.id.magic_underline_view).setBackground(mMyMagicLinesDrawable);

        mLogoTextViewAnimator = ObjectAnimator.ofPropertyValuesHolder(
                header.findViewById(R.id.text_logo),
                PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 0.2f, 1f),
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -130f, -30f, 0f)
        );
        mHomePageTextViewAnimator = ObjectAnimator.ofPropertyValuesHolder(
                header.findViewById(R.id.text_home_page),
                PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 0f, 0.7f),
                PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -50f, -50f, 0f)
        );

        mLogoTextViewAnimator.setInterpolator(new LinearInterpolator());
        mHomePageTextViewAnimator.setInterpolator(new LinearInterpolator());

        mMagicLinesDrawableAnimator = ObjectAnimator.ofInt(mMyMagicLinesDrawable, "level", 0, 10000);
        mMagicLinesDrawableAnimator.setDuration(1000).setInterpolator(new LinearInterpolator());


        if(savedInstanceState == null) {
            selectGlobalItem(R.id.mg_fragment_my_account);
        }
    }

    @Override public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        if(drawerView != mGlobalNavigationView)
            return;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if(mLogoTextViewAnimator != null)
                mLogoTextViewAnimator.setCurrentFraction(slideOffset);
            if(mHomePageTextViewAnimator != null)
                mHomePageTextViewAnimator.setCurrentFraction(slideOffset);
        }
    }


    @Override public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        if(drawerView != mGlobalNavigationView)
            return;
        if(mMagicLinesDrawableAnimator != null)
            if(!mMagicLinesDrawableAnimator.isStarted())
                mMagicLinesDrawableAnimator.start();
    }

    @Override public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        if(drawerView != mGlobalNavigationView)
            return;
        if(mMagicLinesDrawableAnimator != null)
            mMagicLinesDrawableAnimator.cancel();
        if(mMyMagicLinesDrawable != null)
            mMyMagicLinesDrawable.setLevel(0);
    }

    @Override public boolean onItemSelected(IMyNavigation nav, MenuItem item) {
        boolean done = super.onItemSelected(nav, item);
        if(done)
            return true;
        if(item.getItemId() == R.id.reset_all) {
            resetAll();
            return true;
        }
        return false;
    }

    private void resetAll() {
        new MaterialDialog.Builder(this)
                .title(R.string.mg_reset_all)
                .content(R.string.mg_are_you_sure_reset)
                .positiveText(R.string.mg_reset)
                .negativeText(R.string.mg_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.clear(Account.class);
                        realm.commitTransaction();
                        realm.close();
                        onCommand("fragment .ui.MyAccountFragment");
                    }
                })
                .show();
    }

    public void showLocalNavigation() {
        if(mLocalDrawerLayout != null)
            mLocalDrawerLayout.openDrawer(GravityCompat.END);
    }
}

