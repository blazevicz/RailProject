package org.pl.deenes.api.controller.rest;


import lombok.AllArgsConstructor;
import org.pl.deenes.api.controller.dto.TrainDTO;
import org.pl.deenes.api.controller.mapper.TrainDTOMapper;
import org.pl.deenes.infrastructure.repositories.TrainRepository;
import org.pl.deenes.model.Train;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/train")
public class TrainRestController {

    private final TrainDTOMapper trainDTOMapper;
    private final TrainRepository trainRepository;

    @GetMapping(value = "/{trainKwr}")
    public ResponseEntity<List<TrainDTO>> trainInfo(@PathVariable Integer trainKwr) {
        var trainOptional = trainRepository.find(trainKwr);
        if (trainOptional.isPresent()) {
            List<TrainDTO> trainDTOList = trainOptional.stream()
                    .map(trainDTOMapper::mapToDTO)
                    .toList();
            return ResponseEntity.ok(trainDTOList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<TrainDTO>> allTrainsInfo() {
        var trainOptional = trainRepository.findAll();
        List<TrainDTO> list = trainOptional.stream().map(trainDTOMapper::mapToDTO).toList();
        return ResponseEntity.ok(list);
    }


    @DeleteMapping(value = "/{trainKwr}")
    public ResponseEntity<String> deleteTrain(@PathVariable Integer trainKwr) {
        Optional<Train> trainOptional = trainRepository.find(trainKwr);
        if (trainOptional.isPresent()) {
            trainRepository.delete(trainKwr);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/analyse")
    public ResponseEntity<TrainDTO> addNewTrain(@RequestBody TrainDTO trainDto) {
        Train train = trainDTOMapper.mapFromDTO(trainDto);
        Train save = trainRepository.save(train);
        TrainDTO trainDTO = trainDTOMapper.mapToDTO(save);
        return ResponseEntity.ok(trainDTO);
    }
}
