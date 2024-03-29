package org.pl.deenes.api.controller.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pl.deenes.api.controller.dto.DriverDTO;
import org.pl.deenes.api.controller.mapper.DriverDTOMapper;
import org.pl.deenes.infrastructure.repositories.DriverRepository;
import org.pl.deenes.model.Driver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/driver")
public class DriverRestController {

    private final DriverDTOMapper driverDTOMapper;
    private final DriverRepository driverRepository;

    @GetMapping(value = "/all")
    public List<DriverDTO> allDrivers() {
        return driverRepository.findAllDrivers().stream()
                .map(driverDTOMapper::mapToDTO)
                .toList();
    }

    @GetMapping(value = "/{driverId}")
    public ResponseEntity<DriverDTO> findDriver(@PathVariable Integer driverId) {
        Optional<Driver> driverOptional = driverRepository.findDriverById(driverId);
        if (driverOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Driver driver = driverOptional.get();
        return ResponseEntity.ok(driverDTOMapper.mapToDTO(driver));
    }

    @PostMapping()
    public DriverDTO addDriver(@Valid @RequestBody DriverDTO driverDTO) {
        log.warn(driverDTO.toString());
        Driver save = driverRepository.save(driverDTOMapper.mapFromDTO(driverDTO));
        return driverDTOMapper.mapToDTO(save);
    }

    @DeleteMapping(value = "/{driverId}")
    public void deleteDriver(@PathVariable Integer driverId) {
        driverRepository.delete(driverId);
    }
}
