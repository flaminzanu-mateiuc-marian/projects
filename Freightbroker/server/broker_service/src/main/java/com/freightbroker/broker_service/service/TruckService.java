package com.freightbroker.broker_service.service;


import com.freightbroker.broker_service.entity.TruckDTO;

import java.util.List;
import java.util.Optional;

public interface TruckService {
    public TruckDTO save(TruckDTO truck);

    public List<TruckDTO> getAll();

    public void deleteByIdTruck(Long id);

    public Optional<TruckDTO> findByIdTruck(Long id);

    List<TruckDTO> getTruckForProvider(long idProvider);

    void reserveTruckByIdIfPossible(Long id);

    public List<TruckDTO> getAllFree();

    public int availableTrucksCount();

    public void releaseTrucksIfPossible();
    public void saveBulk(List<TruckDTO> trucks);
}
