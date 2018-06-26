package com.alc.diarymohamed.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.alc.diarymohamed.R;

/**
 * Created by mbak on 13/02/18.
 */

public class FavoritesContactsFragment extends Fragment {

    //private static final Logger SLF_LOGGER = LoggerFactory.getLogger(FavoritesContactsFragment.class);

    private static View mView;
    private LayoutInflater myLayoutInflater;
    private Context mContext;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myLayoutInflater = inflater;
        mContext = getActivity().getApplicationContext();

        mView = inflater.inflate(R.layout.fragment_favorites_contacts, container, false);

        mAdView = mView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return mView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //SLF_LOGGER.info("onresume");

    }
}
