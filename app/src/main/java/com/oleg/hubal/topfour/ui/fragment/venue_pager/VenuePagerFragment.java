package com.oleg.hubal.topfour.ui.fragment.venue_pager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
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

    private static final int RECYCLER_FLING_VELOCITY = 12000;

    private VenueAdapter mVenueAdapter;
    private GridLayoutManager mLayoutManager;

    private OnShowVenueItemListener mOnShowVenueItemListener;

    private RecyclerView.OnFlingListener mOnFlingListener = new RecyclerView.OnFlingListener() {
        @Override
        public boolean onFling(int velocityX, int velocityY) {
            if (velocityY > RECYCLER_FLING_VELOCITY) {
                mVenuePagerPresenter.onPowerFling(mLayoutManager.findLastCompletelyVisibleItemPosition());
            }
            return false;
        }
    };

    private VenueAdapter.OnVenueClickListener mOnVenueClickListener = new VenueAdapter.OnVenueClickListener() {
        @Override
        public void onVenueClick(VenueItem venueItem, ImageView imageView) {
            Transition changeTransform = TransitionInflater.from(getContext()).
                    inflateTransition(R.transition.change_image_transform);
            Transition explodeTransform = TransitionInflater.from(getContext()).
                    inflateTransition(android.R.transition.fade);

            setSharedElementReturnTransition(changeTransform);
            setExitTransition(explodeTransform);

            mOnShowVenueItemListener.onShowVenueItem(venueItem, imageView);
        }
    };

    @BindView(R.id.rv_venue_recycler)
    RecyclerView mVenueRecyclerView;
    @BindView(R.id.pb_download_progress)
    ProgressBar mDownloadProgressBar;

    @InjectPresenter(type = PresenterType.GLOBAL)
    VenuePagerPresenter mVenuePagerPresenter;

    public static VenuePagerFragment newInstance() {
        VenuePagerFragment fragment = new VenuePagerFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnShowVenueItemListener = (OnShowVenueItemListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        mVenueAdapter = new VenueAdapter(getContext(), mOnVenueClickListener);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_pager, container, false);
        ButterKnife.bind(VenuePagerFragment.this, view);

        mVenueRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getContext(), 1);

        mVenueRecyclerView.setLayoutManager(mLayoutManager);

        mVenueRecyclerView.setAdapter(mVenueAdapter);
        mVenueRecyclerView.setOnFlingListener(mOnFlingListener);

        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_pager_menu ,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mVenuePagerPresenter.onChangeGridItemSelected();
        return true;
    }

    @Override
    public void changeGrid(int spanCount) {
        mLayoutManager.setSpanCount(spanCount);
    }

    @Override
    public void notifyVenueInserted(int position) {
        mVenueAdapter.notifyItemInserted(position);
    }

    @Override
    public void addVenueList(List<VenueItem> venueItems) {
        mVenueAdapter.addVenueList(venueItems);
    }

    @Override
    public void showProgressDialog() {
       mDownloadProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressDialog() {
        mDownloadProgressBar.setVisibility(View.GONE);
    }

    public interface OnShowVenueItemListener {
        void onShowVenueItem(VenueItem venueItem, ImageView imageView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
