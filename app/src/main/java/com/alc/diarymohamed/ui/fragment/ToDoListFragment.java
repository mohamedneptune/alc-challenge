package com.alc.diarymohamed.ui.fragment;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.student.android.R;
import com.student.android.data.helper.TodoHelper;
import com.student.android.data.model.TodoModel;
import com.student.android.shared.Globals;
import com.student.android.ui.activity.TodoDetailsActivity;
import com.student.android.ui.adapters.TodoRecyclerViewAdapter;
import com.student.android.ui.common.OnUpdateListener;
import com.student.android.ui.common.RecyclerTouchListner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.SEARCH_SERVICE;

public class ToDoListFragment extends Fragment implements View.OnClickListener, OnUpdateListener {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(ToDoListFragment.class);
    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<TodoModel> mTodoArray;
    private TodoHelper mTodoHelper;
    private ImageButton mDateButton, mPriorityButton;
    private String[] mFieldNames;
    private String[] mSortByPriority = {"priorityTodo","dateTodo"};
    private String[] mSortByDate = {"dateTodo","priorityTodo"};
    private OnUpdateListener listener;


    public ToDoListFragment() {
        SLF_LOGGER.info("ToDoListFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SLF_LOGGER.info("onCreateView");
        mContext = getActivity().getApplicationContext();

        View mView = inflater.inflate(R.layout.fragment_todo_list, container, false);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);

        mDateButton = (ImageButton) mView.findViewById(R.id.sort_date_button);
        mPriorityButton = (ImageButton) mView.findViewById(R.id.sort_priority_button);

        mDateButton.setOnClickListener(this);
        mPriorityButton.setOnClickListener(this);



        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListner(getActivity(), mRecyclerView, new ClickListner(){
            @Override
            public void onClick(View view, int position) {}

            @Override
            public void onLongClick(View view, int position) {}

        }));

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TodoDetailsActivity.class);
                saveLastIdTaskToSharedPreference("");
                intent.putExtra("id", "");
                startActivity(intent);
            }
        });

        setUpRecyclerView();

        setHasOptionsMenu(true);

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();
        inflater.inflate(R.menu.todo_list_menu, menu);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(SEARCH_SERVICE);
        searchView.setQueryHint("Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                if("".equals(query)){
                    mTodoArray = mTodoHelper.findDoneSortedTodo(false, mFieldNames);
                }else{
                    mTodoArray = mTodoHelper.findDoneSortedTodo(false, mFieldNames, query);
                }

                showTodo();
                return true;
            }
        });
    }


    private void showTodo() {
        listener = new OnUpdateListener() {
            @Override
            public void onUpdate() {
            }
        };

        RecyclerView.Adapter  adapter = new TodoRecyclerViewAdapter(mContext, mTodoArray, R.layout.list_item_todo, listener);
        mRecyclerView.setAdapter(adapter);
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sort_date_button:
                    SLF_LOGGER.info("date_button");
                mFieldNames = mSortByDate;
                mDateButton.setBackgroundResource(R.drawable.calendar_active);
                mPriorityButton.setBackgroundResource(R.drawable.sort_inactive);
                mTodoArray = mTodoHelper.findDoneSortedTodo(false,mFieldNames);
                showTodo();
                break;

            case R.id.sort_priority_button:
                SLF_LOGGER.info("priority_button");
                mFieldNames = mSortByPriority;
                mPriorityButton.setBackgroundResource(R.drawable.sort_active);
                mDateButton.setBackgroundResource(R.drawable.calendar_inactive);
                mTodoArray = mTodoHelper.findDoneSortedTodo(false,mFieldNames);
                showTodo();
                break;
        }
    }

    @Override
    public void onUpdate() {
        SLF_LOGGER.info("onUpdate");
    }

    public interface ClickListner{
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    @Override
    public void onResume() {
        super.onResume();

         verifyExpiredDate();
        showTodo();

        myReceiver = new MyReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(myReceiver,
                new IntentFilter("TAG_REFRESH"));
    }

    MyReceiver myReceiver;

    private void refresh() {
        verifyExpiredDate();
        showTodo();
    }

    /** verify expired date of not done task **/
    public void verifyExpiredDate(){

        mTodoHelper = new TodoHelper();
        mTodoArray = mTodoHelper.findDoneTodo(false);

        Date currentDate = new Date(System.currentTimeMillis());
        List<TodoModel> todoArray = new ArrayList<>();
        todoArray.addAll(mTodoArray);
        Date toDoDate;
        long diff = 0;
        for (TodoModel todoModel : todoArray) {
            toDoDate = todoModel.getDateTodo();
            diff = 0;
            if(null != toDoDate) {
                diff = Globals.printDifference(currentDate, toDoDate);
            }
            if (diff < 0) {
                mTodoHelper.updateTodoDone(todoModel.getIdTodo(), true);
            }
        }

        mFieldNames = mSortByDate;
        mTodoArray = mTodoHelper.findDoneSortedTodo(false,mFieldNames);
    }

    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(myReceiver);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ToDoListFragment.this.refresh();
        }
    }


    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.RIGHT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(getResources().getColor(R.color.touch));
                xMark = ContextCompat.getDrawable(getContext(), R.drawable.termine);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getContext().getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                TodoRecyclerViewAdapter testAdapter = (TodoRecyclerViewAdapter)recyclerView.getAdapter();
                /*if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }*/
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                TodoRecyclerViewAdapter adapter = (TodoRecyclerViewAdapter)mRecyclerView.getAdapter();
                adapter.update(swipedPosition, true);
                showTodo();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

               //Left Position
                // draw red background
                background.setBounds(itemView.getLeft() , itemView.getTop(), itemView.getLeft() + (int) dX , itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getLeft() + xMarkMargin ;
                int xMarkRight = itemView.getLeft() + xMarkMargin + intrinsicWidth;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.BLUE);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }

    public void saveLastIdTaskToSharedPreference(String id) {
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("last_id_task",id);
        editor.apply();
    }

}