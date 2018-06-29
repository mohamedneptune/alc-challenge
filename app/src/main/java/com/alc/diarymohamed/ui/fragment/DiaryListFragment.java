package com.alc.diarymohamed.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alc.diarymohamed.R;
import com.alc.diarymohamed.data.helper.DiaryHelper;
import com.alc.diarymohamed.data.model.DiaryModel;
import com.alc.diarymohamed.ui.activity.DiaryDetailsActivity;
import com.alc.diarymohamed.ui.adapters.DiaryRecyclerViewAdapter;
import com.alc.diarymohamed.ui.common.RecyclerTouchListner;

import java.util.List;


/**
 * Created by mohamed on 27/06/18.
 */


public class DiaryListFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DiaryListFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private Context mContext;


    public DiaryListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity().getApplicationContext();

        View mView = inflater.inflate(R.layout.fragment_diary_list, container, false);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListner(getActivity(),
                mRecyclerView, new ClickListner() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
            }

        }));

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DiaryDetailsActivity.class);
                saveLastIdTaskToSharedPreference("");
                intent.putExtra("id", "");
                startActivity(intent);
            }
        });

        setUpRecyclerView();

        setHasOptionsMenu(true);

        return mView;
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onClick(View v) {
    }

    public interface ClickListner {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    @Override
    public void onResume() {
        super.onResume();

        showDiary();
    }

    private void showDiary() {
        DiaryHelper diaryHelper = new DiaryHelper(mContext);
        List<DiaryModel> diaryArray = diaryHelper.findAllDiary();

        if (diaryArray != null) {
            RecyclerView.Adapter adapter = new DiaryRecyclerViewAdapter(mContext, diaryArray,
                    R.layout.item_diary);
            mRecyclerView.setAdapter(adapter);
        }
    }


    public void onPause() {
        super.onPause();
    }

    public void saveLastIdTaskToSharedPreference(String id) {
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("MyPrefs",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("last_id_task", id);
        editor.apply();
    }

}