package com.alc.diarymohamed.data.helper;



import android.content.Context;
import android.util.Log;

import com.alc.diarymohamed.data.model.DiaryModel;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class DiaryHelper {

    private static final String TAG = DiaryHelper.class.getSimpleName();
    private Realm mRealm;

    public DiaryHelper(Context context) {
    }

    public void addTodo(String idTodo, String titleTodo, String categoryTodo, Date dateTodo, String descriptionTodo) {

        DiaryModel diaryModel = new DiaryModel();

        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            diaryModel = mRealm.createObject(DiaryModel.class);
            diaryModel.setIdTodo(idTodo);
            diaryModel.setTitleTodo(titleTodo);
            diaryModel.setCategoryTodo(categoryTodo);
            diaryModel.setDateTodo(dateTodo);
            diaryModel.setDescriptionTodo(descriptionTodo);

        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.copyToRealm(diaryModel);
            mRealm.commitTransaction();
            mRealm.close();
        }
    }


    public RealmResults<DiaryModel> findAllTodo() {
        mRealm = Realm.getDefaultInstance();
        RealmQuery<DiaryModel> query = mRealm.where(DiaryModel.class);
        RealmResults<DiaryModel> diaryModels = query.findAll();
        return diaryModels;
    }

    public DiaryModel findTodo(String idTodo) {
        DiaryModel diaryModel = null;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            diaryModel = mRealm.where(DiaryModel.class)
                    .equalTo("idTodo", idTodo)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (diaryModel == null) {
                Log.i(TAG,"diaryModel.find returns null");
            } else {
                Log.i(TAG,"diaryModel.find returns " + diaryModel.getTitleTodo());
            }
            //realm.close();
        }
        return diaryModel;
    }

    public void updateTodo(String id, String titleTodo, String categoryTodo, Date dateTodo, String descriptionTodo) {
        DiaryModel diaryModel = null;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            diaryModel = mRealm.where(DiaryModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (diaryModel == null) {
                Log.i(TAG,"diaryModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    diaryModel.setIdTodo(id);
                    diaryModel.setTitleTodo(titleTodo);
                    diaryModel.setCategoryTodo(categoryTodo);
                    diaryModel.setDateTodo(dateTodo);
                    diaryModel.setDescriptionTodo(descriptionTodo);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeTodo(String idTodo) {
        DiaryModel diaryModel = null;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            diaryModel = mRealm.where(DiaryModel.class)
                    .equalTo("idTodo", idTodo)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (diaryModel == null) {
                Log.i(TAG,"diaryModel.find returns null");
            } else {
                Log.i(TAG,"diaryModel.find returns " + diaryModel.getTitleTodo());
                mRealm.beginTransaction();
                diaryModel.deleteFromRealm();
                mRealm.commitTransaction();
            }
        }
    }
}