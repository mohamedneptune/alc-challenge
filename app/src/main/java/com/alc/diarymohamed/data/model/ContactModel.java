package com.alc.diarymohamed.data.model;

import io.realm.RealmObject;

/**
 * Created by mbak on 24/02/18.
 */

public class ContactModel extends RealmObject {

    private long Id;
    private String Name;
    private String Number;
    private long photoId;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }
}
