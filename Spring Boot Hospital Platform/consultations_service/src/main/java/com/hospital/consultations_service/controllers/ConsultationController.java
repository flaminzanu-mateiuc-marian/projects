package com.hospital.consultations_service.controllers;

import com.hospital.consultations_service.dto.ConsultationDTO;
import com.hospital.consultations_service.repository.ConsultationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@RequestMapping("/api/medical_office/consultations")
public class ConsultationController {
    @Autowired
    ConsultationRepository consultationRepository;


    @Operation(summary = "Obtinere consultatii", description = "Lista consultatii")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consultatii gasite",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CollectionModel.class))),
            @ApiResponse(responseCode = "404", description = "Nu exista consultatii")
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ConsultationDTO>>> getAllConsultations() {
        List<EntityModel<ConsultationDTO>> consultations = StreamSupport.stream(consultationRepository.findAll().spliterator(), false)
                .map(consultation -> EntityModel.of(consultation,
                        linkTo(methodOn(ConsultationController.class).getAllConsultations()).withRel("physicians"))) //
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(consultations,
                        linkTo(methodOn(ConsultationController.class).getAllConsultations()).withSelfRel()));

    }


    @Operation(summary = "Gasire consultatie dupa ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consultatie gasita",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntityModel.class))),
            @ApiResponse(responseCode = "404", description = "Consultatie inexistenta")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getConsultationById(@PathVariable String id) {
        return consultationRepository.findById(id)
                .map(consultation -> EntityModel.of(consultation,
                        linkTo(methodOn(ConsultationController.class).getConsultationById(consultation.getId())).withSelfRel(), //
                        linkTo(methodOn(ConsultationController.class).getAllConsultations()).withRel("physicians"))) //
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation(summary = "Adaugare consultatie noua", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consultatie adaugata cu succes",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntityModel.class))),
            @ApiResponse(responseCode = "400", description = "Eroare la adaugare consultatie")
    })
    @PutMapping
    public ResponseEntity<EntityModel<ConsultationDTO>> addConsultation(@RequestBody ConsultationDTO consultationDTO) {

        ConsultationDTO savedConsultation = consultationRepository.save(consultationDTO);
        EntityModel<ConsultationDTO> entityModel = EntityModel.of(savedConsultation,
                linkTo(methodOn(ConsultationController.class).getAllConsultations()).withRel("consultations"));
        linkTo(methodOn(ConsultationController.class).getConsultationById(savedConsultation.getId())).withSelfRel();

        return ResponseEntity.ok(entityModel);
    }

    @Operation(summary = "Stergere consultatie dupa ID", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Consultatie stearsa cu succes"),
            @ApiResponse(responseCode = "404", description = "Consultatie inexistenta")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConsultationById(@PathVariable String id) {
        consultationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }




}
