package com.task.mobileshare.service.impl;

import com.task.mobileshare.entity.db.Phone;
import com.task.mobileshare.exceptions.BookException;
import com.task.mobileshare.output.NotificationSender;
import com.task.mobileshare.service.PhoneService;
import com.task.mobileshare.utils.PhoneBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@ConditionalOnProperty(prefix = "storage", name = "db", havingValue = "false")
public class InMemoryPhoneService implements PhoneService {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryPhoneService.class);

    private final ConcurrentHashMap<UUID, Phone> phonesMap;

    private final NotificationSender notificationSender;

    public InMemoryPhoneService(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
        phonesMap = PhoneBuilder.generatePhones().stream()
                .collect(Collectors.toConcurrentMap(
                        Phone::getId,
                        Function.identity(),
                        (key1, key2) -> key1,
                        ConcurrentHashMap::new
                ));
    }

    @Override
    public Collection<Phone> getAllPhones() {
        return phonesMap.values();
    }

    @Override
    public String bookPhone(UUID id, String bookedBy) {
        Phone phone = phonesMap.get(id);
        if (phone != null && Boolean.TRUE.equals(phone.getAvailable())) {
            phone.setAvailable(Boolean.FALSE);
            phone.setBookedBy(bookedBy);
            phonesMap.put(id, phone);
            String msg = "Phone " + id + " was booked by " + bookedBy;
            notificationSender.sendNotification("Phone" + id + " is booked");
            return msg;
        } else {
            logger.error("Phone {} not found or not available", id);
            throw new BookException("Phone not found or not available");
        }
    }

    @Override
    public String returnPhone(UUID id, String bookedBy) {
        Phone phone = phonesMap.get(id);
        if (phone != null && phone.getBookedBy().equals(bookedBy)) {
            phone.setAvailable(Boolean.TRUE);
            phone.setBookedBy(null);
            phonesMap.put(id, phone);
            String msg = "Phone " + id + " was returned by " + bookedBy;
            notificationSender.sendNotification(msg);
            return msg;
        } else {
            logger.error("Phone {} not found or was booked by another tester", id);
            throw new BookException("Phone not found or was booked by another tester");
        }
    }
}
