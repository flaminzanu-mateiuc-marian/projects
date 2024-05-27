package com.hospital.consultations_service.dto;
import java.time.LocalDate;
import java.util.List;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;


@Document(collection = "consultatii")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationDTO {

    @Id
    public String id;
    public enum Diagnostic {Sanatos, Bolnav};
    private int id_pacient;
    private int id_doctor;
    private LocalDate data;
    private Diagnostic diagnostic;
}