package com.freightbroker.gateway_service.controller;

import com.freightbroker.gateway_service.entity.CargoDTO;
import com.freightbroker.gateway_service.entity.TruckDTO;
import com.freightbroker.gateway_service.service.GatewayService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@Controller
@RequestMapping("/api/freightbroker")
@Transactional
public class GatewayController {
    @Autowired
    GatewayService gatewayService;

    @PostMapping("/idm/addnewcargo")
    public ResponseEntity<?> addNewCargo(@RequestBody CargoDTO cargo, @RequestHeader("Authorization") String token) throws URISyntaxException, IOException {
        int idCustomer = gatewayService.getIdCustomer(token);
        if (idCustomer != -1) {
            cargo.setId_comerciant(idCustomer);
            int responseCode = gatewayService.addNewCargoRequest(cargo);
            if (responseCode == 200) {
                return ResponseEntity.status(HttpStatus.OK).body("Cerere procesata cu succes");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la adaugarea in baza de date");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }

    @GetMapping("/idm/getmycargo")
    public ResponseEntity<?> getMyCargo(@RequestHeader("Authorization") String token) throws IOException {
        int idCustomer = gatewayService.getIdCustomer(token);
        if (idCustomer != -1) {
            String myCargoList = gatewayService.getMyCargoRequest(idCustomer);
            if (!myCargoList.equals("Error")) {
                return ResponseEntity.status(HttpStatus.OK).body(myCargoList);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Lista este goala");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }


    @PostMapping("/idm/addnewtruck")
    public ResponseEntity<?> addNewTruck(@RequestBody TruckDTO truck, @RequestHeader("Authorization") String token) throws URISyntaxException, IOException {
        int idProvider = gatewayService.getIdCustomer(token);
        if (idProvider != -1) {
            truck.setIdTransportator(idProvider);
            int responseCode = gatewayService.addNewTruckRequest(truck);
            if (responseCode == 200) {
                return ResponseEntity.status(HttpStatus.OK).body("Cerere procesata cu succes");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la adaugarea in baza de date");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }

    @GetMapping("/idm/getmytruck")
    public ResponseEntity<?> getMyTruck(@RequestHeader("Authorization") String token) throws IOException {
        int idProvider = gatewayService.getIdCustomer(token);
        if (idProvider != -1) {
            String myCargoList = gatewayService.getMyTruckRequest(idProvider);
            if (!myCargoList.equals("Error")) {
                return ResponseEntity.status(HttpStatus.OK).body(myCargoList);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Lista este goala");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }

    @GetMapping("/idm/getofferforcustomer")
    public ResponseEntity<?> getOfferForCustomer(@RequestParam long id_cargo,@RequestHeader("Authorization") String token) throws IOException {
        int idCustomer = gatewayService.getIdCustomer(token);
        if (idCustomer != -1) {
            String offerDetails = gatewayService.getOfferForCustomerRequest(idCustomer,id_cargo);
            if (offerDetails != null) {
                return ResponseEntity.status(HttpStatus.OK).body(offerDetails);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Lista este goala");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }
    @GetMapping("/idm/acceptofferforcustomer")
    public ResponseEntity<?> acceptOfferForCustomer(@RequestParam long id_cargo,@RequestHeader("Authorization") String token) throws IOException {
        int idCustomer = gatewayService.getIdCustomer(token);
        if (idCustomer != -1) {
            String response = gatewayService.acceptOfferForCustomerRequest(idCustomer,id_cargo);
            if (response != null) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Eroare la procesare");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }
    @GetMapping("/idm/rejectofferforcustomer")
    public ResponseEntity<?> rejectOfferForCustomer(@RequestParam long id_cargo,@RequestHeader("Authorization") String token) throws IOException {
        int idCustomer = gatewayService.getIdCustomer(token);
        if (idCustomer != -1) {
            String response = gatewayService.rejectOfferForCustomerRequest(idCustomer,id_cargo);
            if (response != null) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Eroare la procesare");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }

    
    @GetMapping("/idm/getofferforprovider")
    public ResponseEntity<?> getOfferForProvider(@RequestParam long id_truck,@RequestHeader("Authorization") String token) throws IOException {
        int idProvider = gatewayService.getIdCustomer(token);
        if (idProvider != -1) {
            String offerDetails = gatewayService.getOfferForProviderRequest(idProvider,id_truck);
            if (offerDetails != null) {
                return ResponseEntity.status(HttpStatus.OK).body(offerDetails);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Lista este goala");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }

    @GetMapping("/idm/acceptofferforprovider")
    public ResponseEntity<?> acceptOfferForProvider(@RequestParam long id_truck,@RequestHeader("Authorization") String token) throws IOException {
        int idProvider = gatewayService.getIdCustomer(token);
        if (idProvider != -1) {
            String response = gatewayService.acceptOfferForProviderRequest(idProvider,id_truck);
            if (response != null) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Eroare la procesare");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }

    @GetMapping("/idm/rejectofferforprovider")
    public ResponseEntity<?> rejectOfferForProvider(@RequestParam long id_truck,@RequestHeader("Authorization") String token) throws IOException {
        int idProvider = gatewayService.getIdCustomer(token);
        if (idProvider != -1) {
            String response = gatewayService.rejectOfferForProviderRequest(idProvider,id_truck);
            if (response != null) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Eroare la procesare");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }


    @GetMapping("/idm/getCustomer")
    public ResponseEntity<?> getCustomer(@RequestHeader("Authorization") String token) throws URISyntaxException, IOException {
        int idCustomer = gatewayService.getIdCustomer(token);
        if (idCustomer != -1) {
            var response = gatewayService.getCustomer(idCustomer);
            if (response != null) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la adaugarea in baza de date");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }


    @GetMapping("/idm/getProvider")
    public ResponseEntity<?> getProvider(@RequestHeader("Authorization") String token) throws URISyntaxException, IOException {
        int idProvider = gatewayService.getIdCustomer(token);
        if (idProvider != -1) {
            var response = gatewayService.getProvider(idProvider);
            if (response!=null) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare la adaugarea in baza de date");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }

    @PostMapping("/idm/deletecargo")
    public ResponseEntity<?> deleteCargo(@RequestParam long id_cargo,@RequestHeader("Authorization") String token) throws IOException {
        int idCustomer = gatewayService.getIdCustomer(token);
        if (idCustomer != -1) {
            String response = gatewayService.deleteCargo(id_cargo);
            if (response != null) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Eroare la procesare");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }

    @PostMapping("/idm/deletetruck")
    public ResponseEntity<?> deleteTruck(@RequestParam long id_truck,@RequestHeader("Authorization") String token) throws IOException {
        int idCustomer = gatewayService.getIdCustomer(token);
        if (idCustomer != -1) {
            String response = gatewayService.deleteTruck(id_truck);
            if (response != null) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Eroare la procesare");
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }
}
