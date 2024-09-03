package com.freightbroker.broker_service.service;

import com.freightbroker.broker_service.entity.CargoTruckAssignmentDTO;

import java.util.List;
import java.util.Optional;

public interface CargoTruckAssignmentService {
    public CargoTruckAssignmentDTO save(CargoTruckAssignmentDTO cargoTruckAssignment);
    public List<CargoTruckAssignmentDTO> getAllByIdCargo(Long id_cargo);
    public List<CargoTruckAssignmentDTO> getAllByIdTruck(Long id_truck);


    public void deleteAll();
    public void deleteByIdCargo(Long id_cargo);
    public void deleteByIdTruck(Long id_truck);

}
