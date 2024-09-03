package com.freightbroker.broker_service.repository;

import com.freightbroker.broker_service.entity.CargoDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface CargoRepository extends CrudRepository<CargoDTO, Long> {
    @Query("SELECT COUNT(c) FROM CargoDTO c WHERE c.status = :status")
    int availableCargoCount(@Param("status") String status);

}
