package com.oleg.hubal.topfour.ui.fragment.venue_pager;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

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

    private static final int RECYCLER_FLING_VELOCITY = 12000;

    private VenueAdapter mVenueAdapter;
    private LinearLayoutManager mLayoutManager;

    private OnShowVenueItemListener mOnShowVenueItemListener;

    private Transition mChangeTransform;
    private Transition mExplodeTransform;

    private VenueItem mVenueItem;
    private ImageView mImageView;

    private VenueAdapter.OnVenueClickListener mOnVenueClickListener = new VenueAdapter.OnVenueClickListener() {
        @Override
        public void onVenueClick(VenueItem venueItem, ImageView imageView) {
            mVenueItem = venueItem;
            mImageView = imageView;
            setSharedElementReturnTransition(mChangeTransform);
            setExitTransition(mExplodeTransform);
            mOnShowVenueItemListener.onShowVenueItem(venueItem, imageView);
        }
    };

    @BindView(R.id.rv_venue_recycler)
    RecyclerView mVenueRecyclerView;
    @BindView(R.id.pb_download_progress)
    ProgressBar mDownloadProgressBar;

    @InjectPresenter
    VenuePagerPresenter mVenuePagerPresenter;

    @ProvidePresenter
    VenuePagerPresenter provideVenuePagerPresenter() {
        return new VenuePagerPresenter(getContext());
    }

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
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnShowVenueItemListener = (OnShowVenueItemListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mChangeTransform = TransitionInflater.from(getContext()).
                inflateTransition(R.transition.change_image_transform);
        mExplodeTransform = TransitionInflater.from(getContext()).
                inflateTransition(android.R.transition.fade);

        mVenueAdapter = new VenueAdapter(getContext(), mOnVenueClickListener);
        mVenuePagerPresenter.onLoadData();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_pager, container, false);
        ButterKnife.bind(VenuePagerFragment.this, view);

        mVenueRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mVenueRecyclerView.setLayoutManager(mLayoutManager);

        mVenueRecyclerView.setAdapter(mVenueAdapter);
        mVenueRecyclerView.setOnFlingListener(mOnFlingListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setExitTransition(TransitionInflater.from(
                getActivity()).inflateTransition(android.R.transition.no_transition));
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

    @Override
    public void showVenueItemFragment() {
        setSharedElementReturnTransition(mChangeTransform);
        setExitTransition(mExplodeTransform);
        mOnShowVenueItemListener.onShowVenueItem(mVenueItem, mImageView);
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
}
