package com.johan.client.app;

import com.johan.client.consumer.ConsoleConsumer;
import com.johan.client.handlers.MongoAdminHandler;
import com.johan.client.handlers.ReportDTOHandler;
import com.johan.client.interfaces.Sender;
import com.johan.client.interfaces.Serialized;
import com.johan.client.utilities.Input;
import com.johan.client.utilities.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Main application class responsible for user interaction and filing disturbance reports through the ApacheKafkaAPI class.
 */
@Component
public class Application {
    private final Sender sender;
    private final Input input;
    private final ReportDTOHandler reportDTOHandler;
    private final ConsoleConsumer consumer;
    private final MongoAdminHandler mongoAdminHandler;

    @Autowired
    public Application
            (
            Sender sender,
            Input input,
            ReportDTOHandler reportDTOHandler,
            ConsoleConsumer consumer,
            MongoAdminHandler handler
            ) throws IOException, URISyntaxException, InterruptedException {
        this.sender = sender;
        this.input = input;
        this.reportDTOHandler = reportDTOHandler;
        this.consumer = consumer;
        this.mongoAdminHandler = handler;
        startApp();
    }

    /**
     * Starts the application and handles user interaction.
     *
     * @throws IOException If an IO error occurs.
     */
    private void startApp() throws IOException {
        while (true) {
            Output.printMainMenu();
            switch (input.integerInput()) {
                case 1 -> fileADisturbanceReport();
                case 2 -> consumer.printAllMessagesInTopic("disturbance-reports", "all-messages");
                case 3 -> mongoAdminHandler.fetchReportsForAdmin();
                case 4 -> mongoAdminHandler.letAdminPatchReport();
                case 5 -> mongoAdminHandler.letAdminDeleteReport();
                case 6 -> System.exit(0);
            }
        }
    }

    private void enterKafkaMenu() throws IOException {
        while (true) {
            Output.printKafkaMenu();
            switch (input.integerInput()) {
                case 1 -> fileADisturbanceReport();
                case 2 -> consumer.printAllMessagesInTopic("disturbance-reports", "all-messages");
                case 3 -> startApp();
            }
        }
    }

    private void enterMongoAdminMenu() throws IOException {
        while (true) {
            Output.printMongoAdminMenu();
            switch (input.integerInput()) {
                case 1 -> mongoAdminHandler.fetchReportsForAdmin();
                case 2 -> mongoAdminHandler.letAdminPatchReport();
                case 3 -> mongoAdminHandler.letAdminDeleteReport();
                case 4 -> startApp();
            }
        }
    }

    /**
     * Creates a disturbance report, which is composed by the ReportDTOHandler class and sends it to Kafka API endpoint.
     *
     * @throws IOException If an IO error occurs.
     */
    private void fileADisturbanceReport() throws IOException {
        Serialized disturbanceReport = reportDTOHandler.createDisturbanceReport();
        String json = sender.serializeToJSON(disturbanceReport);
        sender.postRequest(json, "post");
    }
}
