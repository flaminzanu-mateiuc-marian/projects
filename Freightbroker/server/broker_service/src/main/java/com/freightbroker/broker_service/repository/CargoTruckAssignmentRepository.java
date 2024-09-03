package com.freightbroker.broker_service.repository;

import com.freightbroker.broker_service.entity.CargoTruckAssignmentDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CargoTruckAssignmentRepository extends CrudRepository<CargoTruckAssignmentDTO, Long> {

    @Modifying
    @Query("DELETE FROM CargoTruckAssignmentDTO")
    void deleteAll();

    @Modifying
    @Query("delete from CargoTruckAssignmentDTO u where u.id.id_cargo = ?1")
    void deleteByIdCargo(Long idCargo);

    @Modifying
    @Query("delete from CargoTruckAssignmentDTO u where u.id.id_truck = ?1")
    void deleteByIdTruck(Long idTruck);

}
