package com.hospital.patient_service.controllers;

import com.hospital.patient_service.dto.PatientDTO;
import com.hospital.patient_service.services.PatientService;
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/api/medical_office/patients")
@CrossOrigin(origins = "http://localhost:3000")
@Transactional
@Controller
public class PatientController {
    private PatientService patientService;

    @Autowired
    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "Adaugare pacient nou")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pacient adaugat cu succes",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Eroare la adaugare pacient",
                    content = @Content) })
    @PostMapping
    public ResponseEntity<?> addPatient(@RequestBody PatientDTO patient) {
        try {
            PatientDTO savedPatient = patientService.save(patient);
            EntityModel<PatientDTO> patientResource = EntityModel.of(savedPatient,
                    linkTo(methodOn(PatientController.class).getPatientByCnp(savedPatient.getCnp())).withSelfRel());
            return ResponseEntity
                    .created(new URI(patientResource.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                    .body(patientResource);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare la adaugare pacient");
        }
    }

    @Operation(summary = "Obtinere lista intreaga pacienti")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista pacienti",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CollectionModel.class)) }) })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PatientDTO>>> getAllPatients() {
        List<EntityModel<PatientDTO>> patients = StreamSupport.stream(patientService.getAll().spliterator(), false)
                .map(patient -> EntityModel.of(patient, //
                        linkTo(methodOn(PatientController.class).getPatientByCnp(patient.getCnp())).withSelfRel(), //
                        linkTo(methodOn(PatientController.class).getAllPatients()).withRel("patients"))) //
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(patients,
                        linkTo(methodOn(PatientController.class).getAllPatients()).withSelfRel()));
    }

    @Operation(summary = "Obtinere pacient pe baza CNP-ului")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pacientu gasit",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EntityModel.class)) }),
            @ApiResponse(responseCode = "404", description = "Pacient inexistent",
                    content = @Content) })
    @GetMapping("/{cnp}")
    public ResponseEntity<?> getPatientByCnp(@PathVariable String cnp) {
        return patientService.findByCnp(cnp)
                .map(patient -> EntityModel.of(patient,
                        linkTo(methodOn(PatientController.class).getPatientByCnp(patient.getCnp())).withSelfRel(), //
                        linkTo(methodOn(PatientController.class).getAllPatients()).withRel("patients"))) //
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Stergere pacient din baza de date pe baza CNP-ului")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pacient sters cu succes din baza de date",
                    content = @Content) })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatientByCnp(@PathVariable String id) {
        patientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
