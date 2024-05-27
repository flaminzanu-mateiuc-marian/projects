package com.hospital.appointments_service.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(AppointmentDTO.AppointmentKey.class)
@Table(name = "programari")
public class AppointmentDTO {

    @Id
    @Column(name = "id_pacient")
    private long idPacient;

    @Id
    @Column(name = "id_doctor")
    private long idDoctor;

    @Id
    @Column(name = "data")
    private String data;

    @Basic
    @Column(name = "status")
    private String status;

    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentKey implements Serializable {

        private long idPacient;
        private long idDoctor;
        private String data;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AppointmentKey that = (AppointmentKey) o;
            return idPacient == that.idPacient &&
                    idDoctor == that.idDoctor &&
                    Objects.equals(data, that.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(idPacient, idDoctor, data);
        }
    }
}
