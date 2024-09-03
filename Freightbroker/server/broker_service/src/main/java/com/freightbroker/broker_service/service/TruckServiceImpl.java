package com.freightbroker.broker_service.service;

import com.freightbroker.broker_service.entity.TruckDTO;
import com.freightbroker.broker_service.repository.TruckRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TruckServiceImpl implements TruckService {

    public final TruckRepository truckRepository;

    public TruckServiceImpl(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    @Override
    public TruckDTO save(TruckDTO truck) {
        truckRepository.save(truck);
        return truck;
    }

    @Override
    public List<TruckDTO> getAll() {
        return (List<TruckDTO>) truckRepository.findAll();
    }

    @Override
    public void deleteByIdTruck(Long id) {
        truckRepository.deleteById(id);
    }

    @Override
    public Optional<TruckDTO> findByIdTruck(Long id) {
        return truckRepository.findById(id);
    }

    @Override
    public List<TruckDTO> getTruckForProvider(long idProvider){
        var list = truckRepository.findAll();
        List<TruckDTO> myTruckList = new ArrayList<TruckDTO>();
        for (var truck:list) {
            if(truck.getIdTransportator() == idProvider)
                myTruckList.add(truck);
        }
        return myTruckList;
    }

    @Override
    public void reserveTruckByIdIfPossible(Long id) {
        Optional<TruckDTO> optionalTruck = truckRepository.findById(id);
        if (optionalTruck.isPresent()) {
            TruckDTO truck = optionalTruck.get();
            truck.setStatus("Rezervat");
            truckRepository.save(truck);
        }
    }

    @Override
    public void releaseTrucksIfPossible() {
        var trucks = truckRepository.findAll();
        for(var truck:trucks)
        {
            if(!truck.getStatus().trim().equalsIgnoreCase("Liber")){
                truck.setStatus("Liber");
                truckRepository.save(truck);
            }
        }
    }

    @Override
    public List<TruckDTO> getAllFree() {
        var list =  truckRepository.findAll();
        List<TruckDTO> auxList = new ArrayList<>();
        for(var item:list){
            if (item.getStatus().trim().equalsIgnoreCase("Liber"))
                auxList.add(item);
        }
        return auxList;
    }

    @Override
    public int availableTrucksCount() {
        return truckRepository.availableTrucksCount("Liber");
    }

    @Override
    @Transactional
    public void saveBulk(List<TruckDTO> trucks){
        truckRepository.saveAll(trucks);
    }

}
