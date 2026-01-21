package ru.catwarden.sltest;

import java.sql.Date;

public class Birthday {
    private int id;
    private String name;
    private Date date;
    private String photopath;


    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhotoPath(String photo) {
        this.photopath = photo;
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getPhotoPath() {
        return photopath;
    }
}
