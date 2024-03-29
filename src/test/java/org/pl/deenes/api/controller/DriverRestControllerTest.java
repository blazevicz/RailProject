package org.pl.deenes.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pl.deenes.api.controller.dto.DriverDTO;
import org.pl.deenes.api.controller.mapper.DriverDTOMapper;
import org.pl.deenes.api.controller.rest.DriverRestController;
import org.pl.deenes.cfg.JwtService;
import org.pl.deenes.infrastructure.repositories.DriverRepository;
import org.pl.deenes.infrastructure.repositories.TokenRepository;
import org.pl.deenes.model.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = DriverRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class DriverRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DriverDTOMapper driverMapper;

    @MockBean
    private DriverRepository driverRepository;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private TokenRepository tokenRepository;

    @Test
    void shouldReturnAllDriversAsDTOs() throws Exception {
        Driver driver1 = Driver.builder()
                .driverId(1)
                .name("Test1")
                .pesel(String.valueOf(66071749125L))
                .build();
        Driver driver2 = Driver.builder()
                .driverId(2)
                .name("Test2")
                .pesel("16071749125L")
                .build();
        List<Driver> driverList = List.of(driver1, driver2);

        DriverDTO driverDTO1 = DriverDTO.builder()
                .driverId(1)
                .name("Test1")
                .pesel("66071749125L")
                .build();


        given(driverRepository.findAllDrivers()).willReturn(Collections.singletonList(driver1));
        given(driverMapper.mapToDTO(driverList.get(0))).willReturn(driverDTO1);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/driver/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();
        List<DriverDTO> responseDTOList = objectMapper.readValue(responseBody, new TypeReference<>() {
        });

        Assertions.assertThat(responseDTOList).isEqualTo(Collections.singletonList(driverDTO1));
    }

}