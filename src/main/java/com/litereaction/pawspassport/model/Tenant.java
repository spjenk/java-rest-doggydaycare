package com.litereaction.pawspassport.model;

import com.litereaction.pawspassport.types.Status;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Tenant {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private String locale;

    private Status status;

    public Tenant() {

    }

    public Tenant(String name) {
        this.name = name;
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

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }
}
