package org.pl.deenes.api.controller.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.pl.deenes.api.controller.dto.DriverDTO;
import org.pl.deenes.api.controller.mapper.DriverDTOMapper;
import org.pl.deenes.infrastructure.repositories.DriverRepository;
import org.pl.deenes.model.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DriverController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DriverControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockBean
    private DriverRepository driverRepository;

    @MockBean
    private DriverDTOMapper driverDTOMapper;

    @Test
    void testGetDrivers() throws Exception {
        List<Driver> drivers = Arrays.asList(
                Driver.builder().driverId(1)
                        .name("Jan")
                        .surname("Kowalski")
                        .pesel(123123123)
                        .build(),
                Driver.builder().driverId(2)
                        .name("Henry")
                        .surname("Malinowski")
                        .pesel(321232111)
                        .build()
        );
        List<DriverDTO> driversDTO = Arrays.asList(
                DriverDTO.builder().driverId(1)
                        .name("Jan")
                        .surname("Kowalski")
                        .pesel(123123123)
                        .build(),
                DriverDTO.builder().driverId(2)
                        .name("Henry")
                        .surname("Malinowski")
                        .pesel(321232111)
                        .build()
        );

        when(driverRepository.findAllDrivers()).thenReturn(drivers);

        mockMvc.perform(get("/drivers"))
                .andExpect(status().isOk())
                .andExpect(view().name("drivers"))
                .andExpect(model().attributeExists("driversDTO"));
        // .andExpect(model().attribute("drivers", drivers.stream().map(a -> driverDTOMapper.mapToDTO(a)).toList()));
    }

    @Test
    void testGetDriverById() throws Exception {
        Driver driver = Driver.builder().driverId(1)
                .name("Jan")
                .surname("Kowalski")
                .pesel(123123123)
                .build();
        when(driverRepository.findDriverById(1)).thenReturn(Optional.of(driver));

        mockMvc.perform(get("/driver/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("driver"));
        //   .andExpect(model().attribute("drivers", driverDTOMapper.mapToDTO(driver)));
    }

    @Test
    void testGetDriverById_InvalidId() throws Exception {
        when(driverRepository.findDriverById(1)).thenAnswer(invocation -> {
            throw new ChangeSetPersister.NotFoundException();
        });

        mockMvc.perform(get("/driver/1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));
    }

    @Test
    void testCreateDriver() throws Exception {
        DriverDTO driverDTO =
                DriverDTO.builder().driverId(null)
                        .name("Jan")
                        .surname("Kowalski")
                        .pesel(123123123)
                        .build();

        Driver driver = driverDTOMapper.mapFromDTO(driverDTO);
        when(driverRepository.save(driver)).thenReturn(driver);

        mockMvc.perform(post("/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(driverDTO)))
                .andExpect(status().isOk())
                .andExpect(view().name("driver_success"));
    }

    @Test
    @Disabled
    void testUpdateDriver() throws Exception {
        DriverDTO driverDTO = DriverDTO.builder().driverId(1)
                .name("Jan")
                .surname("Kowalski")
                .pesel(123123123)
                .build();

        Driver driver = Driver.builder().driverId(1)
                .name("Jan")
                .surname("Kowalski")
                .pesel(123123123)
                .locomotiveAuthorization(null)
                .trains(null)
                .lineAuthorization(null)
                .build();
        when(driverRepository.findDriverById(1)).thenReturn(Optional.of(driver));
        when(driverRepository.save(driver)).thenReturn(driver);

        mockMvc.perform(put("/driver/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDTO)))
                .andExpect(status().isOk())
                .andExpect(view().name("driver_success"));
    }

    @Test
    void testUpdateDriver_InvalidId() throws Exception {
        DriverDTO driverDTO = DriverDTO.builder().driverId(1)
                .name("Jan")
                .surname("Kowalski")
                .pesel(123123123)
                .build();
        when(driverRepository.findDriverById(anyInt())).thenAnswer(invocation -> {
            throw new ChangeSetPersister.NotFoundException();
        });

        mockMvc.perform(put("/driver/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(driverDTO)))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"));
    }

    @Test
    void testDeleteDriver() throws Exception {
        Driver driver = Driver.builder().driverId(1)
                .name("Jan")
                .surname("Kowalski")
                .pesel(123123123)
                .build();
        when(driverRepository.findDriverById(1)).thenReturn(Optional.of(driver));

        mockMvc.perform(delete("/driver/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/drivers"));
    }

    @Test
    void testDeleteDriver_InvalidId() throws Exception {
        when(driverRepository.findDriverById(anyInt())).thenAnswer(invocation -> {
            throw new ChangeSetPersister.NotFoundException();
        });
        mockMvc.perform(delete("/driver/1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(result -> assertNotNull(result.getResolvedException()));
    }

}