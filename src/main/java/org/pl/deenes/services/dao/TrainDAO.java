package org.pl.deenes.services.dao;

import org.pl.deenes.model.Train;

import java.util.Optional;

public interface TrainDAO {
    Train save(Train train);

    Optional<Train> find(Integer trainKwr);

    void delete(Integer trainKwr);
}
