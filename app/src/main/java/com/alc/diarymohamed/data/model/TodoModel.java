package com.alc.diarymohamed.data.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;


@RealmClass
public class TodoModel extends RealmObject {


    private String idTodo;
    private String titleTodo;
    private String categoryTodo;
    private Date dateTodo;
    private int timeTodo;
    private String descriptionTodo;


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


    public String getDescriptionTodo() {
        return descriptionTodo;
    }

    public void setDescriptionTodo(String descriptionTodo) {
        this.descriptionTodo = descriptionTodo;
    }
}