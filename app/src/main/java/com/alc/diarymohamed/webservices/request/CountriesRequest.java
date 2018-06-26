package com.alc.diarymohamed.webservices.request;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.alc.diarymohamed.data.helper.CountryHelper;
import com.alc.diarymohamed.data.model.CountryModel;
import com.alc.diarymohamed.parser.ContainerData;
import com.alc.diarymohamed.webservices.notifier.CountriesNotifier;

import java.util.ArrayList;

/**
 * Created by Mohamed on 14/02/2018.
 */
public class CountriesRequest {

    //private static final Logger SLF_LOGGER = LoggerFactory.getLogger(CountriesRequest.class);
    private static final String TAG = "CountriesRequest";

    private final CountriesNotifier mCountriesNotifier;
    //private CountryListResponse mCountryListResponse;
    private Context mContext;

    public CountriesRequest(CountriesNotifier countriesNotifier, Context context) {
        mCountriesNotifier = countriesNotifier;
        mContext = context;
    }

    public void getCountries(String response) {
        new CountriesRequestTask().execute(response);
    }

    private class CountriesRequestTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... arg) {
            try {
                /*XStream xstream = new XStream() {
                    @Override
                    //To ignore unknown elements
                    protected MapperWrapper wrapMapper(MapperWrapper next) {
                        return new MapperWrapper(next) {
                            @Override
                            public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                                if (definedIn == Object.class) {
                                    return false;
                                }
                                return super.shouldSerializeMember(definedIn, fieldName);
                            }
                        };
                    }
                };
                xstream.alias("ListCountry", CountryListResponse.class);
                xstream.alias("Countries", CountriesResponse.class);
                xstream.alias("Country", CountryResponse.class);
                xstream.addImplicitCollection(CountriesResponse.class, "Country", CountryResponse.class);
                mCountryListResponse = (CountryListResponse) xstream.fromXML(arg[0]);
                saveCountriesRealmDataBase(mCountryListResponse);
                */

                ContainerData  contDATA = new ContainerData(mContext);

                ArrayList<CountryModel> prefix_parser = contDATA.getCountries();

                if (prefix_parser==null){
                    Log.v("ParserXMLActivity","parser vide");
                }
                else Log.v("ParserXMLActivity","parser size " + prefix_parser.size());

                for(int i=0; i<prefix_parser.size();i++){

                    if ( prefix_parser.get(i)==null){
                        Log.v("prefix " + i ,"vide");
                    }
                }

                if (prefix_parser.size()!=0) {
                    saveCountriesRealmDataBase(prefix_parser);


                }
            } catch (Exception e) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "Exception caught");
                FirebaseCrash.report(e);
                mCountriesNotifier.countriesXStreamParseFailed();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) mCountriesNotifier.countriesXStreamParseSuccess();
        }
    }

    private void saveCountriesRealmDataBase(ArrayList<CountryModel> countryModels) {

        CountryHelper countryHelper = new CountryHelper(mContext);

        //remove all countries from DB before save new list countries
        countryHelper.deleteAllCountries();

        if (null != countryModels) {



                for (CountryModel countryModel : countryModels) {
                    countryHelper.addCountry(countryModel.getCode(),
                            countryModel.getName(),
                            countryModel.getPrefix(),
                            countryModel.getCurrency(),
                            countryModel.getCapital(),
                            countryModel.getTimeZone(),
                            countryModel.getDst(),
                            countryModel.getDstBegin(),
                            countryModel.getDstEnd());
                }
        }
        countryHelper.closeRealm();
    }

}
