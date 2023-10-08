package com.johan.client.app;

import com.johan.client.consumer.ConsoleConsumer;
import com.johan.client.handlers.ReportDTOHandler;
import com.johan.client.httpRequests.MongoAdmin;
import com.johan.client.interfaces.Sender;
import com.johan.client.interfaces.Serialized;
import com.johan.client.utilities.Input;
import com.johan.client.utilities.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Main application class responsible for user interaction and filing disturbance reports through the ApacheKafkaAPI class.
 */
@Component
public class Application {
    private final Sender sender;
    private final Input input;
    private final ReportDTOHandler reportDTOHandler;
    private final ConsoleConsumer consumer;
    private final MongoAdmin mongoAdmin;

    @Autowired
    public Application(Sender sender, Input input, ReportDTOHandler reportDTOHandler, ConsoleConsumer consumer, MongoAdmin mongoAdmin) throws IOException, URISyntaxException, InterruptedException {
        this.sender = sender;
        this.input = input;
        this.reportDTOHandler = reportDTOHandler;
        this.consumer = consumer;
        this.mongoAdmin = mongoAdmin;
        startApp();
    }

    /**
     * Starts the application and handles user interaction.
     *
     * @throws IOException If an IO error occurs.
     */
    public void startApp() throws IOException, URISyntaxException, InterruptedException {
        while (true) {
            Output.printPostToAPIMenu();
            switch (input.integerInput()) {
                case 1 -> fileADisturbanceReport();
                case 2 -> consumer.printAllMessagesInTopic("disturbance-reports", "all-messages");
                case 3 -> getMongoDisturbanceReports();
                case 4 -> deleteMongoDisturbanceReport();
                case 5 -> System.exit(0);
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

    private void getMongoDisturbanceReports() throws URISyntaxException, IOException, InterruptedException {
        List<String> mongoReports = mongoAdmin.getAll();
        for (String mongoReport : mongoReports) {
            System.out.println(mongoReport);
        }
    }
    private void deleteMongoDisturbanceReport() throws URISyntaxException, IOException, InterruptedException {
        mongoAdmin.delete("65217b9292704926fc313d99");
    }
}
