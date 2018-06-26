package com.alc.diarymohamed.ui.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.alc.diarymohamed.R;

public class DriveFragment extends Fragment implements View.OnClickListener{

    //private static final Logger SLF_LOGGER = LoggerFactory.getLogger(DriveFragment.class);

    private static View mView;
    private LayoutInflater myLayoutInflater;
    private Context mContext;
    private Button mDriveSaveButton;
    private EditText mDriveEditText;
    private SharedPreferences mSharedPreferences;
    private AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myLayoutInflater = inflater;
        mContext = getActivity().getApplicationContext();

        mSharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        mView = inflater.inflate(R.layout.fragment_drive, container, false);

        mAdView = mView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mDriveSaveButton = (Button) mView.findViewById(R.id.drive_save_button);
        mDriveEditText = (EditText)  mView.findViewById(R.id.drive_edit_text);

        mDriveSaveButton.setOnClickListener(this);

        mDriveSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("drive_message", mDriveEditText.getText().toString());
                editor.apply();
            }
        });

        return mView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

    }


}
