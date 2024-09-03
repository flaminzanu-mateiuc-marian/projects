package com.freightbroker.broker_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAndProfitDTO {
    CustomerDTO customer;
    CargoDTO cargo;
    double profit;
    int acceptedByClient;
    int acceptedByProvider;
}
