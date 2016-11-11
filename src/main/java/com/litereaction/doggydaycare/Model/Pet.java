package com.litereaction.doggydaycare.Model;

import javax.persistence.*;

@Entity
@Table(name = "paws")
public class Pet {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private int age;

    private long caregiverId;

    public Pet() { }

    public Pet(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(long caregiverId) {
        this.caregiverId = caregiverId;
    }
}