package com.johan.client.handlers;

import com.johan.client.dtos.ReportDTO;
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

    private Person createPerson(boolean isReporting) {
        if (isReporting)
            log.info("Set your personal information");
        else
            log.info("Set the reported person's personal information");
        String[] names = getNames();
        var address = getAddress();
        return new Person(names[0], names[1], address);
    }
    private String[] getNames() {
        String firstName = input.stringInput("Set first name -> ");
        String lastName = input.stringInput("Set last name -> ");
        return new String[]{firstName, lastName};
    }
    private Address getAddress() {
        String street = input.stringInput("Set street name -> ");
        String apartmentNumber = input.stringInput("Set apartment number -> ");
        String city = input.stringInput("Set city -> ");
        String zipCode = input.stringInput("Set zip code -> ");
        return new Address(street, apartmentNumber, city, zipCode);
    }
    private String createEventDetails() {
        log.info("Give us a detailed description of the event, what happened?");
        return input.stringInput("Set event details -> ");
    }

    public ReportDTO createDisturbanceReport() {

        var reportingPerson = createPerson(true);
        var reportedPerson = createPerson(false);
        String eventDetails = createEventDetails();

        return new ReportDTO(reportingPerson,reportedPerson,eventDetails);
    }
}
