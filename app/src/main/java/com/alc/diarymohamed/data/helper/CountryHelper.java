package com.alc.diarymohamed.data.helper;

import android.content.Context;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.alc.diarymohamed.data.model.CountryModel;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Mohamed on 17/02/2018.
 */
public class CountryHelper {

    //private static final Logger SLF_LOGGER = LoggerFactory.getLogger(CountryHelper.class);
    private static final String TAG = "CountryHelper";
    private Realm mRealm;

    public CountryHelper(Context context) {
    }

    /**
     * Save new country in local database
     */
    public void addCountry(String code, String name, String prefix, String currency,
                           String capital, String timeZone, String dst, String dstBegin,
                           String dstEnd) {

        CountryModel countryModel = new CountryModel();
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            countryModel = mRealm.createObject(CountryModel.class);
            countryModel.setCode(code);
            countryModel.setName(name);
            countryModel.setPrefix(prefix);
            countryModel.setCurrency(currency);
            countryModel.setCapital(capital);
            countryModel.setTimeZone(timeZone);
            countryModel.setDst(dst);
            countryModel.setDstBegin(dstBegin);
            countryModel.setDstEnd(dstEnd);
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.copyToRealm(countryModel);
            mRealm.commitTransaction();
            mRealm.close();
        }
    }

    public CountryModel findCountryByName(String name) {
        CountryModel countryModel = null;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            countryModel = mRealm.where(CountryModel.class)
                    .equalTo("Name", name)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (countryModel == null) {
                FirebaseCrash.logcat(Log.WARN, TAG, "countryModels == null");
            }
            mRealm.close();
        }
        return countryModel;
    }

    public RealmResults<CountryModel> findCountrySorted(String[] fieldNames) {
        RealmResults<CountryModel> countryModels = null;
        try {
            mRealm.beginTransaction();
            Sort sort[] = {Sort.ASCENDING};
            countryModels = mRealm.where(CountryModel.class)
                    .findAllSorted(fieldNames, sort);
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (countryModels == null) {
                FirebaseCrash.logcat(Log.WARN, TAG, "countryModels == null");
            }
        }
        return countryModels;
    }

    public RealmResults<CountryModel> findCountrySorted(String[] fieldNames, String name_capital_prefix) {
        RealmResults<CountryModel> countryModels = null;
        try {
            mRealm.beginTransaction();
            Sort sort[] = {Sort.ASCENDING};
            countryModels = mRealm.where(CountryModel.class)
                    .contains("Name", name_capital_prefix)
                    .or()
                    .contains("Prefix", name_capital_prefix)
                    .or()
                    .contains("Capital", name_capital_prefix)
                    .findAllSorted(fieldNames, sort);
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (countryModels == null) {
                FirebaseCrash.logcat(Log.WARN, TAG, "countryModels == null");
            }
        }
        return countryModels;
    }

    public RealmResults<CountryModel> getAllCountries() {
        mRealm = Realm.getDefaultInstance();
        RealmQuery<CountryModel> query = mRealm.where(CountryModel.class);
        // Execute the query:
        RealmResults<CountryModel> countries = query.findAll();
        return countries;
    }

    public void deleteAllCountries() {
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.clear(CountryModel.class);
            }
        });
    }

    public void closeRealm() {
        try {
            mRealm.close();
        } catch (Exception e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "Exception caught");
            FirebaseCrash.report(e);
        }
    }

}