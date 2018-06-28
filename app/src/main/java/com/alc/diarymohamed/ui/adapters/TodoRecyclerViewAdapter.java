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

public class TodoRecyclerViewAdapter extends RecyclerView.Adapter<TodoRecyclerViewAdapter.Holder>
        implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = TodoRecyclerViewAdapter.class.getSimpleName();
    private final Context mContext;
    private final List<DiaryModel> mTodoArray;
    private final List<DiaryModel> mTodoArrayPendingRemoval;
    private final int mItemList;
    private static Typeface mRobotoMediumTypeface, mRobotoBoldTypeface, mRobotoLightTypeface;
    private final DiaryHelper mDiaryHelper;
    private Date currentDate;
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be


    public TodoRecyclerViewAdapter(Context context, List<DiaryModel> todoArray, int item_list) {
        mContext = context;

        mTodoArrayPendingRemoval = new ArrayList<>();
        mTodoArray = new ArrayList<>();
        mItemList = item_list;
        mRobotoMediumTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + "Roboto-Medium.ttf");
        mRobotoBoldTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + "Roboto-Bold.ttf");
        mRobotoLightTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/" + "Roboto-Light.ttf");
        currentDate = new Date(System.currentTimeMillis());

        mDiaryHelper = new DiaryHelper(mContext);

        mTodoArray.addAll(todoArray);
    }


    public static class Holder extends RecyclerView.ViewHolder {

        final TextView mTitleTextView;
        final TextView mDayTextView;
        final TextView mMonthTextView;
        final TextView mYearTextView;
        final TextView mTodoTimeTextView;
        final TextView mDurationHourTextView;
        final TextView mUnitTexView;
        final TextView mDurationMinuteTextView;
        final TextView mCategoryTextView;
        final LinearLayout mRootLayout;
        final CardView mCardViewRoot;

        public Holder(View itemView) {
            super(itemView);

            mRootLayout = (LinearLayout) itemView.findViewById(R.id.root_list_layout);
            mCardViewRoot = (CardView) itemView.findViewById(R.id.card_view_root);

            mTitleTextView = (TextView) itemView.findViewById(R.id.todo_title);
            mDayTextView = (TextView) itemView.findViewById(R.id.todo_day);
            mMonthTextView = (TextView) itemView.findViewById(R.id.todo_month);
            mYearTextView = (TextView) itemView.findViewById(R.id.todo_year);
            mTodoTimeTextView = (TextView) itemView.findViewById(R.id.todo_time);
            mDurationHourTextView = (TextView) itemView.findViewById(R.id.todo_duration_hour);
            mUnitTexView = (TextView) itemView.findViewById(R.id.unit);
            mDurationMinuteTextView = (TextView) itemView.findViewById(R.id.todo_duration_minute);
            mCategoryTextView = (TextView) itemView.findViewById(R.id.todo_category);

            mTitleTextView.setTypeface(mRobotoMediumTypeface);
            mDayTextView.setTypeface(mRobotoBoldTypeface);
            mMonthTextView.setTypeface(mRobotoBoldTypeface);
            mYearTextView.setTypeface(mRobotoBoldTypeface);
            mCategoryTextView.setTypeface(mRobotoMediumTypeface);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mItemList, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        final DiaryModel diaryModel = mTodoArray.get(position);
        final int pos = position;

        if (mTodoArrayPendingRemoval.contains(diaryModel)) {
            // we need to show the "undo" state of the row
            holder.itemView.setBackgroundColor(Color.BLUE);
            holder.mRootLayout.setVisibility(View.GONE);
        } else {
            holder.mRootLayout.setVisibility(View.VISIBLE);


            holder.mTitleTextView.setText(diaryModel.getTitleTodo());
            try {
                int year = diaryModel.getDateTodo().getYear() + 1900;
                int month = diaryModel.getDateTodo().getMonth() + 1;
                int day = diaryModel.getDateTodo().getDate();
                holder.mDayTextView.setText("" + day);
                holder.mMonthTextView.setText(Globals.getMonth(month));
                holder.mYearTextView.setText("" + year);
            } catch (Exception e) {
                e.printStackTrace();
            }

            int todoHour = diaryModel.getTimeTodo() / 60;
            int todoMinute = diaryModel.getTimeTodo() % 60;
            TimeModel timeModel = Globals.setTimeFormat(todoHour,todoMinute);
            holder.mTodoTimeTextView.setText(timeModel.getHour()+":"+timeModel.getMinute());

            holder.mCategoryTextView.setText(diaryModel.getCategoryTodo());
        }

        holder.mRootLayout.setOnClickListener(this);

        holder.mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveLastIdTaskToSharedPreference(mTodoArray.get(pos).getIdTodo());
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
        return mTodoArray.size();
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public void remove(int position) {
        DiaryModel diaryModel = mTodoArray.get(position);
        if (mTodoArrayPendingRemoval.contains(diaryModel)) {
            mTodoArrayPendingRemoval.remove(diaryModel);
        }
        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    public void saveLastIdTaskToSharedPreference(String id) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("last_id_task",id);
        editor.apply();
    }

}