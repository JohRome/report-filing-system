package com.johan.client.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johan.client.dtos.ReportDTO;
import com.johan.client.httpRequests.MongoAdminAPI;
import com.johan.client.utilities.Input;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**MongoAdminReportHandler lets the Mongo Admin perform various HTTP-requests on a MongoAPI endpoint*/
@Slf4j
public class MongoAdminReportHandler {
    private final Input input;
    private final MongoAdminAPI mongoAdminAPI;
    private final String uri = "http://localhost:8080/api/v1/mongo/reports";

    public MongoAdminReportHandler(Input input, MongoAdminAPI mongoAdminAPI) {
        this.input = input;
        this.mongoAdminAPI = mongoAdminAPI;
    }

    /**
     * http://localhost:8080/api/v1/mongo/reports/patch?id=
     */
    public void letAdminPatchReport() throws JsonProcessingException {
        fetchReportsForAdmin();
        String reportToPatch = input.stringInput(
                "Copy-paste the id in the console to set boolean value \"solved\" to true -> "
        );
        mongoAdminAPI.patchMongoDoc(uri, "/patch?id=", reportToPatch);
    }

    /**
     * http://localhost:8080/api/v1/mongo/reports/delete?id=
     */

    public void letAdminDeleteReport() throws JsonProcessingException {
        fetchReportsForAdmin();
        String reportToDelete = input.stringInput(
                "Copy-paste the id in the console of the report you want to delete -> "
        );
        mongoAdminAPI.deleteMongoDoc(uri, "/delete?id=", reportToDelete);
    }

    public List<ReportDTO> test(String json) throws JsonProcessingException {
        List<ReportDTO> dtos;
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<ReportDTO>> typeReference = new TypeReference<>() {};
        dtos = objectMapper.readValue(json, typeReference);
        return dtos;
    }

    /**
     * http://localhost:8080/api/v1/mongo/reports/get
     */
    public void fetchReportsForAdmin() throws JsonProcessingException {
        List<String> mongoDocs = mongoAdminAPI.getAllMongoDocs(uri,"/get");
        if (!mongoDocs.isEmpty()) {
            List<ReportDTO> dtos = test(String.join("", mongoDocs));
            for (ReportDTO dto : dtos) {
                System.out.println(dto);
            }
        }
    }
}