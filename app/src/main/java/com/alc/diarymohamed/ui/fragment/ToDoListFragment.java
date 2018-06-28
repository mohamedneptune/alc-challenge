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
import com.alc.diarymohamed.data.helper.TodoHelper;
import com.alc.diarymohamed.data.model.TodoModel;
import com.alc.diarymohamed.ui.activity.TodoDetailsActivity;
import com.alc.diarymohamed.ui.adapters.TodoRecyclerViewAdapter;
import com.alc.diarymohamed.ui.common.OnUpdateListener;
import com.alc.diarymohamed.ui.common.RecyclerTouchListner;

import java.util.List;

public class ToDoListFragment extends Fragment implements View.OnClickListener, OnUpdateListener {

    private static final String TAG = ToDoListFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<TodoModel> mTodoArray;
    private TodoHelper mTodoHelper;
    private OnUpdateListener listener;


    public ToDoListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity().getApplicationContext();

        View mView = inflater.inflate(R.layout.fragment_todo_list, container, false);

        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListner(getActivity(), mRecyclerView, new ClickListner() {
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

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onUpdate() {
    }

    public interface ClickListner {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    @Override
    public void onResume() {
        super.onResume();

        showTodo();
    }

    private void showTodo() {
        listener = new OnUpdateListener() {
            @Override
            public void onUpdate() {
            }
        };
        mTodoHelper = new TodoHelper(mContext);
        mTodoArray = mTodoHelper.findAllTodo();

        if (mTodoArray != null) {
            RecyclerView.Adapter adapter = new TodoRecyclerViewAdapter(mContext, mTodoArray, R.layout.list_item_todo, listener);
            mRecyclerView.setAdapter(adapter);
        }
    }


    public void onPause() {
        super.onPause();
    }

    public void saveLastIdTaskToSharedPreference(String id) {
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("last_id_task", id);
        editor.apply();
    }

}