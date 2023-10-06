package com.johan.client.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.johan.client.interfaces.Serialized;
import com.johan.pojos.Person;

/**
 * Data Transfer Object (DTO) class representing a report to be serialized and sent to Kafka.
 */
public class ReportDTO implements Serialized {

    /**
     * The unique identifier for the report (not serialized).
     */
    @JsonProperty
    private String id; // mongoDB autogenerated object id and is not supposed to be set by the user
    @JsonProperty
    private Person reportingPerson;
    @JsonProperty
    private Person reportedPerson;
    @JsonProperty
    private String eventDetails;
    @JsonProperty
    private boolean isSolved; // default value is false and later set to true by MongoDB admin when case is solved
    public ReportDTO(Person reportingPerson, Person reportedPerson, String eventDetails) {
        this.reportingPerson = reportingPerson;
        this.reportedPerson = reportedPerson;
        this.eventDetails = eventDetails;
    }
}
