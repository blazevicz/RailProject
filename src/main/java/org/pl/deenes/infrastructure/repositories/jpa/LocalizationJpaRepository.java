package org.pl.deenes.infrastructure.repositories.jpa;

import org.pl.deenes.infrastructure.entity.LocalizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalizationJpaRepository extends JpaRepository<LocalizationEntity, Integer> {

    LocalizationEntity findByStation(String stationName);
}
