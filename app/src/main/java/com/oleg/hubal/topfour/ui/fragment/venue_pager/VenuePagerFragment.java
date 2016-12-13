package com.oleg.hubal.topfour.ui.fragment.venue_pager;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.oleg.hubal.topfour.R;
import com.oleg.hubal.topfour.adapter.VenueAdapter;
import com.oleg.hubal.topfour.model.VenueItem;
import com.oleg.hubal.topfour.presentation.presenter.venue_pager.VenuePagerPresenter;
import com.oleg.hubal.topfour.presentation.view.venue_pager.VenuePagerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VenuePagerFragment extends MvpAppCompatFragment implements VenuePagerView {
    public static final String TAG = "VenuePagerFragment";

    private VenueAdapter mVenueAdapter;

    @BindView(R.id.rv_venue_recycler)
    RecyclerView mVenueRecyclerView;

    @InjectPresenter
    VenuePagerPresenter mVenuePagerPresenter;

    @ProvidePresenter
    VenuePagerPresenter provideVenuePagerPresenter() {
        return new VenuePagerPresenter(getContext());
    }

    private VenueAdapter.OnVenueClickListener mOnVenueClickListener = new VenueAdapter.OnVenueClickListener() {
        @Override
        public void onVenueClick(int position) {
//            todo
        }
    };

    public static VenuePagerFragment newInstance() {
        VenuePagerFragment fragment = new VenuePagerFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_pager, container, false);
        ButterKnife.bind(VenuePagerFragment.this, view);

        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        mVenueRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mVenueRecyclerView.setLayoutManager(layoutManager);

        mVenueAdapter = new VenueAdapter(mOnVenueClickListener);
        mVenueRecyclerView.setAdapter(mVenueAdapter);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVenuePagerPresenter.onLoadData();
    }

    @Override
    public void addVenue(VenueItem venueItem) {
        Log.d(TAG, "addVenue: ");
        mVenueAdapter.addVenue(venueItem);
    }

    @Override
    public void addVenueList(List<VenueItem> venueItems) {
        Log.d(TAG, "addVenueList: ");
        mVenueAdapter.addVenueList(venueItems);
    }
}
