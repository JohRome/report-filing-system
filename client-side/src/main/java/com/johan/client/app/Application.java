package com.johan.client.app;

import com.johan.client.consumer.ConsoleConsumer;
import com.johan.client.dtos.ReportDTO;
import com.johan.client.handlers.ReportDTOHandler;
import com.johan.client.interfaces.Sender;
import com.johan.client.interfaces.Serialized;
import com.johan.client.utilities.Input;
import com.johan.client.utilities.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Main application class responsible for user interaction and filing disturbance reports through the ApacheKafkaAPI class.
 */
@Component
public class Application {
    private final Sender sender;
    private final Input input;
    private final ReportDTOHandler reportDTOHandler;
    private final ConsoleConsumer consumer;
    @Autowired
    public Application(Sender sender, Input input, ReportDTOHandler reportDTOHandler, ConsoleConsumer consumer) throws IOException {
        this.sender = sender;
        this.input = input;
        this.reportDTOHandler = reportDTOHandler;
        this.consumer = consumer;
        startApp();
    }

    /**
     * Starts the application and handles user interaction.
     *
     * @throws IOException If an IO error occurs.
     */
    public void startApp() throws IOException {
        while (true) {
            Output.printPostToAPIMenu();
            switch (input.integerInput()) {
                case 1 -> fileADisturbanceReport();
                case 2 -> consumer.printAllMessagesInTopic("disturbance-reports", "all-messages");
                case 3 -> System.exit(0);
            }
        }
    }

    /**
     * Creates a disturbance report, which is composed by the ReportDTOHandler class and sends it to Kafka API endpoint.
     *
     * @throws IOException If an IO error occurs.
     */
    private void fileADisturbanceReport() throws IOException {
        var theReportingPerson = reportDTOHandler.createPerson();
        var theReportedPerson = reportDTOHandler.createPerson();
        String eventDetails = reportDTOHandler.createEventDetails();

        Serialized reportForm = new ReportDTO(theReportingPerson, theReportedPerson, eventDetails);

        String json = sender.serializeToJSON(reportForm);
        sender.postRequest(json, "post");
    }
}
