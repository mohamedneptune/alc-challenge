package com.alc.diarymohamed.ui.adapters;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alc.diarymohamed.R;
import com.alc.diarymohamed.data.model.CountryModel;
import com.alc.diarymohamed.shared.Constants;
import com.alc.diarymohamed.ui.activity.CountryDetailsActivity;

import java.util.List;


public class CountryRecyclerViewAdapter extends RecyclerView.Adapter<CountryRecyclerViewAdapter.Holder>
        implements View.OnClickListener, View.OnLongClickListener {

    //private static final Logger SLF_LOGGER = LoggerFactory.getLogger(CountryRecyclerViewAdapter.class);
    private static final String TAG = "CountryRecyclerViewAdapter";

    private final Context mContext;
    private final List<CountryModel> mCountries;
    private final List<String> mCountriesName;
    private final int mItemList;
    private int mPositionCountryFlag, mPositionSelectedCountryFlag;
    private Bundle mBundle;

    public CountryRecyclerViewAdapter(Context context, List<CountryModel> countries,
                                      List<String> countriesName, int item_list) {
        mContext = context;
        mCountries = countries;
        mCountriesName = countriesName;
        mItemList = item_list;
    }

    public static class Holder extends RecyclerView.ViewHolder {

        final LinearLayout mRootLayout;
        final TextView mTitleTextView;
        final TextView mCapitalTextView;
        final TextView mPrefixTextView;
        final ImageView mCountryFlagImageView;


        public Holder(View itemView) {
            super(itemView);

            mRootLayout = (LinearLayout) itemView.findViewById(R.id.root_list_layout);

            mTitleTextView = (TextView) itemView.findViewById(R.id.country_title);
            mCapitalTextView = (TextView) itemView.findViewById(R.id.country_capital);
            mPrefixTextView = (TextView) itemView.findViewById(R.id.country_prefix);
            mCountryFlagImageView = (ImageView)itemView.findViewById(R.id.country_flag);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mItemList, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final CountryModel countryModel = mCountries.get(position);

        try {
        holder.mTitleTextView.setText(countryModel.getName());
        holder.mCapitalTextView.setText(countryModel.getCapital());
        holder.mPrefixTextView.setText("+"+countryModel.getPrefix());
        mPositionCountryFlag = mCountriesName.indexOf(countryModel.getName());
        holder.mCountryFlagImageView.setImageResource(Constants.DEFAULT_RESOURCE_FLAGS_LIST[mPositionCountryFlag]);
        }catch(Exception e){
          e.printStackTrace();
        }

        holder.mRootLayout.setOnClickListener(this);
        holder.mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mPositionSelectedCountryFlag = mCountriesName.indexOf(
                            mCountries.get(position).getName());
                    saveLastIdTaskToSharedPreference(mCountries.get(position).getName(),
                            mCountries.get(position).getPrefix(), mPositionSelectedCountryFlag);
                    Intent intent = new Intent(mContext, CountryDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCountries.size();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public void saveLastIdTaskToSharedPreference(String country_name, String country_prefix, int position_flag) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("selected_country_name", country_name);
        editor.putString("selected_country_prefix", country_prefix);
        editor.putInt("selected_position_country_flag", position_flag);
        editor.apply();
    }

}