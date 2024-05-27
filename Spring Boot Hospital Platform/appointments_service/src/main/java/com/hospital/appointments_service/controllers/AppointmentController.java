package com.hospital.appointments_service.controllers;

import com.hospital.appointments_service.dto.AppointmentDTO;
import com.hospital.appointments_service.services.AppointmentService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/api/medical_office/appointments")
@CrossOrigin(origins = "http://localhost:3000")
@Transactional
@Controller
public class AppointmentController {
    private AppointmentService appointmentService;

    @Autowired
    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Operation(summary = "Adaugare programare noua")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Programare adaugata cu succes",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppointmentDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Cerere invalida",
                    content = @Content)})
    @PostMapping
    ResponseEntity<?> addAppointment(@RequestBody AppointmentDTO appointment) {
        try {
            AppointmentDTO savedAppointment = appointmentService.save(appointment);
             return ResponseEntity
                    .ok("Created");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare la adaugare pacient");
        }
    }

    @Operation(summary = "Obtinere lista programari")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista tuturor programarilor",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CollectionModel.class))})})
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<AppointmentDTO>>> getAllAppointments() {
        List<EntityModel<AppointmentDTO>> appointments = appointmentService.getAll().stream()
                .map(appointment -> EntityModel.of(appointment, //
                        linkTo(methodOn(AppointmentController.class).getAllAppointments()).withRel("appointments"))) //
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(appointments,
                        linkTo(methodOn(AppointmentController.class).getAllAppointments()).withSelfRel()));
    }

    @Operation(summary = "Obtinere programari pentru un utilizator")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de programari a unui utilizator",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CollectionModel.class))})})
    @GetMapping("/PastAppointments")
    public ResponseEntity<CollectionModel<EntityModel<AppointmentDTO>>> getAppointmentsForUser(@RequestHeader("Authorization") String authToken) {
        String jwtToken = authToken.split(" ")[1].trim();
        Claims claims = Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E="))
                .parseClaimsJws(jwtToken)
                .getBody();
        Long id = Long.parseLong(claims.get("id").toString());
        List<EntityModel<AppointmentDTO>> appointments = appointmentService.getAppointmentsForUser(id).stream()
                .map(appointment -> EntityModel.of(appointment, //
                        linkTo(methodOn(AppointmentController.class).getAppointmentsForUser(authToken)).withRel("appointments")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(appointments, linkTo(methodOn(AppointmentController.class).getAppointmentsForUser(authToken)).withSelfRel()));
    }
}
