package com.johan.client.httpRequests;

import com.johan.client.utilities.JSONFormatter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MongoAdminAPI {
    public List<String> getAllDisturbanceReports() {
        List<String> reports = new ArrayList<>(); // list to store all the reports from the GET-request
        // Creating a http client with appropriate GET-request method
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/v1/reports/get"))
                    .GET()
                    .build();
            // Executing the request, and if 200, stores it in the reports list
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            if (response.statusCode() == 200)
                reports.add(JSONFormatter.formatJSON(responseBody));

        } catch (URISyntaxException | IOException | InterruptedException e) {
            log.error("GET-request failed -> " + e.getMessage());
        }

        return reports;
    }

    public void patchDisturbanceReport(String id) {
        // Creating a http client with appropriate PATCH-request method
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/v1/reports/patch?id=" + id))
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(id))
                    .header("Content-Type", "application/json")
                    .build();
            // Executing the request
            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (URISyntaxException | IOException | InterruptedException e) {
            log.error("PATCH-request failed ->" + e.getMessage());
        }
    }


    public void deleteDisturbanceReport(String id) {
        // Creating a http client with appropriate DELETE-request method
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/v1/reports/delete?id=" + id))
                    .DELETE()
                    .build();
            // Executing the request
            client.send(request, HttpResponse.BodyHandlers.ofString());


        } catch (URISyntaxException | IOException | InterruptedException e) {
            log.error("Error when deleting -> " + e.getMessage());
        }
    }
}
