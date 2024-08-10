package com.banking.noticationservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Notify_Details")
public class NotifyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long notifyId;

    Long clientId;

    Long accountId;

    String email;

    Long contact;

    String message;

    @UpdateTimestamp
    Timestamp tmstamp;
}
