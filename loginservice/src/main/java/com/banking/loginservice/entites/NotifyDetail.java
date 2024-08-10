package com.banking.loginservice.entites;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotifyDetail {

    Long clientId;

    Long accountId;

    String email;

    Long contact;

    String message;
}
