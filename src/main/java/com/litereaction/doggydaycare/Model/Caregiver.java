package com.litereaction.doggydaycare.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "caregiver")
public class Caregiver {

    public Caregiver() {}

    public Caregiver(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    private long id;

    private String name;

    @OneToMany(mappedBy="id", targetEntity=Pet.class, fetch=FetchType.EAGER)
    private List<Pet> pets;

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

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }
}
