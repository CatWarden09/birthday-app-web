package ru.catwarden.sltest;

import java.sql.Date;
import java.text.SimpleDateFormat;

// class for console data output
public class BirthdayWithIndex {
    private int id;
    private int index;
    private int age;
    private String name;
    private String date;

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy");


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
        this.date = FORMAT.format(date);
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getIndex() {
        return index;
    }

    public int getAge(){
        return age;
    }
}


