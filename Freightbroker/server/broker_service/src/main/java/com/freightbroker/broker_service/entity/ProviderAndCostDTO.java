package com.freightbroker.broker_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProviderAndCostDTO {
    ProviderDTO provider;
    double cost;
    int acceptedByClient;
    int acceptedByProvider;
}
