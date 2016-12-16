package com.oleg.hubal.topfour.ui.activity.topfour;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.oleg.hubal.topfour.R;
import com.oleg.hubal.topfour.model.VenueItem;
import com.oleg.hubal.topfour.presentation.presenter.topfour.TopFourPresenter;
import com.oleg.hubal.topfour.presentation.view.topfour.TopFourView;
import com.oleg.hubal.topfour.ui.fragment.profile.ProfileFragment;
import com.oleg.hubal.topfour.ui.fragment.venue_item.VenueItemFragment;
import com.oleg.hubal.topfour.ui.fragment.venue_pager.VenuePagerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopFourActivity extends MvpAppCompatActivity implements TopFourView, VenuePagerFragment.OnShowVenueItemListener {
    private static final long DRAWER_ITEM_VENUS_ID = 32;

    public static final String TAG = "TopFourActivity";

    private Drawer mDrawer;

    @BindView(R.id.toolbar_top_four)
    Toolbar mTopFourToolbar;

    @InjectPresenter(type = PresenterType.GLOBAL)
    TopFourPresenter mTopFourPresenter;

    private AccountHeader.OnAccountHeaderProfileImageListener mOnProfileImageListener = new AccountHeader.OnAccountHeaderProfileImageListener() {
        @Override
        public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
            mDrawer.closeDrawer();
            mTopFourPresenter.onProfileImageClick();
            return true;
        }

        @Override
        public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
            return false;
        }
    };

    Drawer.OnDrawerItemClickListener mOnDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            if (drawerItem.getIdentifier() == DRAWER_ITEM_VENUS_ID) {
                mDrawer.closeDrawer();
                mTopFourPresenter.onVenuesItemClick();
            }
            return true;
        }
    };

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, TopFourActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_four);
        ButterKnife.bind(TopFourActivity.this);

        setSupportActionBar(mTopFourToolbar);

        mTopFourPresenter.onLoadProfileData();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, VenuePagerFragment.newInstance())
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("", true);
    }

    @Override
    public void initMaterialDrawer(String userName, String userPhotoUrl) {
        AccountHeader drawerHeader = new AccountHeaderBuilder()
                .withActivity(TopFourActivity.this)
                .withHeaderBackground(R.drawable.drawer_header_background)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(
                        new ProfileDrawerItem().withName(userName).withIcon(userPhotoUrl)
                )
                .withOnAccountHeaderProfileImageListener(mOnProfileImageListener)
                .build();

        PrimaryDrawerItem itemPlaces = new PrimaryDrawerItem()
                .withName(R.string.menu_places)
                .withIdentifier(DRAWER_ITEM_VENUS_ID)
                .withSelectedColorRes(R.color.colorAccent);

        mDrawer = new DrawerBuilder()
                .withActivity(TopFourActivity.this)
                .withToolbar(mTopFourToolbar)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(drawerHeader)
                .addDrawerItems(itemPlaces)
                .withOnDrawerItemClickListener(mOnDrawerItemClickListener)
                .build();
    }

    @Override
    public void launchProfileFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, ProfileFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void launchVenuePagerFragment() {
        clearBackStack();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, VenuePagerFragment.newInstance())
                .commit();
    }

    @Override
    public void onShowVenueItem(VenueItem venueItem, ImageView imageView) {
        String transitionName = imageView.getTransitionName();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, VenueItemFragment.newInstance(venueItem, transitionName))
                .addToBackStack(null)
                .addSharedElement(imageView, transitionName)
                .commit();
    }

    private void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }
}
