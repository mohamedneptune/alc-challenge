package com.alc.diarymohamed.ui.fragment;


import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alc.diarymohamed.R;
import com.alc.diarymohamed.data.helper.ContactsHelper;
import com.alc.diarymohamed.data.helper.CountryHelper;
import com.alc.diarymohamed.data.model.ContactModel;
import com.alc.diarymohamed.data.model.CountryModel;
import com.alc.diarymohamed.shared.Constants;
import com.alc.diarymohamed.shared.Globals;
import com.alc.diarymohamed.ui.activity.MainActivity;
import com.alc.diarymohamed.ui.adapters.SearchableSpinner;
import com.alc.diarymohamed.webservices.request.CountriesRequest;

import java.util.ArrayList;

import io.realm.RealmResults;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class HomeFragment extends Fragment implements View.OnClickListener {

    //private static final Logger SLF_LOGGER = LoggerFactory.getLogger(HomeFragment.class);
    private static final String TAG = "HomeFragment";

    private static View mView;
    private LayoutInflater myLayoutInflater;
    private Context mContext;
    private String mSelectedCountryPrefix, mSelectedCountryName;
    private String mSelectedContactNumber, mSelectedContactName;
    private TextView mCountryPrefixTextView;
    private EditText mContactNumberEditText;

    private RealmResults<ContactModel> mContactModels;
    private RealmResults<CountryModel> mCountryModels;
    private ContactsHelper mContactsHelper;
    private SearchableSpinner mContactSpinner, mCountrySpinner;
    private ArrayList<String> mListContactName;
    private ArrayList<String> mListCountryName, mListCountryPrefix;
    private ArrayAdapter<String> mAdapter;
    private CountriesRequest mCountriesRequest;
    private CountryHelper mCountryHelper;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    private ImageView mHomeContactDialBtn, mHomeCountryFlag;
    private Boolean mDriveActivated;
    private FloatingActionButton mFabButton;
    private LinearLayout mHomeDriveTextLinearLayout;
    private TextView mHomeDriveTextView;
    private SharedPreferences mSharedPreferences;
    private int mPositionCountryFlag;
    private Boolean mContactDial;
    private RelativeLayout mRootLayout;
    private Bundle mBundle;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myLayoutInflater = inflater;
        mContext = getActivity().getApplicationContext();
        mSharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        mView = inflater.inflate(R.layout.fragment_home, container, false);


        mRootLayout = (RelativeLayout) mView.findViewById(R.id.home_root_layout);
        mCountryPrefixTextView = (TextView) mView.findViewById(R.id.home_coutry_prefix_result);
        mContactNumberEditText = (EditText) mView.findViewById(R.id.home_contact_number);

        mHomeDriveTextLinearLayout = (LinearLayout) mView.findViewById(R.id.home_drive_text_linear_layout);
        mHomeDriveTextView = (TextView) mView.findViewById(R.id.home_drive_text_view);

        mFabButton = (FloatingActionButton) mView.findViewById(R.id.home_call_btn);

        mDriveActivated = false;

        mHomeContactDialBtn = (ImageView) mView.findViewById(R.id.home_contact_dial_btn);
        ImageView homeContactAllBtn = (ImageView) mView.findViewById(R.id.home_contact_all_btn);
        ImageView homeCountryAllBtn = (ImageView) mView.findViewById(R.id.home_country_all_btn);
        mHomeCountryFlag = (ImageView) mView.findViewById(R.id.home_country_flag);

        mHomeContactDialBtn.setOnClickListener(this);
        homeContactAllBtn.setOnClickListener(this);
        homeCountryAllBtn.setOnClickListener(this);

        //Spinner
        mCountrySpinner = (SearchableSpinner) mView.findViewById(R.id.country_spinner);
        mCountrySpinner.setTitle("");
        mCountrySpinner.setPositiveButton("");

        mContactSpinner = (SearchableSpinner) mView.findViewById(R.id.contact_spinner);
        mContactSpinner.setTitle("");
        mContactSpinner.setPositiveButton("");

        //Get All users
        try {
            mListContactName = new ArrayList<>();
            mContactsHelper = new ContactsHelper(mContext);
            mContactModels = mContactsHelper.getAllContacts();
        } catch (Exception e) {
            e.printStackTrace();
        }

        loadFromSharedPreference();

        showCountries();

        mCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (position != 0 && null != mCountryModels) {
                    String country_name = mCountryModels.get(position - 1).getName();
                    String country_prefix = mCountryModels.get(position - 1).getPrefix();
                    mCountryPrefixTextView.setText("+" + country_prefix);
                    mPositionCountryFlag = position - 1;
                    mHomeCountryFlag.setImageResource(Constants.DEFAULT_RESOURCE_FLAGS_LIST[mPositionCountryFlag]);


                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("selected_country_name", country_name);
                    editor.putString("selected_country_prefix", country_prefix);
                    editor.putInt("selected_position_country_flag", mPositionCountryFlag);
                    editor.apply();
                    mSelectedCountryName = country_name;
                    mSelectedCountryPrefix = country_prefix;
                }

                Globals.hideKeyboard(getActivity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mContactSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                final long contactId;
                if (position != 0) {
                    //TODO get correct contact ID
                    contactId = mContactModels.get(position - 1).getId();
                    final Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                            contactId);

                    String selectedUserName = mListContactName.get(position);
                    ;
                    String selectedUserNumber = Globals.getContactNumber(contactUri, mContext);
                    mContactNumberEditText.setText(selectedUserNumber);

                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("selected_user_name", selectedUserName);
                    editor.putString("selected_user_number", selectedUserNumber);
                    editor.apply();
                    mSelectedContactName = selectedUserName;
                    mSelectedContactNumber = selectedUserNumber;
                }

                Globals.hideKeyboard(getActivity());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                getActivity(), new String[]{
                                        Manifest.permission.CALL_PHONE, Manifest.permission.CALL_PHONE
                                }, MAKE_CALL_PERMISSION_REQUEST_CODE);
                    } else {
                        callFonction();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return mView;
    }

    private void loadFromSharedPreference() {

        mSelectedCountryName = mSharedPreferences.getString("selected_country_name", "");
        mSelectedCountryPrefix = mSharedPreferences.getString("selected_country_prefix", "");
        mPositionCountryFlag = mSharedPreferences.getInt("selected_position_country_flag", 0);
        mSelectedContactName = mSharedPreferences.getString("selected_user_name", "");
        mSelectedContactNumber = mSharedPreferences.getString("selected_user_number", "");
        mDriveActivated = mSharedPreferences.getBoolean("drive_activated", false);

        /*mContactDial = mSharedPreferences.getBoolean("contact_dial", false);

        if(mContactDial){

            mContactNumberEditText.setText("");
            mContactSpinner.setSelection(0);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean("contact_dial", false);
            editor.apply();
        }*/
    }

    private void showCountries() {
        mListCountryName = new ArrayList<>();
        mListCountryPrefix = new ArrayList<>();

        mCountryHelper = new CountryHelper(mContext);
        mCountryModels = mCountryHelper.getAllCountries();
        try {
            if(null != mCountryModels || !mCountryModels.isEmpty()) {
                fillCountrySpinner(mCountryModels);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public void onResume() {
        super.onResume();

        //TODO commented 19/03 test if necessaire or not
        //loadFromSharedPreference();

        if (!mContactModels.isEmpty()) {
            fillContactSpinner(mContactModels);
        }

        if (null != mSelectedCountryPrefix && !"".equals(mSelectedCountryPrefix)) {
            mCountryPrefixTextView.setText("+" + mSelectedCountryPrefix + " ");
            mHomeCountryFlag.setImageResource(Constants.DEFAULT_RESOURCE_FLAGS_LIST[mPositionCountryFlag]);
        }
        if (null != mSelectedContactNumber && !"".equals(mSelectedContactNumber)) {
            mContactNumberEditText.setText(mSelectedContactNumber);
        }
    }

    private void fillCountrySpinner(RealmResults<CountryModel> countries) {
        mListCountryName.add(getResources().getString(R.string.select_country));
        mListCountryPrefix.add("");
        for (int i = 0; i < countries.size(); i++) {
            if(null != countries.get(i).getName()){
                mListCountryName.add(countries.get(i).getName());
                mListCountryPrefix.add(countries.get(i).getPrefix());
            }
        }
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mListCountryName);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountrySpinner.setAdapter(mAdapter);

        int selectedCountry = mListCountryPrefix.indexOf(mSelectedCountryPrefix);
        if (selectedCountry >= 0) {
            mCountrySpinner.setSelection(selectedCountry);
        }
    }

    private void fillContactSpinner(RealmResults<ContactModel> contacts) {
        mListContactName.add(getResources().getString(R.string.select_contact));
        for (int i = 0; i < contacts.size(); i++) {
            if(null != contacts.get(i).getName()){
                mListContactName.add(contacts.get(i).getName());
            }
        }
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mListContactName);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mContactSpinner.setAdapter(mAdapter);

        mSelectedContactName = mSharedPreferences.getString("selected_user_name", "");

        int selectedContact = mListContactName.indexOf(mSelectedContactName);
        if (selectedContact >= 0) {
            mContactSpinner.setSelection(selectedContact);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_country_all_btn:
                ((MainActivity) getActivity()).setFragment(1, CountryFragment.class);
                break;
            case R.id.home_contact_dial_btn:
                mContactNumberEditText.setText("");
                mContactSpinner.setSelection(0);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                break;

            case R.id.home_contact_all_btn:
                ((MainActivity) getActivity()).setFragment(2, AllContactsFragment.class);
                break;
        }
    }


    private void callFonction() {
        String phoneNumber = mContactNumberEditText.getText().toString();

        if (!TextUtils.isEmpty(phoneNumber)) {
            String completPhoneNumber = mCountryPrefixTextView.getText().toString() +
                    mContactNumberEditText.getText().toString();
            String dial = "tel:" + completPhoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        } else {
            showMessage(getString(R.string.enter_phone_number));
        }
    }

    private void showMessage(String msg) {
        Snackbar snackbar = Snackbar
                .make(mRootLayout, msg, 6000);
        snackbar.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MAKE_CALL_PERMISSION_REQUEST_CODE) {
            if (checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                callFonction();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}