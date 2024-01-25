package com.task.mobileshare.input;

import com.task.mobileshare.dto.ErrorDto;
import com.task.mobileshare.dto.PhoneDto;
import com.task.mobileshare.service.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/phones")
public class PhoneController {

    private static final Logger logger = LoggerFactory.getLogger(PhoneController.class);

    private final ConversionService conversionService;

    private final PhoneService phoneService;

    public PhoneController(ConversionService conversionService, PhoneService phoneService) {
        this.conversionService = conversionService;
        this.phoneService = phoneService;
    }

    @GetMapping
    @Operation(description = "Get all phones",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = PhoneDto.class)))),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class)))
            })
    public ResponseEntity<List<PhoneDto>> getPhones() {
        logger.info("Controller.getPhones");
        return ResponseEntity.ok(phoneService.getAllPhones()
                .stream()
                .map(p -> conversionService.convert(p, PhoneDto.class))
                .toList());
    }

    @Operation(description = "Book phone by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = PhoneDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class)))
            })
    @PostMapping("/book/{phoneId}/{bookedBy}")
    public ResponseEntity<String> bookPhone(
            @PathVariable UUID phoneId,
            @PathVariable String bookedBy) {
        logger.info("Controller.bookPhone phoneId: {} bookedBy: {}", phoneId, bookedBy);
        String result = phoneService.bookPhone(phoneId, bookedBy);
        return ResponseEntity.ok(result);
    }

    @Operation(description = "Return phone by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = PhoneDto.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request",
                            content = @Content(
                                    schema = @Schema(implementation = ErrorDto.class)))
            })
    @PostMapping("/return/{phoneId}/{bookedBy}")
    public ResponseEntity<String> returnPhone(@PathVariable UUID phoneId, @PathVariable String bookedBy) {
        logger.info("Controller.returnPhone phoneId: {} bookedBy: {}", phoneId, bookedBy);
        String result = phoneService.returnPhone(phoneId, bookedBy);
        return ResponseEntity.ok(result);
    }
}
