package ru.catwarden.sltest;

import java.sql.Date;

// class for fetching the data from the DB
public class Birthday {
    private int id;
    private String name;
    private Date date;


    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
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
}
