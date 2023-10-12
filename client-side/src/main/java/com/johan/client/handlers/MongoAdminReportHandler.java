package com.johan.client.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johan.client.dtos.ReportDTO;
import com.johan.client.httpRequests.MongoAdminAPI;
import com.johan.client.utilities.Input;
import com.johan.client.utilities.Output;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * MongoAdminReportHandler lets the Mongo Admin perform various HTTP-requests on MongoAPI endpoints
 */
@Slf4j
public class MongoAdminReportHandler {
    private final Input input;
    private final MongoAdminAPI mongoAdminAPI;
    private final String uri = "http://localhost:8080/api/v1/mongo/reports";

    public MongoAdminReportHandler(Input input, MongoAdminAPI mongoAdminAPI) {
        this.input = input;
        this.mongoAdminAPI = mongoAdminAPI;
    }

    public void letAdminPatchReport() throws JsonProcessingException {
        // The Output class returns an indexed list of ReportDTO
        List<ReportDTO> indexedReports = Output.printIndexedReports(fetchReportsForAdmin());
        // letAdminChoseIndex returning the wanted report do delete
        int reportToPatchIndex;

        try {
            reportToPatchIndex = letAdminChoseIndex("patch", indexedReports);
            ReportDTO reportToPatch = indexedReports.get(reportToPatchIndex);
            mongoAdminAPI.patchMongoDoc(uri, "/patch?id=", reportToPatch.getId());

        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            Output.printError("No such index exists\n");
        }
    }

    public void letAdminDeleteReport() throws JsonProcessingException {
        // The Output class returns an indexed list of ReportDTO
        List<ReportDTO> indexedReports = Output.printIndexedReports(fetchReportsForAdmin());
        
        int reportToDeleteIndex;
        try {
            // letAdminChoseIndex returning the wanted report do delete
            reportToDeleteIndex = letAdminChoseIndex("delete", indexedReports);
            ReportDTO reportToDelete = indexedReports.get(reportToDeleteIndex);
            mongoAdminAPI.deleteMongoDoc(uri, "/delete?id=", reportToDelete.getId());
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            Output.printError("No such index exists\n");
        }
    }

    private int letAdminChoseIndex(String httpRequest, List<ReportDTO> indexedReports) {
        return Integer.parseInt(input.stringInput(
                "Enter index on the report you want to "
                        + httpRequest + " -> "));
    }
    public void getAllReports() throws JsonProcessingException {
        Output.printIndexedReports(fetchReportsForAdmin());
    }


    public List<ReportDTO> fetchReportsForAdmin() throws JsonProcessingException {
        List<String> mongoDocs = mongoAdminAPI.getAllMongoDocs(uri, "/get");
        List<ReportDTO> dtos = new ArrayList<>();
        if (!mongoDocs.isEmpty()) {
            dtos = convertJsonToReportDTO(String.join("", mongoDocs));
        }
        return dtos;
    }

    private List<ReportDTO> convertJsonToReportDTO(String json) throws JsonProcessingException {
        List<ReportDTO> dtos;
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<ReportDTO>> typeReference = new TypeReference<>() {
        };
        dtos = objectMapper.readValue(json, typeReference);
        return dtos;
    }
}