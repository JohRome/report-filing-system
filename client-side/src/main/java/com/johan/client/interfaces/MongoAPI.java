package com.johan.client.interfaces;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface MongoAPI<T> extends JsonConvertable<T>{
    List<T> getAll() throws URISyntaxException, IOException, InterruptedException;
    void patch(String id);
    void delete(String id);
}
