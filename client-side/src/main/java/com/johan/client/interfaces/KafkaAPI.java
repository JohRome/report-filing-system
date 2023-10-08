package com.johan.client.interfaces;

public interface KafkaAPI {
    void postRequest(String jsonMessage, String endpoint);
}
