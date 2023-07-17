package org.pl.deenes.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pl.deenes.model.Train;
import org.pl.deenes.services.dao.TrainDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class ResultServiceImpl implements ResultService {

    private TrainService trainServiceImpl;
    private TrainDAO trainDAO;

    @Override
    @Transactional
    public void runningMethod() {
        Train train = trainServiceImpl.trainCreate();
        Train train1 = saveTrain(train);
        log.warn(train1.getAnalyse().toString());
        log.warn(train.getAnalyse().toString());

    }

    @Transactional
    public Train saveTrain(Train train) {
        return trainDAO.save(train);

    }
}
