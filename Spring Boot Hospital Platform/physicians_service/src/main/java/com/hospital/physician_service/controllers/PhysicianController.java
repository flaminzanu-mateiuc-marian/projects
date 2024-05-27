package com.hospital.physician_service.controllers;

import com.hospital.physician_service.dto.PhysicianDTO;
import com.hospital.physician_service.services.PhysicianService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
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

@RequestMapping("/api/medical_office/physicians")
@CrossOrigin(origins = "http://localhost:3000")
@Transactional
@Controller
public class PhysicianController {
    private PhysicianService physicianService;

    @Autowired
    public void setPhysicianService(PhysicianService physicianService) {
        this.physicianService = physicianService;
    }

    @Operation(summary = "Adaugare doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor adaugat cu succes",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PhysicianDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Cerere invalida",
                    content = @Content) })
    @PostMapping
    public ResponseEntity<?> addPhysician(@RequestBody PhysicianDTO physician) {

        try {
            PhysicianDTO savedPhysician = physicianService.save(physician);
            EntityModel<PhysicianDTO> physicianResource = EntityModel.of(savedPhysician,
                    linkTo(methodOn(PhysicianController.class).getPhysicianById(savedPhysician.getId_doctor())).withSelfRel());
            return ResponseEntity
                    .created(new URI(physicianResource.getRequiredLink(IanaLinkRelations.SELF).getHref()))
                    .body(physicianResource);
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().body("Eroare la creare doctor: " + physician);
        }
    }

    @Operation(summary = "Obtinere lista doctori")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista tuturor doctorilor",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CollectionModel.class)) }) })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PhysicianDTO>>> getAllPhysicians() {
        List<EntityModel<PhysicianDTO>> physicians = StreamSupport.stream(physicianService.getAll().spliterator(), false)
                .map(physician -> EntityModel.of(physician,
                        linkTo(methodOn(PhysicianController.class).getPhysicianById(physician.getId_doctor())).withSelfRel(), //
                        linkTo(methodOn(PhysicianController.class).getAllPhysicians()).withRel("physicians"))) //
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(physicians,
                        linkTo(methodOn(PhysicianController.class).getAllPhysicians()).withSelfRel()));
    }

    @Operation(summary = "Obtinere doctor dupa ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor gasit",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EntityModel.class)) }),
            @ApiResponse(responseCode = "404", description = "Doctor inexistent",
                    content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<?> getPhysicianById(@PathVariable Long id) {
        return physicianService.findById(id)
                .map(physician -> EntityModel.of(physician,
                        linkTo(methodOn(PhysicianController.class).getPhysicianById(physician.getId_doctor())).withSelfRel(), //
                        linkTo(methodOn(PhysicianController.class).getAllPhysicians()).withRel("physicians"))) //
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Stergere doctor dupa ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Doctor sters cu succes",
                    content = @Content) })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePhysicianById(@PathVariable Long id) {
        physicianService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtinere doctor dupa specializare")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista doctrori cu specializarea cautata",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CollectionModel.class)) }) })
    @GetMapping(params = "specialization")
    public ResponseEntity<CollectionModel<EntityModel<PhysicianDTO>>> getPhysiciansBySpecialization(@RequestParam String specialization) {
        List<EntityModel<PhysicianDTO>> physicians = physicianService.getBySpecialization(specialization).stream()
                .map(physician -> EntityModel.of(physician,
                        linkTo(methodOn(PhysicianController.class).getPhysicianById(physician.getId_doctor())).withSelfRel(),
                        linkTo(methodOn(PhysicianController.class).getAllPhysicians()).withRel("physicians")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(physicians,
                        linkTo(methodOn(PhysicianController.class).getAllPhysicians()).withSelfRel()));
    }

    @Operation(summary = "Cautare doctori dupa nume")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista doctori cu numele specificat",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CollectionModel.class)) }) })
    @GetMapping(params = "name")
    public ResponseEntity<CollectionModel<EntityModel<PhysicianDTO>>> searchPhysiciansByName(@RequestParam String name) {
        List<EntityModel<PhysicianDTO>> physicians = physicianService.searchByName(name).stream()
                .map(physician -> EntityModel.of(physician,
                        linkTo(methodOn(PhysicianController.class).getPhysicianById(physician.getId_doctor())).withSelfRel(),
                        linkTo(methodOn(PhysicianController.class).getAllPhysicians()).withRel("physicians")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(physicians,
                        linkTo(methodOn(PhysicianController.class).getAllPhysicians()).withSelfRel()));
    }
}
