package com.johan.client;

import com.johan.client.app.Application;
import com.johan.client.consumer.ConsoleConsumer;
import com.johan.client.handlers.ReportDTOHandler;
import com.johan.client.httpRequests.ApacheKafkaAPI;
import com.johan.client.httpRequests.MongoAdmin;
import com.johan.client.utilities.Input;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.johan.client.consumer"})
public class ClientApp implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ClientApp.class);
    }

    @Override
    public void run(String... args) throws Exception {

        var input = new Input();
        new Application(new ApacheKafkaAPI(), input, new ReportDTOHandler(input), new ConsoleConsumer(), new MongoAdmin());

    }
}
