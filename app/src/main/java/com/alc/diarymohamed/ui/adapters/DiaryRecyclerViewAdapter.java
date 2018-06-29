package com.alc.diarymohamed.ui.adapters;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alc.diarymohamed.R;
import com.alc.diarymohamed.data.helper.DiaryHelper;
import com.alc.diarymohamed.data.model.DiaryModel;
import com.alc.diarymohamed.data.model.TimeModel;
import com.alc.diarymohamed.shared.Globals;
import com.alc.diarymohamed.ui.activity.DiaryDetailsActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mohamed on 27/06/18.
 */


public class DiaryRecyclerViewAdapter extends RecyclerView.Adapter<DiaryRecyclerViewAdapter.Holder>
        implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = DiaryRecyclerViewAdapter.class.getSimpleName();
    private final Context mContext;
    private final List<DiaryModel> mDiaryArray;
    private final List<DiaryModel> mDiaryArrayPendingRemoval;
    private final int mItemList;
    private static Typeface mRobotoMediumTypeface;
    private static Typeface mRobotoBoldTypeface;
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be


    public DiaryRecyclerViewAdapter(Context context, List<DiaryModel> diaryArray, int item_list) {
        mContext = context;

        mDiaryArrayPendingRemoval = new ArrayList<>();
        mDiaryArray = new ArrayList<>();
        mItemList = item_list;
        mRobotoMediumTypeface = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/" + "Roboto-Medium.ttf");
        mRobotoBoldTypeface = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/" + "Roboto-Bold.ttf");

        mDiaryArray.addAll(diaryArray);
    }


    public static class Holder extends RecyclerView.ViewHolder {

        final TextView mTitleTextView;
        final TextView mDayTextView;
        final TextView mMonthTextView;
        final TextView mYearTextView;
        final TextView mDiaryTimeTextView;
        final TextView mCategoryTextView;
        final LinearLayout mRootLayout;
        final CardView mCardViewRoot;

        public Holder(View itemView) {
            super(itemView);

            mRootLayout = (LinearLayout) itemView.findViewById(R.id.root_list_layout);
            mCardViewRoot = (CardView) itemView.findViewById(R.id.card_view_root);

            mTitleTextView = (TextView) itemView.findViewById(R.id.diary_title);
            mDayTextView = (TextView) itemView.findViewById(R.id.diary_day);
            mMonthTextView = (TextView) itemView.findViewById(R.id.diary_month);
            mYearTextView = (TextView) itemView.findViewById(R.id.diary_year);
            mDiaryTimeTextView = (TextView) itemView.findViewById(R.id.diary_time);
            mCategoryTextView = (TextView) itemView.findViewById(R.id.diary_category);

            mTitleTextView.setTypeface(mRobotoMediumTypeface);
            mDayTextView.setTypeface(mRobotoBoldTypeface);
            mMonthTextView.setTypeface(mRobotoBoldTypeface);
            mYearTextView.setTypeface(mRobotoBoldTypeface);
            mCategoryTextView.setTypeface(mRobotoMediumTypeface);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mItemList, parent,
                false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final DiaryModel diaryModel = mDiaryArray.get(position);
        final int pos = position;

        if (mDiaryArrayPendingRemoval.contains(diaryModel)) {
            // we need to show the "undo" state of the row
            holder.itemView.setBackgroundColor(Color.BLUE);
            holder.mRootLayout.setVisibility(View.GONE);
        } else {
            holder.mRootLayout.setVisibility(View.VISIBLE);


            holder.mTitleTextView.setText(diaryModel.getTitleDiary());
            try {
                int year = diaryModel.getDateDiary().getYear() + 1900;
                int month = diaryModel.getDateDiary().getMonth() + 1;
                int day = diaryModel.getDateDiary().getDate();
                holder.mDayTextView.setText("" + day);
                holder.mMonthTextView.setText(Globals.getMonth(month));
                holder.mYearTextView.setText("" + year);
            } catch (Exception e) {
                e.printStackTrace();
            }

            int diaryHour = diaryModel.getTimeDiary() / 60;
            int diaryMinute = diaryModel.getTimeDiary() % 60;
            TimeModel timeModel = Globals.setTimeFormat(diaryHour,diaryMinute);
            holder.mDiaryTimeTextView.setText(timeModel.getHour()+":"+timeModel.getMinute());

            holder.mCategoryTextView.setText(diaryModel.getCategoryDiary());
        }

        holder.mRootLayout.setOnClickListener(this);

        holder.mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveLastIdTaskToSharedPreference(mDiaryArray.get(pos).getIdDiary());
                    Intent intent = new Intent(mContext, DiaryDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.mRootLayout.setOnLongClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mDiaryArray.size();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public void remove(int position) {
        DiaryModel diaryModel = mDiaryArray.get(position);
        if (mDiaryArrayPendingRemoval.contains(diaryModel)) {
            mDiaryArrayPendingRemoval.remove(diaryModel);
        }
        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    public void saveLastIdTaskToSharedPreference(String id) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences(
                "MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("last_id_task",id);
        editor.apply();
    }

}