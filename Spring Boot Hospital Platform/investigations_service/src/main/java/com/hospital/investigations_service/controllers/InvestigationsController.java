package com.hospital.investigations_service.controllers;

import com.hospital.investigations_service.dto.InvestigationsDTO;
import com.hospital.investigations_service.repository.InvestigationsRepository;
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

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/medical_office/investigations")
public class InvestigationsController {
    @Autowired
    InvestigationsRepository investigationsRepository;

    @Operation(summary = "Obtinere lista investigatii")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista investigatiilor",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CollectionModel.class))})})
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<InvestigationsDTO>>> getAllInvestigations() {
        List<EntityModel<InvestigationsDTO>> investigations = StreamSupport.stream(investigationsRepository.findAll().spliterator(), false)
                .map(investigation -> EntityModel.of(investigation,
                        linkTo(methodOn(InvestigationsController.class).getInvestigationById(String.valueOf(investigation.getId()))).withSelfRel(),
                        linkTo(methodOn(InvestigationsController.class).getAllInvestigations()).withRel("investigations")))
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(investigations,
                        linkTo(methodOn(InvestigationsController.class).getAllInvestigations()).withSelfRel()));
    }

    @Operation(summary = "Obtinere investigatie dupa ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Investigatie gasita",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EntityModel.class))}),
            @ApiResponse(responseCode = "404", description = "Investigatie inexistenta",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<?> getInvestigationById(@PathVariable String id) {
        return investigationsRepository.findById(id)
                .map(investigation -> EntityModel.of(investigation,
                        linkTo(methodOn(InvestigationsController.class).getInvestigationById(String.valueOf(investigation.getId()))).withSelfRel(), //
                        linkTo(methodOn(InvestigationsController.class).getAllInvestigations()).withRel("investigations"))) //
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Adaugare investigatie noua")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Investigatie adaugata cu succes",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EntityModel.class))})})
    @PostMapping
    public ResponseEntity<EntityModel<InvestigationsDTO>> addInvestigation(@RequestBody InvestigationsDTO investigationsDTO) {
        InvestigationsDTO savedInvestigation = investigationsRepository.save(investigationsDTO);
        EntityModel<InvestigationsDTO> entityModel = EntityModel.of(savedInvestigation,
                linkTo(methodOn(InvestigationsController.class).getAllInvestigations()).withRel("investigations"));

        return ResponseEntity.ok(entityModel);
    }

    @Operation(summary = "Stergere investigatie dupa ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Investigatie stearsa cu succes",
                    content = @Content)})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConsultationById(@PathVariable String id) {
        investigationsRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
