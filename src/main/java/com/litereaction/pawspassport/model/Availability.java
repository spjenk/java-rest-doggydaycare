package com.litereaction.pawspassport.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.litereaction.pawspassport.util.ModelUtil;

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
    private int capacity;

    @Column(nullable = false)
    private int available;

    public Availability() {

    }

    public Availability(int year, int month, int day, int capacity, Tenant tenant){
        this.id = "" + year + month + day + tenant.getId();
        this.tenant = tenant;
        this.bookingDate = LocalDate.of(year, month, day);
        this.capacity = capacity;
        this.available = capacity;
    }

    public Availability(LocalDate date, int capacity, Tenant tenant){
        this.id = ModelUtil.getAvailabilityId(date, tenant.getId());
        this.tenant = tenant;
        this.bookingDate = date;
        this.capacity = capacity;
        this.available = capacity;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
