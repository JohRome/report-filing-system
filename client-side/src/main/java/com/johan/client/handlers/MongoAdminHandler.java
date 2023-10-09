package com.johan.client.handlers;

import com.johan.client.httpRequests.MongoAdminAPI;
import com.johan.client.utilities.Input;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class MongoAdminHandler {
    private final Input input;
    private final MongoAdminAPI mongoAdminAPI;

    public MongoAdminHandler(Input input, MongoAdminAPI mongoAdminAPI) {
        this.input = input;
        this.mongoAdminAPI = mongoAdminAPI;
    }

    public void letAdminPatchReport() {
        fetchReportsForAdmin();
        String reportToPatch = input.stringInput(
                "Copy-paste the id in the console to set boolean value \"solved\" to true -> "
        );
        mongoAdminAPI.patchDisturbanceReport(reportToPatch);
    }

    public void letAdminDeleteReport() {
        fetchReportsForAdmin();
        String reportToDelete = input.stringInput(
                "Copy-paste the id in the console of the report you want to delete -> "
        );
        mongoAdminAPI.deleteDisturbanceReport(reportToDelete);
    }

    public void fetchReportsForAdmin() {
        List<String> fetchedReports = mongoAdminAPI.getAllDisturbanceReports();

        if (fetchedReports.isEmpty())
            log.info("No reports in your MongoDB");
        else {
            log.info("All Disturbance Reports in your MongoDB");
            for (String fetchedReport : fetchedReports) {
                log.info(fetchedReport);
            }
        }
    }
}