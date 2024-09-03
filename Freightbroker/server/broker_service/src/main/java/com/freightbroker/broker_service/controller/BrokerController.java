package com.freightbroker.broker_service.controller;

import com.freightbroker.broker_service.entity.CargoDTO;
import com.freightbroker.broker_service.entity.TruckDTO;
import com.freightbroker.broker_service.service.BrokerServiceImpl;
import com.freightbroker.broker_service.service.CargoServiceImpl;
import com.freightbroker.broker_service.service.TruckServiceImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping("/api/freightbroker/brokerservice")
@Transactional
public class BrokerController {

    private CargoServiceImpl cargoService;
    private TruckServiceImpl truckService;
    private BrokerServiceImpl brokerService;
    @Autowired
    public void setCargoService(CargoServiceImpl cargoService) {
        this.cargoService = cargoService;
    }

    @Autowired
    public void setTruckService(TruckServiceImpl truckService) {
        this.truckService = truckService;
    }

    @Autowired
    public void setBrokerService(BrokerServiceImpl brokerService) {
        this.brokerService = brokerService;
    }

    @PutMapping("/addnewcargo")
    public ResponseEntity<?> addNewCargo(@RequestBody CargoDTO cargo){
        try {
            String response = (String) brokerService.addCargo(cargo);
            if (response.equalsIgnoreCase("OK"))
                return ResponseEntity.status(HttpStatus.OK).body("OK");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");
        }
    }

    @GetMapping("/getmycargo")
    public ResponseEntity<?> getMyCargo(@RequestParam(name = "id_customer") long id_customer){
        try {
           return ResponseEntity.status(HttpStatus.OK).body(cargoService.getCargoForCustomer(id_customer));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");
        }
    }

    @PutMapping("/addnewtruck")
    public ResponseEntity<?> addNewTruck(@RequestBody TruckDTO truck){
        try {
            String response = (String) brokerService.addTruck(truck);
            if (response.equalsIgnoreCase("OK"))
                return ResponseEntity.status(HttpStatus.OK).body("OK");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");
        }
    }

    @GetMapping("/getmytruck")
    public ResponseEntity<?> getMyTruck(@RequestParam(name = "id_provider") long id_provider){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(truckService.getTruckForProvider(id_provider));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");
        }
    }

    @GetMapping("/getofferforcustomer")
    public ResponseEntity<?> getOfferForCustomer(@RequestParam(name = "id_customer") long id_customer, @RequestParam(name = "id_cargo") long id_cargo){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(brokerService.getOfferForCustomer(id_cargo, id_customer));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");
        }
    }
    @GetMapping("/acceptofferforcustomer")
    public ResponseEntity<?> acceptOfferForCustomer(@RequestParam(name = "id_customer") long id_customer, @RequestParam(name = "id_cargo") long id_cargo){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(brokerService.acceptOfferForCustomer(id_cargo, id_customer));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");
        }
    }
    @GetMapping("/rejectofferforcustomer")
    public ResponseEntity<?> rejectOfferForCustomer(@RequestParam(name = "id_customer") long id_customer, @RequestParam(name = "id_cargo") long id_cargo){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(brokerService.rejectOfferForCustomer(id_cargo, id_customer));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");
        }
    }
    
    @GetMapping("/getofferforprovider")
    public ResponseEntity<?> getOfferForProvider(@RequestParam(name = "id_provider") long id_provider, @RequestParam(name = "id_truck") long id_truck){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(brokerService.getOfferForProvider(id_truck, id_provider));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");
        }
    }

    @GetMapping("/acceptofferforprovider")
    public ResponseEntity<?> acceptOfferForProvider(@RequestParam(name = "id_provider") long id_provider, @RequestParam(name = "id_truck") long id_truck){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(brokerService.acceptOfferForProvider(id_truck, id_provider));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");
        }
    }

    @GetMapping("/rejectofferforprovider")
    public ResponseEntity<?> rejectOfferForProvider(@RequestParam(name = "id_provider") long id_provider, @RequestParam(name = "id_truck") long id_truck){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(brokerService.rejectOfferForProvider(id_truck, id_provider));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");
        }
    }


    @PostMapping("/deletecargo")
    public ResponseEntity<?> deleteCargo(@RequestParam(name = "id_cargo") long id_cargo){
        try {
            String response = (String) brokerService.deleteCargo(id_cargo);
            if (response.equalsIgnoreCase("OK"))
                return ResponseEntity.status(HttpStatus.OK).body("OK");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");
        }
    }

    @PostMapping("/deletetruck")
    public ResponseEntity<?> deleteTruck(@RequestParam(name = "id_truck") long id_truck){
        try {
            String response = (String) brokerService.deleteTruck(id_truck);
            if (response.equalsIgnoreCase("OK"))
                return ResponseEntity.status(HttpStatus.OK).body("OK");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la procesare");
        }
    }

    //***************************
    @GetMapping("/pair")
    public void Pair() throws IOException {
        brokerService.pairCargosTrucks(1);
    }

    @GetMapping("/pairtime")
    public void PairTime() throws IOException {
        var start = System.currentTimeMillis();
        brokerService.pairCargosTrucks(0);
        var stop = System.currentTimeMillis();
        System.out.println("Durata rulare algoritm: "+(stop-start)+ " ms");
    }


    //DOAR PENTRU DEVELOPMENT/DEBUG
    @GetMapping("/releaseall")
    public void ReleaseAll(){
        brokerService.releaseAll();

    }

    @GetMapping("/createsimplescenario")
    public void createSimpleScenario() throws IOException, ParseException {
        brokerService.createDefaultScenario();

    }
    @GetMapping("/createbigscenario")
    public void createBigScenario() throws IOException, ParseException {
        brokerService.createBigScenario();

    }

    @GetMapping("/createscenario1")
    public void createScenario1() throws IOException, ParseException {
        ReleaseAll();
        brokerService.createScenario1();
        PairTime();

    }

    @GetMapping("/createscenario2")
    public void createScenario2() throws IOException, ParseException {
        ReleaseAll();
        brokerService.createScenario2();
        PairTime();

    }

    @GetMapping("/createscenario3")
    public void createScenario3() throws IOException, ParseException {
        ReleaseAll();
        brokerService.createScenario3();
        PairTime();

    }

}
