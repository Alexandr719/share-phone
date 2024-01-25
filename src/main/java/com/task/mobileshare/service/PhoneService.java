package com.task.mobileshare.service;

import com.task.mobileshare.entity.db.Phone;

import java.util.Collection;
import java.util.UUID;

public interface PhoneService {
    Collection<Phone> getAllPhones();

    String bookPhone(UUID id, String bookedBy);

    String returnPhone(UUID id, String bookedBy);
}
