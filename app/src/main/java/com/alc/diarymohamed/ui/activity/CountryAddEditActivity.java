package com.alc.diarymohamed.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alc.diarymohamed.R;
import com.alc.diarymohamed.data.helper.CountryHelper;
import com.alc.diarymohamed.data.model.CountryModel;
import com.alc.diarymohamed.shared.Constants;

/**
 * Created by mbak on 26/06/18.
 */

public class CountryAddEditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CountryAddEditActivity.class.getSimpleName();

    private Context mContext;
    private CountryModel mCountryModel;
    private CountryHelper mCountryHelper;
    private int mPositionCountryFlag;
    private ImageView mAddCountryFlag;
    private Button mCountrySendButton;
    private EditText mDetailCountryNameEditText, mDetailCountryPrefixEditText, mDetailCountryCurrencyEditText,
            mDetailCountryCapitalEditText, mDetailCountryTimeZoneEditText;
    private Boolean mEditMode;
    private SharedPreferences mSharedPref;
    private Bundle mBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_country_add_edit);

        mSharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mEditMode = false;

        mDetailCountryNameEditText = findViewById(R.id.add_country_name_edit);
        mDetailCountryPrefixEditText = findViewById(R.id.add_country_prefix_edit);
        mDetailCountryCurrencyEditText = findViewById(R.id.add_country_currency_edit);
        mDetailCountryCapitalEditText = findViewById(R.id.add_country_capital_edit);
        mDetailCountryTimeZoneEditText = findViewById(R.id.add_country_time_zone_edit);

        mAddCountryFlag = (ImageView) findViewById(R.id.add_country_flag);

        mCountrySendButton = (Button) findViewById(R.id.country_send_button);

        mCountrySendButton.setOnClickListener(this);


        mCountrySendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendNewCountryDetails();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } else {
            Log.w(TAG , "error: Support Action Bar is null");
        }

        //Check name Activity source
        try {
            Intent myIntent = getIntent();
            mEditMode = myIntent.getBooleanExtra("edit_country_mode", false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mEditMode) {
            showDetails();
            setTitle(getResources().getString(R.string.activity_country_edit_title));
        } else {
            setTitle(getResources().getString(R.string.activity_country_add_title));
        }
    }

    private void showDetails() {

        String country_name = mSharedPref.getString("selected_country_name", "");
        String country_prefix = mSharedPref.getString("selected_country_prefix", "");
        mPositionCountryFlag = mSharedPref.getInt("selected_position_country_flag", 0);
        mCountryHelper = new CountryHelper(mContext);
        mCountryModel = mCountryHelper.findCountryByName(country_name);

        if (null != mCountryModel) {
            mDetailCountryNameEditText.setText(mCountryModel.getName());
            mDetailCountryPrefixEditText.setText("+" + mCountryModel.getPrefix());
            mDetailCountryCurrencyEditText.setText(mCountryModel.getCurrency());
            mDetailCountryCapitalEditText.setText(mCountryModel.getCapital());
            mDetailCountryTimeZoneEditText.setText(mCountryModel.getTimeZone());
            mAddCountryFlag.setImageResource(Constants.DEFAULT_RESOURCE_FLAGS_LIST[mPositionCountryFlag]);
        }
    }


    @Override
    public void onClick(View v) {
    }

    private void sendNewCountryDetails() {

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.emailAdmin});
        if (mEditMode) {
            email.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_title_country_modification));
        }else{
            email.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.email_title_country_proposition));
            }
        email.putExtra(Intent.EXTRA_TEXT,
                "CountryName : " + mDetailCountryNameEditText.getText().toString() + "\n" +
                        "CountryPrefix : " + mDetailCountryPrefixEditText.getText().toString() + "\n" +
                        "CountryCurrency : " + mDetailCountryCurrencyEditText.getText().toString() + "\n" +
                        "CountryCapital : " + mDetailCountryCapitalEditText.getText().toString() + "\n" +
                        "CountryTimeZone : " + mDetailCountryTimeZoneEditText.getText().toString());
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, getResources().getString(R.string.email_title_send_to_admin)));
    }

}
