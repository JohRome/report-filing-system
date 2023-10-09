package com.johan.pojos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String street;
    private String apartmentNumber;
    private String city;
    private String zipCode;
}
