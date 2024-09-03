package com.freightbroker.user_service.controller;

import com.freightbroker.user_service.entity.CustomerDTO;
import com.freightbroker.user_service.entity.ProviderDTO;
import com.freightbroker.user_service.grpc.Identity;
import com.freightbroker.user_service.service.*;
import com.google.api.Http;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.*;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
@RequestMapping("/api/freightbroker")
@Transactional
public class UserController {
    private AuthenticationServiceImpl authenticationService;
    private CustomerServiceImpl customerService;
    private ProviderServiceImpl providerService;

    @Autowired
    public void setAuthenticationService(AuthenticationServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Autowired
    public void setCustomerService(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setProviderService(ProviderServiceImpl providerService) {
        this.providerService = providerService;
    }


    @PostMapping(value = "/authenticate", consumes = "application/x-protobuf", produces = "application/json")
    public ResponseEntity<?> authenticate(@RequestBody byte[] data) throws InvalidProtocolBufferException, InterruptedException {

        Identity.AuthenticationRequest authRequest =
                Identity.AuthenticationRequest.parseFrom(data);

        final String[] token = new String[1];
        io.grpc.stub.StreamObserver<Identity.AuthenticationResponse> responseObserver = new StreamObserver<>() {
            String auxToken;

            @Override
            public void onNext(Identity.AuthenticationResponse authenticationResponse) {
                auxToken = authenticationResponse.getToken();
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Eroare la autentificare: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {

                token[0] = auxToken;
            }

        };

        authenticationService.authenticate(authRequest, responseObserver);
        if (token[0] != null && !token[0].equals("invalid-token")) {
            return ResponseEntity.ok(token[0]);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalid");
        }
    }

    @PostMapping("/createCustomer")
    public ResponseEntity<?> addCustomer(@RequestBody CustomerDTO user) throws URISyntaxException, IOException {
        try {
            CustomerDTO savedUser = customerService.save(user);
            EntityModel<CustomerDTO> userResource = EntityModel.of(savedUser,
                    linkTo(methodOn(UserController.class).getById(savedUser.getId_customer(), "customer")).withSelfRel());
            customerService.sendCreatedNotification(savedUser);
            return ResponseEntity
                    .created(new URI(userResource.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                    .body(userResource);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.internalServerError().body("Eroare");
        }
    }

    @PostMapping("/createProvider")
    public ResponseEntity<?> addProvider(@RequestBody ProviderDTO user) throws URISyntaxException, IOException {
        try {
            ProviderDTO savedUser = providerService.save(user);
            EntityModel<ProviderDTO> userResource = EntityModel.of(savedUser,
                    linkTo(methodOn(UserController.class).getById(savedUser.getId_customer(), "provider")).withSelfRel());
            providerService.sendCreatedNotification(user);
            return ResponseEntity
                    .created(new URI(userResource.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                    .body(userResource);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.internalServerError().body("Eroare");
        }
    }


    @GetMapping("/users")
    public ResponseEntity<Optional<?>> getById(@RequestParam Long id, @RequestParam String type) {
        System.out.println("id: " + id + " type:" + type);
        if (type.trim().equalsIgnoreCase("customer")) {
            var customer = customerService.findById(id);
            if (customer.isPresent()) {
                CustomerDTO aux = new CustomerDTO(customer.get());
                aux.setParola("");
                return ResponseEntity.status(HttpStatus.OK).body(Optional.of(aux));
            }
        } else if (type.trim().equalsIgnoreCase("provider")) {
            var provider = providerService.findById(id);
            if (provider.isPresent()) {
                ProviderDTO aux = new ProviderDTO(provider.get());
                aux.setParola("");
                return ResponseEntity.status(HttpStatus.OK).body(Optional.of(aux));
            }
        }
        return null;
    }


    @PostMapping("/updateCustomer")
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerDTO user) throws URISyntaxException, IOException {
        Optional<CustomerDTO> foundUser = customerService.findById(user.getId_customer());
        System.out.println(user.getId_customer()+ " parola: "+user.getParola());
        if(foundUser.isPresent()){
            foundUser.get().setTelefon(user.getTelefon());
            foundUser.get().setNume(user.getNume());
            foundUser.get().setPrenume(user.getPrenume());
            foundUser.get().setParola(user.getParola());
            foundUser.get().setEmail(user.getEmail());
            foundUser.get().setParola(user.getParola());
            customerService.save(foundUser.get());
            return ResponseEntity.status(HttpStatus.OK).body("Operatia a avut loc cu succes!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Actualizare esuata!");
    }

    @PostMapping("/updateProvider")
    public ResponseEntity<?> updateProvider(@RequestBody ProviderDTO provider) throws URISyntaxException, IOException {
        Optional<ProviderDTO> foundProvider = providerService.findById(provider.getId_customer());
        if(foundProvider.isPresent()){
            foundProvider.get().setTelefon(provider.getTelefon());
            foundProvider.get().setNume(provider.getNume());
            foundProvider.get().setPrenume(provider.getPrenume());
            foundProvider.get().setParola(provider.getParola());
            foundProvider.get().setEmail(provider.getEmail());
            foundProvider.get().setAdresa(provider.getAdresa());
            providerService.save(foundProvider.get());
            return ResponseEntity.status(HttpStatus.OK).body("Operatia a avut loc cu succes!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Actualizare esuata!");
    }

}
