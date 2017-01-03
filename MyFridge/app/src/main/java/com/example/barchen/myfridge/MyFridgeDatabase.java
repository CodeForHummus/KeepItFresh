package com.example.barchen.myfridge;

/**
 * Created by pagi on 1/2/17.
 */

public class MyFridgeDatabase {
    private int id;
    private String name;
    private int date;
    private int days;
    public MyFridgeDatabase()
    {
    }
    public MyFridgeDatabase(String name, int date, int days)
    {
        this.name=name;
        this.date=date;
        this.days=days;
    }

    // ID functions
    public int getId() {
        return id;
    }

    // Name functions
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    // Date functions
    public void setDate(int date) {
        this.date = date;
    }

    public int getDate() {
        return date;
    }

    // Days functions
    public void setDays(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }

}