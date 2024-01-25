package com.task.mobileshare.converters;

import com.task.mobileshare.dto.PhoneDto;
import com.task.mobileshare.entity.db.Phone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test for {@link PhoneToPhoneDto}
 */
class PhoneToPhoneDtoTest {

    @Test
    @DisplayName("method convert will return correct dto")
    void methodConvertWillReturnCorrectDto() {
        //given
        PhoneToPhoneDto converter = new PhoneToPhoneDto();
        Phone phone = new Phone();
        phone.setId(UUID.randomUUID());
        phone.setAvailable(Boolean.TRUE);
        phone.setBookedBy("Tester1");
        phone.setBookedTime(ZonedDateTime.now());
        phone.setName("Sony");

        //when
        PhoneDto phoneDto = converter.convert(phone);

        //then
        assertAll(
                () -> assertNotNull(phoneDto),
                () -> assertEquals(phone.getId(), phoneDto.id()),
                () -> assertEquals(phone.getName(), phoneDto.name()),
                () -> assertEquals(phone.getAvailable(), phoneDto.available()),
                () -> assertEquals(phone.getBookedTime(), phoneDto.bookedTime()),
                () -> assertEquals(phone.getBookedBy(), phoneDto.bookedBy())
        );
    }

}