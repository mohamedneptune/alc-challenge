package com.alc.diarymohamed.ui.fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.alc.diarymohamed.BuildConfig;
import com.alc.diarymohamed.R;
import com.alc.diarymohamed.data.helper.ContactsHelper;
import com.alc.diarymohamed.data.model.ContactModel;
import com.alc.diarymohamed.ui.adapters.ContactAdapter;
import com.alc.diarymohamed.ui.adapters.ContactCursorAdapter;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by mbak on 21/02/18.
 */

public class AllContactsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {


    //private static final Logger SLF_LOGGER = LoggerFactory.getLogger(AllContactsFragment.class);
    private static final String TAG = "AllContactsFragment";

    private static final int LOADER_ID = 1;
    private RecyclerView mRecyclerView;
    private ContactCursorAdapter mContactCursorAdapter;
    private ContactAdapter mContactAdapter;
    private static View mView;
    private LayoutInflater myLayoutInflater;
    private Context mContext;

    private static final int REQUEST_PERMISSION = 2001;
    private static final String ARG_SECTION_NUMBER = "section_number";

    private final String[] FROM_COLUMNS = {
            ContactsContract.Data.CONTACT_ID,
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Data.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Data.DISPLAY_NAME,
                    ContactsContract.Data.PHOTO_ID
    };
    private Bundle mBundle;

    //private List<CountryModel> mCountriesFromXml;
    private List<ContactModel> mContactModels;
    private ContactsHelper mContactsHelper;
    private String [] mFieldNames;
    private String [] mSortByName = {"Name"};
    private String [] mSortByNumber = {"Number"};
    private Cursor mContactCursor;

    public AllContactsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AllContactsFragment newInstance(int sectionNumber) {
        AllContactsFragment fragment = new AllContactsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_all_contacts, container, false);

        mBundle = savedInstanceState;

        setUpRecyclerView();

        mFieldNames = mSortByName;

        loadAllContacts();

        showContacts();

        mContactsHelper = new ContactsHelper(mContext);

        setHasOptionsMenu(true);

        return mView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.contacts_list_menu, menu);

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
                    mContactModels = mContactsHelper.getAllContacts();
                }else{
                    mContactModels = mContactsHelper.findContactSorted(mFieldNames, StringUtils.capitalize(query));
                }
                showFiltredContacts();

                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 31) {
            if (checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                getLoaderManager().initLoader(LOADER_ID, mBundle, this);
                showContacts();
            } else {
                getActivity().finish();
            }
        }
    }

    private void setUpRecyclerView() {
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.all_contacts_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
    }

    private void loadAllContacts(){
        try {
            if (checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        getActivity(), new String[]{
                                Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CONTACTS
                        }, 31);
            } else {
                getLoaderManager().initLoader(LOADER_ID, mBundle, this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showContacts() {
        mContactCursorAdapter = new ContactCursorAdapter(getContext(), null, ContactsContract.Data.CONTACT_ID);
        mRecyclerView.setAdapter(mContactCursorAdapter);
    }

    private void showFiltredContacts() {
        mContactAdapter = new ContactAdapter(getContext(), mContactModels);
        mRecyclerView.setAdapter(mContactAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(
                        getContext(),
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
        mContactCursorAdapter.swapCursor(mContactCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mContactCursorAdapter.swapCursor(null);
    }



    //private List<ContactModel> getAllContacts(Cursor cursor){
    private void saveContactsInDataBase(Cursor cursor){
        List<ContactModel> contactModelList = new ArrayList<>();
        long contactId;
        String contactName;
        String contactNumber;
        long contactPhotoId;
        try {
            mContactsHelper.deleteAllContacts();
            while (cursor.moveToNext()) {
                contactName = cursor.getString(cursor.getColumnIndex(
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                                ContactsContract.Data.DISPLAY_NAME_PRIMARY : ContactsContract.Data
                                .DISPLAY_NAME
                ));
                contactId = cursor.getPosition();
                contactPhotoId = cursor.getLong(cursor.getColumnIndex(
                        ContactsContract.Data.PHOTO_ID
                ));
                //contactNumber = setContactNumber(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactId));

                //TODO save in database
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

    //    return contactModelList;
    }

}

