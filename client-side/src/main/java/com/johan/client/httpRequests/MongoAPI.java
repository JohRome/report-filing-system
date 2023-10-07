package com.johan.client.httpRequests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johan.client.dtos.ReportDTO;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.List;

public class MongoAPI {

    private int httpResponseCode;

    public static void getAllReports() throws IOException {
        String uri = "http://localhost:8080/api/v1/reports/get";
        List<ReportDTO> reports;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setHeader("Content-Type", "application/json; utf-8");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                ObjectMapper objectMapper = new ObjectMapper();
                reports = objectMapper.readValue(responseBody, new TypeReference<>() {
                });
                for (ReportDTO report : reports) {
                    System.out.println(report);
                }

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
