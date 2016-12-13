package com.oleg.hubal.topfour.ui.fragment.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oleg.hubal.topfour.R;
import com.oleg.hubal.topfour.model.database.UserDB;
import com.oleg.hubal.topfour.presentation.presenter.profile.ProfilePresenter;
import com.oleg.hubal.topfour.presentation.view.profile.ProfileView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends MvpAppCompatFragment implements ProfileView {
    public static final String TAG = "ProfileFragment";

    @BindView(R.id.iv_profile_image)
    ImageView mProfileImageView;
    @BindView(R.id.tv_profile_name)
    TextView mProfileNameTextView;
    @BindView(R.id.tv_profile_city)
    TextView mProfileCityTextView;
    @BindView(R.id.tv_profile_gender)
    TextView mProfileGenderTextView;
    @BindView(R.id.tv_profile_relationship)
    TextView mProfileRelationshipTextView;


    @InjectPresenter
    ProfilePresenter mProfilePresenter;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(ProfileFragment.this, view);
        mProfilePresenter.onLoadProfileData();
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showUserData(UserDB user) {
        ImageLoader.getInstance().displayImage(user.getPhoto(), mProfileImageView);

        mProfileNameTextView.setText(user.getFirstName() + " " + user.getLastName());
        mProfileGenderTextView.setText(user.getGender());
        mProfileCityTextView.setText(user.getHomeCity());
        mProfileRelationshipTextView.setText(user.getRelationship());
    }
}
