package com.johan.pojos;

import lombok.*;

/**A common Address POJO needed by both the client-side and the server-side modules*/
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
