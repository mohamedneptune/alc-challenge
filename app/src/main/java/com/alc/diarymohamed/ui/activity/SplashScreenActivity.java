package com.alc.diarymohamed.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.alc.diarymohamed.BuildConfig;
import com.alc.diarymohamed.R;
import com.alc.diarymohamed.data.helper.ContactsHelper;
import com.alc.diarymohamed.data.model.ContactModel;
import com.alc.diarymohamed.shared.Config;
import com.alc.diarymohamed.shared.Globals;
import com.alc.diarymohamed.webservices.notifier.CountriesNotifier;
import com.alc.diarymohamed.webservices.request.CountriesRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class SplashScreenActivity extends AppCompatActivity implements CountriesNotifier,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    private Intent intent;
    private CountriesRequest mCountriesRequest;

    private static final int LOADER_ID = 1;
    private Context mContext;
    private final String[] FROM_COLUMNS = {
            ContactsContract.Data.CONTACT_ID,
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Data.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.Data.PHOTO_ID
    };
    private Bundle mBundle;
    private List<ContactModel> mContactModels;
    private ContactsHelper mContactsHelper;
    private String[] mFieldNames;
    private String[] mSortByName = {"Name"};
    private String[] mSortByNumber = {"Number"};
    private Cursor mContactCursor;
    private ArrayList<String> allID;

    /**
     * The {@code FirebaseAnalytics} used to record screen views.
     */
    // [START declare_analytics]
    private FirebaseAnalytics mFirebaseAnalytics;
    // [END declare_analytics]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mContext = getApplicationContext();
        Globals.setApplicationContext(getApplicationContext());

        //Show application in full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        mContactsHelper = new ContactsHelper(mContext);
        loadAllContacts();

        loadFromPrefixFileXML();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, Config.SPLASH_SCREEN_TIME_OUT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //SLF_LOGGER.info("onResume()");
        FirebaseCrash.logcat(Log.INFO, TAG, "onResume");
    }

    private void loadAllContacts() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this, new String[]{
                                Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CONTACTS
                        }, 31);
            } else {
                getSupportLoaderManager().initLoader(LOADER_ID, mBundle, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 31) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                getSupportLoaderManager().initLoader(LOADER_ID, mBundle, this);
            } else {
                finish();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(
                        mContext,
                        ContactsContract.Data.CONTENT_URI,
                        FROM_COLUMNS,
                        null,
                        null,
                        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                                ContactsContract.Data.DISPLAY_NAME_PRIMARY : ContactsContract.Data
                                .DISPLAY_NAME) +
                                " ASC"
                );
            default:
                if (BuildConfig.DEBUG)
                    throw new IllegalArgumentException("no id handled!");
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mContactCursor = data;
        saveContactsInDataBase(mContactCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void saveContactsInDataBase(Cursor cursor) {
        long contactId;
        String contactName;
        String contactNumber;
        long contactPhotoId;
        if(!cursor.isClosed()) {
            try {
                mContactsHelper.deleteAllContacts();
                while (cursor.moveToNext()) {
                    contactName = cursor.getString(cursor.getColumnIndex(
                            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                                    ContactsContract.Data.DISPLAY_NAME_PRIMARY : ContactsContract.Data
                                    .DISPLAY_NAME
                    ));
                    contactId = cursor.getLong(cursor.getColumnIndex(
                            ContactsContract.Data.CONTACT_ID
                    ));
                    contactPhotoId = cursor.getLong(cursor.getColumnIndex(
                            ContactsContract.Data.PHOTO_ID
                    ));

                    mContactsHelper.addContact(contactId,
                            contactName,
                            "",
                            contactPhotoId);
                }
            } finally {
                cursor.close();
                mContactModels = mContactsHelper.getAllContacts();
                mContactsHelper.closeRealm();
            }
        }
    }

    //Load Country Data
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

                mCountriesRequest = new CountriesRequest(SplashScreenActivity.this, mContext);
                mCountriesRequest.getCountries(response);
            } catch (IOException ioException) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "IOException caught");
                FirebaseCrash.report(ioException);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void countriesXStreamParseSuccess() {
    }

    @Override
    public void countriesXStreamParseFailed() {
    }
}