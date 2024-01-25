package com.task.mobileshare.service.impl;

import com.task.mobileshare.entity.db.Phone;
import com.task.mobileshare.exceptions.BookException;
import com.task.mobileshare.output.NotificationSender;
import com.task.mobileshare.repository.PhoneRepository;
import com.task.mobileshare.service.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;


@Service
@ConditionalOnProperty(prefix = "storage", name = "db", havingValue = "true")
public class DataBasePhoneServiceImpl implements PhoneService {
    private static final Logger logger = LoggerFactory.getLogger(DataBasePhoneServiceImpl.class);

    private final PhoneRepository phoneRepository;
    private final NotificationSender notificationSender;

    public DataBasePhoneServiceImpl(PhoneRepository phoneRepository, NotificationSender notificationSender) {
        this.phoneRepository = phoneRepository;
        this.notificationSender = notificationSender;
    }

    @Override
    public Collection<Phone> getAllPhones() {
        return phoneRepository.findAll();
    }

    @Override
    public String bookPhone(UUID id, String bookedBy) {
        int count = phoneRepository.bookPhone(id, ZonedDateTime.now(), bookedBy);

        if (count == 0) {
            logger.error("Phone {} not found or not available", id);
            throw new BookException("Phone not found or not available");
        } else {
            String msg = "Phone " + id + " was booked by " + bookedBy;
            notificationSender.sendNotification(msg);
            return msg;
        }
    }

    @Override
    public String returnPhone(UUID id, String bookedBy) {
        int count = phoneRepository.returnPhone(id, bookedBy);

        if (count == 0) {
            logger.error("Phone {} not found or was booked by another tester", id);
            throw new BookException("Phone not found or was booked by another tester");
        } else {
            String msg = "Phone " + id + " was returned by " + bookedBy;
            notificationSender.sendNotification(msg);
            return msg;
        }
    }
}
