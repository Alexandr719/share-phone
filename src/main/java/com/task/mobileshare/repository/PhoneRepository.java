package com.task.mobileshare.repository;

import com.task.mobileshare.entity.db.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface PhoneRepository extends JpaRepository<Phone, UUID> {

    @Transactional
    @Modifying
    @Query("update Phone ph set ph.bookedBy = :bookedBy, ph.bookedTime = :bookedTime, ph.available = false where ph.id = :id and ph.available = true")
    int bookPhone(UUID id, ZonedDateTime bookedTime, String bookedBy);


    @Transactional
    @Modifying
    @Query("update Phone ph set ph.bookedBy = null, ph.bookedTime = null, ph.available=true where ph.id = :id and ph.bookedBy =:bookedBy")
    int returnPhone(UUID id, String bookedBy);
}