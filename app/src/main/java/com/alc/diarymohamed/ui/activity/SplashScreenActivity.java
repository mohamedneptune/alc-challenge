package com.alc.diarymohamed.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.alc.diarymohamed.R;
import com.alc.diarymohamed.shared.Config;
import com.alc.diarymohamed.shared.Globals;


/**
 * Created by mohamed on 25/06/18.
 */

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    private Intent intent;
    private static final int LOADER_ID = 1;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        Globals.setApplicationContext(getApplicationContext());

        //Show application in full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                intent = new Intent(SplashScreenActivity.this,
                        AuthenticationActivity.class);
                startActivity(intent);
                finish();
            }
        }, Config.SPLASH_SCREEN_TIME_OUT);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}