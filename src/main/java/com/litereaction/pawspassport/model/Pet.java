package com.litereaction.pawspassport.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.litereaction.pawspassport.types.Status;

import javax.persistence.*;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    private String birthDate;

    private Status status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Owner owner;

    public Pet() { }

    public Pet(String name, String birthDate, Owner owner) {
        this.name = name;
        this.birthDate = birthDate;
        this.owner = owner;
        this.status = Status.ACTIVE;
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

    public String getBirthDate() { return birthDate; }

    public void setAge(String birthDate) {
        this.birthDate = birthDate;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }
}