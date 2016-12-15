package com.oleg.hubal.topfour.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oleg.hubal.topfour.R;
import com.oleg.hubal.topfour.model.VenueItem;
import com.oleg.hubal.topfour.model.api.ModelImpl;
import com.oleg.hubal.topfour.model.api.data.Photo;
import com.oleg.hubal.topfour.utils.PreferenceManager;
import com.oleg.hubal.topfour.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 13.12.2016.
 */

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.VenueViewHolder>{

    private final OnVenueClickListener mVenueClickListener;
    private final Context mContext;
    private List<VenueItem> mVenueItems;
    private ModelImpl mModel;
    private Gson mGson;
    private String mToken;

    public VenueAdapter(Context context, OnVenueClickListener venueClickListener) {
        mContext = context;
        mVenueClickListener = venueClickListener;
        mVenueItems = new ArrayList<>();
        mModel = new ModelImpl();
        mGson = new Gson();
        mToken = PreferenceManager.getToken(context);
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

    class VenueViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_venue_image)
        ImageView mVenueImageView;
        @BindView(R.id.tv_venue_name)
        TextView mVenueNameTextView;
        @BindView(R.id.tv_venue_city)
        TextView mVenueCityTextView;
        @BindView(R.id.tv_venue_country)
        TextView mVenueCountryTextView;
        @BindView(R.id.tv_venue_address)
        TextView mVenueAddressTextView;

        private VenueItem mVenueItem;

        View.OnClickListener mViewHolderClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVenueClickListener.onVenueClick(mVenueItem, mVenueImageView);
            }
        };

        private Callback<ResponseBody> mVenuePhotoCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        handleResponse(response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        };

        public VenueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(VenueViewHolder.this, itemView);
            itemView.setOnClickListener(mViewHolderClickListener);
        }

        public void onBind(int position, VenueItem venueItem) {
            mVenueItem = venueItem;
            mVenueImageView.setImageResource(R.drawable.item_no_image_background);
            mVenueNameTextView.setText(venueItem.getName());
            mVenueAddressTextView.setText(venueItem.getAddress());
            mVenueCityTextView.setText(venueItem.getCity());
            mVenueCountryTextView.setText(venueItem.getCountry());
            mVenueImageView.setTransitionName("trans_image" + position);
            loadVenuePhoto();
        }

        private void loadVenuePhoto() {
            String photoUrl = mVenueItem.getPhotoUrl();
            if (TextUtils.isEmpty(photoUrl)) {
                Call<ResponseBody> venuePhotoResponse = mModel.getVenuePhoto(mVenueItem.getId(), 1, mToken);
                venuePhotoResponse.enqueue(mVenuePhotoCallback);
            } else {
                ImageLoader.getInstance().displayImage(photoUrl, mVenueImageView);
            }
        }

        private void handleResponse(Response<ResponseBody> response) throws IOException, JSONException {
            JSONObject responseJson = Utility.getJSONObjectFromResponse(response);
            JSONArray photoJSONArray = responseJson.getJSONObject("photos").getJSONArray("items");
            JSONObject photoJSONObject = photoJSONArray.getJSONObject(0);
            Photo photo = mGson.fromJson(photoJSONObject.toString(), Photo.class);
            String photoUrl = photo.getPhotoUrl();
            mVenueItem.setPhotoUrl(photoUrl);
            loadVenuePhoto();
            if (mVenueItem.isCached()) {
                mVenueItem.saveToDatabase();
            }
        }
    }

    public interface OnVenueClickListener {
        void onVenueClick(VenueItem venueItem, ImageView imageView);
    }
}
