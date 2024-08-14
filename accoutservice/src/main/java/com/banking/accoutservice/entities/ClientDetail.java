package com.banking.accoutservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Client_Details")
public class ClientDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long clientID;

    String name;

    String address;

    Long pincode;

    Long contact;

    Date dateOfBirth;

    String email;

    String nationality;

    @Column(unique=true)
    String govId;

    String password;

    @UpdateTimestamp
    Timestamp tmStamp;
}
