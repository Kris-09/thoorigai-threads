package com.thoorigai.bffapi;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController 
public class CategoryController {
    private final CapabilityClient capabilityClient;

    public CategoryController(CapabilityClient capabilityClient) {
        this.capabilityClient = capabilityClient;
    }

    @GetMapping("/categories")
    public List<Category> getCategories() throws Exception {
        return capabilityClient.getCategories();
    }
}
