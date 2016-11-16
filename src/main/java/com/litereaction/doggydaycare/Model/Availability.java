package com.litereaction.doggydaycare.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "availability")
public class Availability {

    @Id
    private String date;

    private int max;

    private int available;

    public Availability() {

    }

    public Availability(String date, int max, int available){
        this.date = date;
        this.max = max;
        this.available = available;
    }

    public String getDate() { return date; }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
