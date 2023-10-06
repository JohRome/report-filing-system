package com.johan.server.kafka.mongo.consumer;

import com.johan.server.kafka.mongo.entities.ReportEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<ReportEntity, String> {
}
