package com.oleg.hubal.topfour.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oleg.hubal.topfour.R;
import com.oleg.hubal.topfour.model.VenueItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 13.12.2016.
 */

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.VenueViewHolder>{

    private final OnVenueClickListener mVenueClickListener;
    private List<VenueItem> mVenueItems;

    public VenueAdapter(OnVenueClickListener venueClickListener) {
        mVenueClickListener = venueClickListener;
        mVenueItems = new ArrayList<>();
    }

    @Override
    public VenueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_venue, parent, false);

        return new VenueViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VenueViewHolder holder, int position) {
        holder.onBind(position, mVenueItems.get(position));
    }

    @Override
    public int getItemCount() {
        return (mVenueItems.size());
    }

    public void addVenueList(List<VenueItem> venueItems) {
        mVenueItems = venueItems;
        notifyDataSetChanged();
    }

    public void addVenue(VenueItem venueItem) {
        mVenueItems.add(venueItem);
        notifyItemInserted(mVenueItems.size());
    }

    class VenueViewHolder extends RecyclerView.ViewHolder{

        private int mPosition;

        View.OnClickListener mViewHolderClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVenueClickListener.onVenueClick(mPosition);
            }
        };

        public VenueViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(mViewHolderClickListener);
        }

        public void onBind(int position, VenueItem venueItem) {
            mPosition = position;
        }
    }

    public interface OnVenueClickListener {
        void onVenueClick(int position);
    }
}
