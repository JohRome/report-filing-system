package com.johan.pojos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address {
    private String street;
    private String apartmentNumber;
    private String city;
    private String zipCode;
}
