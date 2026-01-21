package ru.catwarden.sltest;

import java.sql.Date;

public class BirthdayWithIndex {
    private int id;
    private int index;
    private int age;
    private String name;
    private Date date;
    private String photopath;



    public void setId(int id) {
        this.id = id;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public java.time.LocalDate getDate() {
        return date.toLocalDate();
    }

    public int getIndex() {
        return index;
    }

    public int getAge(){
        return age;
    }

    public String getPhotopath() {
        return photopath;
    }
}


