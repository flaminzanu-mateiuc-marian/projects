package com.freightbroker.broker_service.service;

import com.freightbroker.broker_service.entity.CargoDTO;
import com.freightbroker.broker_service.entity.TruckDTO;
import com.freightbroker.broker_service.repository.CargoRepository;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Service
@Transactional
public class CargoServiceImpl implements CargoService {

    public final CargoRepository cargoRepository;

    public CargoServiceImpl(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
    }

    @Override
    public CargoDTO save(CargoDTO cargo) {
        cargoRepository.save(cargo);
        return cargo;
    }

    @Override
    public List<CargoDTO> getAll() {
        return (List<CargoDTO>) cargoRepository.findAll();
    }

    @Override
    public void deleteByIdCargo(Long id) {
        cargoRepository.deleteById(id);
    }

    @Override
    public Optional<CargoDTO> findByIdCargo(Long id) {
        return cargoRepository.findById(id);
    }

    @Override
    public List<CargoDTO> getCargoForCustomer(long idCustomer){
        var list = cargoRepository.findAll();
        List<CargoDTO> myCargoList = new ArrayList<>();
        for (var cargo:list) {
            if(cargo.getId_comerciant() == idCustomer)
                myCargoList.add(cargo);
        }
        return myCargoList;
    }

    @Override
    public void reserveCargoByIdIfPossible(long id) {
        Optional<CargoDTO> optionalCargo = cargoRepository.findById(id);
        if (optionalCargo.isPresent()) {
            CargoDTO cargo = optionalCargo.get();
            cargo.setStatus("Rezervat");
            cargoRepository.save(cargo);
        }
    }

    @Override
    public List<CargoDTO> getAllFree() {
        var list =  cargoRepository.findAll();
        List<CargoDTO> auxList = new ArrayList<>();
        for(var item:list){
            if (item.getStatus().trim().equalsIgnoreCase("Neonorată"))
                auxList.add(item);
        }
        return auxList;
    }

    @Override
    public int availableCargoCount() {
        return cargoRepository.availableCargoCount("Neonorată");
    }

    @Override
    public void releaseCargosIfPossible() {
        var cargos = cargoRepository.findAll();
        for(var cargo:cargos) {
            if (!cargo.getStatus().trim().equalsIgnoreCase("Neonorată")) {
                cargo.setStatus("Neonorată");
                cargoRepository.save(cargo);
            }
        }
    }

    @Override
    @Transactional
    public void saveBulk(List<CargoDTO> cargos){
        cargoRepository.saveAll(cargos);
    }


}
