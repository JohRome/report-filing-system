package com.johan.pojos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Person {
    private String firstName;
    private String lastName;
    private Address address;
}
