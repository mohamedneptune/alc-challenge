package com.alc.diarymohamed.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alc.diarymohamed.R;
import com.alc.diarymohamed.data.helper.DiaryHelper;
import com.alc.diarymohamed.data.model.DiaryModel;
import com.alc.diarymohamed.data.model.TimeModel;
import com.alc.diarymohamed.shared.Constants;
import com.alc.diarymohamed.shared.Globals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


/**
 * Created by mohamed on 28/06/18.
 */


public class DiaryDetailsActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private static final String TAG = DiaryDetailsActivity.class.getSimpleName();
    private int mYear;
    private int mMonth;
    private int mDay;
    private TextView mDateTextView, mHourTextView, mMinuteTextView;
    private Spinner mCategorySpinner;
    private ArrayList<String> mListCategory;
    private String mId, mCategory;

    private EditText mTitleEditText, mDescriptionEditText;
    private DiaryHelper mDiaryHelper;
    private DiaryModel mDiaryModel;
    private int mSelectedCategoryPosition;
    private Calendar mCalendar;
    private Date mDate;

    private Intent myIntent;

    String title_draft, desc_draft;
    int categ_draft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_diary_details);

        Context context = getApplicationContext();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Typeface robotoRegularTypeface = Typeface.createFromAsset(getAssets(),
                "fonts/" + "Roboto-Regular.ttf");


        mTitleEditText = (EditText) findViewById(R.id.title_value);
        mDescriptionEditText = (EditText) findViewById(R.id.description_value);
        mDateTextView = (TextView) findViewById(R.id.date_text_view);
        mHourTextView = (TextView) findViewById(R.id.hour_text_view);
        mMinuteTextView = (TextView) findViewById(R.id.minute_text_view);
        TextView timePrefix = (TextView) findViewById(R.id.diary_time_unit);


        mTitleEditText.setTypeface(robotoRegularTypeface);
        mDescriptionEditText.setTypeface(robotoRegularTypeface);
        mDateTextView.setTypeface(robotoRegularTypeface);
        mHourTextView.setTypeface(robotoRegularTypeface);
        mMinuteTextView.setTypeface(robotoRegularTypeface);
        timePrefix.setTypeface(robotoRegularTypeface);


        //Date & Time Picker
        mDate = new Date();
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        mCategorySpinner = (Spinner) findViewById(R.id.category_spinner);

        //Category Spinner
        mListCategory = new ArrayList<>();
        for (Enum category : Constants.CategoryEnum.values()) {
            mListCategory.add(category.toString());
        }
        mCategorySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, mListCategory);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(adapterCategory);

        setTitle(getResources().getString(R.string.activity_details_title));


        mDiaryHelper = new DiaryHelper(context);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.diary_details_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        title_draft = sharedPref.getString("draft_title", "");
        desc_draft = sharedPref.getString("draft_desc", "");
        categ_draft = sharedPref.getInt("draft_categ", 0);

        mId = loadLastIdTaskToSharedPreference();

    //Check if it's new To_do
        if (!("").equals(mId)) {
            loadData(mId);
        }
    }

    public String loadLastIdTaskToSharedPreference() {
        SharedPreferences sp =
                getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        return sp.getString("last_id_task", "");
    }

    private void loadData(String id) {
        mDiaryModel = mDiaryHelper.findDiary(id);

        mId = id;

        if (mDiaryModel != null) {
            if (mDiaryModel.getTitleDiary().equals("") ||
                    mDiaryModel.getDescriptionDiary().equals("")) {
                mTitleEditText.setText(title_draft);
                mDescriptionEditText.setText(desc_draft);
            } else {
                mTitleEditText.setText(mDiaryModel.getTitleDiary());
                mDescriptionEditText.setText(mDiaryModel.getDescriptionDiary());
            }
        }

        try {
            mYear = mDiaryModel.getDateDiary().getYear() + 1900;
            mMonth = mDiaryModel.getDateDiary().getMonth() + 1;
            mDay = mDiaryModel.getDateDiary().getDate();
            String month = Globals.getMonth(mMonth);
            mDateTextView.setText(mDay + " " + month + " " + mYear);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Task time
        int hourTask = mDiaryModel.getTimeDiary() / 60;
        int minuteTask = mDiaryModel.getTimeDiary() % 60;
        TimeModel timeTask = Globals.setTimeFormat(hourTask, minuteTask);
        mHourTextView.setText(timeTask.getHour());
        mMinuteTextView.setText(timeTask.getMinute());

        for (int i = 0; i < mListCategory.size(); i++) {
            if (mDiaryModel.getCategoryDiary().equals(mListCategory.get(i))) {
                mSelectedCategoryPosition = i;
            }
        }
        mCategorySpinner.setSelection(mSelectedCategoryPosition);
    }


    private void saveData() {
        String title, description;
        title = mTitleEditText.getText().toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(mDate);
        int timeDiary = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
        description = mDescriptionEditText.getText().toString();
        try {
            if ("".equals(mId)) {
                mId = UUID.randomUUID().toString();
                saveLastIdTaskToSharedPreference(mId);
                mDiaryHelper.addDiary(mId, title, mCategory, mCalendar.getTime(), timeDiary, description);
            } else {
                saveLastIdTaskToSharedPreference(mId);
                Date diaryDate = mDiaryModel.getDateDiary();
                mDiaryHelper.updateDiary(mId, title, mCategory, diaryDate, timeDiary, description);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLastIdTaskToSharedPreference(String id) {
        SharedPreferences mSharedPreferences = getSharedPreferences("MyPrefs",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("last_id_task", id);
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                return true;

            case R.id.menu_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.diary_details_remove_alert)).setPositiveButton(
                        "Supprimer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDiaryHelper.removeDiary(mId);
                        finish();
                    }
                }).setNegativeButton("Annuler", null);
                AlertDialog alert = builder.create();
                alert.show();
                return true;

            case R.id.menu_save:
                String title = mTitleEditText.getText().toString();
                if (!"".equals(title)) {
                    saveData();
                    exit();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("")
                            .setMessage("Le titre est un champ requis")
                            .setPositiveButton("ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();

                                        }
                                    }).create().show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {

    }

    public void exit() {
        this.finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.category_spinner) {
            mCategory = mListCategory.get(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putString("draft_title", mTitleEditText.getText().toString());
        editor.putString("draft_desc", mDescriptionEditText.getText().toString());
        editor.putInt("draft_categ", mCategorySpinner.getSelectedItemPosition());
        editor.apply();

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem menuItemDelete = menu.getItem(0);
        MenuItem menuItemSave = menu.getItem(1);

        if (null == mDiaryModel) {
            menuItemDelete.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}