package com.johan.client.app;

import com.johan.client.consumer.ConsoleConsumer;
import com.johan.client.handlers.MongoAdminReportHandler;
import com.johan.client.handlers.ReportDTOHandler;
import com.johan.client.interfaces.Sender;
import com.johan.client.interfaces.Serialized;
import com.johan.client.utilities.Input;
import com.johan.client.utilities.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * Application class is responsible to let the user chose what to do, based on the menu system provided.
 */
@Component
public class Application {
    private final Sender sender;
    private final Input input;
    private final ReportDTOHandler reportDTOHandler;
    private final ConsoleConsumer consumer;
    private final MongoAdminReportHandler mongoAdminReportHandler;

    @Autowired
    public Application
            (
            Sender sender,
            Input input,
            ReportDTOHandler reportDTOHandler,
            ConsoleConsumer consumer,
            MongoAdminReportHandler handler
            ) throws IOException {
        this.sender = sender;
        this.input = input;
        this.reportDTOHandler = reportDTOHandler;
        this.consumer = consumer;
        this.mongoAdminReportHandler = handler;
        startApp();
    }


    private void startApp() throws IOException {
        while (true) {
            Output.printMainMenu();
            switch (input.integerInput(1,3)) {
                case 1 -> enterKafkaMenu();
                case 2 -> enterMongoAdminMenu();
                case 3 -> System.exit(0);
            }
        }
    }

    private void enterKafkaMenu() throws IOException {
        while (true) {
            Output.printKafkaMenu();
            switch (input.integerInput(1,3)) {
                case 1 -> fileADisturbanceReport();
                case 2 -> consumer.printAllMessagesInTopic("disturbance-reports", "all-messages");
                case 3 -> startApp();
            }
        }
    }

    private void enterMongoAdminMenu() throws IOException {
        while (true) {
            Output.printMongoAdminMenu();
            switch (input.integerInput(1,4)) {
                case 1 -> mongoAdminReportHandler.fetchReportsForAdmin();
                case 2 -> mongoAdminReportHandler.letAdminPatchReport();
                case 3 -> mongoAdminReportHandler.letAdminDeleteReport();
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
