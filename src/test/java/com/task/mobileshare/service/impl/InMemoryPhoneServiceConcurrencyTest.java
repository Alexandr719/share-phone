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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for {@link InMemoryPhoneService}
 */
@ExtendWith(MockitoExtension.class)
class InMemoryPhoneServiceConcurrencyTest {

    @Mock
    private NotificationSender notificationSender;

    private InMemoryPhoneService underTest;

    @BeforeEach
    public void setUp() {
        underTest = new InMemoryPhoneService(notificationSender);
    }

    private static final int THREAD_POOL_SIZE = 100;
    private static final int ITERATIONS = 1000;

    @Test
    @DisplayName("concurrent book and return phones")
    void concurrentBookAndReturnPhones() throws InterruptedException, ExecutionException {
        List<UUID> allPhones = underTest.getAllPhones().stream().map(Phone::getId).toList();

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < ITERATIONS; i++) {
            int finalI = i;
            tasks.add(() -> {
                UUID id = allPhones.get(finalI % allPhones.size());
                String bookedBy = Thread.currentThread().getName();
                try {
                    underTest.bookPhone(id, bookedBy);
                    underTest.returnPhone(id, bookedBy);
                } catch (BookException e) {
                    //do nothing
                }
                return null;
            });
        }

        List<Future<Void>> futures = executorService.invokeAll(tasks);

        // Ensure that all tasks have completed successfully
        for (Future<Void> future : futures) {
            future.get(); // This will throw an exception if the task failed
        }

        executorService.shutdown();

        // Verify the state of the phones after concurrent operations
        Collection<Phone> allPhones1 = underTest.getAllPhones();
        for (Phone phone : allPhones1) {
            assertTrue(phone.getAvailable());
            assertNull(phone.getBookedBy());
        }
    }
}