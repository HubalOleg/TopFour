package com.oleg.hubal.topfour.ui.fragment.venue_pager;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private static int RECYCLER_FLING_VELOCITY = 12000;
    public static final String TAG = "VenuePagerFragment";

    private VenueAdapter mVenueAdapter;
    private LinearLayoutManager mLayoutManager;

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
        public void onVenueClick(String venueId) {
//            todo
        }
    };

    private RecyclerView.OnFlingListener mOnFlingListener = new RecyclerView.OnFlingListener() {
        @Override
        public boolean onFling(int velocityX, int velocityY) {
            if (velocityY > RECYCLER_FLING_VELOCITY) {
                mVenuePagerPresenter.onPowerFling(mLayoutManager.findLastCompletelyVisibleItemPosition());
            }
            return false;
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
        mVenuePagerPresenter.onLoadData();

        return view;
    }

    private void initRecyclerView() {
        mVenueRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mVenueRecyclerView.setLayoutManager(mLayoutManager);

        mVenueAdapter = new VenueAdapter(getContext(), mOnVenueClickListener);
        mVenueRecyclerView.setAdapter(mVenueAdapter);
        mVenueRecyclerView.setOnFlingListener(mOnFlingListener);

    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void notifyVenueInserted(int position) {
        mVenueAdapter.notifyItemInserted(position);
    }

    @Override
    public void addVenueList(List<VenueItem> venueItems) {
        mVenueAdapter.addVenueList(venueItems);
    }
}
