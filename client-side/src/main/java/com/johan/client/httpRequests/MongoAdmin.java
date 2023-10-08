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
public class MongoAdmin {
    private int httpResponseCode;

    public List<String> getAll() throws URISyntaxException, IOException, InterruptedException {
        List<String> reports = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/v1/reports/get"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        httpResponseCode = response.statusCode();
        if (response.statusCode() == 200)
            reports.add(JSONFormatter.formatJSON(responseBody));
        else
            log.error("Failed to get disturbance reports -> " + httpResponseCode);

        return reports;
    }

    public void patch(String id) {


    }


    public void delete(String id) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/v1/reports/delete?id=" + id))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        httpResponseCode = response.statusCode();
    }
}
