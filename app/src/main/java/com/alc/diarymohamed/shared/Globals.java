package com.alc.diarymohamed.shared;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.crash.FirebaseCrash;
import com.alc.diarymohamed.data.model.TimeModel;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Globals {

    //private static final Logger SLF_LOGGER = LoggerFactory.getLogger(Globals.class);
    private static final String TAG = "Globals";

    private static Context applicationContext;


    private Globals() {

    }

    private static Context getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(Context applicationContext) {
        Globals.applicationContext = applicationContext;
    }

    private static Realm mRealm;

    public static Realm realmMigration() {
        try {
            mRealm = null;
            RealmConfiguration config2 = new RealmConfiguration.Builder(Globals.getApplicationContext())
                    .name("default2")
                    .schemaVersion(3)
                    .deleteRealmIfMigrationNeeded()
                    .build();

            mRealm = Realm.getInstance(config2);

            // instantiate a new realmObject
            //realm = Realm.getInstance(Globals.getApplicationContext());
            mRealm.beginTransaction();
            mRealm.commitTransaction();
            //mRealm.close();
        } catch (Exception e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "Exception caught");
            FirebaseCrash.report(e);
        }

        return mRealm;

    }

    public static long getContactId(Cursor cursor, int position){
            long contactId = 0;
            try {
                while (cursor.moveToNext()) {
                    if (position == cursor.getPosition()) {
                         contactId = cursor.getLong(cursor.getPosition());
                    }
                }
            } finally {
                cursor.close();
            }
        return contactId;
        }

        public static String getContactNumber(Uri uriContact, Context context) {

        String contactNumber = null;
        String contactID = null;

        // getting contacts ID
        Cursor cursorID = context.getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d("", "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Log.d("", "Contact Phone Number: " + contactNumber);
        if(null != contactNumber) return contactNumber;
        else return "";
    }


    public static TimeModel setTimeFormat(int hour, int minute) {
        TimeModel timeModel = new TimeModel();
        int hourModif = hour;
        int minModif = minute;

        if(minute >59){
            minModif = minute % 60;
            hourModif = hour + (minute / 60);
        }

        if(hourModif > 23){
            hourModif = hourModif - 24;
        }

        if(hourModif < 0){
            hourModif = hourModif + 24;
        }

        if (hourModif < 10) {
            timeModel.setHour("0" + hourModif);
        } else {
            timeModel.setHour("" + hourModif);
        }

        if (minModif < 10) {
            timeModel.setMinute("0" + minModif);
        } else {
            timeModel.setMinute("" + minModif);
        }



        return timeModel;
    }

    public static void hideKeyboard(@NonNull Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
