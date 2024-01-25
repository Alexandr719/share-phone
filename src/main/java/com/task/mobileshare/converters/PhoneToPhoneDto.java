package com.task.mobileshare.converters;

import com.task.mobileshare.dto.PhoneDto;
import com.task.mobileshare.entity.db.Phone;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PhoneToPhoneDto implements Converter<Phone, PhoneDto> {

    @Override
    public PhoneDto convert(Phone phone) {
        return new PhoneDto(phone.getId(), phone.getName(), phone.getAvailable(), phone.getBookedTime(), phone.getBookedBy());
    }
}