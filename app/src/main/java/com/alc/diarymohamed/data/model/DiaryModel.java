package com.alc.diarymohamed.data.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;


@RealmClass
public class DiaryModel extends RealmObject {


    private String idDiary;
    private String titleDiary;
    private String categoryDiary;
    private Date dateDiary;
    private int timeDiary;
    private String descriptionDiary;


    public String getIdDiary() {
        return idDiary;
    }

    public void setIdDiary(String idDiary) {
        this.idDiary = idDiary;
    }

    public String getTitleDiary() {
        return titleDiary;
    }

    public void setTitleDiary(String titleDiary) {
        this.titleDiary = titleDiary;
    }

    public String getCategoryDiary() {
        return categoryDiary;
    }

    public void setCategoryDiary(String categoryDiary) {
        this.categoryDiary = categoryDiary;
    }

    public Date getDateDiary() {
        return dateDiary;
    }

    public void setDateDiary(Date dateDiary) {
        this.dateDiary = dateDiary;
    }

    public int getTimeDiary() {
        return timeDiary;
    }

    public void setTimeDiary(int timeDiary) {
        this.timeDiary = timeDiary;
    }

    public String getDescriptionDiary() {
        return descriptionDiary;
    }

    public void setDescriptionDiary(String descriptionDiary) {
        this.descriptionDiary = descriptionDiary;
    }
}