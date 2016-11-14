package com.litereaction.doggydaycare.Model;

import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "caregiver")
public class Owner {

    @Id
    @GeneratedValue
    private long id;
    private String email;
    private String name;
    private String displayName;

    public Owner() {
    }

    public Owner(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getDisplayName() {
        return StringUtils.isEmpty(displayName) ? name : displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

}
