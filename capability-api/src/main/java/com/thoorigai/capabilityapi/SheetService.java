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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SheetService {
    @Value("${sheets.categories-url}")
    private String categoriesUrl;

    @Value("${sheets.products-url}")
    private String productsUrl;

    private List<Category> cachedCategories;
    private long categoriesFetchedAt;
    private List<Product> cachedProducts;
    private long productsFetchedAt;

    private static final Logger log = LoggerFactory.getLogger(SheetService.class);

    public String fetchCsv(String address) throws Exception {
        HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(address))
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Google returned status " + response.statusCode());
        }

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
            try {
                if (row.get("id").isBlank()) {
                    continue;
                }
                categories.add(new Category(row.get("id"), row.get("name")));
            } catch (Exception e) {
                log.warn("Skipping category row {} because {}", row.getRecordNumber() + 1, e.getMessage());
            }
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
            try {
                if (row.get("id").isBlank()) {
                    continue;
                }
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
                    String clean = part.trim();
                    if (!clean.isEmpty()) {
                        media.add(clean);
                    }
                }

                boolean featured = Boolean.parseBoolean(row.get("featured"));
                LocalDate createdAt = LocalDate.parse(row.get("createdAt"));

                products.add(new Product(id, name, description, price, categoryId, size, color, quantity, media, featured, createdAt));
            } catch (Exception e) {
                log.warn("Skipping product row {} because {}", row.getRecordNumber() + 1, e.getMessage());
            }
        }

        return products;
    }

    public List<Category> getCategories() throws Exception {
        long now = System.currentTimeMillis();
        if (cachedCategories == null || now - categoriesFetchedAt > 60000) {
            try {
                cachedCategories = parseCategories(getCategoriesCsv());
                categoriesFetchedAt = now;
            } catch (Exception e) {
                if (cachedCategories == null) {
                    throw e;
                }
                log.warn("Refresh failed, using the old copy of categories: {}", e.getMessage());
            }
        }

        return cachedCategories;
    }

    public List<Product> getProducts() throws Exception {
        long now = System.currentTimeMillis();
        if (cachedProducts == null || now - productsFetchedAt > 60000) {
            try {
                cachedProducts = parseProducts(getProductsCsv());
                productsFetchedAt = now;
            } catch (Exception e) {
                if (cachedProducts == null) {
                    throw e;
                }
                log.warn("Refresh failed, using the old copy of products: {}", e.getMessage());
            }
        }

        return cachedProducts;
    }
}
