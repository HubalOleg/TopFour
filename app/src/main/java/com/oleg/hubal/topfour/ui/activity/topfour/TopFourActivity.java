package com.oleg.hubal.topfour.ui.activity.topfour;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.oleg.hubal.topfour.R;
import com.oleg.hubal.topfour.adapter.VenueAdapter;
import com.oleg.hubal.topfour.model.VenueItem;
import com.oleg.hubal.topfour.presentation.presenter.topfour.TopFourPresenter;
import com.oleg.hubal.topfour.presentation.view.topfour.TopFourView;
import com.oleg.hubal.topfour.ui.fragment.profile.ProfileFragment;
import com.oleg.hubal.topfour.ui.fragment.venue_item.VenueItemFragment;
import com.oleg.hubal.topfour.ui.fragment.venue_pager.VenuePagerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopFourActivity extends MvpAppCompatActivity implements TopFourView, VenueAdapter.OnVenueClickListener {
    private static final long DRAWER_ITEM_PLACES_ID = 32;

    public static final String TAG = "TopFourActivity";

    private Drawer mDrawer;

    @BindView(R.id.toolbar_top_four)
    Toolbar mTopFourToolbar;

    @InjectPresenter
    TopFourPresenter mTopFourPresenter;
    private Transition mChangeTransform;
    private Transition mExplodeTransform;

    @ProvidePresenter
    TopFourPresenter provideTopFourPresenter() {
        return new TopFourPresenter(TopFourActivity.this);
    }

    private AccountHeader.OnAccountHeaderProfileImageListener mOnProfileImageListener = new AccountHeader.OnAccountHeaderProfileImageListener() {
        @Override
        public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
            launchProfileFragment();
            mDrawer.closeDrawer();
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
            if (drawerItem.getIdentifier() == DRAWER_ITEM_PLACES_ID) {
                launchVenuePagerFragment();
            }
            mDrawer.closeDrawer();
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

        mChangeTransform = TransitionInflater.from(TopFourActivity.this).
                inflateTransition(R.transition.change_image_transform);
        mExplodeTransform = TransitionInflater.from(TopFourActivity.this).
                inflateTransition(android.R.transition.explode);
//
        setSupportActionBar(mTopFourToolbar);

        mTopFourPresenter.onLoadProfileData();
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
                .withName("Places")
                .withIdentifier(DRAWER_ITEM_PLACES_ID)
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

        mDrawer.setSelection(DRAWER_ITEM_PLACES_ID);
    }

    private void launchProfileFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, ProfileFragment.newInstance())
                .commit();
    }

    private void launchVenuePagerFragment() {
//        VenuePagerFragment venuePagerFragment = VenuePagerFragment.newInstance();
//        venuePagerFragment.setSharedElementReturnTransition(mChangeTransform);
//        venuePagerFragment.setExitTransition(mExplodeTransform);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, VenuePagerFragment.newInstance())
                .commit();
    }

    @Override
    public void onVenueClick(VenueItem venueItem, ImageView imageView) {
        VenueItemFragment venueItemFragment = VenueItemFragment.newInstance(venueItem, imageView);
        venueItemFragment.setSharedElementEnterTransition(mChangeTransform);
        venueItemFragment.setEnterTransition(mExplodeTransform);

        String imageTransitionName = imageView.getTransitionName();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, venueItemFragment)
                .addToBackStack("transaction")
                .addSharedElement(imageView, imageTransitionName)
                .commit();

//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.frame_container, venueItemFragment)
//                .addToBackStack("transaction")
//                .commit();
    }
}
