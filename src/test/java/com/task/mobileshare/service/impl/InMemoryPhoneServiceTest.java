package com.task.mobileshare.service.impl;

import com.task.mobileshare.entity.db.Phone;
import com.task.mobileshare.exceptions.BookException;
import com.task.mobileshare.output.NotificationSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.UUID;

import static com.task.mobileshare.TestConstants.TESTER_1;
import static com.task.mobileshare.TestConstants.TESTER_2;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link InMemoryPhoneService}
 */
@ExtendWith(MockitoExtension.class)
class InMemoryPhoneServiceTest {

    @Mock
    private NotificationSender notificationSender;

    private InMemoryPhoneService underTest;

    @BeforeEach
    public void setUp() {
        underTest = new InMemoryPhoneService(notificationSender);
    }

    @Test
    @DisplayName("test get all phones")
    void testGetAllPhones() {
        // given
        Collection<Phone> phones = underTest.getAllPhones();

        assertNotNull(phones);

    }

    @Test
    @DisplayName("test book phone")
    void testBookPhone() {
        // given
        UUID phoneId = getFirstPhoneId();

        // when
        String result = underTest.bookPhone(phoneId, TESTER_1);

        assertNotNull(result);
        assertEquals("Phone " + phoneId + " was booked by " + TESTER_1, result);
        verify(notificationSender).sendNotification("Phone" + phoneId + " is booked");
    }

    @Test
    @DisplayName("test return phone")
    void testReturnPhone() {
        // given
        UUID phoneId = getFirstPhoneId();
        underTest.bookPhone(phoneId, TESTER_1);
        // when
        String result = underTest.returnPhone(phoneId, TESTER_1);

        assertNotNull(result);
        assertEquals("Phone " + phoneId + " was returned by " + TESTER_1, result);
        verify(notificationSender).sendNotification("Phone " + phoneId + " was returned by " + TESTER_1);
    }

    @Test
    @DisplayName("test bookPhone throws exception when not available")
    void testBookPhoneThrowsExceptionWhenNotAvailable() {
        // given
        UUID phoneId = getFirstPhoneId();
        underTest.bookPhone(phoneId, TESTER_1);

        assertThrows(BookException.class, () -> underTest.bookPhone(phoneId, TESTER_2));
    }

    @Test
    @DisplayName("test return phone throws Exception when not booked by the same tester")
    void testReturnPhoneThrowsExceptionWhenNotBookedBy() {
        // given
        UUID phoneId = getFirstPhoneId();
        underTest.bookPhone(phoneId, TESTER_1);

        assertThrows(BookException.class, () -> underTest.returnPhone(phoneId, TESTER_2));
    }


    private UUID getFirstPhoneId() {
        return underTest.getAllPhones().iterator().next().getId();
    }
}