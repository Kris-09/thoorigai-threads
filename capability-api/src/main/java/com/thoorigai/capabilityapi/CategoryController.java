package com.thoorigai.capabilityapi;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@RestController
public class CategoryController {
    private final SheetService sheetService;

    public CategoryController(SheetService sheetService) {
        this.sheetService = sheetService;
    }

    @GetMapping("/categories-raw")
    public String getCategoriesRaw() throws Exception {
        return sheetService.getCategoriesCsv();
    }

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
