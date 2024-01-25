package com.task.mobileshare.utils;

import com.task.mobileshare.entity.db.Phone;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PhoneBuilder {

    private PhoneBuilder(){
    }

    public static List<Phone> generatePhones(){
        List<Phone> phones = new ArrayList<>();
        phones.add(new Phone(UUID.randomUUID(), "Samsung Galaxy S9"));
        phones.add(new Phone(UUID.randomUUID(), "Samsung Galaxy S8"));
        phones.add(new Phone(UUID.randomUUID(), "Samsung Galaxy S8"));
        phones.add(new Phone(UUID.randomUUID(), "Motorola Nexus 6"));
        phones.add(new Phone(UUID.randomUUID(), "Oneplus 9"));
        phones.add(new Phone(UUID.randomUUID(), "Apple iPhone 13"));
        phones.add(new Phone(UUID.randomUUID(), "Apple iPhone 12"));
        phones.add(new Phone(UUID.randomUUID(), "Apple iPhone 11"));
        phones.add(new Phone(UUID.randomUUID(), "iPhone X"));
        phones.add(new Phone(UUID.randomUUID(), "Nokia 3310"));
        return phones;
    }
}
