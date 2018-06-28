package com.alc.diarymohamed.data.helper;



import android.content.Context;
import android.util.Log;

import com.alc.diarymohamed.data.model.TodoModel;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class TodoHelper {

    private static final String TAG = TodoHelper.class.getSimpleName();
    private Realm mRealm;

    public TodoHelper(Context context) {
    }

    public void addTodo(String idTodo, String titleTodo, String categoryTodo, Date dateTodo, String descriptionTodo) {

        TodoModel todoModel = new TodoModel();

        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            todoModel = mRealm.createObject(TodoModel.class);
            todoModel.setIdTodo(idTodo);
            todoModel.setTitleTodo(titleTodo);
            todoModel.setCategoryTodo(categoryTodo);
            todoModel.setDateTodo(dateTodo);
            todoModel.setDescriptionTodo(descriptionTodo);

        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.copyToRealm(todoModel);
            mRealm.commitTransaction();
            mRealm.close();
        }
    }


    public RealmResults<TodoModel> findAllTodo() {
        mRealm = Realm.getDefaultInstance();
        RealmQuery<TodoModel> query = mRealm.where(TodoModel.class);
        RealmResults<TodoModel> todoModels = query.findAll();
        return todoModels;
    }

    public TodoModel findTodo(String idTodo) {
        TodoModel todoModel = null;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", idTodo)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                Log.i(TAG,"todoModel.find returns null");
            } else {
                Log.i(TAG,"todoModel.find returns " + todoModel.getTitleTodo());
            }
            //realm.close();
        }
        return todoModel;
    }

    public void updateTodo(String id, String titleTodo, String categoryTodo, Date dateTodo, String descriptionTodo) {
        TodoModel todoModel = null;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                Log.i(TAG,"todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setIdTodo(id);
                    todoModel.setTitleTodo(titleTodo);
                    todoModel.setCategoryTodo(categoryTodo);
                    todoModel.setDateTodo(dateTodo);
                    todoModel.setDescriptionTodo(descriptionTodo);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeTodo(String idTodo) {
        TodoModel todoModel = null;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", idTodo)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                Log.i(TAG,"todoModel.find returns null");
            } else {
                Log.i(TAG,"todoModel.find returns " + todoModel.getTitleTodo());
                mRealm.beginTransaction();
                todoModel.deleteFromRealm();
                mRealm.commitTransaction();
            }
        }
    }
}