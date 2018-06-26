package com.alc.diarymohamed.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alc.diarymohamed.R;
import com.alc.diarymohamed.ui.activity.MainActivity;
import com.alc.diarymohamed.ui.adapters.ContactsViewPagerAdapter;

/**
 * Created by mbak on 21/02/18.
 */

public class ContactsPagerFragment extends Fragment {

    //private static final Logger SLF_LOGGER = LoggerFactory.getLogger(ContactsPagerFragment.class);


    private ViewPager viewPager;
    private TabLayout mTabLayout;
    private static View mView;
    private LayoutInflater myLayoutInflater;
    private Context mContext;
    private SharedPreferences mSharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myLayoutInflater = inflater;
        mContext = getActivity().getApplicationContext();

        mView = inflater.inflate(R.layout.fragment_contacts_pager, container, false);


        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.contact_dial_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putBoolean("contact_dial", true);
                editor.apply();

                ((MainActivity) getActivity()).setFragment(2, HomeFragment.class);
            }
        });

        viewPager = (ViewPager) mView.findViewById(R.id.todo_viewpager);
        setupViewPager(viewPager);
        mTabLayout = (TabLayout) mView.findViewById(R.id.todo_tab_layout);
        mTabLayout.setupWithViewPager(viewPager);


        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));

        mTabLayout.getTabAt(0).setIcon(R.drawable.time);
        mTabLayout.getTabAt(1).setIcon(R.drawable.favoris);
        mTabLayout.getTabAt(2).setIcon(R.drawable.contact);

        viewPager.setOffscreenPageLimit(0);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                                @Override
                                                public void onTabSelected(TabLayout.Tab tab) {
                                                    viewPager.setCurrentItem(tab.getPosition());
                                                    mTabLayout.setTabTextColors(getResources().getColor(R.color.colorGrayWhite), getResources().getColor(R.color.white));

                                                    switch (tab.getPosition()) {
                                                        case 0:
                                                            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(mContext);
                                                            Intent i = new Intent("TAG_REFRESH");
                                                            lbm.sendBroadcast(i);
                                                            break;
                                                        case 1:
                                                            LocalBroadcastManager lbm2 = LocalBroadcastManager.getInstance(mContext);
                                                            Intent i2 = new Intent("TAG_REFRESH");
                                                            lbm2.sendBroadcast(i2);
                                                            break;
                                                        case 2:
                                                            LocalBroadcastManager lbm3 = LocalBroadcastManager.getInstance(mContext);
                                                            Intent i3 = new Intent("TAG_REFRESH");
                                                            lbm3.sendBroadcast(i3);
                                                            break;
                                                    }
                                                }
                                                @Override
                                                public void onTabUnselected(TabLayout.Tab tab) {}
                                                @Override
                                                public void onTabReselected(TabLayout.Tab tab) {}
                                            }
        );

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

    private void setupViewPager(ViewPager viewPager) {
        ContactsViewPagerAdapter adapter = new ContactsViewPagerAdapter(getFragmentManager());
        adapter.addFrag(new FavoritesContactsFragment(),"");
        adapter.addFrag(new LastCallContactsFragment(), "");
        adapter.addFrag(new AllContactsFragment(), "");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
