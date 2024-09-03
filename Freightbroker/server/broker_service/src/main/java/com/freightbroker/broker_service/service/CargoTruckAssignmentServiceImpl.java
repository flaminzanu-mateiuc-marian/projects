package com.freightbroker.broker_service.service;

import com.freightbroker.broker_service.entity.CargoTruckAssignmentDTO;
import com.freightbroker.broker_service.repository.CargoRepository;
import com.freightbroker.broker_service.repository.CargoTruckAssignmentRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CargoTruckAssignmentServiceImpl implements CargoTruckAssignmentService{
    public final CargoTruckAssignmentRepository cargoTruckAssignmentRepository;
    public CargoTruckAssignmentServiceImpl(CargoTruckAssignmentRepository cargoTruckAssignmentRepository) {
        this.cargoTruckAssignmentRepository = cargoTruckAssignmentRepository;

    }

    @Override
    public CargoTruckAssignmentDTO save(CargoTruckAssignmentDTO cargoTruckAssignment) {
        cargoTruckAssignmentRepository.save(cargoTruckAssignment);
        return cargoTruckAssignment;
    }

    @Override
    public List<CargoTruckAssignmentDTO> getAllByIdCargo(Long id_cargo) {
        var list = cargoTruckAssignmentRepository.findAll();
        List<CargoTruckAssignmentDTO> auxList = new ArrayList<>();
        for(var cargo:list){
            if(cargo.getId().getId_cargo() == id_cargo)
                auxList.add(cargo);
        }
        return auxList;
    }

    @Override
    public List<CargoTruckAssignmentDTO> getAllByIdTruck(Long id_truck) {
        var list = cargoTruckAssignmentRepository.findAll();
        List<CargoTruckAssignmentDTO> auxList = new ArrayList<>();
        for(var cargo:list){
            if(cargo.getId().getId_truck() == id_truck)
                auxList.add(cargo);
        }
        return auxList;
    }

    public void deleteByIdCargo(Long id_cargo){
        cargoTruckAssignmentRepository.deleteByIdCargo(id_cargo);
    }

    public void deleteByIdTruck(Long id_truck){
        cargoTruckAssignmentRepository.deleteByIdTruck(id_truck);
    }




    @Override
    public void deleteAll(){
        cargoTruckAssignmentRepository.deleteAll();
    }
}
