package com.alc.diarymohamed.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.alc.diarymohamed.PrefixApplication;
import com.alc.diarymohamed.R;
import com.alc.diarymohamed.ui.fragment.AllContactsFragment;
import com.alc.diarymohamed.ui.fragment.ContactsPagerFragment;
import com.alc.diarymohamed.ui.fragment.CountryFragment;
import com.alc.diarymohamed.ui.fragment.DriveFragment;
import com.alc.diarymohamed.ui.fragment.HomeFragment;
import com.alc.diarymohamed.ui.fragment.SuggestionsFragment;

/**
 * Created by mbak on 26/06/18.
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private NavigationView navigation_view;
    private Context mContext;
    private Toolbar mToolBar;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(getResources().getString(R.string.activity_home_title));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setFragment(0, HomeFragment.class);

    }

    @Override
    public void onBackPressed() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            setFragment(0, HomeFragment.class);
        } else if (id == R.id.nav_prefix) {
            setFragment(1, CountryFragment.class);
        } else if (id == R.id.nav_contact) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            this, new String[]{
                                    Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CONTACTS
                            }, 31);
                } else {
                    setFragment(2, AllContactsFragment.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.nav_drive) {
            setFragment(3, DriveFragment.class);
        }else if (id == R.id.nav_suggest) {
            setFragment(4, SuggestionsFragment.class);
        }
        else if (id == R.id.nav_eval) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("market://details?id=" + getPackageName()));
            startActivity(i);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(int position, Class<? extends Fragment> fragmentClass) {
        Bundle params = new Bundle();
        try {
            switch (position) {
                case 0:
                    getSupportActionBar().setTitle(getResources().getString(R.string.activity_home_title));
                    params.putString("full_text", getResources().getString(R.string.activity_home_title));
                    break;
                case 1:
                    getSupportActionBar().setTitle(getResources().getString(R.string.activity_country_list_title));
                    params.putString("full_text", getResources().getString(R.string.activity_country_list_title));
                    break;
                case 2:
                    getSupportActionBar().setTitle(getResources().getString(R.string.activity_contacts_list_title));
                    params.putString("full_text", getResources().getString(R.string.activity_contacts_list_title));
                    break;
                case 3:
                    getSupportActionBar().setTitle(getResources().getString(R.string.activity_drive_title));
                    params.putString("full_text", getResources().getString(R.string.activity_drive_title));
                    break;
                case 4:
                    getSupportActionBar().setTitle(getResources().getString(R.string.activity_suggestions_title));
                    params.putString("full_text", getResources().getString(R.string.activity_suggestions_title));
                    break;
            }

            mContext = getApplicationContext();
            PrefixApplication.setGlobalAppContext(mContext);
            Fragment fragment = fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment, fragmentClass.getSimpleName());
            fragmentTransaction.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 31) {
            getSupportActionBar().setTitle("Contacts");
            setFragment(2, ContactsPagerFragment.class);

        }
    }
}
