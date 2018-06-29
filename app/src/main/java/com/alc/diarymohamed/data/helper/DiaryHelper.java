package com.alc.diarymohamed.data.helper;



import android.content.Context;
import android.util.Log;

import com.alc.diarymohamed.data.model.DiaryModel;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by mohamed on 26/06/18.
 */


public class DiaryHelper {

    private static final String TAG = DiaryHelper.class.getSimpleName();
    private Realm mRealm;

    public DiaryHelper(Context context) {
    }

    public void addDiary(String idDiary, String titleDiary, String categoryDiary, Date dateDiary,
                        int timeDiary, String descriptionDiary) {

        DiaryModel diaryModel = new DiaryModel();

        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            diaryModel = mRealm.createObject(DiaryModel.class);
            diaryModel.setIdDiary(idDiary);
            diaryModel.setTitleDiary(titleDiary);
            diaryModel.setCategoryDiary(categoryDiary);
            diaryModel.setDateDiary(dateDiary);
            diaryModel.setTimeDiary(timeDiary);
            diaryModel.setDescriptionDiary(descriptionDiary);

        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.copyToRealm(diaryModel);
            mRealm.commitTransaction();
            mRealm.close();
        }
    }


    public RealmResults<DiaryModel> findAllDiary() {
        mRealm = Realm.getDefaultInstance();
        RealmQuery<DiaryModel> query = mRealm.where(DiaryModel.class);
        RealmResults<DiaryModel> diaryModels = query.findAll();
        return diaryModels;
    }

    public DiaryModel findDiary(String idDiary) {
        DiaryModel diaryModel = null;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            diaryModel = mRealm.where(DiaryModel.class)
                    .equalTo("idDiary", idDiary)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (diaryModel == null) {
                Log.i(TAG,"diaryModel.find returns null");
            } else {
                Log.i(TAG,"diaryModel.find returns " + diaryModel.getTitleDiary());
            }
            //realm.close();
        }
        return diaryModel;
    }

    public void updateDiary(String id, String titleDiary, String categoryDiary, Date dateDiary,
                           int timeDiary, String descriptionDiary) {
        DiaryModel diaryModel = null;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            diaryModel = mRealm.where(DiaryModel.class)
                    .equalTo("idDiary", id)
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
                    diaryModel.setIdDiary(id);
                    diaryModel.setTitleDiary(titleDiary);
                    diaryModel.setCategoryDiary(categoryDiary);
                    diaryModel.setDateDiary(dateDiary);
                    diaryModel.setTimeDiary(timeDiary);
                    diaryModel.setDescriptionDiary(descriptionDiary);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeDiary(String idDiary) {
        DiaryModel diaryModel = null;
        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.beginTransaction();
            diaryModel = mRealm.where(DiaryModel.class)
                    .equalTo("idDiary", idDiary)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (diaryModel == null) {
                Log.i(TAG,"diaryModel.find returns null");
            } else {
                Log.i(TAG,"diaryModel.find returns " + diaryModel.getTitleDiary());
                mRealm.beginTransaction();
                diaryModel.deleteFromRealm();
                mRealm.commitTransaction();
            }
        }
    }
}