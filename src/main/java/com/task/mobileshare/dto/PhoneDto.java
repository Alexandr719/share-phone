package com.task.mobileshare.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

public record PhoneDto(UUID id, String name, Boolean available, ZonedDateTime bookedTime, String bookedBy) {
}
