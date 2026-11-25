package com.thoorigai.bffapi;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final CapabilityClient capabilityClient;

    public ProductService(CapabilityClient capabilityClient) {
        this.capabilityClient = capabilityClient;
    }

    public List<ProductOut> getProducts() throws Exception {
        List<Category> categories = capabilityClient.getCategories();
        Map<String, String> nameById = new HashMap<>();

        for (Category category : categories) {
            nameById.put(category.id(), category.name());
        }

        List<ProductIn> incoming = capabilityClient.getProducts();
        List<ProductOut> result = new ArrayList<>();

        for (ProductIn product : incoming) {
            String categoryName = nameById.get(product.categoryId());

            ProductOut out = new ProductOut(
                product.id(),
                product.name(),
                product.description(),
                product.price(),
                product.categoryId(),
                categoryName,
                product.size(),
                product.color(),
                product.quantity(),
                product.media(),
                product.featured(),
                product.createdAt()
            );

            result.add(out);
        }

        return result;
    }
}
