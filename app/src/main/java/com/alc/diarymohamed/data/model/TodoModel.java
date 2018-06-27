package com.alc.diarymohamed.data.model;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;


@RealmClass
public class TodoModel extends RealmObject {


    private String idTodo;
    private String titleTodo;
    private String categoryTodo;
    private Date dateTodo;
    private int timeTodo;
    private int durationTodo;
    private String priorityTodo;
    private String descriptionTodo;
    private Boolean doneTodo;
    private Boolean selected;
    private Boolean preparation;
    private RealmList<PreparationModel> listPreparation;
    private Boolean remindTodo;
    private Boolean priorityImportant;
    private int dateEndTodo;

    public int getDateEndTodo() {
        return dateEndTodo;
    }

    public void setDateEndTodo(int dateEndTodo) {
        this.dateEndTodo = dateEndTodo;
    }

    public Boolean getPriorityImportant() {
        return priorityImportant;
    }

    public void setPriorityImportant(Boolean priorityImportant) {
        this.priorityImportant = priorityImportant;
    }

    public String getIdTodo() {
        return idTodo;
    }

    public void setIdTodo(String idTodo) {
        this.idTodo = idTodo;
    }

    public String getTitleTodo() {
        return titleTodo;
    }

    public void setTitleTodo(String titleTodo) {
        this.titleTodo = titleTodo;
    }

    public String getCategoryTodo() {
        return categoryTodo;
    }

    public void setCategoryTodo(String categoryTodo) {
        this.categoryTodo = categoryTodo;
    }

    public Date getDateTodo() {
        return dateTodo;
    }

    public void setDateTodo(Date dateTodo) {
        this.dateTodo = dateTodo;
    }

    public int getTimeTodo() {
        return timeTodo;
    }

    public void setTimeTodo(int timeTodo) {
        this.timeTodo = timeTodo;
    }

    public String getPriorityTodo() {
        return priorityTodo;
    }

    public void setPriorityTodo(String priorityTodo) {
        this.priorityTodo = priorityTodo;
    }


    public String getDescriptionTodo() {
        return descriptionTodo;
    }

    public void setDescriptionTodo(String descriptionTodo) {
        this.descriptionTodo = descriptionTodo;
    }

    public Boolean getDoneTodo() {
        return doneTodo;
    }

    public void setDoneTodo(Boolean doneTodo) {
        this.doneTodo = doneTodo;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public int getDurationTodo() {
        return durationTodo;
    }

    public void setDurationTodo(int durationTodo) {
        this.durationTodo = durationTodo;
    }



    public Boolean getPreparation() {
        return preparation;
    }

    public void setPreparation(Boolean preparation) {
        this.preparation = preparation;
    }

    public RealmList<PreparationModel> getListPreparation() {
        return listPreparation;
    }

    public void setListPreparation(RealmList<PreparationModel> listPreparation) {
        this.listPreparation = listPreparation;
    }

    public Boolean getRemindTodo() {
        return remindTodo;
    }

    public void setRemindTodo(Boolean remindTodo) {
        this.remindTodo = remindTodo;
    }
}