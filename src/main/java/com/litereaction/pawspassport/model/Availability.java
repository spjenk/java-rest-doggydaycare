package com.litereaction.pawspassport.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "availability")
public class Availability {

    @Id
    private String id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Tenant tenant;

    @JsonIgnore
    @Column(nullable = false, unique = true)
    private LocalDate bookingDate;

    @Column(nullable = false)
    private int max;

    @Column(nullable = false)
    private int available;

    public Availability() {

    }

    public Availability(int year, int month, int day, int max, Tenant tenant){
        this.id = "" + year + month + day + tenant.getId();
        this.tenant = tenant;
        this.bookingDate = LocalDate.of(year, month, day);
        this.max = max;
        this.available = max;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setBookingDate(int year, int month, int day) {
        this.bookingDate = LocalDate.of(year, month, day);
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

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
}
