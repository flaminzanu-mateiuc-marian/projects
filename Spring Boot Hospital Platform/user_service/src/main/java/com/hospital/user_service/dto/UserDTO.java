package com.hospital.user_service.dto;

import lombok .AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "utilizatori")
public class UserDTO {
    public UserDTO(){

    }
    public UserDTO(String username, String password, String role){
        this.password = password;
        this.username = username;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic
    @Column(name = "uid")
    private long uid;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "role")
    private String role;

}
