package com.thoorigai.capabilityapi;

import java.util.*;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SheetService {
    @Value("${sheets.categories-url}")
    private String categoriesUrl;

    @Value("${sheets.products-url}")
    private String productsUrl;

    public String fetchCsv(String address) throws Exception {
        HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(address))
            .build();

        HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getCategoriesCsv() throws Exception {
        return fetchCsv(categoriesUrl);
    }

    public String getProductsCsv() throws Exception {
        return fetchCsv(productsUrl);
    }

    public List<Category> parseCategories(String csv) throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .build();

        CSVParser parser = format.parse(new StringReader(csv));

        List<Category> categories = new ArrayList<>();

        for (CSVRecord row : parser) {
            categories.add(new Category(row.get("id"), row.get("name")));
        }

        return categories;
    }
}
