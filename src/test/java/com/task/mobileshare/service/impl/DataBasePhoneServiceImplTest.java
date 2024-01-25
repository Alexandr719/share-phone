package com.task.mobileshare.service.impl;

import com.task.mobileshare.entity.db.Phone;
import com.task.mobileshare.exceptions.BookException;
import com.task.mobileshare.output.NotificationSender;
import com.task.mobileshare.repository.PhoneRepository;
import com.task.mobileshare.utils.PhoneBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.task.mobileshare.TestConstants.TESTER_1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataBasePhoneServiceImplTest {

    @Mock
    private NotificationSender notificationSender;
    @Mock
    private PhoneRepository phoneRepository;

    private DataBasePhoneServiceImpl underTest;

    private List<Phone> phones;

    @BeforeEach
    public void setUp() {
        phones = new ArrayList<>(PhoneBuilder.generatePhones());
        underTest = new DataBasePhoneServiceImpl(phoneRepository, notificationSender);
    }

    @Test
    @DisplayName("test get all phones")
    void testGetAllPhones() {
        // when
        when(phoneRepository.findAll()).thenReturn(phones);

        Collection<Phone> phones = underTest.getAllPhones();

        assertNotNull(phones);

    }

    @Test
    @DisplayName("test book phone")
    void testBookPhone() {
        UUID phoneId = UUID.randomUUID();
        when(phoneRepository.bookPhone(any(UUID.class), any(ZonedDateTime.class), anyString())).thenReturn(1);

        String result = underTest.bookPhone(phoneId, TESTER_1);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Phone " + phoneId + " was booked by " + TESTER_1, result)
        );
        verify(notificationSender).sendNotification("Phone " + phoneId + " was booked by " + TESTER_1);
    }

    @Test
    @DisplayName("test return phone")
    void testReturnPhone() {
        UUID phoneId = UUID.randomUUID();
        when(phoneRepository.returnPhone(any(UUID.class), anyString())).thenReturn(1);
        String result = underTest.returnPhone(phoneId, TESTER_1);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("Phone " + phoneId + " was returned by " + TESTER_1, result)
        );
        verify(notificationSender).sendNotification("Phone " + phoneId + " was returned by " + TESTER_1);
    }

    @Test
    @DisplayName("test bookPhone throws exception when not available")
    void testBookPhoneThrowsExceptionWhenNotAvailable() {

        UUID phoneId = UUID.randomUUID();
        when(phoneRepository.bookPhone(any(UUID.class), any(ZonedDateTime.class), anyString())).thenReturn(0);

        assertThrows(BookException.class, () -> underTest.bookPhone(phoneId, TESTER_1));
    }

    @Test
    @DisplayName("test return phone throws exception when not booked by")
    void testReturnPhoneThrowsExceptionWhenNotBookedBy() {
        UUID phoneId = UUID.randomUUID();
        when(phoneRepository.returnPhone(any(UUID.class), anyString())).thenReturn(0);

        assertThrows(BookException.class, () -> underTest.returnPhone(phoneId, TESTER_1));
    }

}