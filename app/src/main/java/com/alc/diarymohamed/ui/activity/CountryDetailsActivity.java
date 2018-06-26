package com.alc.diarymohamed.ui.activity;

import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.alc.diarymohamed.R;
import com.alc.diarymohamed.data.helper.CountryHelper;
import com.alc.diarymohamed.data.model.CountryModel;
import com.alc.diarymohamed.data.model.TimeModel;
import com.alc.diarymohamed.shared.Constants;
import com.alc.diarymohamed.shared.Globals;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by mbak on 26/06/18.
 */

public class CountryDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CountryDetailsActivity.class.getSimpleName();

    private Context mContext;
    private CountryModel mCountryModel;
    private CountryHelper mCountryHelper;
    private int mPositionCountryFlag;
    private ImageView mDetailsCountryFlag, mCopiPrefixImageView, mDetailsCountryShare;
    private Button mCountrySelectButton;
    private Calendar mCalendar;
    private BroadcastReceiver mBroadcastReceiver;
    private DateFormat mDateFormat;
    private String mDstCountry;
    private String mDstBegin;
    private String mDstEnd;
    private RelativeLayout mCountryDetailsRoot;

    private TextView mDetailCountryName, mDetailCountryPrefix, mDetailCountryCurrency,
            mDetailCountryCapital, mDetailCountryTimeZone, mDetailCountryLocalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_country_details);

        mDetailCountryName = findViewById(R.id.details_country_title);
        mDetailCountryPrefix = findViewById(R.id.details_country_prefix);
        mDetailCountryCurrency = findViewById(R.id.details_country_currency_value);
        mDetailCountryCapital = findViewById(R.id.details_country_capital_value);
        mDetailCountryTimeZone = findViewById(R.id.details_country_timezone_value);
        mDetailCountryLocalTime = findViewById(R.id.details_country_localhour_value);

        mCountryDetailsRoot = findViewById(R.id.country_details_root);

        mDetailsCountryFlag = (ImageView) findViewById(R.id.details_country_flag);
        mCountrySelectButton = (Button) findViewById(R.id.country_select_button);
        mCopiPrefixImageView = (ImageView) findViewById(R.id.details_country_copi_prefix);
        mDetailsCountryShare = (ImageView) findViewById(R.id.details_country_share);

        mCountrySelectButton.setOnClickListener(this);
        mCopiPrefixImageView.setOnClickListener(this);
        mDetailsCountryShare.setOnClickListener(this);

        mDateFormat = DateFormat.getTimeInstance();
        mDateFormat.setTimeZone(TimeZone.getTimeZone("utc"));
        mCalendar = mDateFormat.getCalendar();


        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } else {
            Log.w(TAG , "error: Support Action Bar is null");
        }

        setTitle(getResources().getString(R.string.activity_country_details_title));

        mCountrySelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mCopiPrefixImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ClipboardManager _clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    _clipboard.setText(mDetailCountryPrefix.getText().toString());
                    showMessage(getResources().getString(R.string.prefix_copied));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mDetailsCountryShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    shareCountryDetails();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showMessage(String msg) {
        Snackbar snackbar = Snackbar
                .make(mCountryDetailsRoot, msg, 4000);
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.country_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_country_details:
                try {
                    Intent intent = new Intent(getApplicationContext(), CountryAddEditActivity.class);
                    intent.putExtra("edit_country_mode", true);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String country_name = sharedPref.getString("selected_country_name", "");
        String country_prefix = sharedPref.getString("selected_country_prefix", "");
        mPositionCountryFlag = sharedPref.getInt("selected_position_country_flag", 0);
        mCountryHelper = new CountryHelper(mContext);
        mCountryModel = mCountryHelper.findCountryByName(country_name);

        showDetails();
    }

    private void showDetails() {
        if (null != mCountryModel) {
            mDetailCountryName.setText(mCountryModel.getName());
            mDetailCountryPrefix.setText("+" + mCountryModel.getPrefix());
            mDetailCountryCurrency.setText(mCountryModel.getCurrency());
            mDetailCountryCapital.setText(mCountryModel.getCapital());
            mDstCountry = mCountryModel.getDst();
            if ("".equals(mDstCountry)) {
                mDetailCountryTimeZone.setText(mCountryModel.getTimeZone());
                mDetailCountryLocalTime.setText(getLocalTime(mCountryModel.getTimeZone()));
            } else {
                try {
                    mDstBegin = mCountryModel.getDstBegin();
                    mDstEnd = mCountryModel.getDstEnd();
                    int currentMonth = (mCalendar.get(Calendar.MONTH)) + 1;
                    int currentDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                    int dstDayBegin = Integer.parseInt(mDstBegin.substring(0, 2));
                    int dstMonthBegin = Integer.parseInt(mDstBegin.substring(2, 4));
                    int dstDayEnd = Integer.parseInt(mDstEnd.substring(0, 2));
                    int dstMonthEnd = Integer.parseInt(mDstEnd.substring(2, 4));
                    if (currentMonth > dstMonthBegin && currentMonth < dstMonthEnd) {
                        mDetailCountryTimeZone.setText(mCountryModel.getDst());
                        mDetailCountryLocalTime.setText(getLocalTime(mCountryModel.getDst()));
                    } else if (currentMonth == dstMonthBegin && currentDay > dstDayBegin) {
                        mDetailCountryTimeZone.setText(mCountryModel.getDst());
                        mDetailCountryLocalTime.setText(getLocalTime(mCountryModel.getDst()));
                    } else if (currentMonth == dstMonthEnd & currentDay < dstDayEnd) {
                        mDetailCountryTimeZone.setText(mCountryModel.getDst());
                        mDetailCountryLocalTime.setText(getLocalTime(mCountryModel.getDst()));
                    } else {
                        mDetailCountryTimeZone.setText(mCountryModel.getTimeZone());
                        mDetailCountryLocalTime.setText(getLocalTime(mCountryModel.getTimeZone()));
                    }
            } catch(Exception e){
                    e.printStackTrace();
                    mDetailCountryTimeZone.setText(mCountryModel.getTimeZone());
                mDetailCountryLocalTime.setText(getLocalTime(mCountryModel.getTimeZone()));
            }

        }

        mDetailsCountryFlag.setImageResource(Constants.DEFAULT_RESOURCE_FLAGS_LIST[mPositionCountryFlag]);
    }

}

    private String getLocalTime(String timeZone) {
        //UTC+04:30
        String time = "";
        TimeModel localTime = null;
        try {
            String operator = timeZone.substring(3, 4);
            int hourTimeZone = Integer.parseInt(timeZone.substring(4, 6));
            int minuteTimeZone = Integer.parseInt(timeZone.substring(7, 9));

            int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
            int min = mCalendar.get(Calendar.MINUTE);
            if ("+".equals(operator)) {
                localTime = Globals.setTimeFormat(hour + hourTimeZone, min + minuteTimeZone);
            } else if ("-".equals(operator)) {
                localTime = Globals.setTimeFormat(hour - hourTimeZone, min - minuteTimeZone);
            }
            if (null != localTime)
                time = localTime.getHour() + (" : ") + localTime.getMinute();
        } catch (Exception e) {
            //SLF_LOGGER.info("Error e", e);
            FirebaseCrash.logcat(Log.ERROR, TAG, "Exception caught");
            FirebaseCrash.report(e);
        }
        return time;
    }


    @Override
    public void onClick(View v) {
    }

    @Override
    public void onStart() {
        super.onStart();
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {
                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                    try {
                        mDetailCountryLocalTime.setText(getLocalTime(mCountryModel.getTimeZone()));
                    } catch (Exception e) {
                        //SLF_LOGGER.error("error setWatchTime : ", e);
                        FirebaseCrash.logcat(Log.ERROR, TAG, "Exception caught setWatchTime");
                        FirebaseCrash.report(e);
                    }
                }
            }
        };

        try {
            registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        } catch (Exception e) {
            //SLF_LOGGER.error("error registerReceiver : ", e);
            FirebaseCrash.logcat(Log.ERROR, TAG, "Exception caught");
            FirebaseCrash.report(e);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != mBroadcastReceiver) {
            try {
                unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e) {
                //SLF_LOGGER.error("error unregisterReceiver: ", e);
                FirebaseCrash.logcat(Log.ERROR, TAG, "Exception caught unregisterReceiver");
                FirebaseCrash.report(e);
            }
        }
    }

    private void shareCountryDetails() {

        if (null != mCountryModel) {
            Intent sendMailIntent = new Intent(Intent.ACTION_SEND);
            sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            sendMailIntent.putExtra(Intent.EXTRA_TEXT, "" + mCountryModel.getName() + "\n"
                    + mCountryModel.getPrefix());
            sendMailIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendMailIntent, "Share"));
        }
    }

}
