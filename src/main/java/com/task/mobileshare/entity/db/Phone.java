package com.task.mobileshare.entity.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "phones")
public class Phone {
    @Id
    private UUID id;
    private String name;
    private Boolean available;
    private ZonedDateTime bookedTime;
    private String bookedBy;

    public Phone(UUID id, String name) {
        this.id = id;
        this.name = name;
        this.available = true;
    }

    public Phone() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public ZonedDateTime getBookedTime() {
        return bookedTime;
    }

    public void setBookedTime(ZonedDateTime bookedTime) {
        this.bookedTime = bookedTime;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(id, phone.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

