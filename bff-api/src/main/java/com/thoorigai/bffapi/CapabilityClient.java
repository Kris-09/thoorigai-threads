package com.thoorigai.bffapi;

import org.springframework.stereotype.Service;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

@Service
public class CapabilityClient {
    @Value("${capability.api-url}")
    private String baseUrl;

    public String fetchText(String path) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + path))
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Capability API returned status " + response.statusCode());
        }

        return response.body();
    }

    public List<Category> getCategories() throws Exception {
        String json = fetchText("/categories");
        ObjectMapper mapper = new ObjectMapper();
        List<Category> categories = mapper.readValue(json, new TypeReference<List<Category>>() {});

        return categories;
    }
}
