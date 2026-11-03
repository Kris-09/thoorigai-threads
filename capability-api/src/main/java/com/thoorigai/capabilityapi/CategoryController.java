package com.thoorigai.capabilityapi;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@RestController
public class CategoryController {
    @GetMapping("/categories")
    public List<Category> getCategories() {
        return List.of(
            new Category(
                "1", "Dress"
            ),
            new Category(
                "2", "Accessory"
            ));
    }
}
