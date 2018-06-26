package com.alc.diarymohamed.ui.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.alc.diarymohamed.R;
import com.alc.diarymohamed.data.helper.CountryHelper;
import com.alc.diarymohamed.data.model.CountryModel;
import com.alc.diarymohamed.ui.activity.CountryAddEditActivity;
import com.alc.diarymohamed.ui.adapters.CountryRecyclerViewAdapter;
import com.alc.diarymohamed.webservices.notifier.CountriesNotifier;
import com.alc.diarymohamed.webservices.request.CountriesRequest;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.SEARCH_SERVICE;

public class CountryFragment extends Fragment implements CountriesNotifier, View.OnClickListener {

    //private static final Logger SLF_LOGGER = LoggerFactory.getLogger(CountryFragment.class);
    private static final String TAG = "CountryFragment";

    private static View mView;
    private LayoutInflater myLayoutInflater;
    private Context mContext;
    private CountriesRequest mCountriesRequest;
    private RecyclerView mRecyclerView;
    private List<CountryModel> mCountriesFromXml;
    private List<CountryModel> mCountries;
    private List<String> mCountriesName;
    private CountryHelper mCountryHelper;
    private String [] mFieldNames;
    private String [] mSortByName = {"Name"};
    private String [] mSortByPrefix = {"Prefix"};
    private FirebaseAnalytics mFirebaseAnalytics;
    private AdView mAdView;
    private Bundle mBundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myLayoutInflater = inflater;
        mContext = getActivity().getApplicationContext();

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        mView = inflater.inflate(R.layout.fragment_country, container, false);

        mAdView = mView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.countries_recyclerView);

        setUpRecyclerView();

        loadFromPrefixFileXML();

        mFieldNames = mSortByName;
        mCountryHelper = new CountryHelper(mContext);
        mCountries = mCountryHelper.getAllCountries();
        mCountriesName = new ArrayList<>();
        for (CountryModel countryModel : mCountries){
            mCountriesName.add(countryModel.getName());
        }

        showCountries();

        setHasOptionsMenu(true);

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CountryAddEditActivity.class);
                intent.putExtra("edit_country_mode", false);
                startActivity(intent);
            }
        });

        return mView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mBundle = new Bundle();
                mBundle.putString(FirebaseAnalytics.Param.ITEM_ID, "menu_CountryList_Back");
                mBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "menu_CountryList_Back");
                mBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "menu");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, mBundle);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.country_list_menu, menu);

        mBundle = new Bundle();
        mBundle.putString(FirebaseAnalytics.Param.ITEM_ID, "menu_CountryList_Search");
        mBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "menu_CountryList_Search");
        mBundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "menu");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, mBundle);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        //SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                if("".equals(query)){
                    mCountries = mCountryHelper.findCountrySorted(mFieldNames);
                }else{
                    mCountries = mCountryHelper.findCountrySorted(mFieldNames, StringUtils.capitalize(query));
                }

                showCountries();
                return true;
            }

        });
    }




    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    public interface ClickListner {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void loadFromPrefixFileXML() {
        try {
            AssetManager assetManager = mContext.getAssets();
            InputStream inputStream = assetManager.open("Countries.xml");
            try {
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                int bytesRead = inputStream.read(buffer);
                String response = new String(buffer, 0, bytesRead, "utf-16");
                inputStream.close();

                mCountriesRequest = new CountriesRequest(CountryFragment.this, mContext);
                mCountriesRequest.getCountries(response);
            } catch (IOException ioException) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "IOException caught");
                FirebaseCrash.report(ioException);
            }
        } catch (Exception e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "Exception caught");
            FirebaseCrash.report(e);
        }
    }

    @Override
    public void countriesXStreamParseSuccess() {
        //SLF_LOGGER.info("countriesXStreamParseSuccess");
    }

    private void showCountries(){
        RecyclerView.Adapter adapter = new CountryRecyclerViewAdapter(mContext, mCountries, mCountriesName, R.layout.list_item_country);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void countriesXStreamParseFailed() {
        FirebaseCrash.logcat(Log.WARN, TAG, "countriesXStreamParseFailed");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {



        }
    }
}
