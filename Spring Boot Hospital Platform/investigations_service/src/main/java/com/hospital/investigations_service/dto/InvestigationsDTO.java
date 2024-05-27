package com.hospital.investigations_service.dto;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;


@Document(collection = "investigatii")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvestigationsDTO {
    @Id
    public String id;
    private int durata_procesare;
    private String denumire;
    private String rezultate;
}
