package com.example.navanee.firebasedemo;

import java.io.Serializable;

/**
 * Created by navanee on 07-11-2016.
 */

public class Expense implements Serializable{
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String name;
    private int category;
    private float amount;
    private String cDate;

    public Expense() {

    }

    public Expense(String name, int category, float amount, String cDate) {
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.cDate = cDate;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

}
