package com.freightbroker.broker_service.repository;

import com.freightbroker.broker_service.entity.TruckDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface TruckRepository extends CrudRepository<TruckDTO, Long> {
    @Query("SELECT COUNT(t) FROM TruckDTO t WHERE t.status = :status")
    int availableTrucksCount(@Param("status") String status);
}
