package com.oleg.hubal.topfour.ui.fragment.venue_item;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oleg.hubal.topfour.R;
import com.oleg.hubal.topfour.model.VenueItem;
import com.oleg.hubal.topfour.presentation.presenter.venue_item.VenueItemPresenter;
import com.oleg.hubal.topfour.presentation.view.venue_item.VenueItemView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VenueItemFragment extends MvpAppCompatFragment implements VenueItemView {
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CITY = "city";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_PHOTO = "photo";

    public static final String TAG = "VenueItemFragment";

    @BindView(R.id.iv_venue_item_image)
    ImageView mVenueImageView;

    @InjectPresenter
    VenueItemPresenter mVenueItemPresenter;

    public static VenueItemFragment newInstance(VenueItem venueItem) {
        VenueItemFragment fragment = new VenueItemFragment();

        Bundle args = new Bundle();
        args.putString(KEY_ID, venueItem.getId());
        args.putString(KEY_CITY, venueItem.getCity());
        args.putString(KEY_COUNTRY, venueItem.getCountry());
        args.putString(KEY_NAME, venueItem.getName());
        args.putString(KEY_ADDRESS, venueItem.getAddress());
        args.putString(KEY_PHOTO, venueItem.getPhotoUrl());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_item, container, false);
        ButterKnife.bind(VenueItemFragment.this, view);
        ImageLoader.getInstance().displayImage(getArguments().getString(KEY_PHOTO), mVenueImageView);
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
