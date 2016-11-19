package com.litereaction.doggydaycare.model;


import javax.persistence.*;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue
    private long id;

    private String date;

    @ManyToOne(fetch = FetchType.EAGER)
    private Pet pet;

    public Booking() {

    }

    public Booking(String date, Pet pet) {
        this.date = date;
        this.pet = pet;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
