package com.alc.diarymohamed.data.helper;

import android.content.Context;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.alc.diarymohamed.data.model.ContactModel;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Mohamed on 24/02/2018.
 */
public class ContactsHelper {

    //private static final Logger SLF_LOGGER = LoggerFactory.getLogger(ContactsHelper.class);
    private static final String TAG = "ContactsHelper";

    private Realm mRealm;

    public ContactsHelper(Context context) {
    }

    /**
     * Save new contact in local database
     */
    public void addContact(long id, String name, String number, long photoId) {

        ContactModel contactModel = new ContactModel();
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            contactModel = mRealm.createObject(ContactModel.class);
            contactModel.setId(id);
            contactModel.setName(name);
            contactModel.setNumber(number);
            contactModel.setPhotoId(photoId);
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.copyToRealm(contactModel);
            mRealm.commitTransaction();
            mRealm.close();
        }
    }

    public ContactModel findContactByName(String name) {
        ContactModel contactModel = null;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            contactModel = mRealm.where(ContactModel.class)
                    .equalTo("Name", name)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (contactModel == null) {
                FirebaseCrash.logcat(Log.WARN, TAG, "contactModel == null");
            }
            mRealm.close();
        }
        return contactModel;
    }

    public RealmResults<ContactModel> findContactSorted(String[] fieldNames) {
        RealmResults<ContactModel> contactModels = null;
        try {
            mRealm.beginTransaction();
            Sort sort[] = {Sort.ASCENDING};
            contactModels = mRealm.where(ContactModel.class)
                    .findAllSorted(fieldNames, sort);
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (contactModels == null) {
                FirebaseCrash.logcat(Log.WARN, TAG, "contactModel == null");
            }
        }
        return contactModels;
    }

    public RealmResults<ContactModel> findContactSorted(String[] fieldNames, String name_number) {
        RealmResults<ContactModel> contactModels = null;
        try {
            mRealm.beginTransaction();
            Sort sort[] = {Sort.ASCENDING};
            contactModels = mRealm.where(ContactModel.class)
                    .contains("Name", name_number)
                    .or()
                    .contains("Number", name_number)
                    .findAllSorted(fieldNames, sort);
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (contactModels == null) {
                FirebaseCrash.logcat(Log.WARN, TAG, "contactModel == null");
            }
        }
        return contactModels;
    }

    public RealmResults<ContactModel> getAllContacts() {
        mRealm = Realm.getDefaultInstance();
        RealmQuery<ContactModel> query = mRealm.where(ContactModel.class);
        // Execute the query:
        RealmResults<ContactModel> contactModels = query.findAll();
        return contactModels;
    }

    public void deleteAllContacts() {
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mRealm.clear(ContactModel.class);
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