package com.johan.server.kafka.mongo.controller;

import com.johan.server.kafka.mongo.consumer.ReportRepository;
import com.johan.server.kafka.mongo.entities.ReportEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/reports")
public class MongoController {

    private final ReportRepository reportRepository;

    public MongoController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }
    @GetMapping("/get")
    public ResponseEntity<List<ReportEntity>> getReports() {
        try {
            List<ReportEntity> reports = reportRepository.findAll();
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
