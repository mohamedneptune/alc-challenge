package com.alc.diarymohamed.ui.activity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.student.android.R;
import com.student.android.data.helper.TodoHelper;
import com.student.android.data.model.PreparationModel;
import com.student.android.data.model.TimeModel;
import com.student.android.data.model.TodoModel;
import com.student.android.shared.Constants;
import com.student.android.shared.Globals;
import com.student.android.ui.adapters.PreparationRecyclerViewAdapter;
import com.student.android.ui.common.AlarmReceiver;
import com.student.android.ui.common.RecyclerTouchListner;
import com.student.android.ui.fragment.ToDoListFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TodoDetailsActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(TodoDetailsActivity.class);
    private int mYear, mMonth, mDay;
    private TextView mDateTextView, mHourTextView, mMinuteTextView, mDurationHourTextView,
            mDurationMinuteTextView;
    private Spinner mCategorySpinner;
    private ArrayList<String> mListCategory, mListPriority;
    private String mId, mCategory, mPriority;
    //private Boolean mPriorityImportant;
    private EditText mTitleEditText, mDescriptionEditText;
    private TodoHelper mTodoHelper;
    private TodoModel mTodoModel;
    private int mSelectedCategoryPosition, mSelectedPriorityPosition;
    private TimePickerDialog mTimePickerDialog;

    private Boolean mPreaparation, mSelected = false;
    private int mTotalTimePreparation;
    private Calendar mCalendar;
    private LinearLayout mLinearTodoDateButton, mLinearTodoDateDetails,mLinearTodoDuration;
    private Button mDateTodayButton, mDateTomorrowButton, mDateCutomButton;
    private ImageView mPrioritySimpleImageView, mPriorityMoyenneImageView, mPriorityHauteImageView;
    private TextView mPrioritySimpleTextView, mPriorityMoyenneTextView, mPriorityHauteTextView;
    private SeekBar mSeekBar;
    private Boolean mSeekSimpleBoolean, mSeekMoyenneBoolean, mSeekHauteBoolean;
    private Drawable mThumbSimple, mThumbMoyenne, mThumbHaute;

    //New graphique
    private RelativeLayout mLayoutWithoutPreparation, mLayoutWithPreparation;
    private TextView mTextWithoutPreparation, mTextWithPreparation;
    private ImageButton mAddPreparationImageButton;
    private List<PreparationModel> mListPreparation;
    private RecyclerView mRecyclerView;
    private Menu mMenu;
    private AlarmManager alarmManager;
    private Intent intentAlarm;
    private Boolean mFromBroadcast;
    private Intent myIntent;
    private MenuItem menuItemDelete, menuItemRemind, menuItemImportant, menuItemClose, menuItemSave;

    String title_draft,desc_draft;
    int categ_draft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SLF_LOGGER.info("onCreate()");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_todo_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } else {
            SLF_LOGGER.warn("error: Support Action Bar is null.");
        }

        Typeface robotoMediumTypeface, robotoRegularTypeface;
        robotoMediumTypeface = Typeface.createFromAsset(getAssets(), "fonts/" + "Roboto-Medium.ttf");
        robotoRegularTypeface = Typeface.createFromAsset(getAssets(), "fonts/" + "Roboto-Regular.ttf");

        mRecyclerView = (RecyclerView) findViewById(R.id.preparation_recycle_view);

        mTitleEditText = (EditText) findViewById(R.id.title_value);
        mDescriptionEditText = (EditText) findViewById(R.id.description_value);
        mDateTextView = (TextView) findViewById(R.id.date_text_view);
        mHourTextView = (TextView) findViewById(R.id.hour_text_view);
        mMinuteTextView = (TextView) findViewById(R.id.minute_text_view);
        mDurationHourTextView = (TextView) findViewById(R.id.duration_hour_text_view);
        mDurationMinuteTextView = (TextView) findViewById(R.id.duration_minute_text_view);
        TextView timePrefix = (TextView) findViewById(R.id.todo_time_unit);

        mPrioritySimpleTextView = (TextView) findViewById(R.id.priority_simple_text_view);
        mPriorityMoyenneTextView = (TextView) findViewById(R.id.priority_moyenne_text_view);
        mPriorityHauteTextView = (TextView) findViewById(R.id.priority_haute_text_view);


        mTitleEditText.setTypeface(robotoRegularTypeface);
        mDescriptionEditText.setTypeface(robotoRegularTypeface);
        mDateTextView.setTypeface(robotoRegularTypeface);
        mHourTextView.setTypeface(robotoRegularTypeface);
        mMinuteTextView.setTypeface(robotoRegularTypeface);
        mDurationHourTextView.setTypeface(robotoRegularTypeface);
        mDurationMinuteTextView.setTypeface(robotoRegularTypeface);
        timePrefix.setTypeface(robotoRegularTypeface);

        mLinearTodoDateDetails = (LinearLayout) findViewById(R.id.linear_todo_date_details);
        mLinearTodoDateButton = (LinearLayout) findViewById(R.id.linear_todo_date_button);
        mLinearTodoDuration=(LinearLayout) findViewById(R.id.linear_todo_duration);
        mDateTodayButton = (Button) findViewById(R.id.date_today_button);
        mDateTomorrowButton = (Button) findViewById(R.id.date_tomorrow_button);
        mDateCutomButton = (Button) findViewById(R.id.date_cutom_button);

        mDateTodayButton.setOnClickListener(this);
        mDateTomorrowButton.setOnClickListener(this);
        mDateCutomButton.setOnClickListener(this);

        mPrioritySimpleImageView = (ImageView) findViewById(R.id.priority_simple_image_view);
        mPriorityMoyenneImageView = (ImageView) findViewById(R.id.priority_moyenne_image_view);
        mPriorityHauteImageView = (ImageView) findViewById(R.id.priority_haute_image_view);

        mPrioritySimpleImageView.setOnClickListener(this);
        mPriorityMoyenneImageView.setOnClickListener(this);
        mPriorityHauteImageView.setOnClickListener(this);


        mLayoutWithoutPreparation = (RelativeLayout) findViewById(R.id.layout_without_preparation);
        mLayoutWithPreparation = (RelativeLayout) findViewById(R.id.layout_with_preparation);
        mAddPreparationImageButton = (ImageButton) findViewById(R.id.add_preparation_image_button);

        mLayoutWithoutPreparation.setOnClickListener(this);
        mLayoutWithPreparation.setOnClickListener(this);
        mAddPreparationImageButton.setOnClickListener(this);

        mTextWithoutPreparation = (TextView) findViewById(R.id.text_without_preparation);
        mTextWithPreparation = (TextView) findViewById(R.id.text_with_preparation);

        mLayoutWithoutPreparation.setBackgroundColor(getResources().getColor(R.color.colorGrayDark));
        mLayoutWithPreparation.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        mTextWithoutPreparation.setTextColor(getResources().getColor(R.color.colorWhite));
        mTextWithPreparation.setTextColor(getResources().getColor(R.color.colorGrayDark1));
        mAddPreparationImageButton.setVisibility(View.GONE);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListner(this, mRecyclerView, new ToDoListFragment.ClickListner() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
            }

        }));


        //Date & Time Picker
        int hour, min;
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        min = mCalendar.get(Calendar.MINUTE);

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


        //Priority Spinner
        mListPriority = new ArrayList<>();
        for (Enum category : Constants.PriorityEnum.values()) {
            mListPriority.add(category.toString());
        }

        mTodoHelper = new TodoHelper();

        //SeekBar
        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mSeekBar.setProgress(2);
        mSeekBar.setMax(100);

        mThumbSimple = getResources().getDrawable(R.drawable.p_basse);
        mThumbSimple.setBounds(new Rect(0, 0, mThumbSimple.getIntrinsicWidth(), mThumbSimple.getIntrinsicHeight()));

        mThumbMoyenne = getResources().getDrawable(R.drawable.p_moyenne);
        mThumbMoyenne.setBounds(new Rect(0, 0, mThumbMoyenne.getIntrinsicWidth(), mThumbMoyenne.getIntrinsicHeight()));

        mThumbHaute = getResources().getDrawable(R.drawable.p_haute);
        mThumbHaute.setBounds(new Rect(0, 0, mThumbHaute.getIntrinsicWidth(), mThumbHaute.getIntrinsicHeight()));

        mSeekBar.setThumb(mThumbSimple);

        //seekBar
        mPrioritySimpleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(2);
                seekPrintersChoice(true, false, false);


            }
        });

        mPriorityMoyenneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(50);
                seekPrintersChoice(false, true, false);


            }
        });

        mPriorityHauteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(98);
                seekPrintersChoice(false, false, true);
            }
        });

        mPrioritySimpleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(2);
                seekPrintersChoice(true, false, false);



            }
        });

        mPriorityMoyenneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(50);
                seekPrintersChoice(false, true, false);

            }
        });

        mPriorityHauteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(98);
                seekPrintersChoice(false, false, true);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressValue = progress;
                SLF_LOGGER.info("progressValue : " + progressValue);
                if (progressValue >= 0 && progressValue <= 30) {
                    mThumbSimple = getResources().getDrawable(R.drawable.p_basse);
                    mSeekBar.setThumb(mThumbSimple);
                    mPriority = mListPriority.get(0);
                    mTodoHelper.makeItUnImportant(mId,false);
                    mTodoHelper.updatePriorityTodo(mId, "Simple");
                } else if (progressValue >= 30 && progressValue <= 70) {
                    mThumbMoyenne = getResources().getDrawable(R.drawable.p_moyenne);
                    mSeekBar.setThumb(mThumbMoyenne);
                    mPriority = mListPriority.get(1);
                    mTodoHelper.makeItUnImportant(mId,false);
                } else {
                    mThumbHaute = getResources().getDrawable(R.drawable.p_haute);
                    mSeekBar.setThumb(mThumbHaute);
                    mPriority = mListPriority.get(2);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                SLF_LOGGER.info("progressValue : " + progressValue);
                if (progressValue >= 0 && progressValue <= 30) {
                    mThumbSimple = getResources().getDrawable(R.drawable.p_basse);
                    mSeekBar.setThumb(mThumbSimple);
                    mPriority = mListPriority.get(0);
                } else if (progressValue >= 30 && progressValue <= 70) {
                    mThumbMoyenne = getResources().getDrawable(R.drawable.p_moyenne);
                    mSeekBar.setThumb(mThumbMoyenne);
                    mPriority = mListPriority.get(1);
                } else {
                    mThumbHaute = getResources().getDrawable(R.drawable.p_haute);
                    mSeekBar.setThumb(mThumbHaute);
                    mPriority = mListPriority.get(2);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SLF_LOGGER.info("progressValue : " + progressValue);
                if (progressValue >= 0 && progressValue <= 30) {
                    //currentCar = "Hatchback";
                    seekBar.setProgress(2);
                    mThumbSimple = getResources().getDrawable(R.drawable.p_basse);
                    mSeekBar.setThumb(mThumbSimple);
                    mPriority = mListPriority.get(0);
                    seekPrintersChoice(true, false, false);
                } else if (progressValue >= 30 && progressValue <= 70) {
                    //currentCar = "Sedan";
                    seekBar.setProgress(50);
                    mThumbMoyenne = getResources().getDrawable(R.drawable.p_moyenne);
                    mSeekBar.setThumb(mThumbMoyenne);
                    mPriority = mListPriority.get(1);
                    seekPrintersChoice(false, true, false);
                } else {
                    //currentCar = "SUV";
                    seekBar.setProgress(98);
                    mThumbHaute = getResources().getDrawable(R.drawable.p_haute);
                    mSeekBar.setThumb(mThumbHaute);
                    mPriority = mListPriority.get(2);
                    seekPrintersChoice(false, false, true);
                }
            }
        });

        mPreaparation = false;

        setUpRecyclerView();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.todo_details_menu, menu);

        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SLF_LOGGER.info("onResume()");

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
         title_draft = sharedPref.getString("draft_title","");
         desc_draft = sharedPref.getString("draft_desc","");
         categ_draft= sharedPref.getInt("draft_categ",0);

        //Menu test
        if (null != mTodoModel) {
            menuItemImportant.setVisible(true);
            menuItemClose.setVisible(true);
            menuItemRemind.setVisible(true);
            menuItemDelete.setVisible(true);

            if (mTodoModel.getDoneTodo()) {
                menuItemClose.setIcon(R.drawable.open);
            } else {
                menuItemClose.setIcon(R.drawable.termine);
            }

            if (null != mTodoModel.getDateTodo()) {
                if (!mTodoModel.getRemindTodo() || mFromBroadcast) {
                    menuItemRemind.setIcon(R.drawable.notif_on);
                } else {
                    menuItemRemind.setIcon(R.drawable.notif_off);
                }
            } else {
                menuItemRemind.setVisible(false);
            }
        } else {
            if(null != menuItemImportant && null !=menuItemClose && null != menuItemRemind && null !=menuItemDelete) {
                menuItemImportant.setVisible(false);
                menuItemClose.setVisible(false);
                menuItemRemind.setVisible(false);
                menuItemDelete.setVisible(false);
            }
        }


        //Check if I came from Notification
        try {
            myIntent = getIntent();
            mFromBroadcast = false;
            mFromBroadcast = myIntent.getBooleanExtra("frombrodcast", mFromBroadcast);
            SLF_LOGGER.info("mFromBroadcast : " + mFromBroadcast);
        } catch (Exception e) {
            SLF_LOGGER.error("error: ", e);
        }

        if (mFromBroadcast) {
            SLF_LOGGER.info("come from Broadcast");
            mId = myIntent.getStringExtra("taskId");
            mTodoHelper.updateRemind(mId, false);
           /* if (null != alarmManager) {
                SLF_LOGGER.info("null != alarmManager");
                alarmManager.cancel(PendingIntent.getBroadcast(TodoDetailsActivity.this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));*/

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();

            //}
        } else {
            mId = loadLastIdTaskToSharedPreference();
            //mFromBroadcast = false;
        }


        //Check if it's new To_do
        try {
            SLF_LOGGER.info("mIdTodo: " + mId);
            mListPreparation = new ArrayList<>();

            if (!("").equals(mId)) {
                loadData(mId);
            } else {
                setTaskDateDetailsVisibility(false);
            }
        } catch (Exception e) {
            SLF_LOGGER.error("error", e);
        }
    }

    private void loadData(String id) {
        mTodoModel = mTodoHelper.findTodo(id);

        mId = id;

        if (mTodoModel != null) {
            if (mTodoModel.getTitleTodo().equals("") || mTodoModel.getDescriptionTodo().equals("")) {
                mTitleEditText.setText(title_draft);
                mDescriptionEditText.setText(desc_draft);
            } else {
                mTitleEditText.setText(mTodoModel.getTitleTodo());
                mDescriptionEditText.setText(mTodoModel.getDescriptionTodo());
            }
                mPreaparation = mTodoModel.getPreparation();
                mSelected = mTodoModel.getSelected();
                mListPreparation = new ArrayList<>();
                Date toDoDate = mTodoModel.getDateTodo();
                if (null != toDoDate) {
                    setTaskDateDetailsVisibility(true);
                } else {
                    setTaskDateDetailsVisibility(false);
                }

                if (mPreaparation) {
                    setPreparationLayoutVisibility(true);
                    mListPreparation = mTodoModel.getListPreparation();
                    if (!mListPreparation.isEmpty()) {
                        RecyclerView.Adapter adapter = new PreparationRecyclerViewAdapter(this, mListPreparation, R.layout.list_item_preparation);
                        mRecyclerView.setAdapter(adapter);
                    }
                } else {
                    setPreparationLayoutVisibility(false);
                }

                try {
                    mYear = mTodoModel.getDateTodo().getYear() + 1900;
                    mMonth = mTodoModel.getDateTodo().getMonth() + 1;
                    mDay = mTodoModel.getDateTodo().getDate();
                    String month = Globals.getMonth(mMonth);
                    mDateTextView.setText(mDay + " " + month + " " + mYear);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Task time
                int hourTask = mTodoModel.getTimeTodo() / 60;
                int minuteTask = mTodoModel.getTimeTodo() % 60;
                TimeModel timeTask = Globals.setTimeFormat(hourTask, minuteTask);
                mHourTextView.setText(timeTask.getHour());
                mMinuteTextView.setText(timeTask.getMinute());

                //Duration time
                if (!mPreaparation) {
                    int hourDuration = mTodoModel.getDurationTodo() / 60;
                    int minuteDuration = mTodoModel.getDurationTodo() % 60;


                    TimeModel timeDuration = Globals.setTimeFormat(hourDuration, minuteDuration);
                    mDurationHourTextView.setText(timeDuration.getHour());
                    mDurationMinuteTextView.setText(timeDuration.getMinute());
                    mTodoHelper.updateTaskDateTime(mId, mTodoModel.getTimeTodo(), mTodoModel.getDurationTodo());
                } else {
                    mListPreparation = new ArrayList<>();
                    mListPreparation = mTodoModel.getListPreparation();
                    int totalDurationPreparation = 0;
                    for (PreparationModel preparationModel : mListPreparation) {
                        totalDurationPreparation = totalDurationPreparation + preparationModel.getDurationPreparation();
                    }
                    int hourDuration = totalDurationPreparation / 60;
                    int minuteDuration = totalDurationPreparation % 60;
                    TimeModel timeDuration = Globals.setTimeFormat(hourDuration, minuteDuration);
                    mDurationHourTextView.setText(timeDuration.getHour());
                    mDurationMinuteTextView.setText(timeDuration.getMinute());
                    mTodoHelper.updateTaskDateTime(mId, mTodoModel.getTimeTodo(), totalDurationPreparation);
                }

                //
                for (int i = 0; i < mListCategory.size(); i++) {
                    if (mTodoModel.getCategoryTodo().equals(mListCategory.get(i))) {
                        mSelectedCategoryPosition = i;
                    }
                }

                    mCategorySpinner.setSelection(categ_draft);


                //priority
                for (int i = 0; i < mListPriority.size(); i++) {
                    if (mTodoModel.getPriorityTodo().equals(mListPriority.get(i))) {
                        mSelectedPriorityPosition = i;
                    }
                }
                switch (mSelectedPriorityPosition) {
                    case 0:
                        mSeekBar.setThumb(mThumbSimple);
                        mSeekBar.setProgress(2);
                        mPriority = mListPriority.get(0);
                        break;
                    case 1:
                        mSeekBar.setThumb(mThumbMoyenne);
                        mSeekBar.setProgress(50);
                        mPriority = mListPriority.get(1);
                        break;
                    case 2:
                        mSeekBar.setThumb(mThumbHaute);
                        mSeekBar.setProgress(98);
                        mPriority = mListPriority.get(2);
                        break;
                    default:
                        mSeekBar.setThumb(mThumbSimple);
                        mSeekBar.setProgress(2);
                        mPriority = mListPriority.get(0);
                        break;
                }
            }
        }

    private void setTaskDuration(String id, Boolean preparation ){
        mTodoModel = mTodoHelper.findTodo(id);
        mId = id;

        //Duration time
        //!preparation && mTodoModel!=null
        if(!preparation){
            mTodoHelper.updateTaskDateTime(mId, mTodoModel.getTimeTodo(), mTodoModel.getDurationTodo());
        } else {
            mListPreparation = new ArrayList<>();
            mListPreparation = mTodoModel.getListPreparation();
            int totalDurationPreparation = 0;
            for (PreparationModel preparationModel : mListPreparation) {
                totalDurationPreparation = totalDurationPreparation + preparationModel.getDurationPreparation();
            }
            int hourDuration = totalDurationPreparation / 60;
            int minuteDuration = totalDurationPreparation % 60;
            TimeModel timeDuration = Globals.setTimeFormat(hourDuration, minuteDuration);
            mDurationHourTextView.setText(timeDuration.getHour());
            mDurationMinuteTextView.setText(timeDuration.getMinute());
            mTodoHelper.updateTaskDateTime(mId, mTodoModel.getTimeTodo(), totalDurationPreparation);
        }



    }


    private void saveData(Boolean todo) {
        String title, description, date, hour, minute, hourPreparation, minutePreparation;
        title = mTitleEditText.getText().toString();
        description = mDescriptionEditText.getText().toString();
        try {
            if ("".equals(mId)) {
                mId = UUID.randomUUID().toString();
                saveLastIdTaskToSharedPreference(mId);
                mTodoHelper.addTodo(mId, title, mCategory, null, 0, 0, mPriority, description, todo, false, mPreaparation, false,false);
            } else {
                saveLastIdTaskToSharedPreference(mId);
                Date toDoDate = mTodoModel.getDateTodo();
                mTodoHelper.updateTodo(mId, title, mCategory, toDoDate, mTodoModel.getTimeTodo(), mTodoModel.getDurationTodo(), mPriority,
                        description, todo, mSelected, mPreaparation,mTodoModel.getPriorityImportant());

            }
        } catch (Exception e) {
            SLF_LOGGER.error("error: ", e);
        }
    }

    public void saveFirstDate(Date date) {
        String title, description;
        title = mTitleEditText.getText().toString();
        description = mDescriptionEditText.getText().toString();
        if (!mPreaparation) mListPreparation = new ArrayList<>();
        try {
            if ("".equals(mId)) {
                mId = UUID.randomUUID().toString();
                saveLastIdTaskToSharedPreference(mId);
                mTodoHelper.addTodo(mId, title, mCategory, date, 0, 0, mPriority, description, false, false, mPreaparation, false,false);
            } else {
                saveLastIdTaskToSharedPreference(mId);
                mTodoHelper.prepareToAddNewTaskDate(mId, title, mCategory, mPriority, description, date,mTodoModel.getDateEndTodo());
            }
        } catch (Exception e) {
            SLF_LOGGER.error("error: ", e);
        }
    }

    private void addNewPreparation() {
        String title, description;
        title = mTitleEditText.getText().toString();
        description = mDescriptionEditText.getText().toString();

        if (!mPreaparation) {
            mListPreparation = new ArrayList<>();
        }
        mPreaparation = true;
        try {
            if ("".equals(mId)) {
                mId = UUID.randomUUID().toString();
                saveLastIdTaskToSharedPreference(mId);
                mTodoHelper.addTodo(mId, title, mCategory, null, 0, 0, mPriority, description, false, false, mPreaparation, false,false);
            } else {
                saveLastIdTaskToSharedPreference(mId);
                mTodoHelper.prepareToAddNewPreparation(mId, title, mCategory, mPriority, description, mPreaparation);
            }
        } catch (Exception e) {
            SLF_LOGGER.error("error: ", e);
        }
    }



    private void setPriorityToImportant() {
        String title, description;
        title = mTitleEditText.getText().toString();
        description = mDescriptionEditText.getText().toString();

        if (!mPreaparation) mListPreparation = new ArrayList<>();
        try {
            if ("".equals(mId)) {
                mId = UUID.randomUUID().toString();
                saveLastIdTaskToSharedPreference(mId);
                mTodoHelper.addTodo(mId, title, mCategory, null, 0, 0, "Haute", description, false, false, mPreaparation, false,true);
            } else {
                saveLastIdTaskToSharedPreference(mId);
                mTodoHelper.updatePriorityTodo(mId, "Haute");
                mTodoHelper.makeItImportant(mId,true);

            }
        } catch (Exception e) {
            SLF_LOGGER.error("error: ", e);
        }
    }

    public void saveLastIdTaskToSharedPreference(String id) {
        SharedPreferences mSharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("last_id_task", id);
        editor.apply();
    }

    public String loadLastIdTaskToSharedPreference() {
        SharedPreferences sp =
                getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String id = "";
        return id = sp.getString("last_id_task", id);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        SLF_LOGGER.info("Menu");

        menuItemDelete = menu.getItem(0);
        menuItemRemind = menu.getItem(1);
        menuItemImportant = menu.getItem(2);
        menuItemClose = menu.getItem(3);
        menuItemSave = menu.getItem(4);


        if (null != mTodoModel) {
            menuItemImportant.setVisible(true);
            menuItemClose.setVisible(true);
            menuItemRemind.setVisible(true);
            menuItemDelete.setVisible(true);

            if (mTodoModel.getDoneTodo()) {
                menuItemClose.setIcon(R.drawable.open);
            } else {
                menuItemClose.setIcon(R.drawable.termine);
            }

            if (null != mTodoModel.getDateTodo()) {
                if (!mTodoModel.getRemindTodo() || mFromBroadcast) {
                    menuItemRemind.setIcon(R.drawable.notif_on);
                } else {
                    menuItemRemind.setIcon(R.drawable.notif_off);
                }
            } else {
                menuItemRemind.setVisible(false);
            }
        } else {
            menuItemImportant.setVisible(false);
            menuItemClose.setVisible(false);
            menuItemRemind.setVisible(false);
            menuItemDelete.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                    /*AlertDialog.Builder Alertbuilder = new AlertDialog.Builder(this);
                    Alertbuilder.setMessage("Souhaitez-vous vraiment quitter ?").setPositiveButton("Quitter", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mTodoHelper.removeTodo(mId);
                            finish();

                        }
                    }).setNegativeButton("Annuler", null);
                    AlertDialog alertDial = Alertbuilder.create();
                    alertDial.show();*/
                /*SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.putString("draft_title","");
                editor.putString("draft_desc","");
                editor.commit();*/
                finish();

                return true;

            case R.id.menu_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.todo_details_remove_alert)).setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mTodoHelper.removeTodo(mId);
                        finish();
                    }
                }).setNegativeButton("Annuler", null);
                AlertDialog alert = builder.create();
                alert.show();
                return true;

            case R.id.menu_notify:
                if (null != mTodoModel) {
                    if (!mTodoModel.getRemindTodo() && null != mTodoModel.getDateTodo()) {
                        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        intentAlarm = new Intent(TodoDetailsActivity.this, AlarmReceiver.class);

                        intentAlarm.putExtra("taskId", "" + mTodoModel.getIdTodo());
                        intentAlarm.putExtra("taskTitle", mTodoModel.getTitleTodo());

                        intentAlarm.putExtra("taskDate", "" + mTodoModel.getDateTodo().getDate() + " "
                                + Globals.getMonth(mTodoModel.getDateTodo().getMonth() + 1) + " "
                                + (mTodoModel.getDateTodo().getYear() + 1900));

                        intentAlarm.putExtra("taskTime", mTodoModel.getTimeTodo() / 60 + ":"
                                + mTodoModel.getTimeTodo() % 60);

                        Calendar calendar = Calendar.getInstance();
                        //calendar.set(Calendar.SECOND, 0);
                        /*calendar.set(Calendar.MINUTE, mTodoModel.getDateTodo().getMinutes());
                        calendar.set(Calendar.HOUR, mTodoModel.getDateTodo().getHours());
                        calendar.set(Calendar.AM_PM, Calendar.PM);
                        calendar.set(Calendar.MONTH, mTodoModel.getDateTodo().getMonth() + 1);
                        calendar.set(Calendar.DAY_OF_MONTH, mTodoModel.getDateTodo().getDay());
                        calendar.set(Calendar.YEAR, mTodoModel.getDateTodo().getYear()+ 1900);*/
                        long when = mTodoModel.getDateTodo().getTime();
                        long currentTimeMillis = System.currentTimeMillis();
                        long diff = currentTimeMillis - when;
                        long nbrHourDiff = diff / (60 * 60 * 1000);

                        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5 * 1000), PendingIntent.getBroadcast(TodoDetailsActivity.this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

                        //Alarm declenche before 12 hours
                        int nbrHourBeforeTask = 12;
                        //TODO nbrHourBeforeTask Dynamic
                        long timeBeforeAlarm = nbrHourBeforeTask * 60 * 60 * 1000;
                        when = when - timeBeforeAlarm;
                        alarmManager.set(AlarmManager.RTC_WAKEUP, when, PendingIntent.getBroadcast(TodoDetailsActivity.this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

                        mTodoHelper.updateRemind(mId, true);
                        item.setIcon(R.drawable.notif_off);
                    } else {
                        item.setIcon(R.drawable.notif_on);
                        if (alarmManager != null) {
                            alarmManager.cancel(PendingIntent.getBroadcast(TodoDetailsActivity.this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
                        }
                        mTodoHelper.updateRemind(mId, false);
                        /*Toast.makeText(this, getResources().getString(R.string.todo_details_notif),
                                Toast.LENGTH_LONG).show();*/
                    }
                }
                return true;

            case R.id.menu_important:
                if (null != mTodoModel) {
                    setPriorityToImportant();
                    mSeekBar.setThumb(mThumbHaute);
                    mSeekBar.setProgress(98);
                    mPriority = mListPriority.get(2);


                }

                return true;

            case R.id.menu_done:
                if (null != mTodoModel) {
                    if (mTodoModel.getDoneTodo()) {
                        saveData(false);
                        item.setIcon(R.drawable.termine);
                    } else {
                        saveData(true);
                        item.setIcon(R.drawable.open);
                    }
                    exit();
                }
                return true;

            case R.id.menu_save:
                String title = mTitleEditText.getText().toString();
                if (!"".equals(title) && mTodoModel != null) {
                //New Task
                if (mTodoModel == null) {
                    saveData(false);
                }
                //Old Task and Done Task
                else if (mTodoModel.getDoneTodo()) {
                    // if user change dateTime and new date time > current date
                   /*Date currentDate = new Date(System.currentTimeMillis());
                    //Date newToDoDate = new Date(mYear-1900, mMonth-1, mDay);
                    Date toDoDate = mTodoModel.getDateTodo();
                    long diff = Globals.printDifference(currentDate, toDoDate);
                    if (diff > 0) {
                        saveData(false);
                    } else {
                        saveData(true);
                    }*/
                    saveData(true);
                    //Old Task and not Done Task
                } else {
                    saveData(false);
                }
                exit();
                }else{
                    new AlertDialog.Builder(this)
                            .setTitle("")
                            .setMessage("Le titre et la date sont des champs requis")
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
        if (mFromBroadcast)

        {
            Intent intent = new Intent(TodoDetailsActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        } else

        {
            this.finish();
        }

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
        switch (v.getId()) {
//TODO

            case R.id.layout_without_preparation:
                setPreparationLayoutVisibility(false);
                //loadData(mId);
                //setTaskDuration(mId, mPreaparation);
                try{
                setTaskDuration(mId, mPreaparation);
                }catch (NullPointerException e){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Choisissez avant une date pour la Tâche").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
                //TODO
                //mPreaparation = false;
                //mTimePreparation = 50;
                break;
            case R.id.layout_with_preparation:
                setPreparationLayoutVisibility(true);
                //TODO
                mPreaparation = true;
                //mTimePreparation = 50;
                if (!mListPreparation.isEmpty()) {
                    RecyclerView.Adapter adapter = new PreparationRecyclerViewAdapter(this, mListPreparation, R.layout.list_item_preparation);
                    mRecyclerView.setAdapter(adapter);
                }
                try{
                setTaskDuration(mId, mPreaparation);
                }catch (NullPointerException e){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Choisissez avant une date pour la Tâche").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }

                break;

            case R.id.add_preparation_image_button:
                Intent intent_add_preparation = new Intent(this, EditCalenderActivity.class);
                intent_add_preparation.putExtra("edit_calendar_key", "add_preparation");
                addNewPreparation();
                intent_add_preparation.putExtra("idTodo", mId);
                startActivity(intent_add_preparation);
                break;

            case R.id.date_today_button:
                Date todayDate = new Date(System.currentTimeMillis());
                saveFirstDate(todayDate);
                Intent intent_add_task = new Intent(this, EditCalenderActivity.class);
                intent_add_task.putExtra("edit_calendar_key", "task_time_today");
                intent_add_task.putExtra("idTodo", mId);
                startActivity(intent_add_task);
                break;

            case R.id.date_tomorrow_button:
                Date tomorrowDate = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(tomorrowDate);
                c.add(Calendar.DATE, 1);
                tomorrowDate = c.getTime();
                saveFirstDate(tomorrowDate);
                intent_add_task = new Intent(this, EditCalenderActivity.class);
                intent_add_task.putExtra("edit_calendar_key", "task_time_tomorrow");
                intent_add_task.putExtra("idTodo", mId);
                startActivity(intent_add_task);
                break;

            case R.id.date_cutom_button:
                Date customDate = new Date(System.currentTimeMillis());
                saveFirstDate(customDate);
                intent_add_task = new Intent(this, EditCalenderActivity.class);
                intent_add_task.putExtra("edit_calendar_key", "task_time_custom");
                intent_add_task.putExtra("idTodo", mId);
                startActivity(intent_add_task);
                break;


        }
    }

    public void setTaskDate(View view) {
        Intent intent_edit_task = new Intent(this, EditCalenderActivity.class);
        intent_edit_task.putExtra("edit_calendar_key", "task_modify_date");
        intent_edit_task.putExtra("idTodo", mTodoModel.getIdTodo());
        startActivity(intent_edit_task);
    }

    public void seekPrintersChoice(Boolean simple, Boolean moyenne, Boolean haute) {
        mSeekSimpleBoolean = simple;
        mSeekMoyenneBoolean = moyenne;
        mSeekHauteBoolean = haute;
    }

    public void setPreparationLayoutVisibility(Boolean preparation) {
        if (preparation) {
            mLayoutWithoutPreparation.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            mLayoutWithPreparation.setBackgroundColor(getResources().getColor(R.color.blue));
            mTextWithoutPreparation.setTextColor(getResources().getColor(R.color.colorGrayDark1));
            mTextWithPreparation.setTextColor(getResources().getColor(R.color.colorWhite));
            mAddPreparationImageButton.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mLinearTodoDuration.setVisibility(View.VISIBLE);
        } else {
            mLayoutWithoutPreparation.setBackgroundColor(getResources().getColor(R.color.colorGrayDark));
            mLayoutWithPreparation.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            mTextWithoutPreparation.setTextColor(getResources().getColor(R.color.colorWhite));
            mTextWithPreparation.setTextColor(getResources().getColor(R.color.colorGrayDark1));
            mAddPreparationImageButton.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            mLinearTodoDuration.setVisibility(View.GONE);
            mPreaparation = false;
            mTodoHelper.clearPreparation(mId);


        }
    }

    public void setTaskDateDetailsVisibility(Boolean taskDateExist) {
        if (taskDateExist) {
            mLinearTodoDateDetails.setVisibility(View.VISIBLE);
            mLinearTodoDateButton.setVisibility(View.GONE);
        } else {
            mLinearTodoDateDetails.setVisibility(View.GONE);
            mLinearTodoDateButton.setVisibility(View.VISIBLE);
        }
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        //setUpItemTouchHelper();
        //setUpAnimationDecoratorHelper();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putString("draft_title", mTitleEditText.getText().toString());
        editor.putString("draft_desc",mDescriptionEditText.getText().toString());
        editor.putInt("draft_categ",mCategorySpinner.getSelectedItemPosition());
        editor.commit();

    }
}