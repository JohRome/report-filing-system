package com.johan.server.kafka.mongo.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johan.pojos.Address;
import com.johan.pojos.Person;
import com.johan.server.kafka.mongo.entities.ReportEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MongoConsumerTest {

    @Autowired
    private MongoConsumer mongoConsumer;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    MongoConsumer mongoConsumerTest;
    ReportEntity reportEntity;

    @BeforeEach
    void setUp() {
        reportEntity = pojoCreation();
        System.out.println("The following object has been created: " + reportEntity);
    }

    @AfterEach
    void tearDown() {
        reportRepository.delete(reportEntity);
    }
    @Test
    void testSavingEntityToMongoDB() throws JsonProcessingException {
        // given
        String jsonString = objectMapper.writeValueAsString(reportEntity);

        // when
        mongoConsumerTest.mongoSave(jsonString);

        // then
        assertTrue(reportRepository.existsById(reportEntity.getId()));
    }

    @Test
    void emptyJSONShouldNotSendToMongoDB() {
        // given
        reportEntity.getReportingPerson().setFirstName("");
        String emptyJson = "";

        // when
        mongoConsumerTest.mongoSave(emptyJson);

        // then
        assertFalse(reportRepository.existsById(reportEntity.getId()));
    }

    @Test
    void testIsJsonCorrect() throws JsonProcessingException {
        // given
        String correctJSON = objectMapper.writeValueAsString(reportEntity);
        String nullJSON = null;
        String emptyJSON = "";

        // when, then
        assertTrue(mongoConsumer.isJsonCorrect(correctJSON));
        assertFalse(mongoConsumer.isJsonCorrect(nullJSON));
        assertFalse(mongoConsumer.isJsonCorrect(emptyJSON));
    }


    ReportEntity pojoCreation() {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setEventDetails("Test");
        reportEntity.setSolved(false);
        reportEntity.setId("MongoConsumerTest");



        Address address = new Address();
        address.setCity("City");
        address.setStreet("Street");
        address.setApartmentNumber("ApartmentNumber");
        address.setZipCode("ZipCode");

        Person reportingPerson = new Person("test", "test", address);
        Person reportedPerson = new Person("test", "test", address);

        reportEntity.setReportingPerson(reportingPerson);
        reportEntity.setReportedPerson(reportedPerson);

        return reportEntity;
    }
}