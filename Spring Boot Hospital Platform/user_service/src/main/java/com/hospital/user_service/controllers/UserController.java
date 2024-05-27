package com.hospital.user_service.controllers;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hospital.user_service.services.AuthenticationServiceImpl;
import com.hospital.user_service.dto.UserDTO;
import com.hospital.user_service.services.UserService;
import io.grpc.stub.StreamObserver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/medical_office")
public class UserController {
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    public void setAuthenticationService(AuthenticationServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Autowired
    private UserService userService;


    @Operation(summary = "Autentificare utilizator", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autentificare cu succes",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Token invalid")
    })
    @PostMapping(value = "/authenticate", consumes = "application/x-protobuf", produces = "application/json")
    public ResponseEntity<?> authenticate(@RequestBody byte[] data) throws InvalidProtocolBufferException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<String> tokenReference = new AtomicReference<>();

        io.grpc.stub.StreamObserver<com.hospital.user_service.grpc.Identity.AuthenticationResponse> responseObserver = new StreamObserver<com.hospital.user_service.grpc.Identity.AuthenticationResponse>() {
            @Override
            public void onNext(com.hospital.user_service.grpc.Identity.AuthenticationResponse authenticationResponse) {
                tokenReference.set(authenticationResponse.getToken());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Authentication error: " + throwable.getMessage());
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        };

        com.hospital.user_service.grpc.Identity.AuthenticationRequest authRequest =
                com.hospital.user_service.grpc.Identity.AuthenticationRequest.parseFrom(data);
        authenticationService.authenticate(authRequest, responseObserver);

        latch.await();
        String token = tokenReference.get();
        if (token != null && !token.equals("invalid-token")) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token received");
        }
    }


    @Operation(summary = "Adaugare utilizator nou", description = "Creare si salvare utilizator.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Utilizator creat cu succes",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntityModel.class)))
    })
    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody UserDTO user) throws URISyntaxException {

        UserDTO savedUser = userService.save(user);
        EntityModel<UserDTO> userResource = EntityModel.of(savedUser,
                linkTo(methodOn(UserController.class).getUserById(savedUser.getUid())).withSelfRel());
        return ResponseEntity
                .created(new URI(userResource.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                .body(userResource);
    }


    @Operation(summary = "Obtinere utilizator dupa ID", description = "Obtinere informatii despre un utilizator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operatie efectuata cu succes",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntityModel.class))),
            @ApiResponse(responseCode = "404", description = "Utilizator inexistent")
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        return userService.findById(id)
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).getUserById(user.getUid())).withSelfRel())) //
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
