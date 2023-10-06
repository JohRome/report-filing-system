package com.johan.client.handlers;

import com.johan.client.utilities.Input;
import com.johan.pojos.Address;
import com.johan.pojos.Person;
import lombok.extern.slf4j.Slf4j;

/**
 * The ReportDTOHandler class is responsible for handling the creation of DTOs (Data Transfer Objects) related to reports.
 * It provides methods for creating Person objects, collecting address information, and gathering event details.
 */
@Slf4j
public class ReportDTOHandler {
    private final Input input;
    public ReportDTOHandler(Input input) {
        this.input = input;
    }

    public Person createPerson() {
        String firstName = input.stringInput("Set first name -> ");
        String lastName = input.stringInput("Set last name -> ");
        var address = getAddress();
        return new Person(firstName,lastName,address);
    }
    private Address getAddress() {
        String street = input.stringInput("Set street name -> ");
        String apartmentNumber = input.stringInput("Set apartment number -> ");
        String city = input.stringInput("Set city -> ");
        String zipCode = input.stringInput("Set zip code -> ");
        return new Address(street, apartmentNumber, city, zipCode);
    }
    public String createEventDetails() {
        log.info("Give us a detailed description of the event, what happened?");
        return input.stringInput("Set event details -> ");
    }
}
