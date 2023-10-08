package com.johan.client.interfaces;

public interface JsonConvertable<T> {
    String toJSON(T dto);
    String fromJSON(String json, T dto);
}
