package com.task.mobileshare.input;

import com.task.mobileshare.converters.PhoneToPhoneDto;
import com.task.mobileshare.dto.PhoneDto;
import com.task.mobileshare.entity.db.Phone;
import com.task.mobileshare.service.PhoneService;
import com.task.mobileshare.utils.PhoneBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static com.task.mobileshare.TestConstants.TESTER_1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhoneControllerTest {

    @Mock
    private ConversionService conversionService;

    @Mock
    private PhoneService phoneService;

    @InjectMocks
    private PhoneController phoneController;

    private MockMvc mockMvc;

    private final List<Phone> phones = PhoneBuilder.generatePhones();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(phoneController).build();
    }

    @DisplayName("test get all phones")
    @Test
    void testGetPhones() throws Exception {

        List<PhoneDto> phoneDtoList = getPhoneDtoList();
        when(phoneService.getAllPhones()).thenReturn(phones);
        when(conversionService.convert(any(Phone.class), eq(PhoneDto.class))).thenReturn(getPhoneDtoList().get(0));

        mockMvc.perform(MockMvcRequestBuilders.get("/phones")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(phoneDtoList.size()));

        verify(phoneService, times(1)).getAllPhones();
        verify(conversionService, times(phoneDtoList.size())).convert(any(Phone.class), eq(PhoneDto.class));
    }

    @DisplayName("test book phone")
    @Test
    void testBookPhone() throws Exception {
        UUID phoneId = UUID.randomUUID();
        String resultMessage = "Phone booked successfully";

        when(phoneService.bookPhone(phoneId, TESTER_1)).thenReturn(resultMessage);

        mockMvc.perform(MockMvcRequestBuilders.post("/phones/book/{phoneId}/{bookedBy}", phoneId, TESTER_1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(resultMessage));

        verify(phoneService, times(1)).bookPhone(phoneId, TESTER_1);
    }

    @DisplayName("test return phone")
    @Test
    void testReturnPhone() throws Exception {
        UUID phoneId = UUID.randomUUID();
        String resultMessage = "Phone returned successfully";

        when(phoneService.returnPhone(phoneId, TESTER_1)).thenReturn(resultMessage);

        mockMvc.perform(MockMvcRequestBuilders.post("/phones/return/{phoneId}/{bookedBy}", phoneId, TESTER_1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(resultMessage));

        verify(phoneService, times(1)).returnPhone(phoneId, TESTER_1);
    }


    private List<PhoneDto> getPhoneDtoList() {
        return phones.stream().map(p -> new PhoneToPhoneDto().convert(p)).toList();
    }
}