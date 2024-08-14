package com.banking.accoutservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
