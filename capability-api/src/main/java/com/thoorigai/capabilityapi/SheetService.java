package com.thoorigai.capabilityapi;

import java.util.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

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

    public List<Product> parseProducts(String csv) throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .build();

        CSVParser parser = format.parse(new StringReader(csv));

        List<Product> products = new ArrayList<>();

        for (CSVRecord row : parser) {
            String id = row.get("id");
            String name = row.get("name");
            String description = row.get("description");
            BigDecimal price = new BigDecimal(row.get("price"));
            String categoryId = row.get("categoryId");
            String size = row.get("size");
            String color = row.get("color");
            int quantity = Integer.parseInt(row.get("quantity"));
            String[] mediaParts = row.get("media").split("\\|");

            List<String> media = new ArrayList<>();
            for (String part : mediaParts) {
                media.add(part.trim());
            }
            
            boolean featured = Boolean.parseBoolean(row.get("featured"));
            LocalDate createdAt = LocalDate.parse(row.get("createdAt"));

            products.add(new Product(id, name, description, price, categoryId, size, color, quantity, media, featured, createdAt));
        }

        return products;
    }
}
