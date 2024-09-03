package com.freightbroker.broker_service.service;


import com.freightbroker.broker_service.entity.CargoDTO;
import com.freightbroker.broker_service.entity.TruckDTO;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;
import java.util.Optional;

public interface CargoService {
    public CargoDTO save(CargoDTO customer);
    public List<CargoDTO> getAll();
    public void deleteByIdCargo(Long id);

    public Optional<CargoDTO> findByIdCargo(Long id);

    List<CargoDTO> getCargoForCustomer(long idCustomer);

    void reserveCargoByIdIfPossible(long idCargo);

    List<CargoDTO> getAllFree();

    public int availableCargoCount();

    public void releaseCargosIfPossible();

    public void saveBulk(List<CargoDTO> cargos);

}
